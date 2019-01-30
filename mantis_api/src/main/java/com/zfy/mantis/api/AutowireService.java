package com.zfy.mantis.api;

import android.util.LruCache;

import java.util.ArrayList;
import java.util.List;

/**
 * CreateAt : 2019/1/30
 * Describe :
 *
 * @author chendong
 */
public class AutowireService {

    private LruCache<String, ISyringe> mSyringeCache;
    private List<String>               mBlackList;

    public AutowireService() {
        mSyringeCache = new LruCache<>(66);
        mBlackList = new ArrayList<>();
    }

    public void autowire(Object instance) {
        String className = instance.getClass().getName();
        try {
            if (!mBlackList.contains(className)) {
                ISyringe syringe = mSyringeCache.get(className);
                if (null == syringe) {  // No cache.
                    syringe = (ISyringe) Class.forName(instance.getClass().getName() + "$$MantisAutoWired").getConstructor().newInstance();
                }
                syringe.inject(instance);
                mSyringeCache.put(className, syringe);
            }
        } catch (Exception ex) {
            mBlackList.add(className);
        }
    }
}
