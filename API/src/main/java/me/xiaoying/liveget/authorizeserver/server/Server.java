package me.xiaoying.liveget.authorizeserver.server;

import me.xiaoying.liveget.authorizeserver.command.CommandManager;
import me.xiaoying.liveget.authorizeserver.entity.CommandSender;
import me.xiaoying.liveget.authorizeserver.plugin.PluginManager;
import me.xiaoying.liveget.authorizeserver.scheduler.ScheduledManager;
import me.xiaoying.liveget.authorizeserver.user.UserManager;

public interface Server {
    String getName();

    void start();

    void stop();

    UserManager getUserManager();

    ScheduledManager getScheduledManager();

    PluginManager getPluginManager();

    CommandManager getCommandManager();

    CommandSender getConsoleSender();
}