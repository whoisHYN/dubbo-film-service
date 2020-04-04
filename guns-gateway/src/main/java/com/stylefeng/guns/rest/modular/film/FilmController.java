package com.stylefeng.guns.rest.modular.film;

import com.stylefeng.guns.rest.modular.vo.ResponseVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: HYN
 * @Description:
 * @Date: 2020/4/4 6:24 下午
 * @Modified By:
 */
@RestController
@RequestMapping("/film/")
public class FilmController {


    /**
     * 1、功能聚合【API聚合】
     *    好处：
     *        1、六个接口，一次请求，同一时刻节省了五次HTTP请求
     *        2、同一个接口对外暴漏，降低了前后端分离开发的难度和复杂度
     *    坏处：
     *        1、一次获取数据
     * 服务聚合，将多个功能接口聚合到这一个接口中进行查询，比如这个方法是获取首页全部信息的，包括热门电影、即将上映，
     * 票房排行等，都在此接口中进行一次查询，将来首页是静态页面，节省服务器资源，缺点是如果其中一步阻塞，整个页面将无法
     * 显示，但是总体利大于弊
     * @return
     */
    @GetMapping("getIndex")
    private ResponseVO<?> getIndex() {
        // 获取banner信息

        // 获取正在热映的电影
        // 即将上映的电影
        // 票房排行榜
        // 获取受欢迎的榜单
        // 获取前一百
        return null;
    }
}
