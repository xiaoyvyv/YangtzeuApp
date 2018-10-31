##新长大助手

- #### 应用简介
###### 一款专门为长大师生打造的App。
###### 适配2018年的新教务系统！
###### github地址：[新长大助手开源项目](https://github.com/xiaoyvyv/YangtzeuApp "新长大助手")

- #### 包含的功能有
###### 教务通知查看以及教学文件下载、学生成绩查询、学生选课、快捷评教、课表保存、大学配套答案、长江大学图书馆索书号查询、留言板吐槽等等。
###### 更多精彩请下载后使用！[新长大助手](https://www.coolapk.com/apk/172018 "新长大助手")

- #### 小提示
###### 长大的学子们，觉得好用的话！多多宣传一下！笔芯^_^!

##原理篇
- #### 基本原理
**App的基本原理**
###### 通过对长江大学教务系统进行抓包，然后分析其登录流程，然后在Android客户端模拟整个登录流程，并且将登录成功后的Cookie保存下来，然后访问相应的页面时携带上Cookie。
**用到的开源项目**
##### 1. [SmartRefreshLayout](https://github.com/scwang90/SmartRefreshLayout "SmartRefreshLayout")
SmartRefreshLayout的目标是打造一个强大，稳定，成熟的下拉刷新框架，并集成各种的炫酷、多样、实用、美观的Header和Footer。
##### 2.[BGABanner-Android](https://github.com/bingoogolapple/BGABanner-Android "BGABanner-Android")
新闻页面轮播图，广告，导航等等
##### 3.[Glide](https://github.com/bumptech/glide "Glide")
图片加载框架，链式调用，一行代码加载网络图片，支持gif，很方便
```java
Glide.with(context).load(iamge).into(imageView);
```
##### 4.[Gson](https://github.com/google/gson "Gson")
官方的json数据解析框架，很方便
##### 5.[Okhttp3](https://github.com/square/okhttp "Okhttp3")
这东西没的说，熟悉Android的人都知道，github上非常火的Android网络请求框架
##### 6.[PersistentCookieJar](https://github.com/franmontiel/PersistentCookieJar "PersistentCookieJar")
这个是持久化Cookie用的，配合Okhttp3使用
```java
SetCookieCache setCookieCache = new SetCookieCache();
SharedPrefsCookiePersistor sharedPrefsCookiePersistor = new SharedPrefsCookiePersistor(context);
PersistentCookieJar cookieJar = new PersistentCookieJar(setCookieCache, sharedPrefsCookiePersistor);
OkHttpClient okHttpClient = new OkHttpClient.Builder()
     .connectTimeout(10000, TimeUnit.MILLISECONDS)
     .proxy(Proxy.NO_PROXY)
     .cookieJar(cookieJar)
     .build();
```
##### 7.[CircleImageView](https://github.com/hdodenhof/CircleImageView "CircleImageView")
圆形图片，如头像框等等
##### 8.[Jsoup](https://github.com/jhy/jsoup "Jsoup")
这个是整个App的核心框架，主要作用是从Html中剔出我们需要的数据，规则是css选择器，很方便的框架。
##### 9.[AndroidUtilCode](https://github.com/Blankj/AndroidUtilCode "AndroidUtilCode")
一个工具类库，有很多工具类，很方便
##### 10.[七牛云](https://github.com/qiniu/android-sdk "七牛云")
这个主要是用来作为图床的，存放用户的图片数据
##### 11.[Mob](http://www.mob.com/ "Mob")
用到了Mob提供的在线聊天系统，和部分Api，这个网站很友好，都是免费的
##### 12.[腾讯X5内核](https://x5.tencent.com/ "腾讯X内核")
主要是Android的原生WebView太垃圾了，所以接入了X5内核

##软件截图
[![主页](https://www.showdoc.cc/server/api/common/visitfile/sign/23c1675cbbdd87ff96e7e801f754676e?showdoc=.jpg "主页")](https://www.showdoc.cc/server/api/common/visitfile/sign/23c1675cbbdd87ff96e7e801f754676e?showdoc=.jpg "主页")[![主页](https://www.showdoc.cc/server/api/common/visitfile/sign/23c1675cbbdd87ff96e7e801f754676e?showdoc=.jpg "主页")](https://www.showdoc.cc/server/api/common/visitfile/sign/23c1675cbbdd87ff96e7e801f754676e?showdoc=.jpg "主页")



















