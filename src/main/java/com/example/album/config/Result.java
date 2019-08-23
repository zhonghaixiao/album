package cn.raiyee.ec.admin.base.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {

    public static final String RSPCODE_SUCCESS = "0";
    public static final String RSPCODE_FAILED = "1";
    public static final String RSPCODE_UNAUTHENTICATED = "11"; // 登录问题
    public static final String RSPCODE_AUTHORIZATION = "12"; // 权限问题
    public static final String RSPCODE_UNKNOWNACCOUNT = "13"; // 用户信息问题
    public static final String RSPCODE_FILE_MAX_SIZE = "14"; // 上传图片过大

    private String code, message;
    private Object data;

    public Result(String code, String message) {
        this.code = code;
        this.message = message;
    }


    public static Result fail(String msg) {
        return new Result(RSPCODE_FAILED, msg);
    }

    public static Result fail(String msg, Object object) {
        return new Result(RSPCODE_FAILED, msg, object);
    }

    public static Result fail(Object object) {
        return new Result(RSPCODE_FAILED, "请求失败", object);
    }

    public static Result ok(String msg) {
        return new Result(RSPCODE_SUCCESS, msg);
    }

    public static Result ok(String msg, Object object) {
        return new Result(RSPCODE_SUCCESS, msg, object);
    }

    public static Result ok(Object object) {
        return new Result(RSPCODE_SUCCESS, "请求成功", object);
    }

}
