package me.xiaoying.liveget.authorizeserver;

import me.xiaoying.liveget.authorizeserver.event.server.ServerStartedEvent;
import me.xiaoying.liveget.authorizeserver.file.FileConfig;
import me.xiaoying.liveget.authorizeserver.utils.EncryptUtil;
import me.xiaoying.sql.MysqlFactory;
import me.xiaoying.sql.SqlFactory;
import me.xiaoying.sql.SqliteFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.util.Locale;

@SpringBootApplication
public class LiveGetAuthorizeServer {
    public static void main(String[] args) {
        LACore.getLogger().setDateFormat("HH:mm:ss");

        LACore.setServer(new AuthorizeServer());
        LACore.getServer().start();
        LACore.getPluginManager().callEvent(new ServerStartedEvent(LACore.getServer()));
    }

    public static String passwordEncrypt(String password) {
        return switch (FileConfig.SETTING_PASSWORD_ENCRYPT.toUpperCase(Locale.ENGLISH)) {
            case "BASE64" -> EncryptUtil.base64Encrypt(password);
            case "MD5" -> EncryptUtil.md5Encrypt(password);
            default -> EncryptUtil.SHA256Encrypt(password);
        };
    }

    public static SqlFactory getSqlFactory() {
        return switch (FileConfig.SETTING_DATA_TYPE.toUpperCase(Locale.ENGLISH)) {
            case "MYSQL" -> new MysqlFactory(FileConfig.SETTING_DATA_MYSQL_HOSTNAME, FileConfig.SETTING_DATA_MYSQL_PORT, FileConfig.SETTING_DATA_MYSQL_DATABASE, FileConfig.SETTING_DATA_MYSQL_USERNAME, FileConfig.SETTING_PASSWORD_PASSWORD);
            case "SQLITE" -> new SqliteFactory(new File(FileConfig.SETTING_DATA_SQLITE));
            default -> new SqliteFactory(new File("./authorize.db"));
        };
    }
}