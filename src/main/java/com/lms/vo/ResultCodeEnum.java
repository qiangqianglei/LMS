package com.lms.vo;

import lombok.Getter;

/**
 * @author wangdi
 * @date 21-4-9
 */
@Getter
public enum ResultCodeEnum {

    SUCCESS(true, 200,null),
    FAIL(false, 500,null),
    SERVER_FAIL(false, 500, null),
    FW_FAIL(false,409,null),
    UNAUTHORIZED(false,401,null),

    /*-----以下可以扩展------*/
    ACCESS_DENIED(false, 403, "无操作权限，如需开通权限，请联系管理员"),
    NOT_LOGIN(false, 530, "用户未登录"),
    RELOGIN_FAIL(false, 531, "用户续签失败"),
    INTERNAL_SERVER_ERROR(false, 500, "系统内部异常"),
    VALID_ERROR(false, 400, "参数校验错误"),

    UNKNOWN_REASON(false, 10001, "未知错误"),
    BAD_SQL_GRAMMAR(false, 11001, "sql语法错误"),
    JSON_PARSE_ERROR(false, 11002, "json解析异常"),
    PARAM_ERROR(false, 11003, "参数不正确"),
    FILE_UPLOAD_ERROR(false, 11004, "文件上传错误"),
    FILE_DEL_ERROR(false, 11007, "文件删除错误"),
    EXCEL_DATA_IMPORT_ERROR(false, 11005, "Excel数据导入错误"),
    PAGE_PARAMETER_ERROR(false, 11006, "请设置分页参数"),
    MAIL_ERROR(false, 11007, "邮件异常"),

    /*** 这里可以根据不同模块用不同的区级分开错误码，例如:  ***/
    // 1000～1999 区间表示auth模块错误
    // 2000～2999 区间表示fw模块错误
    // 3000～3999 区间表示net模块错误
    NETWORK_IN_USE(false, 3002, "无法删除网络。网络在使用中"),
    OUTER_NET_USER_ERROR(false, 3001, "外部网络不能绑定租户"),
    NOT_HAVE_ROUTE(false, 3003, "请确认路由是否正确设置"),
    NOT_MODIFY_USER(false, 3004, "该浮动IP已被绑定，无法修改用户"),
    DEFINE_NET_NOT_DEL(false, 3004, "内置网络无法操作"),
    // 4000～4999 区间表示ops模块错误
    // 5000～5999 区间表示opt模块错误
    // 6000～6999 区间表示sys模块错误
    FILE_BIG_ERROR(false, 6001, "文件大小不能超过2M"),
    FORMAT_ERROR(false, 6002, "文件格式不支持"),
    LICENSE_ERROR(false, 6003, "license证书无效，请核查服务器是否取得授权或重新申请证书！");
    // 。。。




    private Boolean success;
    private Integer status;
    private String msg;
    ResultCodeEnum(Boolean success, Integer status, String msg) {
        this.success = success;
        this.status = status;
        this.msg = msg;
    }
}