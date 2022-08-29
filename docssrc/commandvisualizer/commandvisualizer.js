"use strict";

document.getElementById("cmd-input").oninput = function() {

	/** @type HTMLElement */
	const commandInput = this;
	const errorMessageBox = document.getElementById("error-box");
	const suggestionsBox = document.getElementById("suggestions-box");

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
			let range = createRange(commandInput, { count: index });
	
			if (range) {
				range.collapse(false);
				let selection = window.getSelection();
				selection.removeAllRanges();
				selection.addRange(range);
			}
		}
	};

	let cursorPos = cursorPosition();
	let commands = ["say", "tp", "w", "weather", "whitelist", "worldborder"];

	/** @type string */
	let rawText = commandInput.innerText;

	// Command forward slash
	let element = document.createElement("span");
	element.innerText = "/";

	let errorText = "";
	let suggestions = [];

	// Render colors
	if(rawText.startsWith("/")) {
		// Parse the raw text
		const rawTextNoSlash = rawText.slice(1);
		const command = rawTextNoSlash.split(" ")[0];
		const rawArgs = rawText.split(" ").slice(1);

		if(!(commands.some((x) => x === command))) {
			// The command is invalid (the command doesn't exist). Make the
			// whole text red.
			let redText = document.createElement("span");
			redText.className = "red";
			redText.innerText = rawTextNoSlash;
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

		suggestions = commands.filter((x) => x.startsWith(rawTextNoSlash) && x !== rawTextNoSlash);
	}

	// Reset the text and add the new text.
	commandInput.innerHTML = "";
	commandInput.appendChild(element);

	// Set the cursor back to where it was. Since commands always start with a
	// forward slash, the only possible "starting caret position" is position 1
	// (in front of the slash)
	if(cursorPos === 0) {
		cursorPos = 1;
	}
	setCurrentCursorPosition(cursorPos);
	commandInput.focus();

	// If any errors appear, display them
	if(errorText.length !== 0) {
		errorMessageBox.innerText = errorText;
		errorMessageBox.hidden = false;
	} else {
		errorMessageBox.hidden = true;
	}

	const constructSuggestionsHTML = (suggestions) => {
		let nodesToAdd = [];
		for(let i = 0; i < suggestions.length; i++) {
			const suggestionElement = document.createElement("span");
			suggestionElement.innerText = suggestions[i];
			if(i === 0) {
				suggestionElement.className = "yellow";
			}
			if(i !== suggestions.length - 1) {
				suggestionElement.innerText += "\n";
			}
			nodesToAdd.push(suggestionElement);
		}

		return nodesToAdd;
	};

	// If suggestions are present, display them
	if(suggestions.length !== 0) {
		suggestionsBox.innerHTML = "";
		for(let suggestionElement of constructSuggestionsHTML(suggestions)) {
			suggestionsBox.appendChild(suggestionElement);
		}
		suggestionsBox.hidden = false;
		errorMessageBox.hidden = true;
	} else {
		suggestionsBox.hidden = true;
	}
}

// We really really don't want new lines in our single-lined command!
document.getElementById("cmd-input").addEventListener('keydown', (evt) => {
	switch(evt.key) {
		case "Enter":
			evt.preventDefault();
			break;
		case "ArrowDown":
		case "ArrowUp": {
			const suggestionsBox = document.getElementById("suggestions-box");
			if(!suggestionsBox.hidden) {
				for(let i = 0; i < suggestionsBox.childNodes.length; i++) {
					if(suggestionsBox.childNodes[i].className === "yellow") {
						suggestionsBox.childNodes[i].className = "";

						if(evt.key == "ArrowDown") {
							if(i === suggestionsBox.childNodes.length - 1) {
								suggestionsBox.childNodes[0].className = "yellow";
							} else {
								suggestionsBox.childNodes[i + 1].className = "yellow";
							}
						} else {
							if(i === 0) {
								suggestionsBox.childNodes[suggestionsBox.childNodes.length - 1].className = "yellow";
							} else {
								suggestionsBox.childNodes[i - 1].className = "yellow";
							}
						}
						
						break;
					}
				}
			}
			break;
		}
		default:
			// console.log(evt.key)
			break;
	}
});

// If you click on the chat box, focus the current text input area 
document.getElementById("chatbox").onclick = function() {
	document.getElementById("cmd-input").focus();
};

// Run syntax highlighter
document.getElementById("cmd-input").oninput();