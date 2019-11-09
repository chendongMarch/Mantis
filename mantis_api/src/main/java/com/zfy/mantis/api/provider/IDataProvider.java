package com.zfy.mantis.api.provider;

import android.os.Parcelable;

import com.zfy.mantis.annotation.LookupOpts;

/**
 * CreateAt : 2019/1/29
 * Describe : 用来取传递的数据
 *
 * @author chendong
 */
public interface IDataProvider {

    boolean getBoolean(LookupOpts opts, boolean def);

    byte getByte(LookupOpts opts, byte def);

    short getShort(LookupOpts opts, short def);

    int getInt(LookupOpts opts, int def);

    long getLong(LookupOpts opts, long def);

    float getFloat(LookupOpts opts, float def);

    double getDouble(LookupOpts opts, double def);

    char getChar(LookupOpts opts, char def);

    String getString(LookupOpts opts, String def);

    <T extends Parcelable> T getParcelable(LookupOpts opts);

    <T> T getObject(LookupOpts opts, Class<T> clazz);
}
