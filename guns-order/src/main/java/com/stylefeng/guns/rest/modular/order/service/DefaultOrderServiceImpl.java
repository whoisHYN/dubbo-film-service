package com.stylefeng.guns.rest.modular.order.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.rest.api.order.OrderServiceAPI;
import com.stylefeng.guns.rest.api.order.vo.OrderVO;
import com.stylefeng.guns.rest.common.persistence.dao.OrderTMapper;
import com.stylefeng.guns.rest.common.persistence.model.OrderT;
import com.stylefeng.guns.rest.common.util.FtpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: HYN
 * @Description:
 * @Date: 2020/4/11 1:40 下午
 * @Modified By:
 */
@Component
@Service(interfaceClass = OrderServiceAPI.class)
public class DefaultOrderServiceImpl implements OrderServiceAPI {

    private OrderTMapper orderTMapper;
    private FtpUtil ftpUtil;

    @Autowired
    public DefaultOrderServiceImpl(OrderTMapper orderTMapper, FtpUtil ftpUtil) {
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

    @Override
    public OrderVO saveOrderInfo(Integer fieldId, String soldSeats, String seatsName, Integer userId) {
        return null;
    }

    @Override
    public List<OrderVO> getOrdersByUserId(Integer userId) {
        return null;
    }

    @Override
    public String getSoldSeatsByFieldId(Integer fieldId) {
        return null;
    }
}
