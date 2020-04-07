package com.stylefeng.guns.api.film.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: HYN
 * @Description:
 * @Date: 2020/4/7 9:41 上午
 * @Modified By:
 */
@Data
public class ImgVO implements Serializable {

    private static final long serialVersionUID = 7775702272673775048L;

    private String mainImg;
    private String img01;
    private String img02;
    private String img03;
    private String img04;
}
