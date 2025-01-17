package me.xiaoying.liveget.authorizeserver.command;

import me.xiaoying.liveget.authorizeserver.scommand.RegisteredCommand;
import me.xiaoying.liveget.authorizeserver.scommand.SCommand;
import me.xiaoying.liveget.authorizeserver.entity.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@me.xiaoying.liveget.authorizeserver.scommand.Command(values = "user", length = -1)
public class UserCommand extends SCommand {
    @Override
    public List<String> getHelpMessage() {
        List<String> list = new ArrayList<>();
        list.add("&b/user <email/uuid/phone> <user's email/user's uuid/user's phone> info &7通过 邮箱，UUID，手机号 查看用户信息");
        return list;
    }

    @Override
    public void performCommand(CommandSender sender, String[] args) {
        // 判断命令长度
        if (args.length == 0) {
            this.getHelpMessage().forEach(sender::sendMessage);
            return;
        }

        // 判断是否存在相应命令
        String head = args[0];
        if (!this.getRegisteredCommands().containsKey(head)) {
            this.getHelpMessage().forEach(sender::sendMessage);
            return;
        }

        // 移除 head
        List<String> list = new ArrayList<>(Arrays.asList(args).subList(1, args.length));
        args = list.toArray(new String[0]);

        boolean isDo = false;
        for (RegisteredCommand registeredCommand : this.getRegisteredCommands().get(head)) {
            if (registeredCommand.getLength() != args.length)
                continue;

            registeredCommand.getSubCommand().performCommand(sender, args);
            isDo = true;
        }

        if (isDo)
            return;

        // 未执行则发出帮助信息
        this.getHelpMessage().forEach(sender::sendMessage);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String head, String[] strings) {
        List<String> list = new ArrayList<>();
        if (strings.length == 1 || strings.length == 0) {
            list.add("email");
            list.add("uuid");
            list.add("phone");
        }

        List<String> conditionList = new ArrayList<>();

        if (strings.length == 1) {
            for (String s1 : list) {
                if (!s1.toUpperCase(Locale.ENGLISH).startsWith(strings[0].toUpperCase(Locale.ENGLISH)))
                    continue;
                conditionList.add(s1);
            }

            if (conditionList.isEmpty())
                return list;
        }

        if (strings.length == 3) {
            List<RegisteredCommand> registeredCommand = this.getRegisteredCommands().get(strings[2]);
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

        return list;
    }
}