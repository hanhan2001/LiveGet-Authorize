package me.xiaoying.liveget.authorizeserver.user;

import com.google.gson.JsonArray;
import me.xiaoying.liveget.authorizeserver.LiveGetAuthorizeServer;
import me.xiaoying.liveget.authorizeserver.file.FileConfig;
import me.xiaoying.liveget.authorizeserver.permission.Permission;
import me.xiaoying.liveget.authorizeserver.utils.DateUtil;
import me.xiaoying.sql.sentence.Condition;
import me.xiaoying.sql.sentence.Update;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Server user
 */
public class ServerUser implements User {
    private final String uuid;
    private String name;
    private String password;
    private long phoneNumber;
    private String email;
    private String group;
    private Map<String, Permission> permissions;
    private String ip;
    private Date registerTime;
    private Date lastLoginTime;

    private boolean admin;

    public ServerUser(String uuid, String name, String email, String password, long phoneNumber, String group, Map<String, Permission> permissions, String ip, Date registerTime, Date lastLoginTime) {
        this.uuid = uuid;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.group = group;
        this.permissions = permissions;
        this.ip = ip;
        this.registerTime = registerTime;
        this.lastLoginTime = lastLoginTime;
    }

    /**
     * Get user's UUID
     *
     * @return User's uuid
     */
    @Override
    public String getUUID() {
        return this.uuid;
    }

    /**
     * Get user's name
     *
     * @return User's name
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Set user's name
     *
     * @param name User's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get user's password
     *
     * @return User's password
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Set user's password
     *
     * @param password New password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Get user's phone number
     *
     * @return User's phone number
     */
    @Override
    public long getPhoneNumber() {
        return this.phoneNumber;
    }

    /**
     * Set user's phone number
     *
     * @param phoneNumber User's phone number
     */
    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Get user's e-mail
     *
     * @return User's e-mail
     */
    @Override
    public String getEmail() {
        return this.email;
    }

    /**
     * Set user's e-mail
     *
     * @param email New e-mail
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get user's group
     *
     * @return User's group
     */
    @Override
    public String getGroup() {
        return this.group;
    }

    /**
     * Set user's group
     *
     * @param group New group
     */
    @Override
    public void setGroup(String group) {
        this.group = group;
    }

    /**
     * Get user's permissions
     *
     * @return User's permissions
     */
    @Override
    public Map<String, Permission> getPermissions() {
        return this.permissions;
    }

    /**
     * Set user's permissions
     *
     * @param permissions New permissions
     */
    public void setPermissions(Map<String, Permission> permissions) {
        this.permissions = permissions;
    }

    /**
     * Set permission to the user
     *
     * @param permission Permission
     */
    @Override
    public void setPermission(Permission permission) {
//        if (this.permissions.containsKey(permission.get))
//            return;
//
//        this.permissions.add(permission);
    }

    /**
     * Unset permission to the user
     *
     * @param permission Permission
     */
    @Override
    public void unsetPermission(Permission permission) {
        this.permissions.remove(permission);
    }

    @Override
    public boolean hasPermission(String permission) {
        return false;
    }

    /**
     * Determine whether the user is an admin
     *
     * @return Is the user an admin
     */
    @Override
    public boolean isAdmin() {
        return this.admin;
    }

    /**
     * Set the user is an admin
     *
     * @param isAdmin Is the user an admin
     */
    @Override
    public void setAdmin(boolean isAdmin) {
        this.admin = isAdmin;
    }

    /**
     * Get user's login ip
     *
     * @return User's login ip
     */
    @Override
    public String getIP() {
        return this.ip;
    }

    /**
     * Set user's login ip
     *
     * @param ip New login ip
     */
    public void setIP(String ip) {
        this.ip = ip;
    }

    /**
     * Get user's register time
     *
     * @return User's register time
     */
    @Override
    public Date getRegisterTime() {
        return this.registerTime;
    }

    /**
     * Set user's register time
     *
     * @param registerTime New register time
     */
    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    /**
     * Get user's last login time
     *
     * @return User's last login time
     */
    @Override
    public Date getLastLoginTime() {
        return this.lastLoginTime;
    }

    /**
     * Set user lastLoginTime
     *
     * @param lastLoginTime New login time
     */
    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    /**
     * Save information to database
     */
    public void save() {
        JsonArray permissions = new JsonArray();
        if (this.getPermissions().isEmpty())
            permissions.add("");
        else
            this.permissions.values().forEach(permission -> permissions.add(permission.toString()));

        Update update = new Update(FileConfig.SETTING_TABLE_USER);
        update.set("name", this.getName());
        update.set("password", this.getPassword());
        update.set("phone_number", this.getPhoneNumber());
        update.set("email", this.getEmail());
        update.set("group", this.getGroup());
        update.set("permissions", permissions.getAsString());
        update.set("ip", this.getIP());
        update.set("register_time", DateUtil.getDate(this.getRegisterTime(), FileConfig.SETTING_DATEFORMAT));
        update.set("last_login_time", DateUtil.getDate(this.getLastLoginTime(), FileConfig.SETTING_DATEFORMAT));
        update.set("photo_path", "");
        LiveGetAuthorizeServer.getSqlFactory().run(update.condition(new Condition("uuid", this.getUUID(), Condition.Type.EQUAL)));
    }
}