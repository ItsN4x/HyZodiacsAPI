package com.hyzodiac.api;

import com.hyzodiac.api.branding.Branding;
import com.hyzodiac.api.branding.BrandingRegistry;
import com.hyzodiac.api.log.HyZodiacLogger;
import net.fabricmc.api.ModInitializer;

/**
 * Entry point for the HyZodiac API library mod. Other HyZodiac mods declare a hard
 * dependency on this mod and use its modules at runtime; nothing here is auto-registered
 * for consumer mods — they must opt in by calling the relevant register methods.
 *
 * <p>Module overview:
 * <ul>
 *   <li>{@link com.hyzodiac.api.branding.BrandingRegistry} — register your mod with a name + accent color</li>
 *   <li>{@link com.hyzodiac.api.config.AbstractJsonConfig} — extend for an auto-(de)serialised JSON config</li>
 *   <li>{@link com.hyzodiac.api.hud.HudPos} / {@link com.hyzodiac.api.hud.HudAnchor} — HUD positioning helpers</li>
 *   <li>{@link com.hyzodiac.api.sodium.SodiumOptionsHelper} — add tabs to the Sodium options screen (uses sodium-options-api if present)</li>
 *   <li>{@link com.hyzodiac.api.update.UpdateChecker} — check GitHub releases for newer versions</li>
 *   <li>{@link com.hyzodiac.api.log.HyZodiacLogger} — branded SLF4J wrapper</li>
 * </ul>
 */
public final class HyZodiacApi implements ModInitializer {
	public static final String MOD_ID = "hyzodiacapi";
	public static final HyZodiacLogger LOGGER = HyZodiacLogger.of("HyZodiacAPI");

	@Override
	public void onInitialize() {
		BrandingRegistry.register(new Branding(
			MOD_ID,
			"HyZodiac API",
			0xFF8A2BE2, // BlueViolet — house brand colour
			"https://github.com/ItsN4x/HyZodiacAPI"
		));
		LOGGER.info("HyZodiacAPI initialised");
	}
}
