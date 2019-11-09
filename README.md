# Mantis

![](http://s3.hixd.com/139728.jpeg)

自动依赖注入框架，为了在编写的代码的时候把一些初始化的操作抽离出来，然后使用注解声明如何初始化；

## 引入

```gradle
api 'com.zfy:mantis-api:0.1.0'
annatationProcessor 'com.zfy:mantis-compiler:0.1.1'
```


## 初始化

```java
Mantis.init();
```


## 添加 Mapper

```java
// 让 Presenter 可以从 Intent 里面获取数据
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

// 自动初始化对象
Mantis.addObjProvider(new Mapper<IObjProvider>() {
    @Override
    public int priority() {
        return 400;
    }
    @Override
    public IObjProvider map(LookupOpts value) {
        // 如果字段类型是 MyService 的子类，就初始化
        if(!MantisUtil.isSubClass(value.fieldClazz, MyService.class)){
            return null;
        }
        Class<?> clazz = value.fieldClazz;
        return MantisUtil.newInstance(clazz);
    }
});
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
```
 