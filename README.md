# OpenSPG Schema IDEA Plugin

An IntelliJ Platform plugin for OpenSPG Schema Mark Language (SchemaML) and Concept Rule Mark Language (RuleML), with syntax highlighting, structure view, formatting, preview, and more.

- Marketplace: <https://plugins.jetbrains.com/plugin/26288-openspg-schema-mark-language-highlighter/>

## Preview

<div align="center">
    <img src="./docs/resources/screenshot.png" width="1200" alt="screenshot">
</div>

## Features

- SchemaML and ConceptRuleML syntax highlighting, commenting, and folding
- Structure view, navigation bar, and line markers
- Code formatter, code style settings, and color settings page
- Code completion
- Preview and split editor
- Spell checker

## Supported File Types

- `.schema`: OpenSPG Schema
- `.rule`: OpenSPG Concept Rule

## Requirements

- JDK 17+
- IntelliJ Platform 2024.2 (build dependency)

## Quick Start

Build:

```bash
./gradlew build
```

Run IDE for manual testing:

```bash
./gradlew runIde
```

Build plugin package:

```bash
./gradlew buildPlugin
```

Verify plugin compatibility:

```bash
./gradlew verifyPlugin
```

## Project Structure
```
.
├── AGENTS.md         # CodeX ANGETS
├── LICENSE           # License
├── README.md         # README
├── config            # plugin configs, such as 'checkstyle' 
├── docs              # resources
├── src               # source codes
│   ├── main
│   │   ├── java      # plugin source code (Schema/Concept Rule support)
│   │   ├── lexer     # grammar and lexer definitions (`.bnf`/`.flex`)
│   │   └── gen       # generated PSI/lexer/parser sources (do not edit)
│   └── test          # 
│       ├── java      # junit testcase
│       └── resources # test resources
└── build.gradle.kts  # Gradle build and IntelliJ plugin configuration
```

## Generated Sources

`src/main/gen` is generated from the `.bnf`/`.flex` files under `src/main/lexer`. Regenerate sources after grammar changes.

## Manual Test Checklist

Recommend using `./gradlew runIde` to validate:

- Open `.schema` and `.rule` files; verify highlighting and structure view
- Trigger completion and formatter; check the IDE Event Log for errors
- Use preview/split editor; confirm it updates with edits

## License

[Apache-2.0](./LICENSE)
