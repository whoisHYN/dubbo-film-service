package com.stylefeng.guns.rest.modular.order;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.plugins.Page;
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

    @Reference(interfaceClass = OrderServiceAPI.class, check = false)
    private OrderServiceAPI orderServiceAPI;

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
}
