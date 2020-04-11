package com.stylefeng.guns.rest.api.film;

import com.stylefeng.guns.rest.api.film.vo.ActorVO;
import com.stylefeng.guns.rest.api.film.vo.FilmDescVO;
import com.stylefeng.guns.rest.api.film.vo.ImgVO;

import java.util.List;

/**
 * @Author: HYN
 * @Description: 本接口的几个方法既有可能是同步调用，也有可能是异步调用，异步调用就使用本接口
 * @Date: 2020/4/7 7:59 下午
 * @Modified By:
 */
public interface FilmAsyncServiceAPI {
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
