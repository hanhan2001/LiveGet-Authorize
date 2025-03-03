package me.xiaoying.liveget.authorizeserver.plugin;

public class IllegalPluginAccessException extends RuntimeException {
    public IllegalPluginAccessException() {
    }

    public IllegalPluginAccessException(String msg) {
        super(msg);
    }
}