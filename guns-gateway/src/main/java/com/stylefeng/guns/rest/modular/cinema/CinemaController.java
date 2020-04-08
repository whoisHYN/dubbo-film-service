package com.stylefeng.guns.rest.modular.cinema;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.api.cinema.CinemaServiceAPI;
import com.stylefeng.guns.api.cinema.vo.CinemaQueryVO;
import com.stylefeng.guns.rest.modular.vo.ResponseVO;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: HYN
 * @Description:
 * @Date: 2020/4/8 9:32 上午
 * @Modified By:
 */
@RestController
@RequestMapping("/cinema/")
public class CinemaController {

    @Reference(interfaceClass = CinemaServiceAPI.class, check = false)
    private CinemaServiceAPI cinemaServiceAPI;

    @GetMapping("getCinemas")
    public ResponseVO<?> getCinemas(CinemaQueryVO queryVO) {
        return null;
    }

    public ResponseVO<?> getCondition(CinemaQueryVO queryVO) {
        return null;
    }

    @GetMapping("getFields")
    public ResponseVO<?> getFields(@RequestParam Integer cinemaId) {
        return null;
    }

    @PostMapping("getFieldInfo")
    public ResponseVO<?> getFieldInfo(Integer cinemaId, Integer filedId) {
        return null;
    }
}
