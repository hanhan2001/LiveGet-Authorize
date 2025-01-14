package me.xiaoying.liveget.authorizeserver.command;

import me.xiaoying.liveget.authorizeserver.entity.CommandSender;
import me.xiaoying.liveget.authorizeserver.plugin.Plugin;

import java.util.List;

public interface CommandManager {
    /**
     * Register command
     *
     * @param fallbackPrefix command's prefix
     * @param command Command
     */
    void registerCommand(String fallbackPrefix, CommandExecutor command);

    /**
     * Register command
     *
     * @param plugin Plugin
     * @param command Command
     */
    void registerCommand(Plugin plugin, CommandExecutor command);

    /**
     * Get command by name or alias
     *
     * @param command Command's name or alias
     * @return CommandExecutor
     */
    CommandExecutor getCommand(String command);

    /**
     * Get commands
     *
     * @return ArrayList
     */
    List<CommandExecutor> getCommands();

    /**
     * Perform command
     *
     * @param command string
     * @return Determine command work
     */
    boolean dispatch(CommandSender sender, String command);
}