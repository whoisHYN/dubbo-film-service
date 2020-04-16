package com.stylefeng.guns.rest.api.alipay;

import com.stylefeng.guns.rest.api.alipay.vo.AlipayInfoVO;
import com.stylefeng.guns.rest.api.alipay.vo.AlipayResultVO;

/**
 * @Author: HYN
 * @Description:
 * @Date: 2020/4/16 12:05 下午
 * @Modified By:
 */
public interface AlipayServiceAPI {

    /**
     * 获取支付信息
     * @param orderId
     * @return
     */
    AlipayInfoVO getQRCode(String orderId);

    /**
     * 获取支付结果
     * @param orderId
     * @return
     */
    AlipayResultVO getOrderStatus(String orderId);
}
