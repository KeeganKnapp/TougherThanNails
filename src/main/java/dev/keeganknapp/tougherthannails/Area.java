package dev.keeganknapp.musicsync;

/**
 * The four independent music areas, matching the four fake-players / scoreboard
 * counters used by the original datapack (overworld, nether, end, underwater).
 */
public enum Area {
    OVERWORLD("green"),
    NETHER("red"),
    END("light_purple"),
    UNDERWATER("blue");

    /** Vanilla text color name used for the track title in the "Now Playing" message. */
    public final String titleColor;

    Area(String titleColor) {
        this.titleColor = titleColor;
    }
}
