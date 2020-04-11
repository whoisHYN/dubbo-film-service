package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.rest.api.cinema.vo.FilmInfoVO;
import com.stylefeng.guns.rest.api.cinema.vo.HallInfoVO;
import com.stylefeng.guns.rest.common.persistence.model.FieldT;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 放映场次信息表 Mapper 接口
 * </p>
 *
 * @author haungyaning
 * @since 2020-04-08
 */
public interface FieldTMapper extends BaseMapper<FieldT> {

    /**
     * 根据cinemaId查询该影院所有电影及对应场次
     * @param cinemaId
     * @return
     */
    List<FilmInfoVO> getFilmInfos(@Param("cinemaId") int cinemaId);

    /**
     * 根据fieldId查询影厅场次信息
     * @param fieldId
     * @return
     */
    HallInfoVO getHallInfo(@Param("fieldId") int fieldId);

    /**
     * 根据fieldId获取电影信息FilmInfoVO
     * @param fieldId
     * @return
     */
    FilmInfoVO getFilmInfoById(@Param("fieldId") int fieldId);
}
