package com.example.dccworkflow.enums;

public enum ResultType {
    SUCCESS(0, "请求成功"),
    BRAND_CONSTRAINT(1, "该品牌被引用，无法删除"),
    PARENT_PROJECT_TYPE_REF(2, "项目类型已经被引用，无法删除"),
    SUB_PROJECT_TYPE_REF(3, "项目子类型已经被引用，无法删除"),
    ROLE_REF(4, "角色已被引用，无法删除"),
    USER_REF(5, "用户已被引用，无法删除");


    private Integer code;
    private String message;

    ResultType(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
