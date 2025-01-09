function playMusic() {
	let object = document.querySelector(".music .sound");
	object.setAttribute("onclick", "stopMusic()");
	object.setAttribute("title", "静音");
	object.style.backgroundImage = "url(./images/mute.svg)";
	music.play();
}

function stopMusic() {
	let object = document.querySelector(".music .sound");
	object.setAttribute("onclick", "playMusic()");
	object.setAttribute("title", "播放");
	object.style.backgroundImage = "url(./images/sound.svg)";
	music.pause();
}

function changeMusic(id, url) {
    musicID = id;
	music.src = url;
}