package me.xiaoying.liveget.authorizeserver.command;

import me.xiaoying.liveget.authorizeserver.entity.CommandSender;

import java.util.List;

public interface CommandExecutor {
    boolean onCommand(CommandSender sender, Command command, String head, String[] args);

    List<String> onTabComplete(CommandSender sender, Command command, String head, String[] args);
}