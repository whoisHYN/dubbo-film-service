package com.stylefeng.guns.rest.modular.film.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.film.FilmServiceAPI;
import com.stylefeng.guns.api.film.vo.*;
import com.stylefeng.guns.core.util.DateUtil;
import com.stylefeng.guns.rest.common.persistence.dao.*;
import com.stylefeng.guns.rest.common.persistence.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: HYN
 * @Description:
 * @Date: 2020/4/4 11:35 下午
 * @Modified By:
 */
@Component
@Service(interfaceClass = FilmServiceAPI.class)
public class DefaultFilmServiceImpl implements FilmServiceAPI {

    private final BannerTMapper bannerTMapper;
    private final FilmTMapper filmTMapper;
    private final CatDictTMapper catDictTMapper;
    private final SourceDictTMapper sourceDictTMapper;
    private final YearDictTMapper yearDictTMapper;
    private final ActorTMapper actorTMapper;
    private final FilmInfoTMapper filmInfoTMapper;

    @Autowired
    public DefaultFilmServiceImpl(BannerTMapper bannerTMapper, FilmTMapper filmTMapper, CatDictTMapper catDictTMapper, SourceDictTMapper sourceDictTMapper, YearDictTMapper yearDictTMapper, ActorTMapper actorTMapper, FilmInfoTMapper filmInfoTMapper) {
        this.bannerTMapper = bannerTMapper;
        this.filmTMapper = filmTMapper;
        this.catDictTMapper = catDictTMapper;
        this.sourceDictTMapper = sourceDictTMapper;
        this.yearDictTMapper = yearDictTMapper;
        this.actorTMapper = actorTMapper;
        this.filmInfoTMapper = filmInfoTMapper;
    }

    /**
     * 获取电影海报列表
     * @return
     */
    @Override
    public List<BannerVO> getBanners() {
        List<BannerVO> bannerVOList = new ArrayList<>();
        List<BannerT> bannerTs = bannerTMapper.selectList(null);
        for (BannerT bannerT : bannerTs) {
            BannerVO bannerVO = new BannerVO();
            bannerVO.setBannerId(bannerT.getUuid() + "");
            bannerVO.setBannerAddress(bannerT.getBannerAddress());
            bannerVO.setBannerUrl(bannerT.getBannerUrl());
            bannerVOList.add(bannerVO);
        }
        return bannerVOList;
    }

    /**
     * 影片列表转换成影片信息列表
     * @param filmTs
     * @return
     */
    private List<FilmInfo> getFilmInfo(List<FilmT> filmTs) {
        List<FilmInfo> filmInfos = new ArrayList<>();
        for (FilmT filmT : filmTs) {
            FilmInfo filmInfo = new FilmInfo();

            filmInfo.setShowTime(DateUtil.getDay(filmT.getFilmTime()));
            filmInfo.setScore(filmT.getFilmScore());
            filmInfo.setImgAddress(filmT.getImgAddress());
            filmInfo.setFilmType(filmT.getFilmType());
            filmInfo.setFilmScore(filmT.getFilmScore());
            filmInfo.setFilmName(filmT.getFilmName());
            filmInfo.setFilmId(filmT.getUuid() + "");
            filmInfo.setExpectNum(filmT.getFilmPresalenum());
            filmInfo.setBoxNum(filmT.getFilmBoxOffice());
            //加进列表
            filmInfos.add(filmInfo);
        }
        return filmInfos;
    }

    /**
     * 获取热门影片
     * @param isLimit 是否要限制数量
     * @param nums 数量
     * @return
     */
    @Override
    public FilmVO getHotFilms(boolean isLimit, int nums, int nowPage, int sortId,
                              int sourceId, int yearId, int catId) {
        FilmVO filmVO = new FilmVO();
        List<FilmInfo> filmInfos;

        //根据数据库影片状态字段查询热门影片  影片状态,1-正在热映，2-即将上映，3-经典影片
        EntityWrapper<FilmT> wrapper = new EntityWrapper<>();
        wrapper.eq("film_status", "1");
        //判断是否是首页需要的内容
        if (isLimit) {
            //如果是，则限制条数，限制内容为热映影片
            Page<FilmT> page = new Page<>(1, nums);
            List<FilmT> filmTs = filmTMapper.selectPage(page, wrapper);
            //转换成影片信息列表
            filmInfos = getFilmInfo(filmTs);
            filmVO.setFilmInfos(filmInfos);
            filmVO.setFilmNum(filmInfos.size());

        } else {
            //如果不是，则是列表页，同样需要限制内容为热映影片
            Page<FilmT> page;
            //根据sortId的不同来构建Page对象
            // 1-按热门搜索  2-按时间搜索  3-按评价搜索
            switch (sortId) {
                case 2:
                    page = new Page<>(nowPage, nums, "film_time");
                    break;
                case 3:
                    page = new Page<>(nowPage, nums, "film_score");
                    break;
                default:
                    page = new Page<>(nowPage, nums, "film_box_office");
                    break;
            }
            if (sourceId != 99) {
                wrapper.eq("film_source", sourceId);
            }
            if (yearId != 99) {
                wrapper.eq("film_date", yearId);
            }
            if (catId != 99) {
                //电影表里cat的存储形式为 #2#4#22#
                String catStr = "%#" + catId + "#%";
                wrapper.like("film_cats", catStr);
            }
            List<FilmT> filmTs = filmTMapper.selectPage(page, wrapper);
            //转换成影片信息列表
            filmInfos = getFilmInfo(filmTs);

            filmVO.setFilmInfos(filmInfos);
            filmVO.setFilmNum(filmInfos.size());
            filmVO.setNowPage(nowPage);
            //设置总页数
            int totalCount = filmTMapper.selectCount(wrapper);
            int totalPage = totalCount / nums + 1;
            filmVO.setTotalPage(totalPage);

        }
        return filmVO;
    }

    /**
     * 查询即将上映的影片
     * @param isLimit
     * @param nums
     * @return
     */
    @Override
    public FilmVO getSoonFilms(boolean isLimit, int nums, int nowPage, int sortId,
                               int sourceId, int yearId, int catId) {
        FilmVO filmVO = new FilmVO();
        List<FilmInfo> filmInfos;

        //根据数据库影片状态字段查询影片  影片状态,1-正在热映，2-即将上映，3-经典影片
        EntityWrapper<FilmT> wrapper = new EntityWrapper<>();
        wrapper.eq("film_status", "2");
        //判断是否是首页需要的内容
        if (isLimit) {
            //如果是，则限制条数，限制内容为即将上映影片
            Page<FilmT> page = new Page<>(1, nums);
            List<FilmT> filmTs = filmTMapper.selectPage(page, wrapper);
            //转换成影片信息列表
            filmInfos = getFilmInfo(filmTs);
            filmVO.setFilmInfos(filmInfos);
            filmVO.setFilmNum(filmInfos.size());

        } else {
            //如果不是，则是列表页，同样需要限制内容为即将上映影片
            Page<FilmT> page;
            //根据sortId的不同来构建Page对象
            // 1-按热门搜索  2-按时间搜索  3-按评价搜索,即将上映的电影还没有票房和评分，所以都按预售处理
            if (sortId == 3) {
                page = new Page<>(nowPage, nums, "film_score");
            } else {
                page = new Page<>(nowPage, nums, "film_preSaleNum");
            }
            if (sourceId != 99) {
                wrapper.eq("film_source", sourceId);
            }
            if (yearId != 99) {
                wrapper.eq("film_date", yearId);
            }
            if (catId != 99) {
                //电影表里cat的存储形式为 #2#4#22#
                String catStr = "%#" + catId + "#%";
                wrapper.like("film_cats", catStr);
            }
            List<FilmT> filmTs = filmTMapper.selectPage(page, wrapper);
            //转换成影片信息列表
            filmInfos = getFilmInfo(filmTs);

            filmVO.setFilmInfos(filmInfos);
            filmVO.setFilmNum(filmInfos.size());
            filmVO.setNowPage(nowPage);
            //设置总页数
            int totalCount = filmTMapper.selectCount(wrapper);
            int totalPage = totalCount / nums + 1;
            filmVO.setTotalPage(totalPage);
        }
        return filmVO;
    }

    /**
     * 查询经典影片
     * @param nums
     * @param nowPage
     * @param sortId
     * @param sourceId
     * @param yearId
     * @param catId
     * @return
     */
    @Override
    public FilmVO getClassicFilms(int nums, int nowPage, int sortId,
                                  int sourceId, int yearId, int catId) {
        FilmVO filmVO =new FilmVO();
        List<FilmInfo> filmInfos;
        EntityWrapper<FilmT> wrapper = new EntityWrapper<>();;
        wrapper.eq("film_status", "3");
        Page<FilmT> page;
        //根据sortId的不同来构建Page对象
        // 1-按热门搜索  2-按时间搜索  3-按评价搜索
        switch (sortId) {
            case 2:
                page = new Page<>(nowPage, nums, "film_time");
                break;
            case 3:
                page = new Page<>(nowPage, nums, "film_score");
                break;
            default:
                page = new Page<>(nowPage, nums, "film_box_office");
                break;
        }
        if (sourceId != 99) {
            wrapper.eq("film_source", sourceId);
        }
        if (yearId != 99) {
            wrapper.eq("film_date", yearId);
        }
        if (catId != 99) {
            String catStr = "%#" + catId + "#%";
            wrapper.like("film_cats", catStr);
        }
        List<FilmT> filmTs = filmTMapper.selectPage(page, wrapper);
        filmInfos = getFilmInfo(filmTs);
        filmVO.setFilmInfos(filmInfos);
        filmVO.setNowPage(nowPage);
        filmVO.setFilmNum(filmInfos.size());
        int totalCount = filmTMapper.selectCount(wrapper);
        int totalPage = totalCount / nums + 1;
        filmVO.setTotalPage(totalPage);
        return filmVO;
    }

    /**
     * 查询正在上映的票房前十影片
     * @return
     */
    @Override
    public List<FilmInfo> getBoxRanking() {
        //条件——>正在上映，票房前十名
        EntityWrapper<FilmT> wrapper = new EntityWrapper<>();
        wrapper.eq("film_status", "1");
        //票房前十
        Page<FilmT> page = new Page<>(1, 10, "film_box_office");
        List<FilmT> filmTs = filmTMapper.selectPage(page, wrapper);

        return getFilmInfo(filmTs);
    }

    /**
     * 查询即将上映的预售前十影片
     * @return
     */
    @Override
    public List<FilmInfo> getExpectRanking() {
        //条件——>即将上映，预售前十名
        EntityWrapper<FilmT> wrapper = new EntityWrapper<>();
        wrapper.eq("film_status", "2");
        //预售前十
        Page<FilmT> page = new Page<>(1, 10, "film_preSaleNum");
        List<FilmT> filmTs = filmTMapper.selectPage(page, wrapper);

        return getFilmInfo(filmTs);
    }

    /**
     * 查询正在上映的评分前十影片
     * @return
     */
    @Override
    public List<FilmInfo> getTop() {
        //条件——>正在上映，评分前十名
        EntityWrapper<FilmT> wrapper = new EntityWrapper<>();
        wrapper.eq("film_status", "1");
        //评分前十
        Page<FilmT> page = new Page<>(1, 10, "film_score");
        List<FilmT> filmTs = filmTMapper.selectPage(page, wrapper);

        return getFilmInfo(filmTs);
    }

    /**
     * 获取所有影片类型category
     * @return
     */
    @Override
    public List<CatVO> getCats() {
        //查询出数据库实体再转化成表现层实体
        List<CatVO> catVos = new ArrayList<>();
        List<CatDictT> catDictTs = catDictTMapper.selectList(null);
        for (CatDictT catDictT : catDictTs) {
            CatVO catVO = new CatVO();
            catVO.setCatName(catDictT.getShowName());
            catVO.setCatId(catDictT.getUuid() + "");

            catVos.add(catVO);
        }
        return catVos;
    }

    /**
     * 获取所有影片来源
     * @return
     */
    @Override
    public List<SourceVO> getSources() {
        List<SourceVO> sourceVos = new ArrayList<>();
        List<SourceDictT> sourceDictTs = sourceDictTMapper.selectList(null);
        for (SourceDictT sourceDictT : sourceDictTs) {
            SourceVO sourceVO = new SourceVO();
            sourceVO.setSourceName(sourceDictT.getShowName());
            sourceVO.setSourceId(sourceDictT.getUuid() + "");

            sourceVos.add(sourceVO);
        }
        return sourceVos;
    }

    /**
     * 获取所有影片年代
     * @return
     */
    @Override
    public List<YearVO> getYears() {
        List<YearVO> yearVos = new ArrayList<>();
        List<YearDictT> yearDictTs = yearDictTMapper.selectList(null);
        for (YearDictT yearDictT : yearDictTs) {
            YearVO yearVO = new YearVO();
            yearVO.setYearName(yearDictT.getShowName());
            yearVO.setYearId(yearDictT.getUuid() + "");

            yearVos.add(yearVO);
        }
        return yearVos;
    }

    /**
     * 查询影片详情
     * @param searchType
     * @param searchParam
     * @return
     */
    @Override
    public FilmDetailVO getFilmDetail(int searchType, String searchParam) {
        FilmDetailVO filmDetailVO;
        //searchType,1-按名称查找  2-按id查找
        if (searchType == 1) {
            //sql语句中用"like"进行模糊匹配
            filmDetailVO = filmTMapper.getFilmDetailByName("%" + searchParam + "%");
        } else {
            filmDetailVO = filmTMapper.getFilmDetailById(searchParam);
        }
        return filmDetailVO;
    }

    /**
     * 从数据库根据filmId查询 FilmInfoT 对象
     * @param filmId
     * @return
     */
    private FilmInfoT getFilmInfo(String filmId) {
        FilmInfoT filmInfoT = new FilmInfoT();
        filmInfoT.setFilmId(filmId);
        return filmInfoTMapper.selectOne(filmInfoT);
    }

    /**
     * 获取影片描述信息
     * @param filmId
     * @return
     */
    @Override
    public FilmDescVO getFilmDesc(String filmId) {
        FilmDescVO filmDescVO = new FilmDescVO();
        FilmInfoT filmInfoT = getFilmInfo(filmId);
        filmDescVO.setBiography(filmInfoT.getBiography());
        filmDescVO.setFilmId(filmId);
        return filmDescVO;
    }

    /**
     * 获取影片图片地址
     * @param filmId
     * @return
     */
    @Override
    public ImgVO getImgs(String filmId) {
        ImgVO imgVO = new ImgVO();
        FilmInfoT filmInfoT = getFilmInfo(filmId);
        String filmImgs = filmInfoT.getFilmImgs();
        //图片地址是5个以","分割的url
        String[] imgs = filmImgs.split(",");
        int i = 0;
        imgVO.setMainImg(imgs[i++]);
        imgVO.setImg01(imgs[i++]);
        imgVO.setImg02(imgs[i++]);
        imgVO.setImg03(imgs[i++]);
        imgVO.setImg04(imgs[i]);
        return imgVO;
    }

    /**
     * 获取导演信息
     * @param filmId
     * @return
     */
    @Override
    public ActorVO getDirector(String filmId) {
        ActorVO actorVO = new ActorVO();
        FilmInfoT filmInfoT = getFilmInfo(filmId);
        //获取导演编号
        Integer directorId = filmInfoT.getDirectorId();
        ActorT actorT = actorTMapper.selectById(directorId);
        actorVO.setDirectorName(actorT.getActorName());
        actorVO.setImgAddress(actorT.getActorImg());
        return actorVO;
    }

    /**
     * 获取演员信息
     * @param filmId
     * @return
     */
    @Override
    public List<ActorVO> getActors(String filmId) {
        return actorTMapper.getActors(filmId);
    }
}
