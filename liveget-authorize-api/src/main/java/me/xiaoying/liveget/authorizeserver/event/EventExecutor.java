package me.xiaoying.liveget.authorizeserver.event;

/**
 * Event executor
 */
public interface EventExecutor {
    void execute(Listener listener, Event event) throws EventException;
}