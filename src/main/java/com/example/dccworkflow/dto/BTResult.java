package com.example.dccworkflow.dto;

import java.util.List;

public class BTResult<T> {
    private List<T> rows;
    private Long total;

    public BTResult() {
    }

    public BTResult(List<T> rows, Long total) {
        this.rows = rows;
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public static <T> BTResult<T> of(List<T> rows, Long total) {
        return new BTResult<>(rows, total);
    }
}
