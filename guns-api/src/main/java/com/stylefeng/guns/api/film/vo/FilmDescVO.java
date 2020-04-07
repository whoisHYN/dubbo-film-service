package com.stylefeng.guns.api.film.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: HYN
 * @Description:
 * @Date: 2020/4/7 9:40 上午
 * @Modified By:
 */
@Data
public class FilmDescVO implements Serializable {

    private static final long serialVersionUID = 6626923203214197123L;

    private String biography;
    private String filmId;
}
