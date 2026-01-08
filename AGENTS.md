# Repository Guidelines

## Project Structure & Module Organization
- `src/main/java`: plugin source code (schema and concept rule language support).
- `src/main/lexer`: grammar and lexer specs (`.bnf`, `.flex`).
- `src/main/gen`: generated PSI/lexer/parser sources; do not edit by hand.
- `src/main/resources`: plugin resources (e.g., `META-INF/plugin.xml`, icons, static preview assets).
- `docs`: screenshots and example schema/rule files used for documentation.
- `build.gradle.kts`: Gradle build, IntelliJ plugin configuration, and publishing tasks.

## Build, Test, and Development Commands
- `./gradlew build`: compile and assemble the plugin.
- `./gradlew runIde`: launch a sandboxed IntelliJ IDE with the plugin loaded for manual testing.
- `./gradlew buildPlugin`: produce the distributable plugin ZIP under `build/`.
- `./gradlew verifyPlugin`: run JetBrains plugin verification against the configured IDE.
- `./gradlew test`: run unit tests (none currently defined; add tests before relying on this).

## Coding Style & Naming Conventions
- Java/Kotlin: 4 spaces, no tabs; keep IntelliJ default formatting.
- Classes use `UpperCamelCase`; methods/fields use `lowerCamelCase`.
- Follow existing naming patterns like `Schema*` and `ConceptRule*` for language-specific components.
- Resource keys live in `src/main/resources/messages/*.properties`; keep keys stable and descriptive.

## Testing Guidelines
- No automated tests are present; validate changes via `./gradlew runIde` and example files in `docs/OpenSPG_Schema_Examples`.
- If adding tests, place them under `src/test/java` and wire them into Gradle.

## Commit & Pull Request Guidelines
- Recent history uses concise messages like `Fix(Schema): update preview` and `refactor`.
- Prefer `Type(Scope): short summary` (e.g., `Fix(Schema): handle empty entities`).
- PRs should include a short description, manual testing notes (e.g., `runIde` steps), and screenshots for UI changes.

## Configuration & Publishing
- IntelliJ plugin metadata is defined in `src/main/resources/META-INF/plugin.xml`.
- Signing and publishing rely on environment variables (`CERTIFICATE_CHAIN`, `PRIVATE_KEY`, `PRIVATE_KEY_PASSWORD`, `PUBLISH_TOKEN`).
