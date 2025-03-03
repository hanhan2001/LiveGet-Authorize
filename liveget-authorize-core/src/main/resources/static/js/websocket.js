// websocket
let websocket = new WebSocket("ws://" + window.location.hostname.replace("http://", "").replace("https://", "") + ":22333");

websocket.onopen = function(e) {
}

websocket.onmessage = function(e) {
	let json;
   	try {
   		json = JSON.parse(e.data);
  	} catch (e) {
    	return;
   	}

    console.log(e.data);
   	interpreterJsonMessage(e.data);
}

websocket.onclose = function(e) {
}

websocket.onerror = function(e) {
}