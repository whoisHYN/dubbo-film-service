package com.stylefeng.guns.rest.modular.order.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.core.util.UUIDUtil;
import com.stylefeng.guns.rest.api.cinema.CinemaServiceAPI;
import com.stylefeng.guns.rest.api.cinema.vo.FilmInfoVO;
import com.stylefeng.guns.rest.api.cinema.vo.OrderQueryVO;
import com.stylefeng.guns.rest.api.order.OrderServiceAPI;
import com.stylefeng.guns.rest.api.order.vo.OrderVO;
import com.stylefeng.guns.rest.common.persistence.dao.OrderTMapper;
import com.stylefeng.guns.rest.common.persistence.model.OrderT;
import com.stylefeng.guns.rest.common.util.FtpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: HYN
 * @Description:
 * @Date: 2020/4/11 1:40 下午
 * @Modified By:
 */
@Slf4j
@Component
@Service(interfaceClass = OrderServiceAPI.class)
public class DefaultOrderServiceImpl implements OrderServiceAPI {

    private OrderTMapper orderTMapper;
    private FtpUtil ftpUtil;

    @Reference(interfaceClass = CinemaServiceAPI.class, check = false)
    private CinemaServiceAPI cinemaServiceAPI;

    @Autowired
    public DefaultOrderServiceImpl(OrderTMapper orderTMapper,
                                   FtpUtil ftpUtil) {
        this.orderTMapper = orderTMapper;
        this.ftpUtil = ftpUtil;
    }

    /**
     * 验证售出的票是否为真,是否符合条件,也就是在不在影厅的座位表里
     * @param fieldId
     * @param seats
     * @return
     */
    @Override
    public boolean isTrueSeats(String fieldId, String seats) {
        //1. 根据fieldId查询座位图
        String seatPath = orderTMapper.getSeatsByFieldId(fieldId);
//        System.out.println(seatPath);
        //2.读取位置图，判断seats是否为真
        String fileStrByAddress = ftpUtil.getFileStrByAddress(seatPath);
        JSONObject jsonObject = JSON.parseObject(fileStrByAddress);
        String ids = jsonObject.get("ids").toString();
        String[] seatsSplit = seats.split(",");
        String[] idsSplit = ids.split(",");
        int isTrue = 0;
        for (String seat : seatsSplit) {
            for (String id : idsSplit) {
                if (seat.equalsIgnoreCase(id)) {
                    isTrue++;
                }
            }
        }
        //完全匹配则返回true
        return isTrue == seatsSplit.length;
    }

    /**
     * 是否已售
     * @param fieldId
     * @param seats
     * @return
     */
    @Override
    public boolean isNotSoldSeats(String fieldId, String seats) {
        EntityWrapper<OrderT> wrapper = new EntityWrapper<>();
        wrapper.eq("field_id", fieldId);
        List<OrderT> orderTs = orderTMapper.selectList(wrapper);
        String[] seatSplit = seats.split(",");
        for (OrderT orderT : orderTs) {
            String seatsIds = orderT.getSeatsIds();
            String[] idSplit = seatsIds.split(",");
            for (String seat : seatSplit) {
                for (String id : idSplit) {
                    if (seat.equalsIgnoreCase(id)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * 创建订单信息
     * @param fieldId
     * @param soldSeats
     * @param seatsName
     * @param userId
     * @return
     */
    @Override
    public OrderVO saveOrderInfo(Integer fieldId, String soldSeats, String seatsName, Integer userId) {
        //主键编号
        String uuid = UUIDUtil.getUUID();
        //影片信息,包含你filmId
        FilmInfoVO filmInfo = cinemaServiceAPI.getFilmInfoByFieldId(fieldId);
        Integer filmId = Integer.parseInt(filmInfo.getFilmId());

        //影院信息,包含cinemaId和price
        OrderQueryVO orderQueryVO = cinemaServiceAPI.getOrderNeeds(fieldId);
        Integer cinemaId = Integer.parseInt(orderQueryVO.getCinemaId());
        double filmPrice = Double.parseDouble(orderQueryVO.getFilmPrice());
        //求订单金额，soldSeats = 1,2,3,4,5
        int sold = soldSeats.split(",").length;
        double totalPrice = getTotalPrice(sold, filmPrice);

        //创建订单对象
        OrderT orderT = new OrderT();
        orderT.setCinemaId(cinemaId);
        orderT.setFieldId(fieldId);
        orderT.setFilmId(filmId);
        orderT.setFilmPrice(filmPrice);
        orderT.setSeatsIds(soldSeats);
        orderT.setUuid(uuid);
        orderT.setOrderPrice(totalPrice);
        orderT.setSeatsName(seatsName);
        orderT.setOrderUser(userId);

        //插入订单
        Integer insert = orderTMapper.insert(orderT);
        if (insert > 0) {
            //返回查询结果
            OrderVO orderVO = orderTMapper.getOrderInfoByOrderId(uuid);
            if (orderVO == null || orderVO.getOrderId() == null) {
                log.error("订单信息查询失败，id为{}", uuid);
                return null;
            } else {
                return orderVO;
            }
        } else {
            log.error("订单插入失败");
            return null;
        }
    }

    private double getTotalPrice(int sold, double filmPrice) {
        BigDecimal soldDeci = new BigDecimal(sold);
        BigDecimal filmPriceDeci = new BigDecimal(filmPrice);

        BigDecimal product = soldDeci.multiply(filmPriceDeci);
        //四舍五入，取小数点后两位
        BigDecimal result = product.setScale(2, RoundingMode.HALF_UP);
        return result.doubleValue();
    }

    /**
     * 需要分页
     * @param userId
     * @param page
     * @return
     */
    @Override
    public Page<OrderVO> getOrdersByUserId(Integer userId, Page<OrderVO> page) {
        Page<OrderVO> result = new Page<>();
        if (userId == null) {
            log.error("订单查询失败，用户id未传入");
            return null;
        } else {
            List<OrderVO> orderVos = orderTMapper.getOrderInfosByUserId(userId, page);
            if (orderVos == null || orderVos.size() == 0) {
                result.setTotal(0);
                result.setRecords(new ArrayList<>());
                return result;
            } else {
                //查询总条数
                EntityWrapper<OrderT> wrapper = new EntityWrapper<>();
                wrapper.eq("order_user", userId);
                Integer count = orderTMapper.selectCount(wrapper);
                //把结果放入page
                result.setTotal(count);
                result.setRecords(orderVos);
                return result;
            }
        }
    }

    @Override
    public String getSoldSeatsByFieldId(Integer fieldId) {
        if (fieldId == null) {
            log.error("查询已售座位失败，为传入任何场次编号");
            return "";
        } else {
            return orderTMapper.getSoldSeatsByFieldId(fieldId);
        }
    }

    @Override
    public OrderVO getOrderInfoById(String orderId) {
        return orderTMapper.getOrderInfoByOrderId(orderId);
    }

    @Override
    public boolean paySuccess(String orderId) {
        OrderT orderT = new OrderT();
        orderT.setUuid(orderId);
        orderT.setOrderStatus(1);

        Integer integer = orderTMapper.updateById(orderT);
        return integer >= 1;
    }

    @Override
    public boolean payFail(String orderId) {
        OrderT orderT = new OrderT();
        orderT.setUuid(orderId);
        orderT.setOrderStatus(2);

        Integer integer = orderTMapper.updateById(orderT);
        return integer >= 1;
    }
}
