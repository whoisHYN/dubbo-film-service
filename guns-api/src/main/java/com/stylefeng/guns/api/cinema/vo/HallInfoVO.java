package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: HYN
 * @Description:
 * @Date: 2020/4/8 12:21 下午
 * @Modified By:
 */
@Data
public class HallInfoVO implements Serializable {
    private static final long serialVersionUID = 482709851953246406L;

    private String hallFieldId;
    private String hallName;
    private String price;
    private String seatFile;
    private String soldSeats;
}
