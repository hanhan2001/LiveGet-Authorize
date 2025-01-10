package me.xiaoying.liveget.authorizeserver;

import me.xiaoying.liveget.authorizeserver.command.CommandManager;
import me.xiaoying.liveget.authorizeserver.entity.CommandSender;
import me.xiaoying.liveget.authorizeserver.entity.ConsoleCommandSender;
import me.xiaoying.logger.Logger;

import java.io.File;

public class LACore {
    private static final Logger logger = new Logger();
    private static final CommandSender consoleCommandSender = new ConsoleCommandSender();

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

    /**
     * Get console command sender
     *
     * @return ConsoleCommandSender
     */
    public static CommandSender getConsoleCommandSender() {
        return LACore.consoleCommandSender;
    }

    public static Logger getLogger() {
        return LACore.logger;
    }
}