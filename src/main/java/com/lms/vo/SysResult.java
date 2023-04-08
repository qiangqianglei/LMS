package com.lms.vo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 该类是系统级VO对象.
 *
 * @author Administrator
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class SysResult<T> {
    private Boolean success;  // 是否执行成功
    private Integer status;    //状态码 200成功  201失败 500服务器异常
    private String msg;    //提示信息
    private T data;   //返回数据
    public static final String OPERATE_FAIL = "操作失败";

    /**
     * 自定义返回结果
     */
    public static SysResult defineResult(boolean success, Integer status, String msg, Object data) {
        return new SysResult(success, status, msg, data);
    }

    public static SysResult exceptionResult() {
        return new SysResult(false, ResultCodeEnum.FAIL.getStatus(), OPERATE_FAIL, null);
    }

    /**
     * 使用 ResultCodeEnum 枚举中的返回结果
     */
    public static SysResult setResult(ResultCodeEnum resultCodeEnum) {
        return new SysResult(resultCodeEnum.getSuccess(), resultCodeEnum.getStatus(), resultCodeEnum.getMsg(), null);
    }
    public static SysResult setResult(ResultCodeEnum resultCodeEnum, String msg) {
        return new SysResult(resultCodeEnum.getSuccess(), resultCodeEnum.getStatus(), msg, null);
    }
    public static SysResult setResult(ResultCodeEnum resultCodeEnum, Object data) {
        return new SysResult(resultCodeEnum.getSuccess(), resultCodeEnum.getStatus(), resultCodeEnum.getMsg(), data);
    }
    public static SysResult setResult(ResultCodeEnum resultCodeEnum, String msg, Object data) {
        return new SysResult(resultCodeEnum.getSuccess(), resultCodeEnum.getStatus(), msg, data);
    }

    /**
     * 执行成功
     */
    public static SysResult success() {
        return new SysResult(ResultCodeEnum.SUCCESS.getSuccess(), ResultCodeEnum.SUCCESS.getStatus(), null, null);
    }
    public static SysResult success(String msg) {
        return new SysResult(ResultCodeEnum.SUCCESS.getSuccess(), ResultCodeEnum.SUCCESS.getStatus(), msg, null);
    }
    public static SysResult success(Object data) {
        return new SysResult(ResultCodeEnum.SUCCESS.getSuccess(), ResultCodeEnum.SUCCESS.getStatus(), null, data);
    }
    public static SysResult success(String msg, Object data) {
        return new SysResult(ResultCodeEnum.SUCCESS.getSuccess(), ResultCodeEnum.SUCCESS.getStatus(), msg, data);
    }


    /**
     * 执行失败
     */
    public static SysResult fail() {
        return new SysResult(ResultCodeEnum.FAIL.getSuccess(), ResultCodeEnum.FAIL.getStatus(), null, null);
    }
    public static SysResult fail(String msg) {
        return new SysResult(ResultCodeEnum.FAIL.getSuccess(), ResultCodeEnum.FAIL.getStatus(), msg, null);
    }
    public static SysResult fail(Object data) {
        return new SysResult(ResultCodeEnum.FAIL.getSuccess(), ResultCodeEnum.FAIL.getStatus(),null, data);
    }
    public static SysResult fail(String msg, Object data) {
        return new SysResult(ResultCodeEnum.FAIL.getSuccess(),  ResultCodeEnum.FAIL.getStatus(), msg, data);
    }

    /**
     * 系统异常
     */
    public static SysResult serverfail(String msg) {
        return new SysResult(ResultCodeEnum.SERVER_FAIL.getSuccess(), ResultCodeEnum.SERVER_FAIL.getStatus(), msg, null);
    }
    public static SysResult serverfail() {
        return new SysResult(ResultCodeEnum.SERVER_FAIL.getSuccess(), ResultCodeEnum.SERVER_FAIL.getStatus(),  ResultCodeEnum.SERVER_FAIL.getMsg(), null);
    }


}
