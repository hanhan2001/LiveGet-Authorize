package me.xiaoying.liveget.authorizeserver;

import java.util.Locale;

public class NamespacedKey {
    private final String namespacedKey;
    private final String suffix;

    public NamespacedKey(String namespacedKey, String suffix) {
        this.namespacedKey = namespacedKey.toLowerCase(Locale.ENGLISH);
        this.suffix = suffix.toLowerCase(Locale.ENGLISH);
    }

    public String getNamespacedKey() {
        return this.namespacedKey;
    }

    public String getSuffix() {
        return this.suffix;
    }

    @Override
    public boolean equals(Object obj) {
        return this.toString().equals(obj.toString());
    }

    @Override
    public String toString() {
        return this.namespacedKey + ":" + this.suffix;
    }
}