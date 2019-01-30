package com.zfy.mantis.api.provider;

import android.os.Parcelable;

/**
 * CreateAt : 2019/1/29
 * Describe :
 *
 * @author chendong
 */
public interface IDataProvider {

    boolean getBoolean(String key, boolean def);

    byte getByte(String key, byte def);

    short getShort(String key, short def);

    int getInt(String key, int def);

    long getLong(String key, long def);

    float getFloat(String key, float def);

    double getDouble(String key, double def);

    char getChar(String key, char def);

    String getString(String key, String def);

    <T extends Parcelable> T getParcelable(String key);

    <T> T getObject(String key, Class<T> clazz);
}
