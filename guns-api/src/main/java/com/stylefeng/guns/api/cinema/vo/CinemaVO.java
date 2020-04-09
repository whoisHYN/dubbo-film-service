package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: HYN
 * @Description:
 * @Date: 2020/4/8 10:29 上午
 * @Modified By:
 */
@Data
public class CinemaVO implements Serializable {
    private static final long serialVersionUID = 740392238251003697L;
    private String uuid;
    private String cinemaName;
    private String address;
    private String minPrice;
}
