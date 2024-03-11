package com.example.dccworkflow.utils;

public class LikeWrap {
    public static String like(String s) {
        return "%" + s.replace("%", "\\%").replace("_", "\\_") + "%";
    }
}
