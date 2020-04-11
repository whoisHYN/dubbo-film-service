package com.stylefeng.guns.rest.api.film.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: HYN
 * @Description: 对应开发文档中影片详情查询接口应答报文中data中的info04部分的director和actors
 * @Date: 2020/4/7 5:55 下午
 * @Modified By:
 */
@Data
public class ActorRequestVO implements Serializable {

    private static final long serialVersionUID = -3315645436458590554L;

    private ActorVO director;
    private List<ActorVO> actors;

}
