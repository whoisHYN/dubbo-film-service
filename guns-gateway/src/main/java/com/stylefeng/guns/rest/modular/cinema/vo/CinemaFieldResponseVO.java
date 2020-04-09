package com.stylefeng.guns.rest.modular.cinema.vo;

import com.stylefeng.guns.api.cinema.vo.CinemaInfoVO;
import com.stylefeng.guns.api.cinema.vo.FilmInfoVO;
import com.stylefeng.guns.api.cinema.vo.HallInfoVO;
import lombok.Data;

/**
 * @Author: HYN
 * @Description: 用于封装第四个接口的返回结果，包含一个FilmInfoVO，一个CinemaInfoVO和一个HallInfoVO
 * @Date: 2020/4/9 4:11 下午
 * @Modified By:
 */
@Data
public class CinemaFieldResponseVO {
    private FilmInfoVO filmInfoVO;
    private CinemaInfoVO cinemaInfoVO;
    private HallInfoVO hallInfoVO;
}
