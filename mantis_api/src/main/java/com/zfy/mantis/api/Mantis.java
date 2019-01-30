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
        mReceiveHandler = new AutowireService();
    }

    private ProviderCallback mAutoWireCallback;
    private AutowireService  mReceiveHandler;

    public ProviderCallback getProviderCallback() {
        return mAutoWireCallback;
    }

    public void setProviderCallback(ProviderCallback autoWireCallback) {
        mAutoWireCallback = autoWireCallback;
    }

    public void inject(Object target) {
        mReceiveHandler.autowire(target);
    }
}
