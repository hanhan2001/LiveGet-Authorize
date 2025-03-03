// 主方法体
class JSONExecutor {
	execute(message) {
		throw new Error("Method 'execute' must be implemented.");
	}
}

// 成功登录
class LoginSuccessMessage extends JSONExecutor {
	execute(message) {
		localStorage.setItem("token", JSON.parse(message).token);
		sendPopup("info", "<img src='./images/success.svg' style='width: 30px; margin-top: 2px;'>登录成功", 1800);
		openClassification("preview");
		websocket.send("{\"type\": \"user_info\", \"user\": {\"token\": \"" + localStorage.token + "\"}}");
	}
}

class NeedLoginMessage extends JSONExecutor {
	execute(message) {
		openClassification("login");
		return false;
	}
}

/**
 * 验证通过
 * */
class VerifySuccessMessage extends JSONExecutor {
	execute(message) {
		return true;
	}
}

/**
 * 验证出错
 * */
class VerifyErrorMessage extends JSONExecutor {
	execute(message) {
		return false;
	}
}

class SelfInfoMessage extends JSONExecutor {
    execute(message) {
        let json = JSON.parse(message);
        // 用户名称
        document.querySelector(".content .display .display_box_preview .user_info .user .name").innerText = json.name;
        // 用户头像
        document.querySelector(".content .display .display_box_preview .user_info .user .photo").style.backgroundImage = "url(\"" + json.photo + "\")";
        // 用户手机
        let telephone = document.querySelector(".content .display .display_box_preview .user_info .info .box .account_self_info .telephone");
        if (json.telephone == 0)
            telephone.innerText = "手机号: 未绑定";
        else
            telephone.innerText = "手机号: " + json.telephone;
        // 用户邮箱
        document.querySelector(".content .display .display_box_preview .user_info .info .box .account_self_info .email").innerText = "邮箱: " + json.email;

        // 用户IP
        document.querySelector(".content .display .display_box_preview .user_info .info .box .account_login_info .ip").innerText = "登录IP: " + json.ip;
        // 用户注册时间
        document.querySelector(".content .display .display_box_preview .user_info .info .box .account_login_info .registerTime").innerText = "注册时间: " + json.registerTime;
        // 用户最后登录时间
        document.querySelector(".content .display .display_box_preview .user_info .info .box .account_login_info .lastLoginTime").innerText = "最后登录时间: " + json.lastLoginTime;
    }
}

class PreviewTitleMessage extends JSONExecutor {
	execute(message) {
		let json = JSON.parse(message);
		let box = document.querySelector(".content .display .display_box_preview .show .text .preview .title .message .box");
		box.innerText = "";
		box.insertAdjacentHTML("beforeend", json.message.replaceAll("\n", "<br>"));
	}
}