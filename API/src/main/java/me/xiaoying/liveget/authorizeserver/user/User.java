package me.xiaoying.liveget.authorizeserver.user;

import me.xiaoying.liveget.authorizeserver.permission.Permission;

import java.util.Date;
import java.util.List;

/**
 * User
 */
public interface User {
    /**
     * Get user's UUID
     *
     * @return User's uuid
     */
    String getUUID();

    /**
     * Get user's name
     *
     * @return User's name
     */
    String getName();

    /**
     * Get user's account
     *
     * @return User's account
     */
    String getAccount();

    /**
     * Get user's phone number
     *
     * @return User's phone number
     */
    long getPhoneNumber();

    /**
     * Get user's e-mail
     *
     * @return User's e-mail
     */
    String getEmail();

    /**
     * Get user's group
     *
     * @return User's group
     */
    String getGroup();

    /**
     * Set user's group
     *
     * @param group New group
     */
    void setGroup(String group);

    /**
     * Get user's permissions
     *
     * @return User's permissions
     */
    List<Permission> getPermissions();

    /**
     * Determine the user has the permission
     *
     * @param permission Permission
     * @return Is the user has the permission
     */
    boolean hasPermission(String permission);

    /**
     * Set permission to the user
     *
     * @param permission Permission
     */
    void setPermission(Permission permission);

    /**
     * Unset permission to the user
     *
     * @param permission Permission
     */
    void unsetPermission(Permission permission);

    /**
     * Determine whether the user is an admin
     *
     * @return Is the user an admin
     */
    boolean isAdmin();

    /**
     * Set the user is an admin
     *
     * @param isAdmin Is the user an admin
     */
    void setAdmin(boolean isAdmin);

    /**
     * Get user's login ip
     *
     * @return User's login ip
     */
    String getIP();

    /**
     * Get user's register time
     *
     * @return User's register time
     */
    Date getRegisterTime();

    /**
     * Get user's last login time
     *
     * @return User's last login time
     */
    Date getLastLoginTime();
}