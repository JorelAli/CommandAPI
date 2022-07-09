hljs.registerLanguage("mccmd", function() {

    const COMMAND_NAME = {
        scope: 'title',
        match: /(?<=^\/)[a-zA-Z0-9]+/
    }

    const ENTITY_SELECTOR = {
        scope: 'string',
        match: /@[aeprs](\[.+\])?/
    };

    const NUMBER = {
        scope: 'number',
        match: /-?[0-9]+/
    };

    const TEMPLATE = {
        scope: 'comment',
        match: /<.*?>/
    };

    return {
        case_insensitive: true, // language is case-insensitive
        keywords: 'align anchored as at facing in positioned rotated run if store result score matches',
        contains: [
            COMMAND_NAME,
            ENTITY_SELECTOR,
            NUMBER,
            TEMPLATE
        ]
    };
});
hljs.highlightAll();

for (const multiPreNode of document.getElementsByClassName("multi-pre")) {
    let buttons = [];
    for (const child of multiPreNode.children) {
        // We use a sneaky hack to do with how class names are generated. The
        // class names should always be of the form "language-LANGUAGE ... hljs"
        // where LANGUAGE is the language of the pre and ... is a list of other
        // class names declared in the Markdown. For example, if we have:
        //
        //   ```groovy,build.gradle
        //   ...
        //   ```
        //
        // then the class name is "language-groovy build.gradle hljs"
        const buttonText = child.querySelector("code.hljs").classList[1];

        const button = document.createElement("button");
        button.className = "language-selector";
        button.innerText = buttonText;
        button.onclick = function() {

            for (const codeBlock of multiPreNode.children) {
                if(![...codeBlock.classList].includes("hidden") && codeBlock.tagName === "PRE") {
                    codeBlock.classList.add("hidden");
                }
            }

            for (const childButton of multiPreNode.children) {
                if(childButton.tagName === "BUTTON") {
                    childButton.classList.remove("selected");
                }
            }

            button.classList.add("selected");
            child.classList.remove("hidden");
        };

        buttons.push(button);
    }

    for (let i = buttons.length - 1; i >= 0; i--) {
        multiPreNode.insertBefore(buttons[i], multiPreNode.firstChild);
    }

    buttons[0].onclick();
}