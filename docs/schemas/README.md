# Schemas

A JSON schema to represent the CommandAPI's `config.yml` file.

## Setup (VSCode)

- Download the [YAML VSCode Extension](https://marketplace.visualstudio.com/items?itemName=redhat.vscode-yaml)
- Add the following to VSCode's `settings.json`:

  ```json
  "json.schemaDownload.enable": true,
  "yaml.schemas": {
      "https://commandapi.jorel.dev/schemas/config.schema.json": "plugins/CommandAPI/config.yml"
  },
  ```
