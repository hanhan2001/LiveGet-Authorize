package me.xiaoying.liveget.authorizeserver.entity;

public interface CommandSender {
    String getName();

    void sendMessage(String message);

    /**
     * Determine user is admin
     *
     * @return Is admin ot not
     */
    boolean isAdmin();

    /**
     * Get sender has permission
     *
     * @param permission Check permission
     * @return Has permission or not
     */
    boolean hasPermission(String permission);
}