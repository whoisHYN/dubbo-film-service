package com.stylefeng.guns.rest.common.persistence.dao;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.rest.api.order.vo.OrderVO;
import com.stylefeng.guns.rest.common.persistence.model.OrderT;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    /**
     * 根据orderId查询订单信息，创建订单并返回订单信息时使用
     * @param orderId
     * @return
     */
    OrderVO getOrderInfoByOrderId(@Param("orderId") String orderId);

    /**
     * 根据用户id查询订单信息，用户查询订单信息时使用
     * @param userId
     * @param page
     * @return
     */
    List<OrderVO> getOrderInfosByUserId(@Param("userId") Integer userId, Page<OrderVO> page);

    /**
     * 查询已售出的座位
     * @param fieldId
     * @return
     */
    String getSoldSeatsByFieldId(@Param("fieldId") Integer fieldId);
}
