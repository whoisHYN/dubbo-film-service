package com.stylefeng.guns.rest.api.film.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: HYN
 * @Description:
 * @Date: 2020/4/5 2:45 下午
 * @Modified By:
 */
@Data
public class SourceVO implements Serializable {
    private static final long serialVersionUID = 696612025443798290L;

    private String sourceId;
    private String sourceName;
    private Boolean active;
}
