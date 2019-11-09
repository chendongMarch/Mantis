package com.zfy.mantis.api.provider;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.MainThread;

import com.zfy.mantis.annotation.LookupOpts;

/**
 * CreateAt : 2019/1/29
 * Describe : 用来读取数据使用
 *
 * @author chendong
 */
public class BundleDataProvider implements IDataProvider {

    private volatile static BundleDataProvider sInst        = new BundleDataProvider();
    public static final     Bundle             EMPTY_BUNDLE = new Bundle();

    @MainThread
    public static BundleDataProvider use(Object target) {
        if (sInst.reset(target)) {
            return sInst;
        }
        return null;
    }

    @MainThread
    public static BundleDataProvider use(Bundle bundle) {
        sInst.bundle = bundle;
        return sInst;
    }

    private boolean reset(Object target) {
        Bundle bundle = null;
        if (target instanceof Activity) {
            bundle = ((Activity) target).getIntent().getExtras();
        } else if (target instanceof Fragment) {
            bundle = ((Fragment) target).getArguments();
        } else if (target instanceof android.support.v4.app.Fragment) {
            bundle = ((android.support.v4.app.Fragment) target).getArguments();
        }
        if (bundle == null) {
            return false;
        }
        this.bundle = bundle;
        return true;
    }

    private Bundle bundle;

    BundleDataProvider() {
    }

    @Override
    public boolean getBoolean(LookupOpts opts, boolean def) {
        return bundle.getBoolean(opts.key, def);
    }

    @Override
    public byte getByte(LookupOpts opts, byte def) {
        return bundle.getByte(opts.key, def);
    }

    @Override
    public short getShort(LookupOpts opts, short def) {
        return bundle.getShort(opts.key, def);
    }

    @Override
    public int getInt(LookupOpts opts, int def) {
        return bundle.getInt(opts.key, def);
    }

    @Override
    public long getLong(LookupOpts opts, long def) {
        return bundle.getLong(opts.key, def);
    }

    @Override
    public float getFloat(LookupOpts opts, float def) {
        return bundle.getFloat(opts.key, def);
    }

    @Override
    public double getDouble(LookupOpts opts, double def) {
        return bundle.getDouble(opts.key, def);
    }

    @Override
    public char getChar(LookupOpts opts, char def) {
        return bundle.getChar(opts.key, def);
    }

    @Override
    public String getString(LookupOpts opts, String def) {
        return bundle.getString(opts.key, def);
    }

    @Override
    public <T extends Parcelable> T getParcelable(LookupOpts opts) {
        return bundle.getParcelable(opts.key);
    }

    @Override
    public <T> T getObject(LookupOpts opts, Class<T> clazz) {
        return null;
    }

}
