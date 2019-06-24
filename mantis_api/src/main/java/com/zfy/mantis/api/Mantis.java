package com.zfy.mantis.api;

import com.zfy.mantis.api.provider.ProviderCallback;

/**
 * CreateAt : 2019/1/14
 * Describe :
 *
 * @author chendong
 */
public class Mantis {

    private volatile static Mantis sInst;

    public static Mantis getInst() {
        if (sInst == null) {
            synchronized (Mantis.class) {
                if (sInst == null) {
                    sInst = new Mantis();
                }
            }
        }
        return sInst;
    }

    private Mantis() {
        sAutowireService = new AutowireService();
    }

    private static ProviderCallback sProviderCallback;
    private static AutowireService  sAutowireService;

    public static ProviderCallback getProviderCallback() {
        return sProviderCallback;
    }

    public static void init(ProviderCallback autoWireCallback) {
        sAutowireService = new AutowireService();
        sProviderCallback = autoWireCallback;
    }

    public void injectArgs(Object target) {
        sAutowireService.autowire(target);
    }
}
