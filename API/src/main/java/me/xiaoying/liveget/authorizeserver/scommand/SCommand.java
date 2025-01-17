package me.xiaoying.liveget.authorizeserver.scommand;

import me.xiaoying.liveget.authorizeserver.LACore;
import me.xiaoying.liveget.authorizeserver.command.CommandExecutor;
import me.xiaoying.liveget.authorizeserver.entity.CommandSender;
import me.xiaoying.logger.ChatColor;

import java.util.*;

/**
 * Command SubCommand
 */
public abstract class SCommand {
    private final Map<String, List<RegisteredCommand>> registeredCommands = new HashMap<>();

    /**
     * Get registered commands
     *
     * @return HashMap
     */
    public Map<String, List<RegisteredCommand>> getRegisteredCommands() {
        return this.registeredCommands;
    }

    /**
     * Register new command
     *
     * @param subCommand SubCommand
     */
    public void registerCommand(SCommand subCommand) {
        Command command = subCommand.getClass().getAnnotation(Command.class);

        if (command == null) {
            LACore.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&eFined some command(" + subCommand.getClass().getName() + ") don't use Command annotation, please check your code!"));
            return;
        }

        for (String s : command.values()) {
            List<RegisteredCommand> list = new ArrayList<>();
            for (int i : command.length())
                list.add(new RegisteredCommand(i, subCommand));

            this.registeredCommands.put(s, list);
        }
    }

    /**
     * Get command set value
     *
     * @return alias
     */
    public List<String> getValues() {
        Command command = this.getClass().getAnnotation(Command.class);
        return new ArrayList<>(Arrays.asList(command.values()).subList(0, command.values().length));
    }

    /**
     * Get command supported length
     *
     * @return ArrayList
     */
    public List<Integer> getLengths() {
        List<Integer> list = new ArrayList<>();
        for (int i : this.getClass().getAnnotation(Command.class).length())
            list.add(i);
        return list;
    }

    /**
     * Get help command for this command
     *
     * @return ArrayList
     */
    public abstract List<String> getHelpMessage();

    /**
     * Perform command
     *
     * @param sender Sender
     * @param args Command's functions
     */
    public abstract void performCommand(CommandSender sender, String[] args);

    /**
     * Perform tab<br>
     * If you don't want sender get command help message, you can override this method and return empty list
     *
     * @param sender Sender
     * @param command Command
     * @param head Command head
     * @param strings Command's functions
     * @return ArrayList
     */
    public List<String> onTabComplete(CommandSender sender, me.xiaoying.liveget.authorizeserver.command.Command command, String head, String[] strings) {
        List<String> list = new ArrayList<>(registeredCommands.keySet());
        if (strings.length == 1) {
            List<String> conditionList = new ArrayList<>();
            for (String s1 : list) {
                if (!s1.toUpperCase(Locale.ENGLISH).startsWith(strings[0].toUpperCase(Locale.ENGLISH)))
                    continue;
                conditionList.add(s1);
            }

            if (conditionList.size() == 0)
                return list;
            return conditionList;
        }

        List<RegisteredCommand> registeredCommand = this.registeredCommands.get(strings[0]);
        if (registeredCommand == null)
            return new ArrayList<>();

        strings = new ArrayList<>(Arrays.asList(strings).subList(1, strings.length)).toArray(new String[0]);
        for (RegisteredCommand registeredCommand1 : registeredCommand) {
            List<String> l;
            if ((l = registeredCommand1.getSubCommand().onTabComplete(sender, command, head, strings)) == null)
                return null;

            return l;
        }
        return new ArrayList<>();
    }

    /**
     * Get bukkit TabExecutor
     *
     * @return TabExecutor
     */
    public CommandExecutor getTabExecutor() {
        return new CommandExecutor() {
            @Override
            public boolean onCommand(CommandSender sender, me.xiaoying.liveget.authorizeserver.command.Command cmd, String s, String[] strings) {
                if (!SCommand.this.getLengths().contains(strings.length) && !SCommand.this.getLengths().contains(-1)) {
                    SCommand.this.getHelpMessage().forEach(sender::sendMessage);
                    return false;
                }
                performCommand(sender, strings);
                return true;
            }

            @Override
            public List<String> onTabComplete(CommandSender sender, me.xiaoying.liveget.authorizeserver.command.Command cmd, String s, String[] strings) {
                return SCommand.this.onTabComplete(sender, cmd, s, strings);
            }
        };
    }
}