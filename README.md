# Mantis

![](http://s3.hixd.com/139728.jpeg)

本类库主要做一件事情，通过 `APT` 自动生成代码将注解在 `Field` 上面的信息收集起来，然后传递给使用者，使用者根据注解的信息选择性的生成或或者数据；

- 字段的声明和初始化代码不在一起，代码混乱，容易忘记初始化；
- 创建对象时需要做一些统一操作，比如 `init()` 等；
- 做到注解后就可以使用，避免重复代码编写；

注入分为了两种，数据注入和对象注入

- 数据注入指的是从 `Intent` `Bundle` 等参数中获取传递数据的过程；
- 对象注入指的是根据注解的信息创建新对象的过程；

## 引入

```gradle
api 'com.zfy:mantis-api:0.1.2'
annatationProcessor 'com.zfy:mantis-compiler:0.1.2'
```


## 初始化

```java
Mantis.init();
```

## 启动注入

```java
// 默认注入，会注入未指定 group 的所有属性
Mantis.inject(Object object);
// 分组注入，注入指定 group 的所有属性
Mantis.inject(int group, Object object);
```



## 注解的使用

数据注入

```java
// 从 Intent 中获取 Key 为 KEY_INT_VALUE 的数据，默认值为 100
@Lookup("KEY_INT_VALUE") int dataValue = 100;
```

对象注入

```java
@Lookup(
        group = 101, // 分组，注解的属性会被分组，每次注入一个组，避免重复注入
        value = "KEY", // 字符串类型的 key
        numKey = 100, // 整型类型的 key
        clazz = MyService2Impl.class, // 注入的 class
        extra = 123, // 标记为，可以使用二进制表示32个标记位
        obj = false, // 强制作为对象注入
        required = true, // 如果注入的结果为空，将会抛出 npe
        desc = "我是注释信息" // 自动生成相关的注释信息
)
MyService2 objValue;
```

## 添加 Mapper

`Mapper` 是处理注解信息的一系列函数的集合，默认添加了支持从 `Activity` / `Fragment` 注入数据的 `Mapper`，添加自定的 `Mapper` 就可以灵活的扩展自动注入的过程；

```java
// 让 Presenter 支持使用注解直接从关联的 View 从获取数据
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

// 自动初始化对象，根据注入的 class 自动创建对象
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
 