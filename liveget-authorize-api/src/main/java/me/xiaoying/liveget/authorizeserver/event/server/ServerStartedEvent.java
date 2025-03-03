package me.xiaoying.liveget.authorizeserver.event.server;

import me.xiaoying.liveget.authorizeserver.server.Server;

/**
 * Server started event
 */
public class ServerStartedEvent extends ServerEvent {
    public ServerStartedEvent(Server server) {
        super(server);
    }
}