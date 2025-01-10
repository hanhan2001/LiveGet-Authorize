package me.xiaoying.liveget.authorizeserver.command;

public interface CommandManager {
    /**
     * Register command
     *
     * @param fallbackPrefix command's prefix
     * @param command Command
     */
    void registerCommand(String fallbackPrefix, Command command);

    /**
     * Perform command
     *
     * @param command string
     * @return Determine command work
     */
    boolean dispatch(String command);
}