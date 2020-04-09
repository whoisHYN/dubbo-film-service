package com.stylefeng.guns.rest.modular.cinema.vo;

import com.stylefeng.guns.api.cinema.vo.CinemaInfoVO;
import com.stylefeng.guns.api.cinema.vo.FilmInfoVO;
import lombok.Data;

import java.util.List;

/**
 * @Author: HYN
 * @Description: 用于封装第三个接口的返回结果,包含影院信息和电影信息列表，电影信息又包含场次列表
 * @Date: 2020/4/9 4:01 下午
 * @Modified By:
 */
@Data
public class CinemaFieldsResponseVO {
    private CinemaInfoVO cinemaInfoVO;
    private List<FilmInfoVO> filmInfoList;
}
