package me.xiaoying.liveget.authorizeserver.configuration.serialization;

import java.util.Map;

public interface ConfigurationSerializable {
    Map<String, Object> serialize();
}