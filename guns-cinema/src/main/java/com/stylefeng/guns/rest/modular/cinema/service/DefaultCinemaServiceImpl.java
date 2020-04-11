package com.stylefeng.guns.rest.modular.cinema.service;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.rest.api.cinema.CinemaServiceAPI;
import com.stylefeng.guns.rest.api.cinema.vo.*;
import com.stylefeng.guns.rest.common.persistence.dao.*;
import com.stylefeng.guns.rest.common.persistence.model.AreaDictT;
import com.stylefeng.guns.rest.common.persistence.model.BrandDictT;
import com.stylefeng.guns.rest.common.persistence.model.CinemaT;
import com.stylefeng.guns.rest.common.persistence.model.HallDictT;
import com.stylefeng.guns.rest.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Author: HYN
 * @Description:
 * @Date: 2020/4/8 1:17 下午
 * @Modified By:
 */
@Component
@Service(interfaceClass = CinemaServiceAPI.class, executes = 10)
public class DefaultCinemaServiceImpl implements CinemaServiceAPI {

    private final AreaDictTMapper areaDictTMapper;
    private final BrandDictTMapper brandDictTMapper;
    private final CinemaTMapper cinemaTMapper;
    private final FieldTMapper fieldTMapper;
    private final HallDictTMapper hallDictTMapper;
    private final HallFilmInfoTMapper hallFilmInfoTMapper;

    /**
     * redis
     */
    private final RedisUtil redisUtil;


    @Autowired
    public DefaultCinemaServiceImpl(AreaDictTMapper areaDictTMapper, BrandDictTMapper brandDictTMapper,
                                    CinemaTMapper cinemaTMapper, FieldTMapper fieldTMapper,
                                    HallDictTMapper hallDictTMapper, HallFilmInfoTMapper hallFilmInfoTMapper, RedisUtil redisUtil) {
        this.areaDictTMapper = areaDictTMapper;
        this.brandDictTMapper = brandDictTMapper;
        this.cinemaTMapper = cinemaTMapper;
        this.fieldTMapper = fieldTMapper;
        this.hallDictTMapper = hallDictTMapper;
        this.hallFilmInfoTMapper = hallFilmInfoTMapper;
        this.redisUtil = redisUtil;
    }


    /**
     * 1. 根据CinemaQueryVO查询影院列表
     * @param cinemaQueryVO
     * @return
     */
    @Override
    public Page<CinemaVO> getCinemas(CinemaQueryVO cinemaQueryVO) {
        //初始化返回结果
        Page<CinemaVO> result = new Page<>();
        //结果集
        List<CinemaVO> cinemaVOs = new ArrayList<>();

        //查询CinemaT的page
        Page<CinemaT> cinemaTPage = new Page<>(cinemaQueryVO.getNowPage(), cinemaQueryVO.getPageSize());
        EntityWrapper<CinemaT> wrapper = new EntityWrapper<>();
        // 判断是否传入查询条件 -> brandId,distId,hallType 是否==99
        if (cinemaQueryVO.getAreaId() != 99) {
            wrapper.eq("area_id", cinemaQueryVO.getAreaId());
        }
        if (cinemaQueryVO.getBrandId() != 99) {
            wrapper.eq("brand_id", cinemaQueryVO.getBrandId());
        }
        if (cinemaQueryVO.getHallType() != 99) {
            //包含的影厅类型编号,以#作为分割
            wrapper.like("hall_ids", "%#" + cinemaQueryVO.getHallType() + "#%");
        }
        //数据库实体转化成业务实体
        List<CinemaT> cinemaTs = cinemaTMapper.selectPage(cinemaTPage, wrapper);
        for (CinemaT cinemaT : cinemaTs) {
            CinemaVO cinemaVO=  new CinemaVO();
            cinemaVO.setAddress(cinemaT.getCinemaAddress());
            cinemaVO.setCinemaName(cinemaT.getCinemaName());
            cinemaVO.setMinPrice(cinemaT.getMinimumPrice() + "");
            cinemaVO.setUuid(cinemaT.getUuid() + "");

            cinemaVOs.add(cinemaVO);
        }
        //查询符合条件的影院个数
        long count = cinemaTMapper.selectCount(wrapper);
        //封装结果page
        result.setRecords(cinemaVOs);
        result.setSize(cinemaQueryVO.getPageSize());
        result.setTotal(count);
        return result;
    }

    /**
     * 2. 根据条件获取品牌列表，除了99，其他都是active，先从redis中获取，没有的话去MySQL中获取
     * @param brandId
     * @return
     */
    @Override
    public List<BrandVO> getBrands(int brandId) {
        try (Jedis jedis = redisUtil.getJedis()) {
            List<BrandVO> brandList;

            String brandKey = "brands:" + brandId + ":info:";
            //先从redis中查询
            String brandJson = jedis.get(brandKey);
            if (!StringUtils.isBlank(brandJson)) {
                brandList = JSON.parseArray(brandJson, BrandVO.class);
            } else {
                // 没有的话再从MySQL中查询
                brandList = getBrandsFromDB(brandId);

                //如果数据库中也不存在，为防止缓存穿透，则返回一个空值存入redis，为防止缓存雪崩(缓存大面积失效)
                //过期时间需要加一个随机值
                int randomTime = 60 * 60 + new Random().nextInt(100);
                if (brandList != null) {
                    jedis.setex(brandKey, randomTime, JSON.toJSONString(brandList));
                } else {
                    //
                    jedis.setex(brandKey, randomTime, JSON.toJSONString(""));
                }
            }
            return brandList;
        }
    }

    /**
     * 从数据库查询数据
     * @param brandId
     * @return
     */
    private List<BrandVO> getBrandsFromDB(int brandId) {
        List<BrandVO> result = new ArrayList<>();
        //标记是否为全部,如果brandId不存在或等于99，则为全部
        boolean flag = false;
        BrandDictT brandDictT = brandDictTMapper.selectById(brandId);
        if (brandId == 99 || brandDictT == null || brandDictT.getUuid() == null) {
            flag = true;
        }
        List<BrandDictT> brandDictTs = brandDictTMapper.selectList(null);
        for (BrandDictT brandDictT1 : brandDictTs) {
            BrandVO brandVO = new BrandVO();
            brandVO.setBrandId(brandDictT1.getUuid() + "");
            brandVO.setBrandName(brandDictT1.getShowName());
            //此字段为包装类型，不设置的话就为空
            brandVO.setActive(false);
            if (flag) {
                if (brandDictT1.getUuid() == 99) {
                    brandVO.setActive(true);
                }
            } else {
                if (brandDictT1.getUuid() == brandId) {
                    brandVO.setActive(true);
                }
            }
            result.add(brandVO);
        }
        return result;
    }

    /**
     * 3. 获取行政区域列表,逻辑与getBrands类似
     * @param areaId
     * @return
     */
    @Override
    public List<AreaVO> getAreas(int areaId) {
        try (Jedis jedis = redisUtil.getJedis()) {
            List<AreaVO> areaList;
            String areaKey = "area:" + areaId + ":info:";
            String areaJson = jedis.get(areaKey);
            if (StringUtils.isNotEmpty(areaJson)) {
                areaList = JSON.parseArray(areaJson, AreaVO.class);
            } else {
                areaList = getAreasFromDB(areaId);

                int randomTime = 60 * 60 + new Random().nextInt(100);
                if (areaList != null) {
                    jedis.setex(areaKey, randomTime, JSON.toJSONString(areaList));
                } else {
                    jedis.setex(areaKey, randomTime, JSON.toJSONString(""));

                }
            }
            return areaList;
        }
    }

    private List<AreaVO> getAreasFromDB(int areaId) {
        List<AreaVO> result = new ArrayList<>();

        boolean flag = false;
        AreaDictT areaDictT = areaDictTMapper.selectById(areaId);
        if (areaId == 99 || areaDictT == null || areaDictT.getUuid() == null) {
            flag = true;
        }
        List<AreaDictT> areaDictTs = areaDictTMapper.selectList(null);
        for (AreaDictT areaDictT1 : areaDictTs) {
            AreaVO areaVO = new AreaVO();
            areaVO.setAreaName(areaDictT1.getShowName());
            areaVO.setAreaId(areaDictT1.getUuid() + "");
            areaVO.setActive(false);
            if (flag) {
                if (areaDictT1.getUuid() == 99) {
                    areaVO.setActive(true);
                }
            } else {
                if (areaDictT1.getUuid() == areaId) {
                    areaVO.setActive(true);
                }
            }
            result.add(areaVO);
        }
        return result;
    }

    /**
     * 4. 获取影厅类型列表
     * @param hallTypeId
     * @return
     */
    @Override
    public List<HallTypeVO> getHallTypes(int hallTypeId) {
        try (Jedis jedis = redisUtil.getJedis()) {
            List<HallTypeVO> hallTypeList;
            String hallTypeKey = "hallType:" + hallTypeId + ":info:";
            String hallTypeJson = jedis.get(hallTypeKey);
            if (StringUtils.isNotEmpty(hallTypeJson)) {
                hallTypeList = JSON.parseArray(hallTypeJson, HallTypeVO.class);
            } else {
                hallTypeList = getHallTypesFromDB(hallTypeId);
                int randomTime = 60 * 60 + new Random().nextInt(100);
                if (hallTypeList != null) {
                    jedis.setex(hallTypeKey, randomTime, JSON.toJSONString(hallTypeList));
                } else {
                    jedis.setex(hallTypeKey, randomTime, JSON.toJSONString(""));
                }
            }
            return hallTypeList;
        }
    }

    private List<HallTypeVO> getHallTypesFromDB(int hallTypeId) {
        List<HallTypeVO> result = new ArrayList<>();
        boolean flag = false;
        HallDictT hallDictT = hallDictTMapper.selectById(hallTypeId);
        if (hallTypeId == 99 || hallDictT == null || hallDictT.getUuid() == null) {
            flag = true;
        }
        List<HallDictT> hallDictTs = hallDictTMapper.selectList(null);
        for (HallDictT hallDictT1 : hallDictTs) {
            HallTypeVO hallTypeVO = new HallTypeVO();
            hallTypeVO.setHallTypeName(hallDictT1.getShowName());
            hallTypeVO.setHallTypeId(hallDictT1.getUuid() + "");
            hallTypeVO.setActive(false);
            if (flag) {
                if (hallDictT1.getUuid() == 99) {
                    hallTypeVO.setActive(true);
                }
            } else {
                if (hallDictT1.getUuid() == hallTypeId) {
                    hallTypeVO.setActive(true);
                }
            }
            result.add(hallTypeVO);
        }
        return result;
    }

        /**
         *  5. 根据影院编号获取影院信息
         * @param cinemaId
         * @return
         */
    @Override
    public CinemaInfoVO getCinemaInfoById(int cinemaId) {
        CinemaInfoVO cinemaInfoVO = new CinemaInfoVO();
        CinemaT cinemaT = cinemaTMapper.selectById(cinemaId);
        if (cinemaT == null) {
            return cinemaInfoVO;
        }
        cinemaInfoVO.setImgUrl(cinemaT.getImgAddress());
        cinemaInfoVO.setCinemaPhone(cinemaT.getCinemaPhone());
        cinemaInfoVO.setCinemaName(cinemaT.getCinemaName());
        cinemaInfoVO.setCinemaId(cinemaT.getUuid() + "");
        cinemaInfoVO.setCinemaAddress(cinemaT.getCinemaAddress());

        return cinemaInfoVO;
    }

    /**
     *  6. 根据影院编号获取所有电影信息和对应的放映场次信息
     * @param cinemaId
     * @return
     */
    @Override
    public List<FilmInfoVO> getFilmInfosByCinemaId(int cinemaId) {
        return fieldTMapper.getFilmInfos(cinemaId);
    }

    /**
     * 7. 根据放映场次ID获取影厅信息
     * @param fieldId
     * @return
     */
    @Override
    public HallInfoVO getHallInfoByFieldId(int fieldId) {
        return fieldTMapper.getHallInfo(fieldId);
    }

    /**
     * 8. 根据放映场次查询播放的电影编号，然后根据电影编号获取对应的电影信息
     * @param fieldId
     * @return
     */
    @Override
    public FilmInfoVO getFilmInfoByFieldId(int fieldId) {
        return fieldTMapper.getFilmInfoById(fieldId);
    }
}
