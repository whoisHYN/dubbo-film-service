package com.stylefeng.guns.rest.modular.order;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.plugins.Page;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.stylefeng.guns.rest.api.alipay.AlipayServiceAPI;
import com.stylefeng.guns.rest.api.alipay.vo.AlipayResultVO;
import com.stylefeng.guns.rest.api.order.OrderServiceAPI;
import com.stylefeng.guns.rest.api.order.vo.OrderVO;
import com.stylefeng.guns.rest.common.CurrentUser;
import com.stylefeng.guns.rest.modular.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: HYN
 * @Description:
 * @Date: 2020/4/11 12:37 下午
 * @Modified By:
 */
@Slf4j
@RestController
@RequestMapping("/order/")
public class OrderController {

    private static final String IMG_PRE = "http://img.meetingshop.cn/";

    @Reference(interfaceClass = OrderServiceAPI.class, check = false)
    private OrderServiceAPI orderServiceAPI;

    @Reference(interfaceClass = AlipayServiceAPI.class, check = false)
    private AlipayServiceAPI alipayServiceAPI;

    /**
     * 下单接口
     * @param fieldId
     * @param soldSeats
     * @param seatsName
     * @return
     */
    @HystrixCommand(fallbackMethod = "error", commandProperties = {
            @HystrixProperty(name = "execution.isolation.strategy", value = "THREAD"),
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "4000"),
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50")},
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "1"),
                    @HystrixProperty(name = "maxQueueSize", value = "10"),
                    @HystrixProperty(name = "keepAliveTimeMinutes", value = "1000"),
                    @HystrixProperty(name = "queueSizeRejectionThreshold", value = "8"),
                    @HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "12"),
                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "1500")
    })
    @PostMapping("buyTickets")
    public ResponseVO<?> buyTickets(Integer fieldId, String soldSeats, String seatsName) {
        try {
            //1.验证售出的票是否为真(信息在json文件中)
            boolean isTrue = orderServiceAPI.isTrueSeats(fieldId + "", soldSeats);
            //2.查看已售座位中是否有这些票
            boolean notSoldSeats = orderServiceAPI.isNotSoldSeats(fieldId + "", soldSeats);
            if (isTrue && notSoldSeats) {
                //3.根据当前登录用户创建订单信息
                String userId = CurrentUser.getUser();
                if (userId == null || userId.trim().length() == 0) {
                    return ResponseVO.serviceFail("用户未登录");
                }
                OrderVO orderVO = orderServiceAPI.saveOrderInfo(fieldId, soldSeats, seatsName, Integer.parseInt(userId));
                if (orderVO == null) {
                    log.error("下单失败");
                    return ResponseVO.serviceFail("下单失败");
                } else {
                    return ResponseVO.success(orderVO);
                }
            }
            return ResponseVO.serviceFail("订单中的座位编号有问题");
        } catch (NumberFormatException e) {
            log.error("购票业务异常", e);
            return ResponseVO.serviceFail("购票业务异常");
        }
    }

    public ResponseVO<?> error(Integer fieldId, String soldSeats, String seatsName) {
        return ResponseVO.serviceFail("抱歉，下单人数太多，请稍后再试");
    }

    /**
     * 查询当前用户所有订单信息
     * @param nowPage
     * @param pageSize
     * @return
     */
    @PostMapping("getOrderInfo")
    public ResponseVO<?> getOrderInfo(
            @RequestParam(name = "nowPage", required = false, defaultValue = "1") Integer nowPage,
            @RequestParam(name = "pageSize", required = false, defaultValue = "5") Integer pageSize
    ) {
        //1. 获取当前登录人的信息
        String userId = CurrentUser.getUser();
        if (userId == null || userId.trim().length() == 0) {
            return ResponseVO.serviceFail("用户未登录");
        }
        //2. 使用当前登录人获取订单信息
        Page<OrderVO> page = new Page<>(nowPage, pageSize);
        Page<OrderVO> result = orderServiceAPI.getOrdersByUserId(Integer.parseInt(userId), page);

        return ResponseVO.success(nowPage, (int) result.getPages(), "", result.getRecords());
    }

    /**
     * 查询当前用户支付信息，二维码
     * @param orderId
     * @return
     */
    @PostMapping("getPayInfo")
    public ResponseVO<?> getPayInfo(@RequestParam("orderId") String orderId) {
        // 获取当前登录人id
        String userId = CurrentUser.getUser();
        if (userId == null || userId.trim().length() == 0) {
            return ResponseVO.serviceFail("抱歉，用户未登录");
        }
        // 订单二维码返回结果
        return ResponseVO.success(IMG_PRE, alipayServiceAPI.getQRCode(orderId));
    }

    /**
     * 查询支付结果
     * @param orderId
     * @param tryNums
     * @return
     */
    @PostMapping("getPayResult")
    public ResponseVO<?> getPayResult(
            @RequestParam("orderId") String orderId,
            @RequestParam(value = "tryNums", required = false, defaultValue = "1") Integer tryNums) {

        // 获取当前登录人id
        String userId = CurrentUser.getUser();
        if (userId == null || userId.trim().length() == 0) {
            return ResponseVO.serviceFail("抱歉，用户未登录");
        }

        //判断支付是否超时
        if (tryNums > 3) {
            return ResponseVO.serviceFail("订单支付失败，请稍后再试");
        }
        AlipayResultVO resultVO = alipayServiceAPI.getOrderStatus(orderId);
        if (resultVO == null || resultVO.getOrderId() == null) {
            resultVO = new AlipayResultVO();
            resultVO.setOrderStatus(0);
            resultVO.setOrderMsg("支付不成功");
            resultVO.setOrderId(orderId);
            return ResponseVO.success(resultVO);
        }
        return ResponseVO.success(resultVO);
    }
}
