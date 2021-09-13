function handleFile(file) {
    let zip = new JSZip();
    zip.loadAsync(file)
        .then(function(zip) {
            zip.file("plugin.yml").async("string").then(function(plugintext) {
                try {
                    let nativeObject = YAML.parse(plugintext);
                    console.log(nativeObject);

                    let output = document.getElementById("drop_zone_output");
                    output.innerText = "Plugin name: " + nativeObject["name"];
                    if (nativeObject.commands !== undefined) {
                        for (let commandName of Object.keys(nativeObject.commands)) {
                            output.innerText += "\n" + commandName;
                        }

                    }
                } catch (err) {
                    let output = document.getElementById("drop_zone_output");
                    output.innerText = err.message + " on line " + err.parsedLine + ": " + err.snippet;
                }

            });
        }, function() {
            alert("Not a valid zip file");
        });
}

// https://developer.mozilla.org/en-US/docs/Web/API/HTML_Drag_and_Drop_API/File_drag_and_drop
function dropHandler(event) {
    // Prevent default behavior (Prevent file from being opened)
    event.preventDefault();

    if (event.dataTransfer.items) {
        // Use DataTransferItemList interface to access the file(s)
        for (let i = 0; i < event.dataTransfer.items.length; i++) {
            // If dropped items aren't files, reject them
            if (event.dataTransfer.items[i].kind === 'file') {
                let file = event.dataTransfer.items[i].getAsFile();
                handleFile(file);
            }
        }
    } else {
        // Use DataTransfer interface to access the file(s)
        for (let i = 0; i < event.dataTransfer.files.length; i++) {
            handleFile(file);
        }
    }
}

function dragOverHandler(event) {
    // Prevent default behavior (Prevent file from being opened)
    event.preventDefault();

    document.getElementById("drop_zone_id").innerText = "Drop the file here!";
}