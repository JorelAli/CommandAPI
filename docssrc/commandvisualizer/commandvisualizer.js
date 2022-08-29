"use strict";

document.getElementById("cmd-input").oninput = function() {

	/** @type HTMLElement */
	const commandInput = this;
	const errorMessageBox = document.getElementById("error-chatbox-bg");

	/**
	 * Gets the current cursor position.
	 *
	 * From https://thewebdev.info/2022/04/24/how-to-get-contenteditable-caret-position-with-javascript/
	 * @returns The current cursor position for the current element
	 */
	const cursorPosition = () => {
		const sel = document.getSelection();
		sel.modify("extend", "backward", "paragraphboundary");
		const pos = sel.toString().length;
		if (sel.anchorNode !== undefined && sel.anchorNode !== null) {
			sel.collapseToEnd();
		}
		return pos;
	};

	/**
	 * Sets the current cursor position. This has to take into account the fact
	 * that the current element is made up of many HTML elements instead of
	 * plaintext, so selection ranges have to span across those elements to find
	 * the exact location you're looking for.
	 *
	 * From https://stackoverflow.com/a/41034697/4779071
	 * @param {number} index the number of characters into the current element
	 * to place the cursor at
	 */
	// 
	function setCurrentCursorPosition(index) {
		if (index >= 0) {
			const createRange = (/** @type Node */ node, /** @type {{count: number}} */ chars, /** @type Range */ range) => {
				if (!range) {
					range = document.createRange();
					range.selectNode(node);
					range.setStart(node, 0);
				}
			
				if (chars.count === 0) {
					range.setEnd(node, chars.count);
				} else if (node && chars.count > 0) {
					if (node.nodeType === Node.TEXT_NODE) {
						if (node.textContent.length < chars.count) {
							chars.count -= node.textContent.length;
						} else {
							range.setEnd(node, chars.count);
							chars.count = 0;
						}
					} else {
						for (let lp = 0; lp < node.childNodes.length; lp++) {
							range = createRange(node.childNodes[lp], chars, range);
			
							if (chars.count === 0) {
								break;
							}
						}
					}
				} 
			
				return range;
			};
	
			// We wrap index in an object so that recursive calls can use the
			// "new" value which is updated inside the object itself
			let range = createRange(commandInput.parentNode, { count: index });
	
			if (range) {
				range.collapse(false);
				let selection = window.getSelection();
				selection.removeAllRanges();
				selection.addRange(range);
			}
		}
	};

	let cursorPos = cursorPosition();

	let commands = ["say", "tp"]

	/** @type string */
	let rawText = commandInput.innerText;

	// Command forward slash
	let element = document.createElement("span");
	element.innerText = "/";

	let errorText = "";

	if(rawText.startsWith("/")) {
		if(!(commands.some((x) => rawText.startsWith("/" + x)))) {
			// The command is invalid (the command doesn't exist). Make the
			// whole text red.
			let redText = document.createElement("span");
			redText.className = "red";
			redText.innerText = rawText.slice(1);
			element.append(redText);

			errorText = "Unknown or incomplete command, see below for error at position 1: /<--[HERE]";
		} else {
			// Make the command gray
			const rawTextElements = rawText.split(" ");
			let grayText = document.createElement("span");
			grayText.innerText = rawTextElements[0].slice(1); // Don't include the /
			element.append(grayText);

			// Make everything else cyan (for now)
			let cyanText = document.createElement("span");
			cyanText.className = "cyan";
			cyanText.innerText = " " + rawTextElements.slice(1).join(" ");
			element.append(cyanText);
		}
	}
	commandInput.innerText = "";
	commandInput.innerHTML = "";
	commandInput.appendChild(element);
	setCurrentCursorPosition(cursorPos);
	commandInput.focus();

	if(errorText.length !== 0) {
		errorMessageBox.innerText = errorText;
		errorMessageBox.hidden = false;
	} else {
		errorMessageBox.hidden = true;
	}
}

// We really really don't want new lines in our single-lined command!
document.getElementById("cmd-input").addEventListener('keydown', (evt) => {
	if (evt.key === "Enter") {
		evt.preventDefault();
	}
});

// Run syntax highlighter
document.getElementById("cmd-input").oninput();