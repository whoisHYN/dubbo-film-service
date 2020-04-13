package com.stylefeng.guns.rest.modular.cinema;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.rest.api.cinema.CinemaServiceAPI;
import com.stylefeng.guns.rest.api.cinema.vo.CinemaQueryVO;
import com.stylefeng.guns.rest.api.cinema.vo.CinemaVO;
import com.stylefeng.guns.rest.api.cinema.vo.HallInfoVO;
import com.stylefeng.guns.rest.api.order.OrderServiceAPI;
import com.stylefeng.guns.rest.modular.cinema.vo.CinemaConditionResponseVO;
import com.stylefeng.guns.rest.modular.cinema.vo.CinemaFieldResponseVO;
import com.stylefeng.guns.rest.modular.cinema.vo.CinemaFieldsResponseVO;
import com.stylefeng.guns.rest.modular.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: HYN
 * @Description:
 * @Date: 2020/4/8 9:32 上午
 * @Modified By:
 */
@Slf4j
@RestController
@RequestMapping("/cinema/")
public class CinemaController {

    private static final String IMG_PRE = "http://img.meetingshop.cn/";

    @Reference(interfaceClass = CinemaServiceAPI.class, check = false)
    private CinemaServiceAPI cinemaServiceAPI;

    @Reference(interfaceClass = OrderServiceAPI.class, check = false)
    private OrderServiceAPI orderServiceAPI;

    /**
     * 查询影院列表
     * @param queryVO
     * @return
     */
    @GetMapping("getCinemas")
    public ResponseVO<?> getCinemas(CinemaQueryVO queryVO) {
        try {
            //按照查询条件进行查询
            Page<CinemaVO> cinemas = cinemaServiceAPI.getCinemas(queryVO);
            //判断是否有符合条件的结果
            if (cinemas.getRecords() == null || cinemas.getRecords().size() == 0) {
                return ResponseVO.success("没有影院可查");
            }
            return ResponseVO.success(cinemas.getCurrent(),
                    (int)cinemas.getPages(), IMG_PRE, cinemas.getRecords());

        } catch (Exception e) {
            //处理异常
            log.error("查询影院列表异常", e);
            return ResponseVO.serviceFail("查询影院列表失败");
        }
    }

    /**
     * 查询影院条件列表,按照区域、品牌和影厅类型分类
     * @param queryVO
     * @return
     */
    @GetMapping("getCondition")
    public ResponseVO<?> getCondition(CinemaQueryVO queryVO) {
        try {
            //封装查询结果
            CinemaConditionResponseVO conditionResponseVO = new CinemaConditionResponseVO();

            conditionResponseVO.setAreaList(cinemaServiceAPI.getAreas(queryVO.getAreaId()));
            conditionResponseVO.setBrandList(cinemaServiceAPI.getBrands(queryVO.getBrandId()));
            conditionResponseVO.setHallTypeList(cinemaServiceAPI.getHallTypes(queryVO.getHallType()));

            return ResponseVO.success(conditionResponseVO);
        } catch (Exception e) {
            log.error("查询影院条件列表异常", e);
            return ResponseVO.serviceFail("查询影院条件列表失败");
        }
    }

    /**
     * 查询影片场次
     * @param cinemaId
     * @return
     */
    @GetMapping("getFields")
    public ResponseVO<?> getFields(@RequestParam Integer cinemaId) {
        try {
            //封装查询结果
            CinemaFieldsResponseVO fieldsResponseVO = new CinemaFieldsResponseVO();

            fieldsResponseVO.setCinemaInfoVO(cinemaServiceAPI.getCinemaInfoById(cinemaId));
            fieldsResponseVO.setFilmInfoList(cinemaServiceAPI.getFilmInfosByCinemaId(cinemaId));

            return ResponseVO.success(IMG_PRE, fieldsResponseVO);
        } catch (Exception e) {
            log.error("查询影片场次异常", e);
            return ResponseVO.serviceFail("查询影片场次失败");
        }
    }

    @PostMapping("getFieldInfo")
    public ResponseVO<?> getFieldInfo(Integer cinemaId, Integer fieldId) {
        try {
            CinemaFieldResponseVO fieldResponseVO = new CinemaFieldResponseVO();
            HallInfoVO hallInfo = cinemaServiceAPI.getHallInfoByFieldId(fieldId);

            // 对接订单接口
            hallInfo.setSoldSeats(orderServiceAPI.getSoldSeatsByFieldId(fieldId));

            fieldResponseVO.setCinemaInfoVO(cinemaServiceAPI.getCinemaInfoById(cinemaId));
            fieldResponseVO.setFilmInfoVO(cinemaServiceAPI.getFilmInfoByFieldId(fieldId));
            fieldResponseVO.setHallInfoVO(hallInfo);

            return ResponseVO.success(IMG_PRE, fieldResponseVO);
        } catch (Exception e) {
            log.error("查询影片场次详细信息异常", e);
            return ResponseVO.serviceFail("查询影片场次详细信息失败");
        }
    }
}
