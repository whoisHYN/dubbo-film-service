package com.stylefeng.guns.rest.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: HYN
 * @Description: 影院电影信息
 * @Date: 2020/4/8 10:53 上午
 * @Modified By:
 */
@Data
public class FilmInfoVO implements Serializable {
    private static final long serialVersionUID = -8576885479845365066L;

    private String filmId;
    private String filmName;
    private String filmLength;
    private String filmType;
    private String filmCats;
    private String actors;
    private String imgAddress;
    private List<FilmFieldVO> filmFields;
}
