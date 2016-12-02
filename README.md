#混合开发基础模板

@[Hiboboo]

**Library.Hybrid**是一个专门为混合开发提供的基础原生API接口及中间驱动件的支持库，特点概述：
- **驱动件使用方便**：只需要在页面中引入*zepto.js*文件；
- **直接调用原生API**：在Js代码块中，引用HostApp类直接调用静态的原生API方法
- **原生API接口/方法高度定制**：原生的API接口可根据实际需求，快速高度定制化

-------------------

[TOC]

##Library.Hybrid混合支持库简介

>引入Library.Hybrid支持库之后，您的混合应用开发将变的简单，它可以提供轻量级且高能量的中间驱动件，同时提供个性化高度定制的原生API接口/方法

##使用方法介绍

- **页面引入**
``` vbscript-html
<script src="../zepto.js" type="text/javascript"/>
```
- **调用原生API**
``` javaScript
HostApp.showToast('我是消息提示框');
```
- **示例介绍**
``` HTML
<script src="../zepto.js" type="text/javascript"/>
<b>弹出提示框</b>
代码：
<button onclick="HostApp.showToast('我是消息提示框');">测试</button>
```

##版本2.7支持的原生API列表
- **弹出一个消息提示框**
``` java
showToast(String message);
```
- **显示一个带有确认/取消按钮的消息提示框**
``` java
showAlertDialog(String title, String message, function(){});
```
- **显示一个只带有确认按钮的消息提示框**
``` java
showAlertDialogForSingleBut(String title, String message, function(){});
```
- **显示一个进度提示对话框**
``` java
showProgressDialog(String message, boolean cancelable);
```
- **销毁一个正在显示中的进度提示对话框**
``` java
dismissProgressDialog();
```
- **显示一个只带有多个选项的消息提示框**
``` java
showAlertDialogForItems(String title, String items, function(item, position){});
```
- **显示一个只带有多个并且可以切换选项卡的消息提示框**
``` java
showTabsDialog(String urls, String tabs);
```
- **打开新页面**
``` java
openNewpage(String pageClsName, String title, String url);
```
- **打开不用传类名的新页面**
``` java
openNotclsNewpage(String title, String url);
```
- **打开一个传类名且带有自定义参数的新页面**
``` java
openNewpage(String pageClsName, String title, String url, String params);
```
- **打开一个不用传类名却带有自定义参数的新页面**
``` java
openNotclsNewpage(String title, String url, String params);
```
- **打开可以播放视频的新页面**
``` java
openVideoPlayerPage(String title, String url);
```
- **打开一个专用于表单的页面**
``` java
openFormPage(String title, String url);
```
- **回退至上一级页面并关闭当前页**
``` java
goBackBeforePage();
```
- **关闭当前页并回到首页**
``` java
goHomePage();
```
- **清除当前页面Cookies**
``` java
clearCookies();
```
- **获取当前设备的IMEI号码**
``` java
String getDeviceIMEI();
```
- **获取已选择的服务器地址**
``` java
String getBaseRequestUrl();
```
- **设置当前已登录用户的信息**
``` java
setUserInfo(String userinfo);
```
- **获取当前登录用户的信息**
``` java
String getCurrentUserInfo();
```
- **打开本地设备相机并获取照片拍摄后的绝对路径**
``` java
openCamera(function getFiles(filesInfo){});
//注意：参数filesInfo为标准JSON数组
返回结果样板：
[{'name':'IMG_-1198173822.jpg','size':2964508,'path':'/storage/emulated/0/DCIM/IMG_-1198173822.jpg'}]
```
- **打开本地设备相册并获取被选择的某张照片绝对路径**
``` java
openAlbum(function getFiles(filesInfo){});
//注意：参数filesInfo为标准JSON数组
返回结果样板：
[{'name':'IMG_-1198173822.jpg','size':2964508,'path':'/storage/emulated/0/DCIM/IMG_-1198173822.jpg'}]
```
- **打开本地文件管理器并获取已选择文件的绝对路径**
``` java
openFilePicker(function getFiles(filesInfo){});
//注意：参数filesInfo为标准JSON数组
返回结果样板：
[{'name':'IMG_-1198173822.jpg','size':2964508,'path':'/storage/emulated/0/DCIM/IMG_-1198173822.jpg'}]
```
- **上传文件**
该方法具有特殊性与高度定制性，不公开对外使用
``` java
uploadFiles(function uploadFile(response){}, , boolean isShowProgress, String message, String url, String params, String files);
```

##*注意*
该支持库中同时使用了一些第三方库，可能未是最新版，若与实际项目中的引入哭冲突，请Download当前支持库后，在本地更新最新版即可。
- **Gson2.7** [最新版点这里](https://github.com/google/gson)
- **NumberProgressBar1.2** [最新版点这里](https://github.com/daimajia/NumberProgressBar)
- **Galleryfinal:1.4.8.7** [最新版点这里](https://github.com/pengjianbo/GalleryFinal)
- **Glide:3.7.0**[最新版点这里](https://github.com/bumptech/glide)
- **Nbsp1.1** [最新版点这里](https://github.com/nbsp-team/MaterialFilePicker)
- **Permissionsdispatcher2.1.3** [最新版点这里](https://github.com/hotchemi/PermissionsDispatcher)
- **OkHttpUtils:2.6.2** [最新版点这里](https://github.com/hongyangAndroid/okhttputils)
- **SmartTablayout:1.6.1** [最新版点这里](https://github.com/ogaclejapan/SmartTabLayout)