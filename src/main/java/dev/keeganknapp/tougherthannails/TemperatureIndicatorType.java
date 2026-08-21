package dev.keeganknapp.tougherthannails;

import java.util.HashMap;
import java.util.Map;

public class TemperatureIndicatorType {
    private static final String[] GLYPHS = {
        "\uE786",
        "\uE786",
        "\uE786",
        "\uE786",
        "\uE786",
        "\uE786",
        "\uE806",
        "\uE807",
        "\uE808",
        "\uE809",
        "\uE810",
        "\uE811",
        "\uE812",
        "\uE813",
        "\uE814",
        "\uE815",
        "\uE816",
        "\uE817",
        "\uE818",
        "\uE819",
        "\uE785",
        "\uE785",
        "\uE785",
        "\uE785",
        "\uE785"
    };

    public static String getGlyph(int level) {
        if(level < 0 || level > 26) {
            return "INVALID";
        }
        else {
            return GLYPHS[level];
        }
    }

}

