package me.xiaoying.liveget.authorizeserver.command.usercommand;

import me.xiaoying.liveget.authorizeserver.LACore;
import me.xiaoying.liveget.authorizeserver.entity.CommandSender;
import me.xiaoying.liveget.authorizeserver.file.FileConfig;
import me.xiaoying.liveget.authorizeserver.file.FileLanguage;
import me.xiaoying.liveget.authorizeserver.scommand.Command;
import me.xiaoying.liveget.authorizeserver.scommand.SCommand;
import me.xiaoying.liveget.authorizeserver.user.User;
import me.xiaoying.liveget.authorizeserver.utils.DateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Command(values = "info", length = 2)
public class UserInfoCommand extends SCommand {
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
        if (!sender.hasPermission("authorize.admin") && !sender.isAdmin()) {
            sender.sendMessage(FileLanguage.COMMAND_MISSING_PERMISSION);
            return;
        }

        String type = args[0];

        if (!type.equalsIgnoreCase("email") && !type.equalsIgnoreCase("phone") && !type.equalsIgnoreCase("uuid")) {
            sender.sendMessage("&c错误的选择类型 &e" + type);
            return;
        }

        String value = args[1];

        User user = switch (type.toUpperCase(Locale.ENGLISH)) {
            case "EMAIL" -> LACore.getUserManager().getUserByEmail(value);
            case "PHONE" -> LACore.getUserManager().getUserByPhoneNumber(Long.parseLong(value));
            case "UUID" -> LACore.getUserManager().getUserByUUID(value);
            default -> null;
        };

        if (user == null) {
            sender.sendMessage("&c无法找到用户 &e" + type + "-" + value);
            return;
        }

        sender.sendMessage("");
        sender.sendMessage("&e查询用户: ");
        sender.sendMessage("  &b昵称: &f" + user.getName());
        sender.sendMessage("  &b邮箱: &f" + user.getEmail());
        sender.sendMessage("  &bUUID: &f" + user.getUUID());
        sender.sendMessage("  &b权限: &f" + user.getPermissions());
        sender.sendMessage("  &b权限组: &f" + user.getGroup());
        sender.sendMessage("  &bIP: &f" + user.getIP());
        sender.sendMessage("  &b注册时间: &f" + DateUtil.getDate(user.getRegisterTime(), FileConfig.SETTING_DATEFORMAT));
        sender.sendMessage("  &b上次登录: &f" + DateUtil.getDate(user.getLastLoginTime(), FileConfig.SETTING_DATEFORMAT));
        sender.sendMessage("");
    }
}