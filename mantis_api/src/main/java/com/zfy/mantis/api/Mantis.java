package com.zfy.mantis.api;

import com.zfy.mantis.api.provider.ProviderCallback;

/**
 * CreateAt : 2019/1/14
 * Describe :
 *
 * @author chendong
 */
public class Mantis {

    private static ProviderCallback sProviderCallback;
    private static AutowireService  sAutowireService;

    public static ProviderCallback getProviderCallback() {
        return sProviderCallback;
    }

    public static void init(ProviderCallback autoWireCallback) {
        sAutowireService = new AutowireService();
        sProviderCallback = autoWireCallback;
    }

    public static void injectArgs(Object target) {
        sAutowireService.autowire(target);
    }
}
