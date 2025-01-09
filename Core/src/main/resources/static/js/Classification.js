class Classification {
	/**
	 * 构造 Classification
	 * 
	 * @param hrefs buttons
	 * @param displayBoxMap display Map
	 * @param defaultDisplayBoxName 默认displaybox
	 * */
	constructor(hrefs, displayBoxMap, defaultDisplayBoxName) {
		this.hrefs = document.querySelector(hrefs);
		this.displayBoxMap = displayBoxMap;
		this.defaultDisplayBoxName = defaultDisplayBoxName;
		this.defaultDisplayBox = displayBoxMap.get(defaultDisplayBoxName);
	}

	/**
	 * 打开 Classification
	 * */
	open() {
		if (openedClassification != null) {
			openedClassification.getHref().classList.remove("open");
			openedDisplayBox.close();
		}

		this.getHref().classList.add("open");
		this.defaultDisplayBox.open();
	}

	close() {
		this.getHref().classList.remove("open");

		openedDisplayBox.close();
	}

	getDisplayBox(displayBox) {
		return this.displayBoxMap.get(displayBox);
	}

	getDefaultDisplayBoxName() {
		return this.defaultDisplayBoxName;
	}

	setLine(line) {
		this.line = document.querySelector(line);
	}

	getLine() {
		return this.line;
	}

	getHref() {
		return this.hrefs;
	}
}

class DisplayBox {
	/**
	 * 构造 DisplayBox
	 * 
	 * @param displayBox dom实体
	 * */
	constructor(displayBox) {
		this.displayBox = document.querySelector(displayBox);
	}

	/**
	 * 打开 display
	 * */
	open() {
		if (this.displayBox.classList.contains("open"))
			return;

		if (openedDisplayBox != null)
			openedDisplayBox.close();

		let button = this.getButton();

		this.line.style.left = button.offsetLeft + "px";
		this.line.style.width = button.offsetWidth + "px";

		openedDisplayBox = this;
		this.displayBox.classList.add("open");
	}

	/**
	 * 关闭 displayBox
	 * */
	close() {
		if (!this.displayBox.classList.contains("open"))
			return;

		this.displayBox.classList.remove("open");
	}

	setButton(button) {
		this.button = document.querySelector(button);
	}

	getButton() {
		return this.button;
	}

	setLine(line) {
		this.line = document.querySelector(line);
	}

	getLine() {
		return this.line;
	}
}
