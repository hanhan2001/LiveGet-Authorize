package me.xiaoying.liveget.authorizeserver.plugin;

import java.util.List;

/**
 * Description of plugin
 */
public class PluginDescription {
    private final String name;
    private final String version;
    private final String main;
    private final List<String> authors;

    public PluginDescription(String name, String version, String main, List<String> authors) {
        this.name = name;
        this.version = version;
        this.main = main;
        this.authors = authors;
    }

    public String getName() {
        return this.name;
    }

    public String getVersion() {
        return this.version;
    }

    public String getMain() {
        return this.main;
    }

    public List<String> getAuthors() {
        return this.authors;
    }
}