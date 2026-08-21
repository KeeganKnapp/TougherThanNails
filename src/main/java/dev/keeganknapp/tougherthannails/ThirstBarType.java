package dev.keeganknapp.tougherthannails;

import java.util.HashMap;
import java.util.Map;

public enum ThirstBarType {
        FULL("\uE784"),
        HALF("\uE790"),
        EMPTY("\uE791");

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

