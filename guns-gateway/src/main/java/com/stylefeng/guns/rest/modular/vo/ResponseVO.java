package com.stylefeng.guns.rest.modular.vo;


import lombok.Data;

/**
 * @Author: HYN
 * @Description:
 * @Date: 2020/4/3 12:52 下午
 * @Modified By:
 */
@Data
public class ResponseVO<M> {

    /**
     * 返回状态 0-成功，1-业务失败，999-系统异常
     */
    private Integer status;

    /**
     * 返回信息
     */
    private String msg;

    /**
     * 返回数据实体
     */
    private M data;

    /**
     * 首图地址
     */
    private String imgPre;

    /**
     * 当前页
     */
    private Integer nowPage;

    /**
     * 总页数
     */
    private Integer totalPage;

    private ResponseVO() {}

    /**
     * 成功
     * @param m
     * @param <M>
     * @return
     */
    public static <M> ResponseVO<M> success(M m) {
        ResponseVO<M> responseVO = new ResponseVO<>();
        responseVO.setStatus(0);
        responseVO.setData(m);
        return responseVO;
    }

    /**
     * 带首图地址的返回
     * @param imgPre
     * @param m
     * @param <M>
     * @return
     */
    public static <M> ResponseVO<M> success(String imgPre, M m) {
        ResponseVO<M> responseVO = new ResponseVO<>();
        responseVO.setStatus(0);
        responseVO.setData(m);
        responseVO.setImgPre(imgPre);
        return responseVO;
    }

    public static <M> ResponseVO<M> success(int nowPage, int totalPage, String imgPre, M m) {
        ResponseVO<M> responseVO = new ResponseVO<>();
        responseVO.setStatus(0);
        responseVO.setData(m);
        responseVO.setImgPre(imgPre);
        responseVO.setNowPage(nowPage);
        responseVO.setTotalPage(totalPage);
        return responseVO;
    }

    /**
     * 需要返回消息的success
     * @param msg
     * @param <M>
     * @return
     */
    public static <M> ResponseVO<M> success(String msg) {
        ResponseVO<M> responseVO = new ResponseVO<>();
        responseVO.setStatus(0);
        responseVO.setMsg(msg);
        return responseVO;
    }

    /**
     * 业务失败
     * @param msg
     * @param <M>
     * @return
     */
    public static <M> ResponseVO<M> serviceFail(String msg) {
        ResponseVO<M> responseVO = new ResponseVO<>();
        responseVO.setStatus(1);
        responseVO.setMsg(msg);
        return responseVO;
    }

    public static <M> ResponseVO<M> appFail(String msg) {
        ResponseVO<M> responseVO = new ResponseVO<>();
        responseVO.setStatus(999);
        responseVO.setMsg(msg);
        return responseVO;
    }
}
