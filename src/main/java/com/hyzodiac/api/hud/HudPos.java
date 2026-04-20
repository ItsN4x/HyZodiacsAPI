package com.hyzodiac.api.hud;

/**
 * Mutable position descriptor for a HUD overlay. Backed by an anchor + (x, y) offset so a
 * single position survives screen resizes and scale changes.
 *
 * <p>Resolved screen coordinates are produced by {@link #resolve(int, int, int, int)}.
 */
public final class HudPos {
	public boolean enabled;
	public HudAnchor anchor;
	public int x;
	public int y;

	public HudPos() { this(true, HudAnchor.TOP_LEFT, 4, 4); }

	public HudPos(boolean enabled, HudAnchor anchor, int x, int y) {
		this.enabled = enabled;
		this.anchor = anchor;
		this.x = x;
		this.y = y;
	}

	/**
	 * Convert this anchor + offset into absolute screen pixel coordinates.
	 *
	 * @param screenW  screen width (scaled units)
	 * @param screenH  screen height (scaled units)
	 * @param contentW width of the element being placed
	 * @param contentH height of the element being placed
	 * @return resolved {@code [x, y]}
	 */
	public int[] resolve(int screenW, int screenH, int contentW, int contentH) {
		HudAnchor a = anchor == null ? HudAnchor.TOP_LEFT : anchor;
		int rx = switch (a) {
			case TOP_LEFT, MID_LEFT, BOTTOM_LEFT -> x;
			case TOP_CENTER, MID_CENTER, BOTTOM_CENTER -> (screenW - contentW) / 2 + x;
			case TOP_RIGHT, MID_RIGHT, BOTTOM_RIGHT -> screenW - contentW - x;
		};
		int ry = switch (a) {
			case TOP_LEFT, TOP_CENTER, TOP_RIGHT -> y;
			case MID_LEFT, MID_CENTER, MID_RIGHT -> (screenH - contentH) / 2 + y;
			case BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT -> screenH - contentH - y;
		};
		return new int[] { rx, ry };
	}
}
