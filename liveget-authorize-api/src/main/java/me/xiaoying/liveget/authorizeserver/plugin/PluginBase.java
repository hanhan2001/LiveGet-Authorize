package me.xiaoying.liveget.authorizeserver.plugin;

public abstract class PluginBase implements Plugin {
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public final String getName() {
        return this.getDescription().getName();
    }
}