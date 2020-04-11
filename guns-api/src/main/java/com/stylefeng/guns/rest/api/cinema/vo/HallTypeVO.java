package com.stylefeng.guns.rest.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: HYN
 * @Description:
 * @Date: 2020/4/8 10:49 上午
 * @Modified By:
 */
@Data
public class HallTypeVO implements Serializable {
    private static final long serialVersionUID = 6276116391803263882L;

    private String hallTypeId;
    private String hallTypeName;
    private Boolean active;
}
