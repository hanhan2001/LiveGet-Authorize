package me.xiaoying.liveget.authorizeserver.command;

import me.xiaoying.liveget.authorizeserver.command.usercommand.UserCreateCommand;
import me.xiaoying.liveget.authorizeserver.command.usercommand.UserInfoCommand;
import me.xiaoying.liveget.authorizeserver.entity.CommandSender;
import me.xiaoying.liveget.authorizeserver.scommand.RegisteredCommand;
import me.xiaoying.liveget.authorizeserver.scommand.SCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@me.xiaoying.liveget.authorizeserver.scommand.Command(values = "user", length = -1)
public class UserCommand extends SCommand {
    public UserCommand() {
        this.registerCommand(new UserInfoCommand());
        this.registerCommand(new UserCreateCommand());
    }

    @Override
    public List<String> getHelpMessage() {
        List<String> list = new ArrayList<>();
        list.add("&b/user create <email> <password> <phone> &7创建新用户");
        list.add("&b/user info <用户选择类型(email/uuid/phone)> <用户> &7通过 邮箱/UUID/手机号 查询用户信息");
        list.add("&b/user set email <用户选择类型(email/uuid/phone)> <用户> <新邮箱> &7通过 邮箱 设置用户的 邮箱");
        list.add("&b/user set phone <用户选择类型(email/uuid/phone)> <用户> <新号码> &7通过 邮箱 设置用户的 邮箱");
        list.add("&b/user delete <email/uuid/phone> <email/uuid/phone> &7通过 邮箱/UUID/手机号 删除对应用户");
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
            if (registeredCommand.getLength() != args.length && registeredCommand.getLength() != -1)
                continue;

            registeredCommand.getSubCommand().performCommand(sender, args);
            isDo = true;
        }

        if (isDo)
            return;

        // 未执行则发出帮助信息
        this.getHelpMessage().forEach(sender::sendMessage);
    }
}