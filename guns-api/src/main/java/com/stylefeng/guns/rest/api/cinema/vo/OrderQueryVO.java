package com.stylefeng.guns.rest.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: HYN
 * @Description:
 * @Date: 2020/4/12 9:19 上午
 * @Modified By:
 */
@Data
public class OrderQueryVO implements Serializable {
    private static final long serialVersionUID = 4725858130233752789L;

    private String cinemaId;
    private String filmPrice;
}
