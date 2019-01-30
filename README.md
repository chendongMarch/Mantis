AutoWired


```gradle
api 'com.zfy:mantis-api:0.0.2'
api 'com.zfy:mantis-annotation:0.0.2'
annatationProcessor 'com.zfy:mantis-compiler:0.0.3'
```



```java
@LookUp(value = "test1", desc = "我是名字")   byte  test1;
@LookUp(value = "test2", required = true) short test2;
@LookUp("test3")  int       test3;
@LookUp("test4")  long      test4;
@LookUp("test5")  float     test5;
@LookUp("test6")  double    test6;
@LookUp("test7")  boolean   test7;
@LookUp("test8")  char      test8;
@LookUp("test9")  String    test9;
@LookUp("test10") WxInfo    test10;
@LookUp("test11") MyService test11;
```


```java
public class BasicProviderCallbackImpl extends ProviderCallbackImpl {
    @Override
    public IDataProvider getDataProvider(Object target) {
        // presenter 可以注入 data
        if (target instanceof MvpPresenter) {
            IMvpView view = ((MvpPresenter) target).getView();
            return BundleProvider.use(view.getData());
        }
        return super.getDataProvider(target);
    }

    @Override
    public IObjProvider getObjProvider(Object target, IDataProvider dataProvider) {
        return (key, clazz) -> {
            if (IProvider.class.isAssignableFrom(clazz)) {
                // 为 ARouter 注入 Service 实例
                return provideARouterService(target, key, clazz);
            }
            return null;
        };
    }

    // 帮助发现服务
    private IProvider provideARouterService(Object target, String key, Class<?> clazz) {
        IProvider provider = null;
        if (EmptyX.isEmpty(key)) {
            provider = (IProvider) ComponentX.service(clazz);
        } else {
            provider = ComponentX.service(key);
        }
        if (provider != null) {
            if (target instanceof Context) {
                provider.init((Context) target);
            } else if (target instanceof MvpPresenter) {
                provider.init(((MvpPresenter) target).getView().getContext());
            }
        }
        return provider;
    }
}
```