package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: HYN
 * @Description:
 * @Date: 2020/4/8 10:46 上午
 * @Modified By:
 */
@Data
public class BrandVO implements Serializable {

    private static final long serialVersionUID = -6084747966907885964L;

    private String brandId;
    private String brandName;
    private Boolean active;
}
