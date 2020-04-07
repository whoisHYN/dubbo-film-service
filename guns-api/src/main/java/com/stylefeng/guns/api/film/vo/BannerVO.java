package com.stylefeng.guns.api.film.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: HYN
 * @Description:
 * @Date: 2020/4/4 6:23 下午
 * @Modified By:
 */
@Data
public class BannerVO implements Serializable {
    private static final long serialVersionUID = -4567604908960408638L;

    private String bannerId;
    private String bannerAddress;
    private String bannerUrl;
}
