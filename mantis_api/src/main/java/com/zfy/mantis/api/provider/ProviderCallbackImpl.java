package com.zfy.mantis.api.provider;

/**
 * CreateAt : 2019/1/29
 * Describe :
 *
 * @author chendong
 */
public class ProviderCallbackImpl implements ProviderCallback {

    @Override
    public IDataProvider getDataProvider(Object target) {
        return BundleProvider.use(target);
    }

    @Override
    public IObjProvider getObjProvider(Object target, IDataProvider dataProvider) {
        return null;
    }

}
