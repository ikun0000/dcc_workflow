package com.example.dccworkflow.enums;

public enum ProjectState {
    UNDEFINE(0, "未定义"),
    NORMAL(1, "正常"),
    DELAY(2, "延迟"),
    SUSPEND(3, "暂停"),
    FINISH(4, "完成");

    private Integer code;
    private String msg;

    ProjectState(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static ProjectState of(Integer code) {
        if (code == null) {
            return UNDEFINE;
        }
        return switch (code) {
            case 1 -> NORMAL;
            case 2 -> DELAY;
            case 3 -> SUSPEND;
            case 4 -> FINISH;
            default -> UNDEFINE;
        };
    }
}
