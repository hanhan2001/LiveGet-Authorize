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
    void registerCommand(String fallbackPrefix, Command command);

    /**
     * Register command
     *
     * @param plugin Plugin
     * @param command Command
     */
    void registerCommand(Plugin plugin, Command command);

    /**
     * Get command by name or alias
     *
     * @param command Command's name or alias
     * @return Command
     */
    Command getCommand(String command);

    /**
     * Get commands
     *
     * @return ArrayList
     */
    List<Command> getCommands();

    /**
     * Perform command
     *
     * @param command string
     * @return Determine command work
     */
    boolean dispatch(CommandSender sender, String command);
}