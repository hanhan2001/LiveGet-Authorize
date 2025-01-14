package me.xiaoying.liveget.authorizeserver.command;

import me.xiaoying.liveget.authorizeserver.LACore;
import me.xiaoying.liveget.authorizeserver.entity.CommandSender;

import java.util.Collections;
import java.util.List;

public class HelpCommand extends Command {
    public HelpCommand(String name, String description, String usage, List<String> alias) {
        super(name, description, usage, alias);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String head, String[] args) {
        LACore.getLogger().println("&6Command numbers about: {}", LACore.getCommandManager().getCommands().size());
        LACore.getCommandManager().getCommands().forEach(cmd -> {
            Command _command = (Command) cmd;
            LACore.getLogger().println("&8|- &e{} &8->&f {}\n   &8└─ Usage: &7{}", _command.getName(), _command.getDescription(), _command.getUsage());
        });
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String head, String[] args) {
        return Collections.emptyList();
    }
}