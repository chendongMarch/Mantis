package com.zfy.mantis.api;

import android.app.Activity;
import android.app.Fragment;

import com.zfy.mantis.annotation.Lookup;
import com.zfy.mantis.annotation.LookupOpts;
import com.zfy.mantis.api.provider.BundleDataProvider;
import com.zfy.mantis.api.provider.IDataProvider;
import com.zfy.mantis.api.provider.IObjProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * CreateAt : 2019/1/14
 * Describe :
 *
 * @author chendong
 */
public class Mantis {

    public static final int DEFAULT_PRIORITY = -1000;

    private static boolean sInit;

    private static ThreadLocal<LookupOpts> sLookupOptsThreadLocal;
    private static MantisService           sInjectService;

    private static List<Mapper<IDataProvider>> sDataMappers;
    private static List<Mapper<IObjProvider>>  sObjMappers;

    public static void init() {
        if (sInit) {
            return;
        }
        sInit = true;
        sLookupOptsThreadLocal = new ThreadLocal<>();
        sLookupOptsThreadLocal.set(new LookupOpts());
        sInjectService = new MantisService();
        sDataMappers = new ArrayList<>();
        sObjMappers = new ArrayList<>();

        sDataMappers.add(new DefaultBundleProviderMapper());
    }

    public static IObjProvider getObjProvider(LookupOpts opts) {
        for (Mapper<IObjProvider> objMapper : sObjMappers) {
            IObjProvider provider = objMapper.map(opts);
            if (provider != null) {
                return provider;
            }
        }
        return null;
    }

    public static IDataProvider getDataProvider(LookupOpts opts) {
        for (Mapper<IDataProvider> dataMapper : sDataMappers) {
            IDataProvider provider = dataMapper.map(opts);
            if (provider != null) {
                return provider;
            }
        }
        return null;
    }

    public static void addObjProvider(Mapper<IObjProvider> mapper) {
        if (mapper.priority() <= DEFAULT_PRIORITY) {
            throw new IllegalArgumentException("priority must < " + DEFAULT_PRIORITY);
        }
        sObjMappers.add(0, mapper);
        Collections.sort(sObjMappers, (o1, o2) -> o2.priority() - o1.priority());
    }

    public static void addDataProvider(Mapper<IDataProvider> mapper) {
        if (mapper.priority() <= DEFAULT_PRIORITY) {
            throw new IllegalArgumentException("priority must < " + DEFAULT_PRIORITY);
        }
        sDataMappers.add(0, mapper);
        Collections.sort(sDataMappers, (o1, o2) -> o2.priority() - o1.priority());
    }

    public static LookupOpts obtainOpts() {
        return new LookupOpts();
    }

    public static void inject(int group, Object target) {
        sInjectService.inject(group, target);
    }

    public static void inject(Object target) {
        sInjectService.inject(Lookup.DEF_GROUP, target);
    }


    public static class DefaultBundleProviderMapper implements Mapper<IDataProvider> {

        @Override
        public int priority() {
            return DEFAULT_PRIORITY;
        }

        @Override
        public IDataProvider map(LookupOpts opts) {
            Object target = opts.target;
            if (target instanceof Activity
                    || target instanceof Fragment
                    || target instanceof android.support.v4.app.Fragment) {
                return BundleDataProvider.use(target);
            }
            return null;
        }
    }


}
