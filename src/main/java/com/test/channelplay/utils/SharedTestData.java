package com.test.channelplay.utils;

import lombok.Getter;
import lombok.Setter;

/**
 * Shared data holder for cross-layer data sharing between API and UI tests
 */
public class SharedTestData {

    private SharedTestData() {}
    
    @Getter
    @Setter
    private static String singularNameJson;


    @Getter
    @Setter
    private static String currentActivityName;
    
    /**
     * Reset all shared data - useful for cleanup between test scenarios
     */
    public static void reset() {
        singularNameJson = null;
        currentActivityName = null;
    }

}