package dev.keeganknapp.musicsync;

/**
 * The exact track tables transcribed from the original datapack's
 * {@code data/globalmusic/function/music/*.mcfunction} files. Index 0 in each
 * array corresponds to roll result {@code 1} in the original {@code random value 1..N}
 * (i.e. {@code array[roll - 1]}).
 */
public final class Tracks {
    private Tracks() {
    }

    /**
     * From music/overworld.mcfunction — random value 1..37 originally; Blocks, Far, Mellohi, and
     * Wait were removed (they're music disc tracks, mixed/mastered as positional jukebox audio,
     * and sound bad played back non-positionally through this system), leaving 1..33.
     */
    public static final Track[] OVERWORLD = {
            new Track("alpha", 12060, "Alpha", "10:03"),
            new Track("aria_math", 6200, "Aria Math", "5:10"),
            new Track("beginning_2", 3520, "Beginning 2", "2:56"),
            new Track("biome_fest", 7560, "Biome Fest", "6:18"),
            new Track("blind_spots", 6640, "Blind Spots", "5:32"),
            new Track("chris", 1740, "Chris", "1:27"),
            new Track("clark", 3820, "Clark", "3:11"),
            new Track("danny", 5080, "Danny", "4:14"),
            new Track("death", 840, "Death", "0:42"),
            new Track("door", 2220, "Door", "1:51"),
            new Track("dreiton", 9940, "Dreiton", "8:17"),
            new Track("droopy_likes_ricochet", 1920, "Droopy Likes Ricochet", "1:36"),
            new Track("droopy_likes_your_face", 2340, "Droopy Likes Your Face", "1:57"),
            new Track("dry_hands", 1360, "Dry Hands", "1:08"),
            new Track("excuse", 2480, "Excuse", "2:04"),
            new Track("flake", 3400, "Flake", "2:50"),
            new Track("floating_trees", 4880, "Floating Trees", "4:04"),
            new Track("haggstrom", 4080, "Haggstrom", "3:24"),
            new Track("haunt_muskie", 7220, "Haunt Muskie", "6:01"),
            new Track("intro", 5520, "Intro", "4:36"),
            new Track("key", 1300, "Key", "1:05"),
            new Track("ki", 1840, "Ki", "1:32"),
            new Track("kyoto", 4980, "Kyoto", "4:09"),
            new Track("living_mice", 3540, "Living Mice", "2:57"),
            new Track("mice_on_venus", 5620, "Mice on Venus", "4:41"),
            new Track("minecraft_theme", 5080, "Minecraft", "4:14"),
            new Track("moog_city", 3200, "Moog City", "2:40"),
            new Track("mutation", 3700, "Mutation", "3:05"),
            new Track("oxygene", 1300, "Oxygène", "1:05"),
            new Track("subwoofer_lullaby", 4160, "Subwoofer Lullaby", "3:28"),
            new Track("sweden", 4300, "Sweden", "3:35"),
            new Track("taswell", 10300, "Taswell", "8:35"),
            new Track("wet_hands", 1800, "Wet Hands", "1:30"),
    };

    /** From music/nether.mcfunction — random value 1..4. */
    public static final Track[] NETHER = {
            new Track("ballad_of_the_cats", 5500, "Ballad of the Cats", "4:35"),
            new Track("concrete_halls", 5080, "Concrete Halls", "4:14"),
            new Track("dead_voxel", 5920, "Dead Voxel", "4:56"),
            new Track("warmth", 4780, "Warmth", "3:59"),
    };

    /** From music/end.mcfunction — random value 1..2. */
    public static final Track[] END = {
            new Track("boss", 6860, "Boss", "5:43"),
            new Track("the_end", 18080, "The End", "15:04"),
    };

    /** From music/water.mcfunction — random value 1..3. */
    public static final Track[] UNDERWATER = {
            new Track("axolotl", 6060, "Axolotl", "5:03"),
            new Track("dragon_fish", 7460, "Dragon Fish", "6:13"),
            new Track("shuniji", 4900, "Shuniji", "4:05"),
    };

    public static Track[] forArea(Area area) {
        return switch (area) {
            case OVERWORLD -> OVERWORLD;
            case NETHER -> NETHER;
            case END -> END;
            case UNDERWATER -> UNDERWATER;
        };
    }
}
