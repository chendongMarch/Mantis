package com.zfy.mantis.api;

import android.util.LruCache;

import com.zfy.mantis.annotation.Lookup;

import java.util.ArrayList;
import java.util.List;

/**
 * CreateAt : 2019/1/30
 * Describe :
 *
 * @author chendong
 */
public class AutowireService {

    private LruCache<String, ISyringe> mSyringeLruCache;
    private List<String>               mBlackList;

    public AutowireService() {
        mSyringeLruCache = new LruCache<>(66);
        mBlackList = new ArrayList<>();
    }

    public void injectArgs(int type, Object instance) {
        ISyringe injector = findInjector(instance);
        if (injector != null) {
            injector.inject(type, instance);
        }
    }

    private ISyringe findInjector(Object instance) {
        String className = instance.getClass().getName();
        try {
            if (!mBlackList.contains(className)) {
                ISyringe injector = mSyringeLruCache.get(className);
                if (null == injector) {
                    // No cache.
                    injector = (ISyringe) Class.forName(instance.getClass().getName() + Lookup.SEP + Lookup.SUFFIX).getConstructor().newInstance();
                }
                mSyringeLruCache.put(className, injector);
                return injector;
            }
            return null;
        } catch (Exception ex) {
            mBlackList.add(className);
        }
        return null;
    }
}
