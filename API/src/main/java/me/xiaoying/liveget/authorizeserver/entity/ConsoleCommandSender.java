package me.xiaoying.liveget.authorizeserver.entity;

import me.xiaoying.liveget.authorizeserver.LACore;

public class ConsoleCommandSender implements CommandSender {
    @Override
    public String getName() {
        return "Console";
    }

    @Override
    public void sendMessage(String message) {
        LACore.getLogger().info(message);
    }
}