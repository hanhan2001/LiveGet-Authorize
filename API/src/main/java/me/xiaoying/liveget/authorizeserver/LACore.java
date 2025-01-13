package me.xiaoying.liveget.authorizeserver;

import me.xiaoying.liveget.authorizeserver.command.CommandManager;
import me.xiaoying.liveget.authorizeserver.entity.CommandSender;
import me.xiaoying.liveget.authorizeserver.plugin.PluginManager;
import me.xiaoying.liveget.authorizeserver.scheduler.ScheduledManager;
import me.xiaoying.liveget.authorizeserver.server.Server;
import me.xiaoying.logger.Logger;

import java.io.File;

public class LACore {
    private static final Logger logger = new Logger();

    private static Server server;

    /**
     * Set main server
     *
     * @param server Main server
     */
    protected static void setServer(Server server) {
        if (LACore.server != null)
            return;

        LACore.server = server;
    }

    /**
     * Get main server
     *
     * @return Main server
     */
    public static Server getServer() {
        return LACore.server;
    }

    /**
     * Get server running path in system
     *
     * @return File
     */
    public static File getDataFolder() {
        return new File(System.getProperty("user.dir"));
    }

    /**
     * Get command manager of server
     *
     * @return CommandManager
     */
    public static CommandManager getCommandManager() {
        return LACore.server.getCommandManager();
    }

    /**
     * Get scheduled manager of server
     *
     * @return ScheduledManager
     */
    public static ScheduledManager getScheduledManager() {
        return LACore.server.getScheduledManager();
    }

    /**
     * Get plugin manager of server
     *
     * @return PluginManager
     */
    public static PluginManager getPluginManager() {
        return LACore.server.getPluginManager();
    }

    /**
     * Get console command sender
     *
     * @return ConsoleCommandSender
     */
    public static CommandSender getConsoleCommandSender() {
        return LACore.getServer().getConsoleCommandSender();
    }

    public static Logger getLogger() {
        return LACore.logger;
    }
}