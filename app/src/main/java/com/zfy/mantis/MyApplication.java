package com.zfy.mantis;

import android.app.Application;

import com.zfy.mantis.api.Mantis;
import com.zfy.mantis.api.provider.BundleProvider;
import com.zfy.mantis.api.provider.IDataProviderFactory;
import com.zfy.mantis.model.MyService;
import com.zfy.mantis.model.MyService2;
import com.zfy.mantis.model.MyService2Impl;

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
        Mantis.init(target -> {
            if (target instanceof MainPresenter) {
                return BundleProvider.use(((MainPresenter) target).mMainActivity.getIntent().getExtras());
            }
            return IDataProviderFactory.BUNDLE_PROVIDER.create(target);
        }, opts -> {
            Class<?> clazz = opts.clazz != null ? opts.clazz : opts.fieldClazz;
            if (clazz == null) {
                return null;
            }
            if (opts.group == 11) {
                return new MyService2Impl();
            }
            // 接口无法实现实例
            if (clazz.isInterface()) {
                return null;
            }
            // 是 MyService2 的子类
            if (MyService2.class.isAssignableFrom(clazz)) {
                try {
                    return clazz.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            // 是 MyService 的子类
            if (MyService.class.isAssignableFrom(clazz)) {
                try {
                    return clazz.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            return null;
        });
    }
}
