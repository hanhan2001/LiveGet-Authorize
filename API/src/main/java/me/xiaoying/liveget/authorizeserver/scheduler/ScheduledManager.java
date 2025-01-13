package me.xiaoying.liveget.authorizeserver.scheduler;

public interface ScheduledManager {
    /**
     * Stop task
     *
     * @param task Task's id
     */
    void cancelTask(int task);
}