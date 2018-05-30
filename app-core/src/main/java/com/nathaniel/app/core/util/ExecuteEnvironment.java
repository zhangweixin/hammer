package com.nathaniel.app.core.util;

public enum ExecuteEnvironment {
    STANDARD("standard", "非web环境执行"),
    WEB("web", "web容器环境执行"),
    WEN_EMBED("web_embed", "内嵌web容器环境执行");

    private String key;
    private String display;

    ExecuteEnvironment(String key, String display) {
        this.key = key;
        this.display = display;
    }

    public String getKey() {
        return key;
    }

    public String getDisplay() {
        return display;
    }

    public static ExecuteEnvironment getInstance(String key) {
        for (ExecuteEnvironment environment : ExecuteEnvironment.values()) {
            if (environment.getKey().equals(key)) {
                return environment;
            }
        }
        return null;
    }
}
