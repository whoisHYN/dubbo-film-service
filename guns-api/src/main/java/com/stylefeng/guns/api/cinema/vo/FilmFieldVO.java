package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: HYN
 * @Description: 电影场次
 * @Date: 2020/4/8 10:55 上午
 * @Modified By:
 */
@Data
public class FilmFieldVO implements Serializable {
    private static final long serialVersionUID = -6201125514161434833L;

    private String fieldId;
    private String beginTime;
    private String endTime;
    private String language;
    private String hallName;
    private String price;
}
