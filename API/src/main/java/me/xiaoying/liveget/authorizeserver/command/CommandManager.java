package me.xiaoying.liveget.authorizeserver.command;

import me.xiaoying.liveget.authorizeserver.entity.CommandSender;

public interface CommandManager {
    /**
     * Register command
     *
     * @param fallbackPrefix command's prefix
     * @param command Command
     */
    void registerCommand(String fallbackPrefix, Command command);

    /**
     * Get command by name or alias
     *
     * @param command Command's name or alias
     * @return Command
     */
    Command getCommand(String command);

    /**
     * Perform command
     *
     * @param command string
     * @return Determine command work
     */
    boolean dispatch(CommandSender sender, String command);
}