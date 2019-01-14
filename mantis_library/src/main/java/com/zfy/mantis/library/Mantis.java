package com.zfy.mantis.library;

import android.content.SharedPreferences;

/**
 * CreateAt : 2019/1/14
 * Describe :
 *
 * @author chendong
 */
public class Mantis {

    private static SerializeParser   sSerializeParser;
    private static SharedPreferences sSharedPreferences;

    public static void initKv(SharedPreferences preferences, SerializeParser parser) {
        sSerializeParser = parser;
        sSharedPreferences = preferences;
    }

    public static SerializeParser getParser() {
        return sSerializeParser;
    }


    public static SharedPreferences getPreferences() {
        return sSharedPreferences;
    }
}
