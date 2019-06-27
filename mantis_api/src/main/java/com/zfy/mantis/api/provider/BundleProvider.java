package com.zfy.mantis.api.provider;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Parcelable;

/**
 * CreateAt : 2019/1/29
 * Describe : 用来读取数据使用
 *
 * @author chendong
 */
public class BundleProvider implements IDataProvider {


    public static BundleProvider use(Object target) {
        Bundle bundle = null;
        if (target instanceof Activity) {
            bundle = ((Activity) target).getIntent().getExtras();
        } else if (target instanceof Fragment) {
            bundle = ((Fragment) target).getArguments();
        } else if (target instanceof android.support.v4.app.Fragment) {
            bundle = ((android.support.v4.app.Fragment) target).getArguments();
        }
        if (bundle == null) {
            bundle = new Bundle();
        }
        return new BundleProvider(bundle);
    }

    public static BundleProvider use(Bundle bundle) {
        return new BundleProvider(bundle);
    }

    public BundleProvider reset(Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        this.bundle = bundle;
        return this;
    }

    public BundleProvider reset(Object target) {
        Bundle bundle = null;
        if (target instanceof Activity) {
            bundle = ((Activity) target).getIntent().getExtras();
        } else if (target instanceof Fragment) {
            bundle = ((Fragment) target).getArguments();
        } else if (target instanceof android.support.v4.app.Fragment) {
            bundle = ((android.support.v4.app.Fragment) target).getArguments();
        }
        if (bundle == null) {
            bundle = new Bundle();
        }
        return reset(bundle);
    }

    private Bundle bundle;

    private BundleProvider(Bundle bundle) {
        this.bundle = bundle;
    }

    public BundleProvider() {
    }

    @Override
    public boolean getBoolean(String key, boolean def) {
        return bundle.getBoolean(key, def);
    }

    @Override
    public byte getByte(String key, byte def) {
        return bundle.getByte(key, def);
    }

    @Override
    public short getShort(String key, short def) {
        return bundle.getShort(key, def);
    }

    @Override
    public int getInt(String key, int def) {
        return bundle.getInt(key, def);
    }

    @Override
    public long getLong(String key, long def) {
        return bundle.getLong(key, def);
    }

    @Override
    public float getFloat(String key, float def) {
        return bundle.getFloat(key, def);
    }

    @Override
    public double getDouble(String key, double def) {
        return bundle.getDouble(key, def);
    }

    @Override
    public char getChar(String key, char def) {
        return bundle.getChar(key, def);
    }

    @Override
    public String getString(String key, String def) {
        return bundle.getString(key, def);
    }

    @Override
    public <T extends Parcelable> T getParcelable(String key) {
        return bundle.getParcelable(key);
    }

    @Override
    public <T> T getObject(String key, Class<T> clazz) {
        return null;
    }

}
