const knownJsonMessage = new Map();

function interpreterJsonMessage(message) {
	let json;
	try {
		json = JSON.parse(message);
	} catch {
		return;
	}

	let code = json.code;

	if (code == -1) {
		// do some
		return;
	}

	let jsonExecutor;
	if ((jsonExecutor = knownJsonMessage.get(json.type)) == null)
		return;

	jsonExecutor.execute(message);
}

function registerJSONMessage(type, jsonExecutor) {
	knownJsonMessage.set(type, jsonExecutor);
}