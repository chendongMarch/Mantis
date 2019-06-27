package com.zfy.mantis.api;

import com.zfy.mantis.annotation.Lookup;
import com.zfy.mantis.annotation.LookupOpts;
import com.zfy.mantis.api.provider.IDataProviderFactory;
import com.zfy.mantis.api.provider.IObjProvider;

/**
 * CreateAt : 2019/1/14
 * Describe :
 *
 * @author chendong
 */
public class Mantis {

    private static ThreadLocal<LookupOpts> sLookupOptsThreadLocal;

    private static AutowireService  sAutowireService;

    private static IObjProvider         sObjProvider;
    private static IDataProviderFactory sDataProviderFactory;

    public static void init(IDataProviderFactory dataProviderFactory, IObjProvider provider) {
        sLookupOptsThreadLocal = new ThreadLocal<>();
        sLookupOptsThreadLocal.set(new LookupOpts());
        sAutowireService = new AutowireService();
        sObjProvider = provider;
        sDataProviderFactory = dataProviderFactory;
    }

    public static IObjProvider getObjProvider() {
        if (sObjProvider == null) {
            throw new IllegalStateException("set IDataProviderFactory first ~");
        }
        return sObjProvider;
    }

    public static IDataProviderFactory getDataProviderFactory() {
        if (sDataProviderFactory == null) {
            return IDataProviderFactory.BUNDLE_PROVIDER;
        }
        return sDataProviderFactory;
    }

    public static LookupOpts obtainOpts() {
        return sLookupOptsThreadLocal.get();
    }

    public static void inject(int group, Object target) {
        sAutowireService.inject(group, target);
    }

    public static void inject(Object target) {
        sAutowireService.inject(Lookup.DEF_GROUP, target);
    }
}
