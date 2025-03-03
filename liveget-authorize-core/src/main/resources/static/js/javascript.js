let popup = null;
let openedClassification = null;
let openedDisplayBox = null;
let classifications = new Map();

// 音乐播放器
let music;
let musicID;

// 页面初始化
window.onload = function(event) {
	// 初始化
    initialize();

    while (websocket.readyState == 0) {};

	// 使用循环遍历检测websocket是否建立连接
	// let interval = setInterval(() => {
	// 	if (websocket.readyState != 1)
	// 		return;
	//     if (websocket.readyState == 0)
	//         return;

	// 	// websocket.send(new WindowLoad().toString());
	// 	clearInterval(interval);
	// }, 100);

    // 页面加载事件
	music = document.getElementById("music");
	music.onended = function (event) {
		// websocket.send(new MusicEndedMessage(musicID, music.src).toString());
	}

	// 验证是否登录
    if (verify()) {
    	websocket.send("{\"type\": \"user_info\", \"user\": {\"token\": \"" + localStorage.token + "\"}}");
        openClassification("preview");
        return;
    }

	openClassification("login");
}

/**
 * 初始化
 * */
function initialize() {
	// 注册消息处理体
	registerJSONMessage("account_login", new LoginSuccessMessage());
	registerJSONMessage("account_need_login", new NeedLoginMessage());
	registerJSONMessage("account_info", new SelfInfoMessage());
	registerJSONMessage("display_preview_title", new PreviewTitleMessage());

	// 初始化表单监听
	const loginForm = document.querySelector(".content .display .display_box_login_login .login .box .form");
    loginForm.onsubmit = function (e) {
        e.preventDefault();
        fetch(e.target.action, { method: e.target.method, body: new FormData(loginForm) })
            .then(res => res.text())
            .then(res => {
   				interpreterJsonMessage(res);
            });
    };
    const registerForm = document.querySelector(".content .display .display_box_login_register .register .box .form");
    registerForm.onsubmit = function(e) {
        e.preventDefault();
        fetch(e.target.action, { method: e.target.method, body: new FormData(registerForm) })
            .then(res => res.text())
            .then(res => {
   				interpreterJsonMessage(res);
            })
            .catch(() => {
                console.error("请求出错");
            });
    };

	// 初始化界面
	// 分类 登录
	let box_login = new Map();
	let box_login_login = new DisplayBox(".content .display .display_box_login_login");
	box_login_login.setLine(".content .display .display_hrefs .display_hrefs_login .line");
	box_login_login.setButton(".content .display .display_hrefs .display_hrefs_login .display_box_login_login_button");
	let box_login_register = new DisplayBox(".content .display .display_box_login_register");
	box_login_register.setLine(".content .display .display_hrefs .display_hrefs_login .line");
	box_login_register.setButton(".content .display .display_hrefs .display_hrefs_login .display_box_login_register_button");
	box_login.set("login", box_login_login);
	box_login.set("register", box_login_register);
	let classification_login = new Classification(".content .display .display_hrefs .display_hrefs_login", box_login, "login");
	classification_login.setLine(".content .display .display_hrefs .display_hrefs_login .line");

	// 分类 概览
	// 界面
	let box_preview = new Map();
	let box_preview_default = new DisplayBox(".content .display .display_box_preview");
	box_preview_default.setButton(".content .display .display_hrefs .display_hrefs_preview .display_box_preview_button");
	box_preview_default.setLine(".content .display .display_hrefs .display_hrefs_preview .line");
	box_preview.set("preview", box_preview_default);
	// 分类
	let classification_preview = new Classification(".content .display .display_hrefs .display_hrefs_preview", box_preview, "preview");
	classification_preview.setLine(".content .display .display_hrefs .display_hrefs_preview .line");

	// 分类 授权码
	// 界面
	let box_code = new Map();
	let box_code_default = new DisplayBox(".content .display .display_box_code");
	box_code_default.setLine(".content .display .display_hrefs .display_hrefs_code .line");
	box_code_default.setButton(".content .display .display_hrefs .display_hrefs_code .display_box_code_button");
	box_code.set("code", box_code_default);
	// 分类
	let classification_code = new Classification(".content .display .display_hrefs .display_hrefs_code", box_code, "code");
	classification_code.setLine(".content .display .display_hrefs .display_hrefs_code .line");

	// 分类 服务
	// 界面
	let box_shop = new Map();
	let box_shop_code = new DisplayBox(".content .display .display_box_shop_code");
	box_shop_code.setLine(".content .display .display_hrefs .display_hrefs_shop .line");
	box_shop_code.setButton(".content .display .display_hrefs .display_hrefs_shop .display_box_shop_code_button");
	let box_shop_subsidiary = new DisplayBox(".content .display .display_box_shop_subsidiary");
	box_shop_subsidiary.setLine(".content .display .display_hrefs .display_hrefs_shop .line");
	box_shop_subsidiary.setButton(".content .display .display_hrefs .display_hrefs_shop .display_box_shop_subsidiary_button");
	box_shop.set("code", box_shop_code)
	box_shop.set("subsidiary", box_shop_subsidiary);
	// 分类
	let classification_shop = new Classification(".content .display .display_hrefs .display_hrefs_shop", box_shop, "code");
	classification_shop.setLine(".content .display .display_hrefs .display_hrefs_shop .line");

	// 分类 设置
	// 界面
	let box_set = new Map();
	let box_set_default = new DisplayBox(".content .display .display_box_set");
	box_set_default.setLine(".content .display .display_hrefs .display_hrefs_set .line");
	box_set_default.setButton(".content .display .display_hrefs .display_hrefs_set .display_box_set_button");
	box_set.set("set", box_set_default)
	// 分类
	let classification_set = new Classification(".content .display .display_hrefs .display_hrefs_set", box_set, "set");
	classification_set.setLine(".content .display .display_hrefs .display_hrefs_set .line");

	// 存入分类
	classifications.set("login", classification_login);
	classifications.set("preview", classification_preview);
	classifications.set("code", classification_code);
	classifications.set("shop", classification_shop);
	classifications.set("set", classification_set);
}

/**
 * 打开 界面
 *
 * @param classification 分类
 * @param name 界面名称
 * */
function openDisplayBox(classification, name) {
     if (classification != "login") {
         if (!verify())
             return;
	 }
	let classificationEntity = classifications.get(classification);
	let line = classificationEntity.getLine();
	let display = classificationEntity.getDisplayBox(name);

	display.open(display.getLine());
	openedDisplayBox = display;

	websocket.send("{\"type\": \"open_display\", \"user\": {\"token\": \"" + localStorage.token + "\"}, \"classification\": \"" + classification + "\", \"dispalyPage\": \"" + name + "\"}");
}

function openClassification(classification) {
    if (classification != "login") {
	    if (!verify())
	       return;
	}
	if (openedClassification == classifications.get(classification))
		return;

	if (openedClassification != null)
		openedClassification.close();

	let classificationEntity = classifications.get(classification);
	classificationEntity.open();

	openedClassification = classificationEntity;

	websocket.send("{\"type\": \"open_classification\", \"user\": {\"token\": \"" + localStorage.token + "\"}, \"classification\": \"" + classification + "\"}");
}

function logout() {
    websocket.send("{\"type\": \"user_quit\", \"user\": {\"token\": \"" + localStorage.token + "\"}}");
    openClassification("login");
    localStorage.setItem("token", "");
}

function verify() {
	if (localStorage.token == null)
		return false;

	let formdata = new FormData();
	formdata.append("token", localStorage.token);
	fetch("./verify", { method: "post", body: formdata })
		.then(res => res.text())
		.then(res => {
			interpreterJsonMessage(res);
		})
		.catch(() => {
			sendPopup("info", "<img src='./images/error.svg' style='width: 30px; margin-top: 2px;'>请求出错", 1800);
		});
	return true;
}

/**
 * 显示弹窗信息
 * 
 * @param type 弹窗类型
 * @param message 消息
 * @param ms 显示毫秒
 * */
function sendPopup(type, message, ms) {
	let box;
	if (type == "info") {
		box = document.querySelector(".popup .info");
		if (popup != null)
			return;
		box.style.display = "flex";
		content = document.querySelector(".popup .info label");
		content.innerHTML = message;
		// box.classList.add("topto");

		popup = setTimeout(() => {
			// box.classList.remove("topto");
			box.style.display = "none";
			popup = null;
			clearTimeout(popup);
		}, ms);
	}
}

/**
 * 根据值移除 ArrayList 中的元素
 * 
 * @param list 列表
 * @param value 值
 * */
function removeArrayListValue(list, value) {
	if (!users.includes(id))
		return;

	for (let i = 0; i < list.length; i++) {
		if (list[i] != value)
			continue;

		list.splice(i, 1);
	}
}