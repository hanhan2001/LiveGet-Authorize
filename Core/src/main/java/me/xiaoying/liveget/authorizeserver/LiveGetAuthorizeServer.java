package me.xiaoying.liveget.authorizeserver;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiveGetAuthorizeServer {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(LiveGetAuthorizeServer.class);

        springApplication.setBannerMode(Banner.Mode.OFF);
    }
}