package com.stylefeng.guns.rest.api.order;

import com.stylefeng.guns.rest.api.order.vo.OrderVO;

import java.util.List;

/**
 * @Author: HYN
 * @Description:
 * @Date: 2020/4/11 1:07 下午
 * @Modified By:
 */
public interface OrderServiceAPI {

    /**
     * 验证售出的票是否为真
     * @param fieldId
     * @param seats
     * @return
     */
    boolean isTrueSeats(String fieldId, String seats);

    /**
     * 是否已售
     * @param fieldId
     * @param seats
     * @return
     */
    boolean isNotSoldSeats(String fieldId, String seats);

    /**
     * 创建订单信息
     * @param fieldId
     * @param soldSeats
     * @param seatsName
     * @param userId
     * @return
     */
    OrderVO saveOrderInfo(Integer fieldId, String soldSeats, String seatsName, Integer userId);

    /**
     * 根据登录用户的信息查询对应的订单信息
     * @param userId
     * @return
     */
    List<OrderVO> getOrdersByUserId(Integer userId);

    /**
     * 根据fieldId获取已售座位
     * @param fieldId
     * @return
     */
    String getSoldSeatsByFieldId(Integer fieldId);
}
