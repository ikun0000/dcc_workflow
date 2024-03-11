package com.example.dccworkflow.enums;

public enum ClientState {
    NORMAL(0, "正常"),
    CLOSE(1, "关闭");

    private Integer state;
    private String content;

    ClientState(Integer state, String content) {
        this.state = state;
        this.content = content;
    }

    public Integer getState() {
        return state;
    }

    public String getContent() {
        return content;
    }
}
