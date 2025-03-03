package me.xiaoying.liveget.authorizeserver.plugin;

import me.xiaoying.liveget.authorizeserver.command.Command;
import me.xiaoying.liveget.authorizeserver.scommand.SCommand;
import me.xiaoying.liveget.authorizeserver.server.Server;

import java.io.File;

/**
 * Plugin
 */
public interface Plugin {
    /**
     * Get plugin's data folder
     *
     * @return File
     */
    File getDataFolder();

    /**
     * PluginManager will call the method when the plugin loaded
     */
    void onLoad();

    /**
     * PluginManager will call the method when the plugin enable
     */
    void onEnable();

    /**
     * PluginManager will call the method when the plugin disable
     */
    void onDisable();

    /**
     * Determine the plugin enabled
     *
     * @return plugin enabled
     */
    boolean isEnabled();

    /**
     * Save default file of Config.yml where in plugin package
     */
    void saveConfig();

    /**
     * Save default file of Config.yml where in plugin package
     */
    void saveDefaultConfig();

    void saveResource(String filename, boolean replace);

    /**
     * Register command by me.xiaoying.liveget.authorizserver.command.Command
     *
     * @param command Command
     */
    void registerCommand(Command command);

    /**
     * Register command by me.xiaoying.liveget.authorizserver.scommand.SCommand
     *
     * @param command SCommand
     */
    void registerCommand(SCommand command);

    /**
     * Get server who loaded the plugin
     *
     * @return Who load the plugin
     */
    Server getServer();

    /**
     * Get plugin loader
     *
     * @return PluginLoader
     */
    PluginLoader getPluginloader();

    /**
     * Get plugin description
     *
     * @return PluginDescriptionFile
     */
    PluginDescriptionFile getDescription();
}