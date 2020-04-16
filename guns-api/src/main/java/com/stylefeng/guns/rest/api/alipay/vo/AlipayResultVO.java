package com.stylefeng.guns.rest.api.alipay.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: HYN
 * @Description:
 * @Date: 2020/4/16 12:07 下午
 * @Modified By:
 */
@Data
public class AlipayResultVO implements Serializable {
    private static final long serialVersionUID = -5255019734923296565L;

    private String orderId;
    private Integer orderStatus;
    private String orderMsg;
}
