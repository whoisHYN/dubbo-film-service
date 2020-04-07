package com.stylefeng.guns.api.film.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: HYN
 * @Description:
 * @Date: 2020/4/6 3:40 下午
 * @Modified By:
 */
@Data
public class FilmDetailVO implements Serializable {

    private static final long serialVersionUID = 4255779276000887787L;

    private String filmId;
    private String filmName;
    private String filmEngName;
    private String imgAddress;
    private String score;
    private String scoreNum;
    private String totalBox;
    private String info01;
    private String info02;
    private String info03;
    private InfoRequestVO info04;
}
