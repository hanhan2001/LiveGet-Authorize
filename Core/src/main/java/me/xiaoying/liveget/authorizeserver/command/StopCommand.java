package me.xiaoying.liveget.authorizeserver.command;

import me.xiaoying.liveget.authorizeserver.LACore;
import me.xiaoying.liveget.authorizeserver.entity.CommandSender;

import java.util.Collections;
import java.util.List;

public class StopCommand extends Command {
    public StopCommand(String name, String description, String usage) {
        super(name, description, usage);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        LACore.getServer().stop();
    }

    @Override
    public List<String> getTabComplete(CommandSender sender, Command command, String head, String[] args) {
        return Collections.emptyList();
    }
}