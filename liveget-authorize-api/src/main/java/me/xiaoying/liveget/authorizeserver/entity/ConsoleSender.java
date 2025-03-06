package me.xiaoying.liveget.authorizeserver.entity;

import me.xiaoying.liveget.authorizeserver.LACore;

public class ConsoleSender implements CommandSender {
    @Override
    public String getName() {
        return "Console";
    }

    @Override
    public void sendMessage(String message) {
        LACore.getLogger().info(message);
    }

    @Override
    public boolean isAdmin() {
        return true;
    }

    @Override
    public boolean hasPermission(String permission) {
        return true;
    }
}