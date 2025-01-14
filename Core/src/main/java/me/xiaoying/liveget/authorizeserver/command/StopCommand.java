package me.xiaoying.liveget.authorizeserver.command;

import me.xiaoying.liveget.authorizeserver.entity.CommandSender;

import java.util.Collections;
import java.util.List;

public class StopCommand extends Command {
    public StopCommand(String name, String description, String usage) {
        super(name, description, usage);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String head, String[] args) {
        System.exit(0);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String head, String[] args) {
        return Collections.emptyList();
    }
}