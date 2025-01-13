package me.xiaoying.liveget.authorizeserver.event;

/**
 * Event cancellable
 */
public interface Cancellable {
    /**
     * Determine event is cancelled
     *
     * @return is event cancelled
     */
    boolean isCancelled();

    /**
     * Set cancel for the event
     *
     * @param cancelled cancel or not
     */
    void setCancelled(boolean cancelled);
}