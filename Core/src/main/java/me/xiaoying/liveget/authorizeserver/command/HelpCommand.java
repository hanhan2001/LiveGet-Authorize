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
    public void execute(CommandSender sender, String[] args) {
        LACore.getLogger().println("&6Command numbers about: {}", LACore.getCommandManager().getCommands().size());
        LACore.getCommandManager().getCommands().forEach(command -> LACore.getLogger().println("&8|- &e{} &8->&f {}\n   &8└─ Usage: &7{}", command.getName(), command.getDescription(), command.getUsage()));
    }

    @Override
    public List<String> getTabComplete(CommandSender sender, Command command, String head, String[] args) {
        return Collections.emptyList();
    }
}