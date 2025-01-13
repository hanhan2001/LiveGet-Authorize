package me.xiaoying.liveget.authorizeserver.terminal;

import me.xiaoying.liveget.authorizeserver.LACore;
import me.xiaoying.liveget.authorizeserver.command.Command;
import me.xiaoying.liveget.authorizeserver.scheduler.SimpleSchedulerManager;
import me.xiaoying.logger.LoggerFactory;
import me.xiaoying.logger.event.EventHandler;
import me.xiaoying.logger.event.Listener;
import me.xiaoying.logger.event.terminal.TerminalLogEndEvent;
import me.xiaoying.logger.event.terminal.TerminalWantLogEvent;
import org.jline.builtins.Completers;
import org.jline.reader.Completer;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Terminal implements Listener {
    private final String prompt = "> ";

    @EventHandler
    public void onTerminalWantLog(TerminalWantLogEvent event) {
        LACore.getLogger().print("\r");
    }

    public void start() throws IOException {
        Scanner scanner = new Scanner(System.in);

        org.jline.terminal.Terminal terminal = TerminalBuilder.builder().system(true).build();

        SimpleSchedulerManager scheduledManager = (SimpleSchedulerManager) LACore.getScheduledManager();
        scheduledManager.scheduleAsyncRepeatingTask(() -> {
            LACore.getLogger().print(this.prompt);

            String input = scanner.nextLine();

            String[] split = input.split(" ");
            String head = split[0];
            String[] args = {};

            if (split.length > 1)
                args = new ArrayList<>(Arrays.asList(split).subList(1, split.length)).toArray(new String[0]);

            LineReaderBuilder.builder().terminal(terminal).completer(this.getCompleter(head, args)).build();

            Command command = LACore.getCommandManager().getCommand(head);
            if (command == null) {
                LACore.getLogger().info("Unknown command. Type \"/help\" for help.");
                return;
            }

            try {
                command.execute(LACore.getConsoleCommandSender(), args);
            } catch (Exception e) {
                LACore.getLogger().warn(e.getMessage());
            }
        }, 0, 0);
    }

    private Completer getCompleter(String commandHead, String[] args) {
        Command command = LACore.getCommandManager().getCommand(commandHead);

        if (command == null)
            return null;

        List<Completers.TreeCompleter.Node> nodes = new ArrayList<>();
        command.getTabComplete(LACore.getConsoleCommandSender(), command, commandHead, args).forEach(string -> nodes.add(Completers.TreeCompleter.node(string)));
        return new Completers.TreeCompleter(nodes);
    }
}