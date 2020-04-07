package com.stylefeng.guns.api.film.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: HYN
 * @Description:
 * @Date: 2020/4/4 6:30 下午
 * @Modified By:
 */
@Data
public class FilmVO implements Serializable {

    private static final long serialVersionUID = -890700722857993671L;
    private Integer filmNum;
    private Integer nowPage;
    private Integer totalPage;
    private List<FilmInfo> filmInfos;

}
