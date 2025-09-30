package com.test.channelplay.utils;

import lombok.Getter;
import lombok.Setter;

/**
 * Shared data holder for cross-layer data sharing between classes and test layers
 * Implemented as a singleton with static fields for simplicity
 */

public class SharedTestData {
    private SharedTestData() {}


    @Getter
    @Setter
    private static String singularNameJson;


    @Getter
    @Setter
    private static String currentActivityName;


    //  Feature - AddActivity_testUser
    @Getter
    @Setter
    private static String current_SerialNo_OffsiteAct;
    @Getter
    @Setter
    private static String current_Title_OffsiteAct;




    //  Reset all shared data - useful for cleanup between test scenarios
    public static void reset() {
        singularNameJson = null;
        currentActivityName = null;
        current_SerialNo_OffsiteAct = null;
        current_Title_OffsiteAct = null;
    }

}