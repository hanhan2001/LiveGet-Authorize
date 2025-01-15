package me.xiaoying.liveget.authorizeserver;

import me.xiaoying.liveget.authorizeserver.command.*;
import me.xiaoying.liveget.authorizeserver.command.command.Command;
import me.xiaoying.liveget.authorizeserver.command.command.SCommand;
import me.xiaoying.liveget.authorizeserver.entity.CommandSender;
import me.xiaoying.liveget.authorizeserver.entity.ConsoleSender;
import me.xiaoying.liveget.authorizeserver.file.FileConfig;
import me.xiaoying.liveget.authorizeserver.file.FileLanguage;
import me.xiaoying.liveget.authorizeserver.file.FileManager;
import me.xiaoying.liveget.authorizeserver.file.SimpleFileManager;
import me.xiaoying.liveget.authorizeserver.plugin.PluginManager;
import me.xiaoying.liveget.authorizeserver.plugin.SimplePluginManager;
import me.xiaoying.liveget.authorizeserver.scheduler.ScheduledManager;
import me.xiaoying.liveget.authorizeserver.scheduler.SimpleSchedulerManager;
import me.xiaoying.liveget.authorizeserver.server.Server;
import me.xiaoying.liveget.authorizeserver.terminal.Terminal;
import me.xiaoying.liveget.authorizeserver.user.SimpleUserManager;
import me.xiaoying.liveget.authorizeserver.user.UserManager;
import me.xiaoying.logger.ChatColor;
import me.xiaoying.logger.event.EventHandle;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AuthorizeServer implements Server {
    private boolean running = false;

    private FileManager fileManager;
    private Terminal terminal;

    private UserManager userManager;
    private PluginManager pluginManager;
    private CommandManager commandManager;
    private ScheduledManager scheduledManager;

    private final CommandSender consoleSender = new ConsoleSender();

    private final List<SCommand> commands = new ArrayList<>();

    @Override
    public String getName() {
        return "Authorize";
    }

    @Override
    public void start() {
        if (this.running) {
            LACore.getLogger().warn("Server {} already running.", this.getName());
            return;
        }

        this.running = true;

        // initialize
        LACore.getLogger().info("Initialize...");
        this.initialize();
        LACore.getLogger().info("Starting server...");

        System.setProperty("server.address", FileConfig.SERVER_HOST);
        System.setProperty("server.port", String.valueOf(FileConfig.SERVER_PORT));

        SpringApplication springApplication = new SpringApplication(LiveGetAuthorizeServer.class);
        springApplication.setLogStartupInfo(false);
        springApplication.setBannerMode(Banner.Mode.OFF);
        springApplication.run();

        LACore.getLogger().info("Server is listening {} - {}", FileConfig.SERVER_HOST, FileConfig.SERVER_PORT);

        // terminal
        try { this.terminal.start(); } catch (IOException e) { throw new RuntimeException(e); }

        // hook of the server shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }

    @Override
    public void stop() {
        this.unInitialize();
    }

    @Override
    public UserManager getUserManager() {
        return this.userManager;
    }

    @Override
    public ScheduledManager getScheduledManager() {
        return this.scheduledManager;
    }

    @Override
    public PluginManager getPluginManager() {
        return this.pluginManager;
    }

    @Override
    public CommandManager getCommandManager() {
        return this.commandManager;
    }

    @Override
    public CommandSender getConsoleSender() {
        return this.consoleSender;
    }

    public void initialize() {
        // file
        LACore.getLogger().info("Loading file...");
        this.fileManager = new SimpleFileManager();
        this.fileManager.register(new FileConfig());
        this.fileManager.register(new FileLanguage());
        this.fileManager.loads();

        // managers
        LACore.getLogger().info("Setting default managers...");
        this.scheduledManager = new SimpleSchedulerManager();
        this.commandManager = new SimpleCommandManager();

        // commands
        LACore.getLogger().info("Registering default commands...");
        LACore.getCommandManager().registerCommand("authorize", new ServerCommand("stop", "A default command of server", "/stop"));
        LACore.getCommandManager().registerCommand("authorize", new ServerCommand("help", "A default command of server", "/help or /?", Collections.singletonList("?")));
        LACore.getCommandManager().registerCommand("authorize", new ServerCommand("plugins", "A default command of server", "/plugins or /pl", Collections.singletonList("pl")));

        LACore.getCommandManager().getCommand("authorize:stop").setExecutor(new StopCommand());
        LACore.getCommandManager().getCommand("authorize:help").setExecutor(new HelpCommand());
        LACore.getCommandManager().getCommand("authorize:plugins").setExecutor(new PluginCommand());

        // user manager
        this.userManager = new SimpleUserManager();

        // plugin manager
        this.pluginManager = new SimplePluginManager(this);
        File plugins = new File(LACore.getDataFolder(), "plugins");
        if (!plugins.exists()) plugins.mkdirs();
        this.pluginManager.loadPlugins(plugins);

        // terminal
        this.terminal = new Terminal();
        EventHandle.registerEvent(this.terminal);
    }

    public void unInitialize() {

    }

    public void registerCommand(SCommand scommand) {
        if (this.commands.contains(scommand))
            return;

        this.commands.add(scommand);

        Command annotation = scommand.getClass().getAnnotation(Command.class);

        if (annotation == null) {
            LACore.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&eFined some command(" + scommand.getClass().getName() + ") don't use Command annotation, please check your code!"));
            return;
        }

//        LACore.getCommandManager().registerCommand("authorize", new CommandExecutor() {
//            @Override
//            public boolean onCommand(CommandSender sender, me.xiaoying.liveget.authorizeserver.command.Command command, String head, String[] args) {
//                scommand.performCommand(sender, args);
//                return true;
//            }
//
//            @Override
//            public List<String> onTabComplete(CommandSender sender, me.xiaoying.liveget.authorizeserver.command.Command command, String head, String[] args) {
//                return scommand.onTabComplete(sender, command, head, args);
//            }
//        });
    }
}