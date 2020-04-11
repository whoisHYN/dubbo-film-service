package com.stylefeng.guns.rest.api.film.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: HYN
 * @Description:
 * @Date: 2020/4/7 9:43 上午
 * @Modified By:
 */
@Data
public class ActorVO implements Serializable {

    private static final long serialVersionUID = -1554316414549101851L;

    private String imgAddress;
    private String directorName;
    private String roleName;
}
