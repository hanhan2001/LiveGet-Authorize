// class JSONMessage {
// 	getCode() {
// 		return 0;
// 	}
// }

// /**
//  *  JSON Message 页面加载完成
//  * */
// class WindowLoad extends JSONMessage {
//     constructor() {
//         super();
//     }

//     getCode() {
//         return 10;
//     }

//     toString() {
// 		return "{\n" +
// 				"  \"code\": " + this.getCode() + ",\n" +
// 				"  \"message\": \"Window on load\"\n" +
// 				"}";
//     }
// }

// /**
//  * JSON Message 音乐播放完成
//  * */
// class MusicEndedMessage extends JSONMessage {
// 	constructor(id, url) {
// 	    super();
// 	    this.id = id;
// 		this.url = url;
// 	}

//     getID() {
//         return this.id;
//     }

// 	getCode() {
// 		return 201;
// 	}

// 	toString() {
// 		return "{\n" +
// 				"  \"code\": " + this.getCode() + ",\n" +
// 				"  \"id\": \"" + this.getID() + "\"\n," +
// 				"  \"url\": \"" + this.url + "\"\n" +
// 				"}";
// 	}
// }