package me.xiaoying.liveget.authorizeserver;

import me.xiaoying.liveget.authorizeserver.command.CommandManager;
import me.xiaoying.logger.Logger;

import java.io.File;

public class LACore {
    private static final Logger logger = new Logger();

    private static CommandManager commandManager = null;

    /**
     * Get server running path in system
     *
     * @return File
     */
    public static File getDataFolder() {
        return new File(System.getProperty("user.dir"));
    }

    /**
     * Set command manager of server<br>
     * It will make server collapse, please use it carefully.
     *
     * @param commandManager CommandManager
     */
    public static void setCommandManager(CommandManager commandManager) {
        LACore.commandManager = commandManager;
    }

    /**
     * Get command manager of server
     *
     * @return CommandManager
     */
    public static CommandManager getCommandManager() {
        return LACore.commandManager;
    }

    public static Logger getLogger() {
        return LACore.logger;
    }
}