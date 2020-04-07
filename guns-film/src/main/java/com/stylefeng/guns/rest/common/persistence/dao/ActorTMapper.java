package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.api.film.vo.ActorVO;
import com.stylefeng.guns.rest.common.persistence.model.ActorT;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 演员表 Mapper 接口
 * </p>
 *
 * @author haungyaning
 * @since 2020-04-05
 */
public interface ActorTMapper extends BaseMapper<ActorT> {
    /**
     * 联合查询演员信息
     * @param filmId
     * @return
     */
    List<ActorVO> getActors(@Param("filmId") String filmId);
}
