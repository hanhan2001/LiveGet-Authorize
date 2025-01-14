package me.xiaoying.liveget.authorizeserver.command;

import me.xiaoying.liveget.authorizeserver.entity.CommandSender;

import java.util.List;

public class UserCommand extends Command {
    public UserCommand(String name, String description, String usage) {
        super(name, description, usage);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String head, String[] args) {
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String head, String[] args) {
        return List.of();
    }
}