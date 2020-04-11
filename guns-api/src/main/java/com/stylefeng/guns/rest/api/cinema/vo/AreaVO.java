package com.stylefeng.guns.rest.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: HYN
 * @Description:
 * @Date: 2020/4/8 10:48 上午
 * @Modified By:
 */
@Data
public class AreaVO implements Serializable {
    private static final long serialVersionUID = 3156706137052862985L;

    private String areaId;
    private String areaName;
    private Boolean active;
}
