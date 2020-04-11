package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.rest.api.film.vo.FilmDetailVO;
import com.stylefeng.guns.rest.common.persistence.model.FilmT;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 影片主表 Mapper 接口
 * </p>
 *
 * @author haungyaning
 * @since 2020-04-05
 */
public interface FilmTMapper extends BaseMapper<FilmT> {

    /**
     * 根据名称查询
     * @param filmName
     * @return
     */
    FilmDetailVO getFilmDetailByName(@Param("filmName") String filmName);

    /**
     * 根据id查询
     * @param uuid
     * @return
     */
    FilmDetailVO getFilmDetailById(@Param("uuid") String uuid);
}
