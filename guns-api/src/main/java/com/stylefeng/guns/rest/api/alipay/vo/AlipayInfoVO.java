package com.stylefeng.guns.rest.api.alipay.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: HYN
 * @Description:
 * @Date: 2020/4/16 12:05 下午
 * @Modified By:
 */
@Data
public class AlipayInfoVO implements Serializable {
    private static final long serialVersionUID = -3426634710274809969L;

    private String orderId;
    private String QRCodeAddress;
}
