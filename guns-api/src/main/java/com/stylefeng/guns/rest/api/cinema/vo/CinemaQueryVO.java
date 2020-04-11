package com.stylefeng.guns.rest.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: HYN
 * @Description:
 * @Date: 2020/4/8 9:51 上午
 * @Modified By:
 */
@Data
public class CinemaQueryVO implements Serializable {

    private static final long serialVersionUID = 5817044547503038407L;

    private Integer brandId = 99;
    private Integer hallType = 99;
    private Integer areaId = 99;
    private Integer pageSize = 12;
    private Integer nowPage = 1;

}
