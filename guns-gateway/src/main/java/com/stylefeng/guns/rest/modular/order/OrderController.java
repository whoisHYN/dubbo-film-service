package com.stylefeng.guns.rest.modular.order;

import com.stylefeng.guns.rest.modular.vo.ResponseVO;
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
@RestController
@RequestMapping("/order/")
public class OrderController {

    @PostMapping("buyTickets")
    public ResponseVO<?> buyTickets(Integer fieldId, String soldSeats, String seatsName) {
        //1.验证售出的票是否为真(信息在json文件中)
        //2.查看已售座位中是否有这些票
        //3.创建订单信息
        return null;
    }

    @PostMapping("getOrderInfo")
    public ResponseVO<?> getOrderInfo(
            @RequestParam(name = "nowPage", required = false, defaultValue = "1") Integer nowPage,
            @RequestParam(name = "pageSize", required = false, defaultValue = "5") Integer pageSize
    ) {
        //1. 获取当前登录人的信息
        //2. 使用当前登录人获取订单信息
        return null;
    }
}
