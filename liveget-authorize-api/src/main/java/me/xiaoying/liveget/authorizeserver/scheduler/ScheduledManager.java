package me.xiaoying.liveget.authorizeserver.scheduler;

import me.xiaoying.liveget.authorizeserver.plugin.Plugin;

public interface ScheduledManager {
    /**
     * Stop task
     *
     * @param task Task's id
     */
    void cancelTask(int task);

    /**
     * Stop task by plugin
     *
     * @param plugin Plugin
     */
    void cancelTask(Plugin plugin);

    /**
     * Sync task
     *
     * @param plugin Plugin
     * @param runnable codes
     * @return Task's id
     */
    int scheduleSyncDelayedTask(Plugin plugin, Runnable runnable);

    /**
     * Sync and delay task
     *
     * @param plugin Plugin
     * @param runnable codes
     * @param delay when to run the runnable(20tick = 1s)
     * @return Task's id
     */
    int scheduleSyncDelayedTask(Plugin plugin, Runnable runnable, long delay);

    /**
     * Sync, delay and repeat task
     *
     * @param plugin Plugin
     * @param runnable codes
     * @param delay when to run the runnable(20tick = 1s)
     * @param period interval tick(20tick = 1s)
     * @return Task's id
     */
    int scheduleSyncRepeatingTask(Plugin plugin, Runnable runnable, long delay, long period);

    /**
     * Async task
     *
     * @param plugin Plugins
     * @param runnable codes
     * @return Task's id
     */
    int scheduleAsyncDelayedTask(Plugin plugin, Runnable runnable);

    /**
     * Async and delay task
     *
     * @param plugin Plugin
     * @param runnable codes
     * @param delay when to run the runnable(20tick = 1s)
     * @return Task's id
     */
    int scheduleAsyncDelayedTask(Plugin plugin, Runnable runnable, long delay);

    /**
     * Async, delay and repeat task
     *
     * @param plugin Plugin
     * @param runnable codes
     * @param delay when to run the runnable(20tick = 1s)
     * @param period interval time(20tick = 1s)
     * @return Task's id
     */
    int scheduleAsyncRepeatingTask(Plugin plugin, Runnable runnable, long delay, long period);
}