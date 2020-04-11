package com.stylefeng.guns.rest.api.film.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: HYN
 * @Description:
 * @Date: 2020/4/4 8:27 下午
 * @Modified By:
 */
@Data
public class FilmInfo implements Serializable {
    private static final long serialVersionUID = 2249044758164987610L;
    private String filmId;
    private Integer filmType;
    private String imgAddress;
    private String filmName;
    private String filmScore;
    private Integer expectNum;
    private String showTime;
    private int boxNum;
    private String score;
}
