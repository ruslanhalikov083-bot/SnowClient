# SnowClient

Client-side visual mod for Minecraft 1.21.11 (Fabric).

## Features
- Minimal icy-blue HUD: FPS, coordinates and ESP status.
- Player highlight (ESP) using Minecraft's glowing outline.
- Toggle ESP with **O** and HUD with **P**.
- GitHub Actions workflow builds the mod JAR automatically.

## Build
Requires Java 21 and Gradle.

```bash
gradle build
```

The JAR will be in `build/libs/`.

## GitHub build
1. Create a GitHub repository and upload the contents of this folder.
2. Open the **Actions** tab.
3. Run **Build SnowClient** (or push a commit).
4. Open the completed workflow run and download the `SnowClient-...` artifact.

## Notes
This is a starter implementation. Fabric/Yarn coordinates can change; if Gradle reports that a Minecraft, Yarn, Loader, Fabric API or Loom version does not exist, update the matching values in `gradle.properties` / `build.gradle` to the current versions published for Minecraft 1.21.11.

The player highlight is client-side and intended for testing in your own worlds or on servers where visual client mods are allowed.
