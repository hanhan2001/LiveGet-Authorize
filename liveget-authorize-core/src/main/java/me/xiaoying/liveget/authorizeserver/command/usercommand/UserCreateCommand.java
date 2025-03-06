package me.xiaoying.liveget.authorizeserver.command.usercommand;

import me.xiaoying.liveget.authorizeserver.LACore;
import me.xiaoying.liveget.authorizeserver.entity.CommandSender;
import me.xiaoying.liveget.authorizeserver.file.FileLanguage;
import me.xiaoying.liveget.authorizeserver.scommand.Command;
import me.xiaoying.liveget.authorizeserver.scommand.SCommand;

import java.util.ArrayList;
import java.util.List;

@Command(values = "create", length = 3)
public class UserCreateCommand extends SCommand {
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
        if (!sender.isAdmin()) {
            sender.sendMessage(FileLanguage.COMMAND_MISSING_PERMISSION);
            return;
        }

        String email = args[0], password = args[1], phone = args[2];

        if (LACore.getUserManager().getUserByEmail(email) != null) {
            sender.sendMessage("&c邮箱 &e" + email + " &c已绑定别的账户");
            return;
        }

        if (LACore.getUserManager().getUserByPhoneNumber(Long.parseLong(phone)) != null) {
            sender.sendMessage("&c手机号 &e" + phone + " &c已绑定别的账户");
            return;
        }

        LACore.getUserManager().createUser(email, email, password, Long.parseLong(phone), "default");

        sender.sendMessage("&a已创建用户 " + email);
    }
}