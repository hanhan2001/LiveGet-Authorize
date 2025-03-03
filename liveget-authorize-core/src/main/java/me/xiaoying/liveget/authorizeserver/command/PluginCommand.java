package me.xiaoying.liveget.authorizeserver.command;

import me.xiaoying.liveget.authorizeserver.LACore;
import me.xiaoying.liveget.authorizeserver.entity.CommandSender;
import me.xiaoying.liveget.authorizeserver.entity.ConsoleSender;
import me.xiaoying.liveget.authorizeserver.file.FileLanguage;
import me.xiaoying.liveget.authorizeserver.plugin.Plugin;

import java.util.Collections;
import java.util.List;

public class PluginCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String head, String[] args) {
        if (!(sender instanceof ConsoleSender)) {
            sender.sendMessage(FileLanguage.COMMAND_MISSING_PERMISSION);
            return false;
        }

        LACore.getLogger().println("&6Plugin numbers about: {}", LACore.getPluginManager().getPlugins().length);

        for (Plugin plugin : LACore.getPluginManager().getPlugins()) {
            StringBuilder author = new StringBuilder();
            for (int i = 0; i < plugin.getDescription().getAuthors().length; i++) {
                author.append(plugin.getDescription().getAuthors()[i]);

                if (i == plugin.getDescription().getAuthors().length - 1)
                    break;

                author.append(", ");
            }

            String nameColor = plugin.isEnabled() ? "&a" : "&c";
            LACore.getLogger().info("&8|- {}&f({}) &8- {}\n   &8└─ by {}", nameColor + plugin.getDescription().getName(), plugin.getDescription().getVersion(), plugin.getDescription().getDescription(), author.toString());
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String head, String[] args) {
        return Collections.emptyList();
    }
}