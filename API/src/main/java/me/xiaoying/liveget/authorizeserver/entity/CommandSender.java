package me.xiaoying.liveget.authorizeserver.entity;

public interface CommandSender {
    String getName();

    void sendMessage(String message);
}