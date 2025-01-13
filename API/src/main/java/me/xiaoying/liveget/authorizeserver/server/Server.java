package me.xiaoying.liveget.authorizeserver.server;

import me.xiaoying.liveget.authorizeserver.command.CommandManager;
import me.xiaoying.liveget.authorizeserver.entity.CommandSender;
import me.xiaoying.liveget.authorizeserver.plugin.PluginManager;
import me.xiaoying.liveget.authorizeserver.scheduler.ScheduledManager;

public interface Server {
    String getName();

    void start();

    void stop();

    ScheduledManager getScheduledManager();

    PluginManager getPluginManager();

    CommandManager getCommandManager();

    CommandSender getConsoleCommandSender();
}