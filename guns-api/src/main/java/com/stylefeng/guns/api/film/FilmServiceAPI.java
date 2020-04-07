package com.stylefeng.guns.api.film;

import com.stylefeng.guns.api.film.vo.*;

import java.util.List;

/**
 * @Author: HYN
 * @Description:
 * @Date: 2020/4/4 8:48 下午
 * @Modified By:
 */
public interface FilmServiceAPI {
    /**
     * 获取banners
     * @return
     */
    List<BannerVO> getBanners();

    /**
     * 获取热门影片
     * @param isLimit 是否要限制数量，是否是首页
     * @param nums
     * @param nowPage
     * @param sortId
     * @param sourceId
     * @param yearId
     * @param catId
     * @return
     */
    FilmVO getHotFilms(boolean isLimit, int nums, int nowPage, int sortId, int sourceId, int yearId, int catId);

    /**
     * 获取即将上映影片
     * @param isLimit
     * @param nums
     * @param nowPage
     * @param sortId
     * @param sourceId
     * @param yearId
     * @param catId
     * @return
     */
    FilmVO getSoonFilms(boolean isLimit, int nums, int nowPage, int sortId, int sourceId, int yearId, int catId);

    /**
     * 获取经典影片
     * @param nums
     * @param nowPage
     * @param sortId
     * @param sourceId
     * @param yearId
     * @param catId
     * @return
     */
    FilmVO getClassicFilms(int nums, int nowPage, int sortId, int sourceId, int yearId, int catId);

    /**
     * 获取票房排行
     * @return
     */
    List<FilmInfo> getBoxRanking();

    /**
     * 获取人气排行
     * @return
     */
    List<FilmInfo> getExpectRanking();

    /**
     * 获取Top100
     * @return
     */
    List<FilmInfo> getTop();

    /*================获取影片条件接口==================*/

    /**
     * 分类条件
     * @return
     */
    List<CatVO> getCats();

    /**
     * 片源条件
     * @return
     */
    List<SourceVO> getSources();

    /**
     * 年代条件
     * @return
     */
    List<YearVO> getYears();

    /*=======================电影详细信息===========================*/
    /**
     * 根据影片id或名称获取影片详细信息
     * @param searchType
     * @param searchParam
     * @return
     */
    FilmDetailVO getFilmDetail(int searchType, String searchParam);

    /**
     * 查询影片描述信息
     * @param filmId
     * @return
     */
    FilmDescVO getFilmDesc(String filmId);

    /**
     * 查询影片图片信息
     * @param filmId
     * @return
     */
    ImgVO getImgs(String filmId);

    /**
     * 查询导演信息
     * @param filmId
     * @return
     */
    ActorVO getDirector(String filmId);

    /**
     * 查询演员信息
     * @param filmId
     * @return
     */
    List<ActorVO> getActors(String filmId);
}
