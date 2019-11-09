package com.zfy.mantis.api;

import android.support.annotation.NonNull;

import com.zfy.mantis.api.provider.IObjProvider;

/**
 * CreateAt : 2019-11-08
 * Describe :
 *
 * @author chendong
 */
public class MantisUtil {


    public static boolean isSubClass(Class<?> targetClazz, @NonNull Class<?> parentClazz) {
        if (targetClazz == null) {
            return false;
        }
        return parentClazz.isAssignableFrom(targetClazz);
    }

    public static IObjProvider newInstance(Class<?> targetClazz) {
        return opts -> {
            try {
                if (targetClazz == null || targetClazz.isInterface()) {
                    return null;
                }
                return targetClazz.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        };
    }


}
