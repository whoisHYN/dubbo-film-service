package com.stylefeng.guns.rest.api.order.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: HYN
 * @Description:
 * @Date: 2020/4/11 1:14 下午
 * @Modified By:
 */
@Data
public class OrderVO implements Serializable {
    private static final long serialVersionUID = 4391118286377787222L;

    private String orderId;
    private String filmName;
    private String fieldTime;
    private String cinemaName;
    private String seatsName;
    private String orderPrice;
    private String orderTimestamp;
    private String orderStatus;
}
