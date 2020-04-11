package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.rest.common.persistence.model.OrderT;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 订单信息表 Mapper 接口
 * </p>
 *
 * @author haungyaning
 * @since 2020-04-11
 */
public interface OrderTMapper extends BaseMapper<OrderT> {

    /**
     * 根据fieldId查询座位信息
     * @param fieldId
     * @return
     */
    String getSeatsByFieldId(@Param("fieldId") String fieldId);
}
