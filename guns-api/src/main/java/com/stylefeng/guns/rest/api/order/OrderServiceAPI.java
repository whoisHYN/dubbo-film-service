package com.stylefeng.guns.rest.api.order;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.rest.api.order.vo.OrderVO;


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
     * 根据用户id获取全部订单信息
     * @param userId
     * @param page
     * @return
     */
    Page<OrderVO> getOrdersByUserId(Integer userId, Page<OrderVO> page);

    /**
     * 根据fieldId获取已售座位
     * @param fieldId
     * @return
     */
    String getSoldSeatsByFieldId(Integer fieldId);

    /**
     * 根据订单编号获取订单信息
     * @param orderId
     * @return
     */
    OrderVO getOrderInfoById(String orderId);

    /**
     * 是否支付成功
     * @param orderId
     * @return
     */
    boolean paySuccess(String orderId);

    /**
     * 是否支付失败
     * @param orderId
     * @return
     */
    boolean payFail(String orderId);
}
