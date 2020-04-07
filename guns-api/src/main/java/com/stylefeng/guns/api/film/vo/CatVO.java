package com.stylefeng.guns.api.film.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: HYN
 * @Description:
 * @Date: 2020/4/5 2:43 下午
 * @Modified By:
 */
@Data
public class CatVO implements Serializable {
    private static final long serialVersionUID = -7475278867717942684L;

    private String catId;
    private String catName;
    private Boolean active;
}
