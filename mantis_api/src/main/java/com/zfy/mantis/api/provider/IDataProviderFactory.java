package com.zfy.mantis.api.provider;

/**
 * CreateAt : 2019/6/27
 * Describe :
 *
 * @author chendong
 */
public interface IDataProviderFactory {

    IDataProviderFactory BUNDLE_PROVIDER = new IDataProviderFactory() {

        BundleProvider provider = new BundleProvider();

        @Override
        public IDataProvider create(Object target) {
            provider.reset(target);
            return provider;
        }
    };

    IDataProvider create(Object target);
}
