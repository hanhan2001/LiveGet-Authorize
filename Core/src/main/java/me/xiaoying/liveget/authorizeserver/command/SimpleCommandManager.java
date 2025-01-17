package me.xiaoying.liveget.authorizeserver.command;

import me.xiaoying.liveget.authorizeserver.LACore;
import me.xiaoying.liveget.authorizeserver.NamespacedKey;
import me.xiaoying.liveget.authorizeserver.ServerCommand;
import me.xiaoying.liveget.authorizeserver.entity.CommandSender;
import me.xiaoying.liveget.authorizeserver.plugin.Plugin;
import me.xiaoying.liveget.authorizeserver.scommand.SCommand;

import java.util.*;

public class SimpleCommandManager implements CommandManager {
    private final Map<String, Command> knownCommands = new HashMap<>();

    public void registerCommand(String fallbackPrefix, Command command) {
        this.knownCommands.put(new NamespacedKey(fallbackPrefix, command.getName()).toString(), command);
    }

    @Override
    public void registerCommand(Plugin plugin, Command command) {
        this.knownCommands.put(new NamespacedKey(plugin.getDescription().getName(), command.getName()).toString(), command);
    }

    public void registerCommand(String fallbackPrefix, SCommand command) {
        me.xiaoying.liveget.authorizeserver.scommand.Command annotation = command.getClass().getAnnotation(me.xiaoying.liveget.authorizeserver.scommand.Command.class);

        if (annotation == null) {
            LACore.getLogger().warn("SCommand class of {} don't have me.xiaoying.liveget.authorizeserver.scommand.Command annotation!", command.getClass().getName());
            return;
        }

        if (annotation.values().length == 0)
            return;

        String commandName = annotation.values()[0];
        List<String> alias = new ArrayList<>();
        if (annotation.values().length > 1)
            alias.addAll(Arrays.asList(annotation.values()).subList(1, annotation.values().length));

        Command cmd;
        if (!alias.isEmpty())
            cmd = new ServerCommand(commandName, "", "", alias);
        else
            cmd = new ServerCommand(commandName, "", "");

        cmd.setExecutor(command.getTabExecutor());

        this.registerCommand(fallbackPrefix, cmd);
    }

    @Override
    public void registerCommand(Plugin plugin, SCommand command) {
        this.registerCommand(plugin.getDescription().getName(), command);
    }

    @Override
    public Command getCommand(String command) {
        if (command == null)
            return null;

        command = command.toLowerCase(Locale.ENGLISH);
        String origin = command;
        command = this.matchCommand(command);

        Command cmd;
        if ((cmd = this.knownCommands.get(command)) != null)
            return cmd;

        for (Command c : this.knownCommands.values()) {
            if (!c.getAlias().contains(origin))
                continue;

            return c;
        }

        return null;
    }

    @Override
    public Map<String, Command> getCommands() {
        return this.knownCommands;
    }

    @Override
    public boolean dispatch(CommandSender sender, String command) {
        String[] split = command.split(" ");
        String head = split[0];

        head = this.matchCommand(head);
        Command cmd = this.getCommand(head);

        if (cmd == null)
            return false;

        String[] parameters = {};
        if (split.length != 1)
            parameters = new ArrayList<>(Arrays.asList(split)).subList(1, split.length).toArray(new String[0]);

        try {
            cmd.execute(sender, cmd, head, parameters);
            return true;
        } catch (Exception e) {
            LACore.getLogger().error(e.getMessage());
        }

        return false;
    }

    private String matchCommand(String command) {
        if (command == null || command.contains(":"))
            return command;

        String prefix = null;
        for (String s : this.knownCommands.keySet()) {
            if (!s.endsWith(":" + command))
                continue;

            prefix = s.replace(":" + command, "");
        }

        if (prefix == null)
            prefix = "authorize";
        return prefix + ":" + command;
    }
}