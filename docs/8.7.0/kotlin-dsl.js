// Adds support for multiple languages in code blocks, with "tab bars" to toggle
// between them. To do this, wrap your Markdown code blocks in a div with the
// "multi-pre" class name. To set the name of the tab, add a comma followed by
// the name of the tab after the line where you specify the language of a code
// block. For example:
//
//   <div class="multi-pre">
//   
//   ```groovy,build.gradle
//   repositories {
//       maven { url = "https://repo.codemc.org/repository/maven-public/" }
//   }
//   ```
//   
//   ```kotlin,build.gradle.kts
//   repositories {
//       maven(url = "https://repo.codemc.org/repository/maven-public/")
//   }
//   ```
//   
//   </div>

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
        //
        // If we want to have spaces, use an underscore
        const buttonText = child.querySelector("code.hljs").classList[1].replace(/_/g, " ");
        const button = document.createElement("button");
        button.className = "language-selector";
        button.innerText = buttonText;
        button.onclick = function() {
            for (const childElement of multiPreNode.children) {
                switch(childElement.tagName) {
                    case "PRE": childElement.classList.add("hidden"); break;
                    case "BUTTON": childElement.classList.remove("selected"); break;
                }
            }
            button.classList.add("selected");
            child.classList.remove("hidden");
        };
        buttons.push(button);
    }
    // We do a second pass so we're not mutating the list we're iterating over
    for (let i = buttons.length - 1; i >= 0; i--) {
        multiPreNode.insertBefore(buttons[i], multiPreNode.firstChild);
    }
    buttons[0].onclick();
}