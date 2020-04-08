package com.stylefeng.guns.api.cinema;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.cinema.vo.*;

import java.util.List;

/**
 * @Author: HYN
 * @Description:
 * @Date: 2020/4/8 10:01 上午
 * @Modified By:
 */
public interface CinemaServiceAPI {
    /*
    * 1. 根据CinemaQueryVO穿影院列表
    * 2. 根据条件获取品牌列表
    * 3. 获取行政区域列表
    * 4. 获取影厅类型列表
    * 5. 根据影院编号获取影院信息
    * 6. 根据影院编号获取所有电影信息和对应的放映场次信息
    * 7. 根据放映场次ID获取场次信息
    * 8. 根据放映场次查询播放的电影编号，然后根据电影编号获取对应的电影信息
    * */

    /**
     * 1. 根据CinemaQueryVO穿影院列表
     * @param cinemaQueryVO
     * @return
     */
    Page<CinemaVO> getCinemas(CinemaQueryVO cinemaQueryVO);

    /**
     * 2. 根据条件获取品牌列表，除了99，其他都是active
     * @param brandId
     * @return
     */
    List<BrandVO> getBrands(int brandId);

    /**
     * 3. 获取行政区域列表
     * @param areaId
     * @return
     */
    List<AreaVO> getAreas(int areaId);

    /**
     * 4. 获取影厅类型列表
     * @param hallTypeId
     * @return
     */
    List<HallTypeVO> hetHallTypes(int hallTypeId);

    /**
     *  5. 根据影院编号获取影院信息
     * @param cinemaId
     * @return
     */
    CinemaInfoVO getCinemaInfoById(int cinemaId);

    /**
     *  6. 根据影院编号获取所有电影信息和对应的放映场次信息
     * @param cinemaId
     * @return
     */
    List<FilmInfoVO> getFilmInfoByCinemaId(int cinemaId);

    /**
     * 7. 根据放映场次ID获取场次信息
     * @param fieldId
     * @return
     */
    FilmFieldVO getFilmField(int fieldId);

    /**
     * 8. 根据放映场次查询播放的电影编号，然后根据电影编号获取对应的电影信息
     * @param fieldId
     * @return
     */
    FilmInfoVO getFilmInfoByFieldId(int fieldId);
}
