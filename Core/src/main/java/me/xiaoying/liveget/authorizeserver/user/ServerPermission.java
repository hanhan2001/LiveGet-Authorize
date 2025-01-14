package me.xiaoying.liveget.authorizeserver.user;

import com.google.gson.JsonObject;
import me.xiaoying.liveget.authorizeserver.file.FileConfig;
import me.xiaoying.liveget.authorizeserver.permission.Permission;
import me.xiaoying.liveget.authorizeserver.utils.DateUtil;

import java.util.Date;

public class ServerPermission implements Permission {
    private final String permission;
    private Date save;
    private Date over;

    public ServerPermission(String permission, Date save, Date over) {
        this.permission = permission;
        this.save = save;
        this.over = over;
    }

    /**
     * Get permission as string
     *
     * @return Permission key
     */
    public String getPermission() {
        return this.permission;
    }

    @Override
    public boolean isRental() {
        return this.save != null || this.over != null;
    }

    @Override
    public Date getSaveTime() {
        return this.save;
    }

    @Override
    public void setSaveTime(Date saveTime) {
        this.save = saveTime;
    }

    @Override
    public Date getOverTime() {
        return this.over;
    }

    @Override
    public void setOverTime(Date overTime) {
        this.over = overTime;
    }

    @Override
    public String toString() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("permission", this.getPermission());

        if (this.isRental()) {
            jsonObject.addProperty("save", DateUtil.getDate(this.save, FileConfig.SETTING_DATEFORMAT));
            jsonObject.addProperty("over", DateUtil.getDate(this.over, FileConfig.SETTING_DATEFORMAT));
        }
        return jsonObject.getAsString();
    }
}