package me.xiaoying.liveget.authorizeserver.event.server;

import me.xiaoying.liveget.authorizeserver.event.Event;
import me.xiaoying.liveget.authorizeserver.event.HandlerList;
import me.xiaoying.liveget.authorizeserver.server.Server;

/**
 * Server event
 */
public class ServerEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final Server server;

    public ServerEvent(Server server) {
        this.server = server;
    }

    public Server getServer() {
        return this.server;
    }

    @Override
    public HandlerList getHandlers() {
        return ServerEvent.handlers;
    }
}