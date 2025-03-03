package me.xiaoying.liveget.authorizeserver.command;

import me.xiaoying.liveget.authorizeserver.entity.CommandSender;
import me.xiaoying.liveget.authorizeserver.plugin.Plugin;
import me.xiaoying.liveget.authorizeserver.scommand.SCommand;

import java.util.Map;

public interface CommandManager {
    /**
     * Register command
     *
     * @param plugin Plugin
     * @param command Command
     */
    void registerCommand(Plugin plugin, Command command);

    /**
     * Register command
     *
     * @param plugin Plugin
     * @param command SCommand
     */
    void registerCommand(Plugin plugin, SCommand command);

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
    Map<String, Command> getCommands();

    /**
     * Perform command
     *
     * @param command string
     * @return Determine command work
     */
    boolean dispatch(CommandSender sender, String command);
}