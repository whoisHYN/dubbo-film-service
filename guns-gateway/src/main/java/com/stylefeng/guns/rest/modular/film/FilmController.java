package com.stylefeng.guns.rest.modular.film;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.rpc.RpcContext;
import com.stylefeng.guns.api.film.FilmAsyncServiceAPI;
import com.stylefeng.guns.api.film.FilmServiceAPI;
import com.stylefeng.guns.api.film.vo.*;
import com.stylefeng.guns.rest.modular.film.vo.FilmConditionVO;
import com.stylefeng.guns.rest.modular.film.vo.FilmIndexVO;
import com.stylefeng.guns.rest.modular.film.vo.FilmRequestVO;
import com.stylefeng.guns.rest.modular.vo.ResponseVO;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @Author: HYN
 * @Description:
 * @Date: 2020/4/4 6:24 下午
 * @Modified By:
 */
@RestController
@RequestMapping("/film/")
public class FilmController {

    private static final String IMG_PRE = "http://img.meetingshop.cn/";

    /**
     * 同步调用
     */
    @Reference(interfaceClass = FilmServiceAPI.class)
    private FilmServiceAPI filmServiceAPI;

    /**
     * 异步调用
     */
    @Reference(interfaceClass = FilmAsyncServiceAPI.class, async = true)
    private FilmAsyncServiceAPI filmAsyncServiceAPI;

    /**
     * 1、功能聚合【API聚合】
     *    好处：
     *        1、六个接口，一次请求，同一时刻节省了五次HTTP请求
     *        2、同一个接口对外暴漏，降低了前后端分离开发的难度和复杂度
     *    坏处：
     *        1、一次获取数据
     * 服务聚合，将多个功能接口聚合到这一个接口中进行查询，比如这个方法是获取首页全部信息的，包括热门电影、即将上映，
     * 票房排行等，都在此接口中进行一次查询，将来首页是静态页面，节省服务器资源，缺点是如果其中一步阻塞，整个页面将无法
     * 显示，但是总体利大于弊
     * @return
     */
    @GetMapping("getIndex")
    private ResponseVO<?> getIndex() {
        FilmIndexVO filmIndexVO = new FilmIndexVO();
        // 获取banner信息
        filmIndexVO.setBanners(filmServiceAPI.getBanners());
        // 获取正在热映的电影
        filmIndexVO.setHotFilms(filmServiceAPI.getHotFilms(true, 8, 1, 1, 99, 99 ,99));
        // 即将上映的电影
        filmIndexVO.setSoonFilms(filmServiceAPI.getSoonFilms(true, 8, 1, 1, 99, 99 ,99));
        // 票房排行榜
        filmIndexVO.setBoxRanking(filmServiceAPI.getBoxRanking());
        // 获取受欢迎的榜单
        filmIndexVO.setExpectRanking(filmServiceAPI.getExpectRanking());
        // 获取前一百
        filmIndexVO.setTop100(filmServiceAPI.getTop());
        return ResponseVO.success(IMG_PRE, filmIndexVO);
    }

    /**
     * 影片条件列表，包括类型、年代、来源地等
     * @param catId
     * @param sourceId
     * @param yearId
     * @return
     */
    @GetMapping(value = "getConditionList")
    public ResponseVO<?> getConditionList(
            @RequestParam(value = "catId", required = false, defaultValue = "99") String catId,
            @RequestParam(value = "sourceId", required = false, defaultValue = "99") String sourceId,
            @RequestParam(value = "yearId", required = false, defaultValue = "99") String yearId) {

        FilmConditionVO filmConditionVO = new FilmConditionVO();
        List<CatVO> catVos = filmServiceAPI.getCats();
        List<CatVO> catResult = new ArrayList<>();
        //标记是否存在符合条件的catId
        boolean flag = false;
        CatVO cat = new CatVO();
        for (CatVO catVO : catVos) {
            //判断集合是否存在catId，如果存在，则将对应的实体变成active状态,否则将"全部（id为99）"设置为active,都需要添加进list
            if ("99".equals(catVO.getCatId())) {
                cat = catVO;
                continue;
            }
            if (catId.equals(catVO.getCatId())) {
                flag = true;
                catVO.setActive(true);
            } else {
                catVO.setActive(false);
            }
            catResult.add(catVO);
        }
        //再判断需不需要添加"全部"这个分类
        if (!flag) {
            cat.setActive(true);
        } else {
            cat.setActive(false);
        }
        catResult.add(cat);

        //片源集合,逻辑同上
        flag = false;
        List<SourceVO> sources = filmServiceAPI.getSources();
        List<SourceVO> sourceResult = new ArrayList<>();
        SourceVO source = new SourceVO();
        for (SourceVO sourceVO : sources) {
            if ("99".equals(sourceVO.getSourceId())) {
                source = sourceVO;
                continue;
            }
            if (sourceId.equals(sourceVO.getSourceId())) {
                flag = true;
                sourceVO.setActive(true);
            } else {
                sourceVO.setActive(false);
            }
            sourceResult.add(sourceVO);
        }
        if (!flag) {
            source.setActive(true);
        } else {
            source.setActive(false);
        }
        sourceResult.add(source);

        //年代集合,逻辑同上
        flag = false;
        List<YearVO> years = filmServiceAPI.getYears();
        List<YearVO> yearResult = new ArrayList<>();
        YearVO year = new YearVO();
        for (YearVO yearVO : years) {
            if ("99".equals(yearVO.getYearId())) {
                year = yearVO;
                continue;
            }
            if (yearId.equals(yearVO.getYearId())) {
                flag = true;
                yearVO.setActive(true);
            } else {
                yearVO.setActive(false);
            }
            yearResult.add(yearVO);
        }
        if (!flag) {
            year.setActive(true);
        } else {
            year.setActive(false);
        }
        yearResult.add(year);

        //集成结果并返回
        filmConditionVO.setCatInfo(catResult);
        filmConditionVO.setSourceInfo(sourceResult);
        filmConditionVO.setYearInfo(yearResult);
        return ResponseVO.success(filmConditionVO);
    }


    /**
     * 根据showType查询，按页显示热映影片、即将上映和经典影片
     * @param filmRequestVo
     * @return
     */
    @GetMapping("getFilms")
    public ResponseVO<?> getFilms(FilmRequestVO filmRequestVo) {

        FilmVO filmVO;
        //根据showType判断影片查询类型，1-热映 2-即将上映 3-经典，默认1-热映
        switch (filmRequestVo.getShowType()) {
            case 2:
                filmVO = filmServiceAPI.getSoonFilms(false, filmRequestVo.getPageSize(),
                        filmRequestVo.getNowPage(), filmRequestVo.getSortId(),
                        filmRequestVo.getSourceId(), filmRequestVo.getYearId(),
                        filmRequestVo.getCatId());
                break;
            case 3:
                filmVO = filmServiceAPI.getClassicFilms(filmRequestVo.getPageSize(),
                        filmRequestVo.getNowPage(), filmRequestVo.getSortId(),
                        filmRequestVo.getSourceId(), filmRequestVo.getYearId(),
                        filmRequestVo.getCatId());
                break;
            default:
                filmVO = filmServiceAPI.getHotFilms(false, filmRequestVo.getPageSize(),
                        filmRequestVo.getNowPage(), filmRequestVo.getSortId(),
                        filmRequestVo.getSourceId(), filmRequestVo.getYearId(),
                        filmRequestVo.getCatId());
                break;
        }
        return ResponseVO.success(filmVO.getNowPage(), filmVO.getTotalPage(),
                IMG_PRE, filmVO.getFilmInfos());
    }

    /**
     * 查询影片详情
     * @param searchParam
     * @param searchType
     * @return
     */
    @GetMapping("films/{searchParam}")
    public ResponseVO<?> films(@PathVariable("searchParam") String searchParam,
                               int searchType) throws ExecutionException, InterruptedException {
        //根据searchType，判断查询类型，1-按名称查询(redis或数据库)  1-按id查询(elasticSearch)
        //此对象还差一个info04字段信息，下面进行查询
        FilmDetailVO filmDetailVO = filmServiceAPI.getFilmDetail(searchType, searchParam);
        if (filmDetailVO == null) {
            return ResponseVO.serviceFail("没有可查询的影片");
        } else if (filmDetailVO.getFilmId() == null || filmDetailVO.getFilmId().trim().length() == 0) {
            return ResponseVO.serviceFail("没有可查询的影片");
        }
        String filmId = filmDetailVO.getFilmId();
        //info04对应InfoRequestVO对象,其中又包含ActorRequestVO，ImgVO，biography和filmId
        InfoRequestVO infoRequestVO = new InfoRequestVO();

        //异步获取影片描述信息
        Future<FilmDescVO> filmDescFuture = RpcContext.getContext().asyncCall(
                () -> filmAsyncServiceAPI.getFilmDesc(filmId));

        //异步获取影片图片信息
        Future<ImgVO> imgFuture = RpcContext.getContext().asyncCall(
                () -> filmAsyncServiceAPI.getImgs(filmId));

        //获取影片演员信息,对应ActorRequestVO对象，包含director和actors
        ActorRequestVO actorRequestVO = new ActorRequestVO();
        Future<ActorVO> directorFuture = RpcContext.getContext().asyncCall(
                () -> filmAsyncServiceAPI.getDirector(filmId));
        Future<List<ActorVO>> actorsFuture = RpcContext.getContext().asyncCall(
                () -> filmAsyncServiceAPI.getActors(filmId));

        actorRequestVO.setDirector(directorFuture.get());
        actorRequestVO.setActors(actorsFuture.get());

        //拼装infoRequestVO
        infoRequestVO.setActors(actorRequestVO);
        infoRequestVO.setBiography(filmDescFuture.get().getBiography());
        infoRequestVO.setFilmId(filmId);
        infoRequestVO.setImgVO(imgFuture.get());

        //拼装filmDetailVO
        filmDetailVO.setInfo04(infoRequestVO);

        return ResponseVO.success(IMG_PRE, filmDetailVO);
    }
}
