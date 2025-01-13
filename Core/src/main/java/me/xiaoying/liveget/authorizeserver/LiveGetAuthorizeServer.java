package me.xiaoying.liveget.authorizeserver;

import me.xiaoying.liveget.authorizeserver.command.SimpleCommandManager;
import me.xiaoying.liveget.authorizeserver.file.FileConfig;
import me.xiaoying.liveget.authorizeserver.file.FileManager;
import me.xiaoying.liveget.authorizeserver.file.SimpleFileManager;
import me.xiaoying.liveget.authorizeserver.scheduler.SimpleSchedulerManager;
import me.xiaoying.sql.MysqlFactory;
import me.xiaoying.sql.SqlFactory;
import me.xiaoying.sql.SqliteFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.util.Locale;

@SpringBootApplication
public class LiveGetAuthorizeServer {

    private static FileManager fileManager;

    public static void main(String[] args) {
        // initialize
        LACore.getLogger().info("Initialize...");
        LiveGetAuthorizeServer.initialize();

        System.setProperty("server.address", FileConfig.SERVER_HOST);
        System.setProperty("server.port", String.valueOf(FileConfig.SERVER_PORT));

        SpringApplication springApplication = new SpringApplication(LiveGetAuthorizeServer.class);
        springApplication.setLogStartupInfo(false);
        springApplication.setBannerMode(Banner.Mode.OFF);
        springApplication.run();
    }

    public static void initialize() {
        // file
        LiveGetAuthorizeServer.fileManager = new SimpleFileManager();
        LiveGetAuthorizeServer.fileManager.register(new FileConfig());
        LiveGetAuthorizeServer.fileManager.loads();

        // command manager
        LACore.setCommandManager(new SimpleCommandManager());

        // scheduler manager
        LACore.setScheduledManager(new SimpleSchedulerManager());
    }

    public static SqlFactory getSqlFactory() {
        return switch (FileConfig.SETTING_DATA_TYPE.toUpperCase(Locale.ENGLISH)) {
            case "MYSQL" -> new MysqlFactory(FileConfig.SETTING_DATA_MYSQL_HOSTNAME, FileConfig.SETTING_DATA_MYSQL_PORT, FileConfig.SETTING_DATA_MYSQL_DATABASE, FileConfig.SETTING_DATA_MYSQL_USERNAME, FileConfig.SETTING_PASSWORD_PASSWORD);
            case "SQLITE" -> new SqliteFactory(new File(FileConfig.SETTING_DATA_SQLITE));
            default -> new SqliteFactory(new File("./authorize.db"));
        };
    }
}