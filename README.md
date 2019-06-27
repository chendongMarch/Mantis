# Mantis


## 引入

```gradle
api 'com.zfy:mantis-api:0.0.6'
annatationProcessor 'com.zfy:mantis-compiler:0.0.6'
```



## 初始化

初始化需要提供两个东西：

- `IDataProviderFactory` **数据传输对象提供者**的构造工厂，用来完成从 `Intent/Bundle` 中获取数据；如果仅仅需要支持在 `Activity` 和 `Fragment` 中注解数据，则可以直接使用 `IDataProviderFactory.BUNDLE_PROVIDER` 不需要自己去实现复杂的逻辑；   

- `IObjProvider` **自定义对象提供者**，用来完成根据注解自动创建对象的操作；


```java
Mantis.init(new IDataProviderFactory() {
    @Override
    public IDataProvider create(Object target) {
        // 工厂创建 DataProvider 
        // 负责从 Bundle 和 Intent 里面取数据
        return null;
    }
}, new IObjProvider() {
    @Override
    public Object getObject(LookupOpts opts) {
        // 按照注解的数据，使用自己的规则来生成对象
        return null;
    }
});
```

为了更好的体现用法，提供一个比较复杂的例子：


DataProviderFactory

```java
public static class IDataProviderImpl implements IDataProviderFactory {
    BundleProvider provider = new BundleProvider();
    @Override
    public IDataProvider create(Object target) {
        // MVP - 给 Presenter 注解对应的传输数据
        if (target instanceof MvpPresenter) {
            IMvpView view = ((MvpPresenter) target).getView();
            return provider.reset(view.getData());
        }
      	 // MVVM 给 ViewModel 注解对应的传输数据
        if (target instanceof MvvmViewModel) {
            return provider.reset(((MvvmViewModel) target).getData());
        }
        provider.reset(target);
        return provider;
    }
}
```

ObjProvider

```java
public static class IObjProviderImpl implements IObjProvider {
    @Override
    public Object getObject(LookupOpts opts) {
        // 获取 class
        Class<?> clazz = opts.clazz != null ? opts.clazz : opts.fieldClazz;
        // 获取对应分组
        int group = opts.group;
        // 获取 target
        Object target = opts.target;
        // 生成 repo
        if (group == Const.REPO && AppRepository.class.isAssignableFrom(clazz)) {
            return X.newInst(clazz);
        }
        // class + target 指定在哪里生成什么样子的 viewModel
        if (group == Const.VM && MvvmViewModel.class.isAssignableFrom(clazz)) {
            return useViewModel(target, clazz);
        }
        // 生成 ARouter Service
        if (IProvider.class.isAssignableFrom(clazz)) {
            return provideARouterService(target, opts.key, clazz);
        }
        return null;
    }
}
```

## 启动注入

```java
// 默认注入，会注入未指定 group 的所有属性
Mantis.inject(Object object);
// 分组注入，注入指定 group 的所有属性
Mantis.inject(int group, Object object);
```

## 使用

从 `Intent` 或者 `Bundle` 中获取传递过来的数据:

```java
// 从 Bundle 中获取 key 为 ID 的 int 类型数据
@Lookup("ID") int id;

// 从 Bundle 中获取 key 为 NAME 的 String 类型数据
@Lookup(value = "NAME", desc = "我是名字") String  name;

// 从 Bundle 中获取 key 为 WXINFO 的 Parcelable 类型数据
@Lookup("WXINFO") WxInfo wxInfo;
```


自动注入生成对象:

```java
// 根据 key 生成对应的对象
// 类似 ARouter 的路由注解
@LookUp("/service/my/service") MyService service;

// 根据 组 + Class 生成对应的对象
// 将会生成 MyViewModel 类型的对象
@LookUp(group = 100) MyViewModel viewModel;

// 根据 组 + Class 生成对应的对象
// 将会生成 MainPresenter 类型的对象
// 可以使用接口持有变量，注入时使用 clazz 对应的子类
@LookUp(group = 100, clazz = MainPresenter.class) IPresenter presenter;
```®®
 