package com.stylefeng.guns.rest.modular.film.vo;

import lombok.Data;

/**
 * @Author: HYN
 * @Description:
 * @Date: 2020/4/5 8:04 下午
 * @Modified By:
 */
@Data
public class FilmRequestVO {
    private Integer showType = 1;
    private Integer sortId = 1;
    private Integer catId = 99;
    private Integer sourceId = 99;
    private Integer yearId = 99;
    private Integer nowPage = 1;
    private Integer pageSize = 18;
}
