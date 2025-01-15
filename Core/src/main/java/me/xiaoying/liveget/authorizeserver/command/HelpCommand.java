package me.xiaoying.liveget.authorizeserver.command;

import me.xiaoying.liveget.authorizeserver.LACore;
import me.xiaoying.liveget.authorizeserver.entity.CommandSender;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class HelpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String head, String[] args) {
        LACore.getLogger().println("&6Command numbers about: {}", LACore.getCommandManager().getCommands().size());
        LACore.getCommandManager().getCommands().forEach((string, cmd) -> {
            if (string.startsWith(LACore.getServer().getName().toLowerCase(Locale.ENGLISH) + ":"))
                string = string.replaceFirst(LACore.getServer().getName().toLowerCase(Locale.ENGLISH) + ":", "");
            LACore.getLogger().println("&8|- &e{} &8->&f {}\n   &8└─ Usage: &7{}", string, cmd.getDescription(), cmd.getUsage());
        });
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String head, String[] args) {
        return Collections.emptyList();
    }
}