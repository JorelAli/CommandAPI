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
    const length = multiPreNode.children.length;
    for(let i = length - 1; i >= 0; i--) {
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
        const buttonText = multiPreNode.children[length - i].querySelector("code.hljs").classList[1];

        const button = document.createElement("button");
        button.className = "language-selector";
        button.innerText = buttonText;
        button.onclick = function() {

            for (const multiPreNodeChild of multiPreNode.children) {
                if(multiPreNodeChild.tagName === "PRE") {
                    multiPreNodeChild.classList.add("hidden");
                }
                if(multiPreNodeChild.tagName === "BUTTON") {
                    multiPreNodeChild.classList.remove("selected");
                }
            }

            button.classList.add("selected");
            this.parentElement.children[[...this.parentElement.children].indexOf(this) + length].classList.remove("hidden");
        };

        multiPreNode.insertBefore(button, multiPreNode.firstChild);
        
        if(i === 0) {
            button.onclick();
        }
    }

}