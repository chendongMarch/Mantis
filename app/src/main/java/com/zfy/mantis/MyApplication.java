package com.zfy.mantis;

import android.app.Application;

import com.zfy.mantis.api.provider.BundleProvider;
import com.zfy.mantis.api.provider.IDataProvider;
import com.zfy.mantis.api.provider.IObjProvider;
import com.zfy.mantis.api.Mantis;
import com.zfy.mantis.api.provider.ProviderCallbackImpl;

/**
 * CreateAt : 2019/1/30
 * Describe :
 *
 * @author chendong
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Mantis.init(new ProviderCallbackImpl() {
            @Override
            public IDataProvider getDataProvider(Object target) {
                if (target instanceof MainPresenter) {
                    return BundleProvider.use(((MainPresenter) target).mMainActivity.getIntent().getExtras());
                }
                return super.getDataProvider(target);
            }

            @Override
            public IObjProvider getObjProvider(Object target, IDataProvider dataProvider) {
                return (key, clazz) -> {
                    if (clazz.isAssignableFrom(MyService.class)) {
                        try {
                            return clazz.newInstance();
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                    return null;
                };
            }
        });
    }
}
