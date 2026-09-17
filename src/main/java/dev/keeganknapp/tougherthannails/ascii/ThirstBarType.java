package dev.keeganknapp.tougherthannails.ascii;

import java.util.HashMap;
import java.util.Map;

public enum ThirstBarType {
        FULL("\uE784"),
        HALF("\uE790"),
        EMPTY("\uE791"),
        FULL_UNDERWATER("\uE828"),
        HALF_UNDERWATER("\uE826"),
        EMPTY_UNDERWATER("\uE827"),
        FULL_SATURATION_NUDGE("\uE846"),
        HALF_SATURATION_NUDGE("\uE847"),
        EMPTY_SATURATION_NUDGE("\uE848"),
        FULL_DEHYDRATION("\uE849"),
        HALF_DEHYDRATION("\uE850"),
        FULL_DEHYDRATION_UNDERWATER("\uE851"),
        HALF_DEHYDRATION_UNDERWATER("\uE852");

        private final String asciiCode;

        private ThirstBarType(String asciiCode) {
            this.asciiCode = asciiCode;
        }

        public String getAsciiCode() {
            return this.asciiCode;
        }

        private static final Map<String, ThirstBarType> CODE_MAP = new HashMap<>();

        static {
            for (ThirstBarType thirstUnit : ThirstBarType.values()) {
                CODE_MAP.put(thirstUnit.asciiCode, thirstUnit);
            }
        }
}

