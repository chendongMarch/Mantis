package com.zfy.mantis;

import android.app.Application;

import com.zfy.mantis.annotation.LookupOpts;
import com.zfy.mantis.api.Mantis;
import com.zfy.mantis.api.MantisUtil;
import com.zfy.mantis.api.Mapper;
import com.zfy.mantis.api.provider.BundleDataProvider;
import com.zfy.mantis.api.provider.IDataProvider;
import com.zfy.mantis.api.provider.IObjProvider;
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

        Mantis.init();

        Mantis.addDataProvider(new Mapper<IDataProvider>() {

            @Override
            public int priority() {
                return 100;
            }

            @Override
            public IDataProvider map(LookupOpts opts) {
                if (!(opts.target instanceof MainPresenter)) {
                    return null;
                }
                MainPresenter target = ((MainPresenter) opts.target);
                return BundleDataProvider.use(target.mMainActivity.getIntent().getExtras());
            }
        });

        Mantis.addObjProvider(new Mapper<IObjProvider>() {

            @Override
            public int priority() {
                return 200;
            }

            @Override
            public IObjProvider map(LookupOpts value) {
                if (value.group != 11) {
                    return null;
                }
                return opts -> new MyService2Impl();
            }
        });

        Mantis.addObjProvider(new Mapper<IObjProvider>() {

            @Override
            public int priority() {
                return 300;
            }

            @Override
            public IObjProvider map(LookupOpts value) {
                if (MantisUtil.isSubClass(value.clazz, MyService2.class)) {
                    return MantisUtil.newInstance(value.clazz);
                } else if (MantisUtil.isSubClass(value.fieldClazz, MyService2.class)) {
                    return MantisUtil.newInstance(value.fieldClazz);
                }
                return null;
            }
        });

        Mantis.addObjProvider(new Mapper<IObjProvider>() {

            @Override
            public int priority() {
                return 400;
            }

            @Override
            public IObjProvider map(LookupOpts value) {
                if(!MantisUtil.isSubClass(value.fieldClazz, MyService.class)){
                    return null;
                }
                Class<?> clazz = value.fieldClazz;
                return MantisUtil.newInstance(clazz);
            }
        });
    }
}
