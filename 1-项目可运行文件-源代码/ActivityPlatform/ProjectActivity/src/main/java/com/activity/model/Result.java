package com.activity.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class Result<T> {
    @Schema(description = "状态码")
    private int code;    // 状态码
    @Schema(description = "返回的消息")
    private String message; // 返回消息
    @Schema(description = "返回的数据")
    private T data;     // 返回的数据

    // 使用枚举构造成功返回的结果
    public static <T> Result<T> success(T data) {
        return buildResult(ResultCode.SUCCESS, data);
    }

    // 使用枚举构造无数据的成功返回
    public static <T> Result<T> success() {
        return success(null);
    }

    // 使用枚举构造失败返回的结果
    public static <T> Result<T> error(ResultCode resultCode) {
        return buildResult(resultCode, null);
    }

    // 使用枚举构造失败返回的结果并自定义错误信息
    public static <T> Result<T> error(ResultCode resultCode, String message) {
        return buildResult(resultCode.getCode(), message, null);
    }

    // 构建Result对象
    private static <T> Result<T> buildResult(ResultCode resultCode, T data) {
        return buildResult(resultCode.getCode(), resultCode.getMessage(), data);
    }

    // 自定义状态码、消息、数据构建Result对象
    private static <T> Result<T> buildResult(int code, String message, T data) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        return result;
    }
}
