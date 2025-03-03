package me.xiaoying.liveget.authorizeserver.plugin;

import me.xiaoying.liveget.authorizeserver.command.Command;
import me.xiaoying.liveget.authorizeserver.scommand.SCommand;
import me.xiaoying.liveget.authorizeserver.server.Server;

import java.io.File;

/**
 * Plugin
 */
public interface Plugin {
    File getDataFolder();

    void onLoad();

    void onEnable();

    void onDisable();

    boolean isEnabled();

    void saveConfig();

    void saveDefaultConfig();

    void saveResource(String filename, boolean replace);

    void registerCommand(Command command);

    void registerCommand(SCommand command);

    Server getServer();

    PluginLoader getPluginloader();

    PluginDescriptionFile getDescription();
}