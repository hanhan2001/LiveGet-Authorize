package me.xiaoying.liveget.authorizeserver;

import me.xiaoying.liveget.authorizeserver.command.Command;

import java.util.List;

public class ServerCommand extends Command {
    public ServerCommand(String name, String description, String usage) {
        super(name, description, usage);
    }

    public ServerCommand(String name, String description, String usage, List<String> alias) {
        super(name, description, usage, alias);
    }
}