package com.stylefeng.guns.rest.api.film.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: HYN
 * @Description: 对应开发文档中影片详情查询接口应答报文中data中的info04部分
 * @Date: 2020/4/7 5:58 下午
 * @Modified By:
 */
@Data
public class InfoRequestVO implements Serializable {

    private static final long serialVersionUID = -3513350886151609872L;

    private String biography;
    private ActorRequestVO actors;
    private ImgVO imgVO;
    private String filmId;

}
