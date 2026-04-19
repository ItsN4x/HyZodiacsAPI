# HyZodiac's API

A shared library mod for hyZodiac's Minecraft mods. Provides a thin, platform-neutral
API surface — events, HUD helpers, config utilities, and a small service-locator — so
that companion mods don't have to re-implement the same scaffolding on every loader
and Minecraft version.

Consumed by:

- [Kexosyn](https://github.com/) — Fabric 1.20.1 + Forge 1.20.1
- [PvpSkill](https://github.com/ItsN4x/PvpSkill) — Fabric 1.21+

## Supported platforms

| Loader | Minecraft | Java | Jar |
| ------ | --------- | ---- | --- |
| Fabric | 1.21.1 | 21 | `hyzodiacs_api-fabric-1.21.1-<ver>.jar` |
| Fabric | 1.20.1 | 17 | `hyzodiacs_api-fabric-1.20.1-<ver>.jar` |
| Forge  | 1.20.1 | 17 | `hyzodiacs_api-forge-1.20.1-<ver>.jar` |

## Repository layout

```
common/            plain-Java module, the only thing consumers compile against
fabric-1.21/       Fabric 1.21+ impl (loom)
fabric-1.20.1/     Fabric 1.20.1 impl (loom)
forge-1.20.1/      Forge 1.20.1 impl (ForgeGradle)
```

## API surface

- `com.hyzodiac.api.HyZodiacAPI` — entrypoint; call `HyZodiacAPI.platform()` to get
  loader/version info, config dir, and mod-loaded queries.
- `com.hyzodiac.api.events.Event<T>` — minimal, thread-safe event dispatcher modelled
  after Fabric API's `Event`, but with no MC dependency.
- `com.hyzodiac.api.events.CombatEvents` — `PLAYER_ATTACKED` / `PLAYER_KILLED` events
  with `UUID`-based payloads so subscribers can live in common code across MC versions.
- `com.hyzodiac.api.events.LifecycleEvents` — `CLIENT_TICK_END` / `SERVER_TICK_END` /
  `CLIENT_STARTED` / `SERVER_STARTED` hooks.
- `com.hyzodiac.api.hud.HudOverlays` — registry of HUD draw callbacks.
  `HudRenderContext.graphics()` returns the native platform graphics object
  (`DrawContext` on Fabric, `GuiGraphics` on Forge).
- `com.hyzodiac.api.config.JsonConfig<T>` — load/save a POJO as pretty-printed JSON
  under the active loader's config directory.

## Building

Requires JDK 17 and JDK 21 to be available to Gradle toolchains.

```bash
./gradlew build
```

Artifacts land in each module's `build/libs/`.

## Using it in a consumer mod

### Fabric (1.21.1)

```groovy
repositories {
    maven { url = 'https://jitpack.io' }  // or a custom maven once published
}

dependencies {
    modImplementation "com.hyzodiac.api:hyzodiacs_api-fabric-1.21.1:1.0.0"
}
```

`fabric.mod.json`:

```json
"depends": {
    "hyzodiacs_api": "*"
}
```

### Forge (1.20.1)

`mods.toml`:

```toml
[[dependencies.yourmod]]
modId="hyzodiacs_api"
mandatory=true
versionRange="[1.0.0,)"
ordering="NONE"
side="BOTH"
```

## License

All Rights Reserved. Contact the authors for permission before redistribution.
