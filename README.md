## 新长大助手

- #### 应用简介
###### 一款专门为长大师生打造的App。
###### 适配2018年的新教务系统！
###### github地址：[新长大助手开源项目](https://github.com/xiaoyvyv/YangtzeuApp "新长大助手")

- #### 包含的功能有
###### 教务通知查看以及教学文件下载、学生成绩查询、学生选课、快捷评教、课表保存、大学配套答案、长江大学图书馆索书号查询、留言板吐槽等等。
###### 更多精彩请下载后使用！[新长大助手](https://www.coolapk.com/apk/172018 "新长大助手")

- #### 小提示
###### 长大的学子们，觉得好用的话！多多宣传一下！笔芯^_^!

## 原理篇

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

## 软件截图

|主页|新闻|功能|
|:---:|:---:|:---:|
|![](https://www.showdoc.cc/server/api/common/visitfile/sign/6086e755233d6c8e88ec23b854854a91?showdoc=.jpg)|![](https://www.showdoc.cc/server/api/common/visitfile/sign/255e19a64c959515cd9b780385bb217f?showdoc=.jpg)|![](https://www.showdoc.cc/server/api/common/visitfile/sign/a74b885a2e8c3edcb56f1cee4489647c?showdoc=.jpg)|

|课表|我的|考试|
|:---:|:---:|:---:|
|![](https://www.showdoc.cc/server/api/common/visitfile/sign/861d01f10e4acc6ae25a67a322aaf35b?showdoc=.jpg)|![](https://www.showdoc.cc/server/api/common/visitfile/sign/b1febe9d1900fe593643f789d539598c?showdoc=.jpg)|![](https://www.showdoc.cc/server/api/common/visitfile/sign/dca4caa626a2a85b0d429d0b8efb39ac?showdoc=.jpg)|

## 代码示例

- **登录流程**
> [UserUtils.java](https://github.com/xiaoyvyv/YangtzeuApp/blob/master/app/src/main/java/com/yangtzeu/utils/UserUtils.java "UserUtils.java")

- **[长江大学官网](http://www.yangtzeu.edu.cn/ "大学官网")轮播图爬取，更新检查，在线人数等等**
>[YangtzeuUtils.java](https://github.com/xiaoyvyv/YangtzeuApp/blob/master/app/src/main/java/com/yangtzeu/utils/YangtzeuUtils.java "YangtzeuUtils.java")

- **爬取成绩，绩点**
>爬取成绩：[GradePart1Model.java](https://github.com/xiaoyvyv/YangtzeuApp/blob/master/app/src/main/java/com/yangtzeu/model/GradePart1Model.java "GradePart1Model.java")
>爬取绩点：[GradePart2Model.java](https://github.com/xiaoyvyv/YangtzeuApp/blob/master/app/src/main/java/com/yangtzeu/model/GradePart2Model.java "GradePart2Model.java")

- **爬取课表Js并解析：**
>[TableModel.java](https://github.com/xiaoyvyv/YangtzeuApp/blob/master/app/src/main/java/com/yangtzeu/model/TableModel.java "TableModel.java")

- **一卡通服务爬取**
>[CardCenterActivity.java](https://github.com/xiaoyvyv/YangtzeuApp/blob/master/app/src/main/java/com/yangtzeu/ui/activity/CardCenterActivity.java "CardCenterActivity.java")

- **修改密码**
>[ChangePassModel.java](https://github.com/xiaoyvyv/YangtzeuApp/blob/master/app/src/main/java/com/yangtzeu/model/ChangePassModel.java "ChangePassModel.java")

- **更多内容请自行翻阅源码**

## 写在最后

- **开发此新长大助手花费了许多个人精力，其中会有很多问题和不足的地方，如果您在使用的过程中遇到了Bug，请谅解！**

- **软件为个人开发，从前端到后台，独立完成。所以没有充足的时间和机型去测试App的兼容性，如果您遇到了什么问题，可以加入我们的[【官方群：617082514】](http://shang.qq.com/wpa/qunwpa?idkey=cce0b4adab1b1de2d6dc261ff73cf54396c22751fb36a9c9296fc8f376fb23f7 "官方群")进行咨询解答！群里会分享很多有用的东西**

- **本人大三狗一枚，机械专业，非计科。由于课程繁多难，更新时间可能会慢一点，如果没能在第一时间适配长江大学教务系统，还请您到[教务系统官网](http://jwc3.yangtzeu.edu.cn/eams/login.action "官网")进行操作！**

- **此源码请勿用做非法用途，并且最终解释权归我[&#64;小玉](http://ll.xyll520.top/ "&#64;小玉")所有**

- **大家都是学生党一枚，App的服务器都是租的，真心贵呀！由于没有经费租好的服务器，所以App在使用过程中，难免出现网络加载缓慢等现象，请谅解！**

- **最后，我又来厚脸皮一下下，如果此软件的确给您带来了方便，不妨打赏我一点点，或者领一下支付宝红包也利人利己，就当支持一下我吧！&hearts; &hearts; &hearts;**


|支付宝红包|支付宝|微信|简书|
|:---:|:---:|:---:|:---:|
|[![支付宝红包](https://www.showdoc.cc/server/api/common/visitfile/sign/d193c8d58ae66bad1ddb8d894eae5386?showdoc=.jpg "支付宝红包")](https://qr.alipay.com/c1x08894fliska9rxlrecb5 "支付宝红包")|![](https://www.showdoc.cc/server/api/common/visitfile/sign/33c3e6361a84bd44ac2efb36be829075?showdoc=.jpg)|[![微信](https://www.showdoc.cc/server/api/common/visitfile/sign/a67fc8383cae5a3b31b2e1c9284009eb?showdoc=.jpg "微信")](https://www.showdoc.cc/server/api/common/visitfile/sign/a67fc8383cae5a3b31b2e1c9284009eb?showdoc=.jpg "微信")|[![简书](https://www.showdoc.cc/server/api/common/visitfile/sign/a3e3243e51573f6568fd4031c4527ad0?showdoc=.jpg "简书")](https://www.showdoc.cc/server/api/common/visitfile/sign/a3e3243e51573f6568fd4031c4527ad0?showdoc=.jpg "简书")|

## 谢谢支持，完！





