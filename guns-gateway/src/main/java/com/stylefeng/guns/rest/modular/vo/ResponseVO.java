package com.stylefeng.guns.rest.modular.vo;


/**
 * @Author: HYN
 * @Description:
 * @Date: 2020/4/3 12:52 下午
 * @Modified By:
 */
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public M getData() {
        return data;
    }

    public void setData(M data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return super.toString() + "ResponseVO{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
