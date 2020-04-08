package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: HYN
 * @Description:
 * @Date: 2020/4/8 10:51 上午
 * @Modified By:
 */
@Data
public class CinemaInfoVO implements Serializable {
    private static final long serialVersionUID = 7158058319077621068L;

    private String cinemaId;
    private String imgUrl;
    private String cinemaName;
    private String cinemaAddress;
    private String cinemaPhone;
}
