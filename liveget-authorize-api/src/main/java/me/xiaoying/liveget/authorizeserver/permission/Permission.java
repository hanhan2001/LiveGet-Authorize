package me.xiaoying.liveget.authorizeserver.permission;

import java.util.Date;

/**
 * Permission
 */
public interface Permission {
    /**
     * Get the key of the permission
     *
     * @return The key of the permission
     */
    String getPermission();

    /**
     * Determine whither the permission is rental
     *
     * @return Is the permission rental
     */
    boolean isRental();

    /**
     * Get save time of the permission
     *
     * @return The save time of the permission
     */
    Date getSaveTime();

    /**
     * Set the permission save time
     *
     * @param saveTime New save time
     */
    void setSaveTime(Date saveTime);

    /**
     * Get over time of the permission
     *
     * @return The over time of the permission
     */
    Date getOverTime();

    /**
     * Set the permission over time
     *
     * @param overTime New over time
     */
    void setOverTime(Date overTime);
}