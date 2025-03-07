package me.xiaoying.liveget.authorizeserver.controller;

import me.xiaoying.liveget.authorizeserver.LACore;
import me.xiaoying.liveget.authorizeserver.factory.VariableFactory;
import me.xiaoying.liveget.authorizeserver.file.FileMessage;
import me.xiaoying.liveget.authorizeserver.utils.StringUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    @PostMapping("/user/register")
    public String register(String email, String password, String phoneNumber) {
        List<String> missing_parameters = new ArrayList<>();

        if (StringUtil.isEmpty(email))
            missing_parameters.add("'email'");
        if (StringUtil.isEmpty(password))
            missing_parameters.add("'password'");
        if (StringUtil.isEmpty(phoneNumber))
            missing_parameters.add("'phoneNumber'");

        if (!missing_parameters.isEmpty())
            return new VariableFactory(FileMessage.PARAMETER_MISSING_PARAMETER).parameters(missing_parameters).toString();

        if (LACore.getUserManager().getUserByEmail(email) != null)
            return FileMessage.USER_CREATE_EXISTS_EMAIL;
        if (LACore.getUserManager().getUserByPhoneNumber(Long.parseLong(phoneNumber)) != null)
            return FileMessage.USER_CREATE_EXISTS_PHONE;

        LACore.getUserManager().createUser(email, email, password, Long.parseLong(phoneNumber), "default");

        return FileMessage.USER_CREATE_CREATE_SUCCESS;
    }
}