package com.hyzodiac.api.branding;

import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

/**
 * Visual identity for a HyZodiac mod. Used by {@link BrandingRegistry} so config screens,
 * update banners, and log lines can share consistent colours and labels without each mod
 * hard-coding its own constants.
 *
 * @param modId        Fabric mod id (used as the lookup key in the registry)
 * @param displayName  Human-readable name (e.g. "PvpSkill", "Kexosyn")
 * @param accentArgb   ARGB accent colour for headers / tab highlights
 * @param homepage     URL shown in the about dialog and update banner
 */
public record Branding(String modId, String displayName, int accentArgb, String homepage) {

	private Style accentStyle() {
		// Mask the alpha byte — Minecraft's TextColor is RGB-only.
		return Style.EMPTY.withColor(TextColor.fromRgb(accentArgb & 0xFFFFFF));
	}

	public MutableText badge() {
		return Text.literal("[").formatted(Formatting.DARK_GRAY)
			.append(Text.literal(displayName).setStyle(accentStyle()))
			.append(Text.literal("]").formatted(Formatting.DARK_GRAY));
	}

	public MutableText title() {
		return Text.literal(displayName).setStyle(accentStyle());
	}
}
