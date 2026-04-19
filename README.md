# HyZodiac API

Shared Fabric library for [HyZodiac](https://github.com/ItsN4x) mods (PvpSkill, Kexosyn, …).

Provides reusable infrastructure so each consumer mod doesn't re-roll its own config loader,
HUD positioning code, Sodium-options-tab glue, or update checker.

## Modules

| Module | Class | Purpose |
|---|---|---|
| Branding | [`Branding`](src/main/java/com/hyzodiac/api/branding/Branding.java) / [`BrandingRegistry`](src/main/java/com/hyzodiac/api/branding/BrandingRegistry.java) | Register a display name + accent colour + homepage for your mod. |
| Config | [`AbstractJsonConfig`](src/main/java/com/hyzodiac/api/config/AbstractJsonConfig.java) | Base class for JSON-backed config files with auto-save + default backfill. |
| HUD | [`HudPos`](src/main/java/com/hyzodiac/api/hud/HudPos.java) + [`HudAnchor`](src/main/java/com/hyzodiac/api/hud/HudAnchor.java) | Screen-anchor based positioning that survives resizes. |
| Sodium options | [`SodiumOptionsHelper`](src/main/java/com/hyzodiac/api/sodium/SodiumOptionsHelper.java) | Register a Sodium options tab via [`sodium-options-api`](https://modrinth.com/mod/sodium-options-api) at runtime. |
| Update checker | [`UpdateChecker`](src/main/java/com/hyzodiac/api/update/UpdateChecker.java) | Async GitHub Releases lookup. |
| Logger | [`HyZodiacLogger`](src/main/java/com/hyzodiac/api/log/HyZodiacLogger.java) | Branded SLF4J wrapper. |

## Branches

- **`main`** — Minecraft **1.21** (Yarn, Fabric Loader ≥ 0.18, Java 21)
- **`mc/1.20.1`** — Minecraft **1.20.1** (Yarn, Fabric Loader ≥ 0.14, Java 17)

Pick the branch matching the MC version you're modding against.

## Consumer usage

```java
public class MyMod implements ModInitializer {
    public static final HyZodiacLogger LOG = HyZodiacLogger.of("MyMod");

    @Override
    public void onInitialize() {
        BrandingRegistry.register(new Branding(
            "mymod", "My Mod", 0xFF00FFAA, "https://github.com/you/mymod"));

        MyConfig cfg = AbstractJsonConfig.load(MyConfig.class, "mymod");

        SodiumOptionsHelper.registerPage("mymod", () -> new MyOptionPage(cfg));

        UpdateChecker.check("mymod", "you/mymod").thenAccept(r -> {
            if (r instanceof UpdateChecker.Result.Newer n) {
                LOG.info("Update available: {} → {}", n.installed(), n.latest());
            }
        });
    }
}
```

## License

CC0-1.0 — do whatever you want.
