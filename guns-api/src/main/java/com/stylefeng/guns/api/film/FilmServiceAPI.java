package com.stylefeng.guns.api.film;

import com.stylefeng.guns.api.film.vo.BannerVO;
import com.stylefeng.guns.api.film.vo.FilmInfo;
import com.stylefeng.guns.api.film.vo.FilmVO;

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
    BannerVO getBanners();

    /**
     * 获取热门影片
     * @param isLimit 是否要限制数量
     * @param nums 数量
     * @return
     */
    FilmVO getHotFilms(boolean isLimit, int nums);

    /**
     * 获取即将上映影片，按受欢迎程度排序
     * @param isLimit
     * @param nums
     * @return
     */
    FilmVO getSoonFilms(boolean isLimit, int nums);

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
}
