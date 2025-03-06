package me.xiaoying.liveget.authorizeserver.terminal;

import me.xiaoying.liveget.authorizeserver.LACore;
import me.xiaoying.liveget.authorizeserver.command.Command;
import me.xiaoying.liveget.authorizeserver.command.CommandExecutor;
import me.xiaoying.liveget.authorizeserver.scheduler.SimpleSchedulerManager;
import me.xiaoying.logger.event.EventHandler;
import me.xiaoying.logger.event.Listener;
import me.xiaoying.logger.event.terminal.TerminalWantLogEvent;
import org.jline.builtins.Completers;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Terminal implements Listener {
    private final String prompt = "> ";

    @EventHandler
    public void onTerminalWantLog(TerminalWantLogEvent event) {
        LACore.getLogger().print("\r");
    }

    public void start() throws IOException {
        org.jline.terminal.Terminal terminal = TerminalBuilder.builder().system(true).build();

        LACore.getLogger().info("For help, type \"help\" or \"?\"");
        SimpleSchedulerManager scheduledManager = (SimpleSchedulerManager) LACore.getScheduledManager();
        scheduledManager.scheduleAsyncRepeatingTask(() -> {
            LACore.getLogger().print(this.prompt);

            String head = null;
            String[] args = {};
            LineReader lineReader = LineReaderBuilder.builder().terminal(terminal).completer(this.getCompleter(head, args)).build();
            String input = lineReader.readLine();

            String[] split = input.split(" ");
            head = split[0];

            if (split.length > 1)
                args = new ArrayList<>(Arrays.asList(split).subList(1, split.length)).toArray(new String[0]);

            Command command = LACore.getCommandManager().getCommand(head);
            if (command == null) {
                LACore.getLogger().info("Unknown command. Type \"/help\" for help.");
                return;
            }

            try {
                command.execute(LACore.getConsoleCommandSender(), command, head, args);
            } catch (Exception e) {
                LACore.getLogger().warn(e.getMessage());
                e.printStackTrace();
            }
        }, 0, 0);
    }

    private Completer getCompleter(String commandHead, String[] args) {
        Command command = LACore.getCommandManager().getCommand(commandHead);

        List<String> list = new ArrayList<>();

        if (commandHead == null || commandHead.isEmpty())
            list.addAll(LACore.getCommandManager().getCommands().keySet());
        else if (command == null)
            return null;

        if (command != null)
            list.addAll(command.onTabComplete(LACore.getConsoleCommandSender(), command, commandHead, args));

        List<Completers.TreeCompleter.Node> nodes = new ArrayList<>();
        list.forEach(string -> nodes.add(Completers.TreeCompleter.node(string)));
        return new Completers.TreeCompleter(nodes);
    }
}