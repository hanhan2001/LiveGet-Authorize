package me.xiaoying.liveget.authorizeserver.command;

import me.xiaoying.liveget.authorizeserver.entity.CommandSender;

import java.util.ArrayList;
import java.util.List;

public abstract class Command {
    private final String name;
    private final String description;
    private final String usage;
    private final List<String> alias;

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

    /**
     * Execute command
     *
     * @param sender Who called this command
     * @param args parameters
     */
    public abstract void execute(CommandSender sender, String[] args);
}