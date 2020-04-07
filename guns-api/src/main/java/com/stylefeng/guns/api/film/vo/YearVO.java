package com.stylefeng.guns.api.film.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: HYN
 * @Description:
 * @Date: 2020/4/5 2:45 下午
 * @Modified By:
 */
@Data
public class YearVO implements Serializable {
    private static final long serialVersionUID = 5286462175826954741L;

    private String yearId;
    private String yearName;
    private Boolean active;
}
