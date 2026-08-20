package dev.keeganknapp.musicsync;

/**
 * A single playable track, mirroring one indexed entry from one of the
 * {@code music/*.mcfunction} tables in the original datapack.
 *
 * @param soundId       the path component of the {@code globalmusic:<soundId>} sound event
 * @param durationTicks exact track length in ticks, as hardcoded in the original mcfunction
 * @param title         display title used in the "Now Playing" message
 * @param timeText      pre-formatted mm:ss text used in the original message (kept verbatim
 *                      rather than recomputed, since the original file's text and tick count
 *                      are not always a perfectly rounded pair)
 */
public record Track(String soundId, int durationTicks, String title, String timeText) {
}
