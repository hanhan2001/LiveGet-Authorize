package me.xiaoying.liveget.authorizeserver;

import me.xiaoying.liveget.authorizeserver.file.FileConfig;
import me.xiaoying.liveget.authorizeserver.file.FileManager;
import me.xiaoying.liveget.authorizeserver.file.SimpleFileManager;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
        springApplication.setBannerMode(Banner.Mode.OFF);
    }

    public static void initialize() {
        // file
        LiveGetAuthorizeServer.fileManager = new SimpleFileManager();
        LiveGetAuthorizeServer.fileManager.register(new FileConfig());

        // SqlFactory
//        switch (FileConfig.)
    }
}