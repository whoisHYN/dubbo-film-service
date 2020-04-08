package com.stylefeng.guns.rest.modular.cinema.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.cinema.CinemaServiceAPI;
import com.stylefeng.guns.api.cinema.vo.*;
import com.stylefeng.guns.rest.common.persistence.dao.*;
import com.stylefeng.guns.rest.common.persistence.model.AreaDictT;
import com.stylefeng.guns.rest.common.persistence.model.BrandDictT;
import com.stylefeng.guns.rest.common.persistence.model.CinemaT;
import com.stylefeng.guns.rest.common.persistence.model.HallDictT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: HYN
 * @Description:
 * @Date: 2020/4/8 1:17 下午
 * @Modified By:
 */
@Component
@Service(interfaceClass = CinemaServiceAPI.class)
public class DefaultCinemaServiceImpl implements CinemaServiceAPI {

    private final AreaDictTMapper areaDictTMapper;
    private final BrandDictTMapper brandDictTMapper;
    private final CinemaTMapper cinemaTMapper;
    private final FieldTMapper fieldTMapper;
    private final HallDictTMapper hallDictTMapper;
    private final HallFilmInfoTMapper hallFilmInfoTMapper;

    @Autowired
    public DefaultCinemaServiceImpl(AreaDictTMapper areaDictTMapper, BrandDictTMapper brandDictTMapper,
                                    CinemaTMapper cinemaTMapper, FieldTMapper fieldTMapper,
                                    HallDictTMapper hallDictTMapper, HallFilmInfoTMapper hallFilmInfoTMapper) {
        this.areaDictTMapper = areaDictTMapper;
        this.brandDictTMapper = brandDictTMapper;
        this.cinemaTMapper = cinemaTMapper;
        this.fieldTMapper = fieldTMapper;
        this.hallDictTMapper = hallDictTMapper;
        this.hallFilmInfoTMapper = hallFilmInfoTMapper;
    }

    /**
     * 1. 根据CinemaQueryVO穿影院列表
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
     * 2. 根据条件获取品牌列表，除了99，其他都是active
     * @param brandId
     * @return
     */
    @Override
    public List<BrandVO> getBrands(int brandId) {
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
    public List<HallTypeVO> hetHallTypes(int hallTypeId) {
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
    public List<FilmInfoVO> getFilmInfoByCinemaId(int cinemaId) {
        return null;
    }

    /**
     * 7. 根据放映场次ID获取场次信息
     * @param fieldId
     * @return
     */
    @Override
    public FilmFieldVO getFilmField(int fieldId) {
        return null;
    }

    /**
     * 8. 根据放映场次查询播放的电影编号，然后根据电影编号获取对应的电影信息
     * @param fieldId
     * @return
     */
    @Override
    public FilmInfoVO getFilmInfoByFieldId(int fieldId) {
        return null;
    }
}
