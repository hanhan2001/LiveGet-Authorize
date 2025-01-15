package me.xiaoying.liveget.authorizeserver.command;

import me.xiaoying.liveget.authorizeserver.entity.CommandSender;

import java.util.ArrayList;
import java.util.List;

public abstract class Command {
    private final String name;
    private final String description;
    private final String usage;
    private final List<String> alias;
    private CommandExecutor commandExecutor;

    public Command(String name, String description, String usage) {
        this.name = name;
        this.description = description;
        this.usage = usage;
        this.alias = new ArrayList<>();
    }

    public Command(String name, String description, String usage, List<String> alias) {
        this.name = name;
        this.description = description;
        this.usage = usage;
        this.alias = alias;
    }

    /**
     * Get command's name
     *
     * @return name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get command's description
     *
     * @return description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Get command's usage
     *
     * @return usage
     */
    public String getUsage() {
        return this.usage;
    }

    /**
     * Get command's alias
     *
     * @return alias
     */
    public List<String> getAlias() {
        return this.alias;
    }

    public void setExecutor(CommandExecutor executor) {
        this.commandExecutor = executor;
    }

    public boolean execute(CommandSender sender, Command command, String head, String[] args) {
        if (this.commandExecutor == null)
            return false;

        return this.commandExecutor.onCommand(sender, command, head, args);
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String head, String[] args) {
        if (this.commandExecutor == null)
            return new ArrayList<>();

        return this.commandExecutor.onTabComplete(sender, command, head, args);
    }
}