package com.zzstxx.library.hybrid.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.widget.Toast;

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;
import com.zzstxx.library.hybrid.R;
import com.zzstxx.library.hybrid.actions.H5VideoPlayerActivity;
import com.zzstxx.library.hybrid.actions.HybridFormActivity;
import com.zzstxx.library.hybrid.actions.HybridNewActivity;
import com.zzstxx.library.hybrid.safebridge.JsCallback;
import com.zzstxx.library.hybrid.views.HybridTabsDialog;
import com.zzstxx.library.hybrid.views.ProgressDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import okhttp3.Call;

public class HostJsScope
{
    /**
     * 弹出一个消息提示
     *
     * @param webView 浏览器对象
     * @param message 消息内容
     */
    public static void showToast(WebView webView, String message)
    {
        Toast.makeText(webView.getContext(), message, Toast.LENGTH_LONG).show();
    }

    /**
     * 显示一个持久性的系统消息对话框
     *
     * @param webView    浏览器
     * @param title      标题
     * @param message    消息内容
     * @param jsCallback JavaScript函数{@code function(){}}
     */
    public static void showAlertDialog(WebView webView, String title, String message, JsCallback jsCallback)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(webView.getContext());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        final JsCallback mCallback = jsCallback;
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                try
                {
                    mCallback.onClickCallback();
                } catch (JsCallback.JsCallbackException e)
                {
                    e.printStackTrace();
                }
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }

    /**
     * 显示一个用于进度提示的消息提示对话框
     *
     * @param webView    浏览器
     * @param message    文字消息
     * @param cancelable 是否允许触摸对话框以外的其他区域来关闭对话框
     */
    public static void showProgressDialog(WebView webView, String message, boolean cancelable)
    {
        Context context = webView.getContext();
        if (context instanceof FragmentActivity)
        {
            FragmentActivity activity = (FragmentActivity) context;
            FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
            ProgressDialogFragment dialog = ProgressDialogFragment.newInstance(message, cancelable);
            dialog.show(transaction, "progress.dialog");
        }
    }

    /**
     * 销毁一个正在显示中的进度对话框
     *
     * @param webView 浏览器
     */
    public static void dismissProgressDialog(WebView webView)
    {
        Context context = webView.getContext();
        if (context instanceof FragmentActivity)
        {
            FragmentActivity activity = (FragmentActivity) context;
            Fragment fragment = activity.getSupportFragmentManager().findFragmentByTag("progress.dialog");
            if (fragment instanceof DialogFragment)
                ((DialogFragment) fragment).dismiss();
        }
    }

    /**
     * 显示一个持久性的系统消息对话框，且只带有一个‘确认’按钮
     *
     * @param webView    浏览器
     * @param title      标题
     * @param message    消息内容
     * @param jsCallback JavaScript函数{@code function(){}}
     */
    public static void showAlertDialogForSingleBut(WebView webView, String title, String message, JsCallback jsCallback)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(webView.getContext());
        builder.setTitle(title);
        builder.setMessage(message);
        final JsCallback mCallback = jsCallback;
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                try
                {
                    mCallback.onClickCallback();
                } catch (JsCallback.JsCallbackException e)
                {
                    e.printStackTrace();
                }
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }

    /**
     * 显示一个持久性的系统消息对话框，将带有多个Item选项
     *
     * @param webView    浏览器
     * @param title      标题
     * @param items      菜单选项。以英文半角,符号分割
     * @param jsCallback 结果回传
     */
    public static void showAlertDialogForItems(WebView webView, String title, String items, final JsCallback jsCallback)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(webView.getContext());
        builder.setTitle(title);
        final String[] itemNames = items.split(",");
        builder.setItems(itemNames, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                try
                {
                    jsCallback.onClickCallback(itemNames[which], which);
                } catch (JsCallback.JsCallbackException e)
                {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }

    /**
     * 先是一个带有选项卡并且可以切换的对话框
     *
     * @param webView 浏览器
     * @param urls    每个选项卡对应的页面要显示的URL源链接合集
     * @param tabs    选项卡名称合集，并与参数urls的数量必须要保持一一对应
     * @throws Exception 当对话框创建失败，抛出该异常
     */
    public static void showTabsDialog(WebView webView, String urls, String tabs) throws Exception
    {
        Context context = webView.getContext();
        if (context instanceof AppCompatActivity)
        {
            AppCompatActivity compatActivity = (AppCompatActivity) context;
            Bundle data = new Bundle();
            data.putStringArray(HybridTabsDialog.KEY_TAB_TITLES, tabs.split(","));
            data.putStringArray(HybridTabsDialog.KEY_TAB_URLS, urls.split(","));
            FragmentManager manager = compatActivity.getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            Fragment fragment = manager.findFragmentByTag("hybrid.tabs.dialog");
            if (fragment != null)
                transaction.remove(fragment);
            HybridTabsDialog dialog = HybridTabsDialog.newInstance(data);
            dialog.setCancelable(true);
            dialog.show(transaction, "hybrid.tabs.dialog");
        } else
            throw new Exception("基类没有继承AppCompatActivity！");
    }

    /**
     * 打开一个新页面
     *
     * @param webView     浏览器对象
     * @param pageClsName 要被打开的新页面类全名
     * @param title       新页面的标题
     * @param url         在页面中要打开的链接地址
     */
    public static void openNewpage(WebView webView, String pageClsName, String title, String url)
    {
        openNewpage(webView, pageClsName, title, url, null, false);
    }

    /**
     * 打开一个新页面，同时允许设定是否要关闭当前页面
     *
     * @param webView            浏览器对象
     * @param pageClsName        要被打开的新页面类全名
     * @param title              新页面的标题
     * @param url                在页面中要打开的链接地址
     * @param isCloseCurrentPage 是否要关闭当前页面
     */
    public static void openNewpage(WebView webView, String pageClsName, String title, String url, boolean isCloseCurrentPage)
    {
        openNewpage(webView, pageClsName, title, url, null, isCloseCurrentPage);
    }

    /**
     * 打开一个新页面
     *
     * @param webView 浏览器对象
     * @param title   新页面的标题
     * @param url     在页面中要打开的链接地址
     */
    public static void openNotclsNewpage(WebView webView, String title, String url)
    {
        openNotclsNewpage(webView, title, url, null);
    }

    /**
     * 打开一个新页面
     *
     * @param webView 浏览器对象
     * @param title   新页面的标题
     * @param url     在页面中要打开的链接地址
     * @param params  自定义的参数
     */
    public static void openNotclsNewpage(WebView webView, String title, String url, String params)
    {
        Context context = webView.getContext();
        Intent intent = new Intent(context, HybridNewActivity.class);
        intent.putExtra(ExtraKey.KEY_TOOLBAR_TITLE, title);
        intent.putExtra(ExtraKey.KEY_TOOLBAR_URL, url);
        if (params != null)
            intent.putExtra(ExtraKey.KEY_TOOLBAR_PARAMS, params);
        context.startActivity(intent);
    }

    /**
     * 打开一个专用于视频播放的页面
     *
     * @param webView 浏览器
     * @param title   页面标题
     * @param url     要打开的链接
     */
    public static void openVideoPlayerPage(WebView webView, String title, String url)
    {
        Context context = webView.getContext();
        Intent intent = new Intent(context, H5VideoPlayerActivity.class);
        intent.putExtra(ExtraKey.KEY_TOOLBAR_TITLE, title);
        intent.putExtra(ExtraKey.KEY_TOOLBAR_URL, url);
        context.startActivity(intent);
    }

    /**
     * 打开一个专用于表单的页面
     *
     * @param webView 浏览器
     * @param title   页面标题
     * @param url     页面URL
     */
    public static void openFormPage(WebView webView, String title, String url)
    {
        Context context = webView.getContext();
        Intent intent = new Intent(context, HybridFormActivity.class);
        intent.putExtra(ExtraKey.KEY_TOOLBAR_TITLE, title);
        intent.putExtra(ExtraKey.KEY_TOOLBAR_URL, url);
        context.startActivity(intent);
    }

    /**
     * 页面值传递的常量字段定义
     */
    public static class ExtraKey
    {
        /**
         * 定义页面标题
         */
        public static final String KEY_TOOLBAR_TITLE = "com.zzstxx.hybrid.KEY_TOOLBAR_TITLE";
        /**
         * 定义页面URL
         */
        public static final String KEY_TOOLBAR_URL = "com.zzstxx.hybrid.KEY_TOOLBAR_URL";
        /**
         * 自定义数据
         */
        public static final String KEY_TOOLBAR_PARAMS = "com.zzstxx.hybrid.KEY_TOOLBAR_PARAMS";
    }

    /**
     * 打开一个带有自定义参数的新页面
     *
     * @param webView            浏览器对象
     * @param pageClsName        要被打开的新页面类全名
     * @param title              新页面标题
     * @param url                在页面中要打开的链接地址
     * @param params             自定义的参数
     * @param isCloseCurrentPage 是否关闭当前页面
     */
    public static void openNewpage(WebView webView, String pageClsName,
                                   String title, String url, String params, boolean isCloseCurrentPage)
    {
        try
        {
            Context context = webView.getContext();
            Class targetActivity = Class.forName(pageClsName);
            Intent intent = new Intent(context, targetActivity);
            intent.putExtra(ExtraKey.KEY_TOOLBAR_TITLE, title);
            intent.putExtra(ExtraKey.KEY_TOOLBAR_URL, url);
            if (params != null)
                intent.putExtra(ExtraKey.KEY_TOOLBAR_PARAMS, params);
            context.startActivity(intent);
            if (context instanceof Activity && isCloseCurrentPage)
                ((Activity) context).finish();
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 直接刷新当前页面
     *
     * @param webView 浏览器
     * @param newUrl  要重新加载的新页面地址
     */
    public static void refreshCurrentPage(WebView webView, String newUrl)
    {
        webView.loadUrl(newUrl);
    }

    /**
     * 回退至上一个页面并关闭当前页
     *
     * @param webView 浏览器
     */
    public static void goBackBeforePage(WebView webView)
    {
        Context context = webView.getContext();
        if (context instanceof Activity)
            ((Activity) context).finish();
    }

    /**
     * 返回至程序首页
     *
     * @param webView 浏览器
     */
    public static void goHomePage(WebView webView)
    {
        ActivityManager.finishActivitys();
    }

    /**
     * 打开指定的App
     *
     * @param webView  浏览器
     * @param packname 应用包名
     */
    public static void openDesignatedApp(WebView webView, String packname)
    {
        openDesignatedApp(webView, packname, null);
    }

    /**
     * 打开指定的App，并同时向其传递自定义数据
     *
     * @param webView    浏览器
     * @param packname   应用包名
     * @param customData 自定义数据
     */
    public static void openDesignatedApp(WebView webView, String packname, String customData)
    {
        Context context = webView.getContext();
        if (isAppInstalled(context, packname))
        {
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(packname);
            intent.putExtra(ExtraKey.KEY_TOOLBAR_PARAMS, customData);
            context.startActivity(intent);
        } else
            goToMarket(webView);
    }

    /**
     * 检测某个应用是否安装
     *
     * @param context     应用上下文
     * @param packageName 应用包名
     * @return 如果存在，则返回{@code true}，否则返回{@code false}
     */
    public static boolean isAppInstalled(Context context, String packageName)
    {
        try
        {
            context.getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e)
        {
            return false;
        }
    }

    /**
     * 去市场下载页面
     */
    public static void goToMarket(WebView webView)
    {
        openNotclsNewpage(webView, "郑州教育移动客户端", "http://sjkhd.zzedu.net.cn");
    }

    /**
     * 获取当前登录用户的基本信息
     *
     * @param webView 浏览器
     * @return 返回保存在本地的用户登录信息，以JSON字符串的形式返回
     */
    public static String getCurrentUserInfo(WebView webView)
    {
        Context context = webView.getContext();
        return getCurrentUserInfo(context);
    }

    /**
     * 获取当前登录用户的基本信息
     *
     * @param context 应用上下文
     * @return 返回保存在本地的用户登录信息，以JSON字符串的形式返回
     */
    public static String getCurrentUserInfo(Context context)
    {
        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return mPreferences.getString("com.zzstxx.library.hybrid.USERINFO", "null");
    }

    /**
     * 获取当前登录用户的基本信息
     *
     * @param webView    浏览器对象
     * @param jsCallback 结果回传
     * @see #getCurrentUserInfo(WebView)
     */
    @Deprecated
    public static void getCurrentUserInfo(WebView webView, JsCallback jsCallback)
    {
        String userinfo = getCurrentUserInfo(webView);
        try
        {
            jsCallback.onClickCallback(userinfo);
        } catch (JsCallback.JsCallbackException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 设置已登录用户的基本信息
     *
     * @param webView  浏览器
     * @param userinfo 登录成功后的用户信息
     */
    public static void setUserInfo(WebView webView, String userinfo)
    {
        Context context = webView.getContext();
        setUserInfo(context, userinfo);
    }

    /**
     * 设置当前登录的个人信息
     *
     * @param userinfo 当前已登录用户的个人信息
     */
    public static void setUserInfo(Context context, String userinfo)
    {
        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mPreferences.edit().putString("com.zzstxx.library.hybrid.USERINFO", userinfo).apply();
    }

    /**
     * 设置当前用户已选择的服务器地址
     *
     * @param baseRequestUrl 基础服务器地址
     */
    public static void setBaseRequestUrl(Context context, String baseRequestUrl)
    {
        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mPreferences.edit().putString("com.zzstxx.library.hybrid.BASEREQUESTURL", baseRequestUrl).apply();
    }

    /**
     * 获取用户已选择的服务器地址
     *
     * @param webView 浏览器对象
     * @return 返回已选择后的服务器地址，若用户未选择则返回{@code null}
     */
    public static String getBaseRequestUrl(WebView webView)
    {
        Context context = webView.getContext();
        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String message = context.getResources().getString(R.string.notfound_baserequesturl);
        return mPreferences.getString("com.zzstxx.library.hybrid.BASEREQUESTURL", message);
    }

    /**
     * 清除浏览器Cookie
     *
     * @param webView 浏览器
     */
    public static void clearCookies(WebView webView)
    {
        clearCookies(webView.getContext());
    }

    /**
     * 清除浏览器Cookie
     *
     * @param context 上下文引用
     */
    public static void clearCookies(Context context)
    {
        CookieSyncManager.createInstance(context);
        CookieManager manager = CookieManager.getInstance();
        manager.removeAllCookie();
        CookieSyncManager.getInstance().sync();
    }

    /**
     * 获取当前设备的IMEI号码
     *
     * @param webView 浏览器
     * @return 返回获取到的设备IMEI号码
     */
    public static String getDeviceIMEI(WebView webView)
    {
        return ((TelephonyManager) webView.getContext().getSystemService(Context.TELEPHONY_SERVICE)).getSubscriberId();
    }

    public static final int REQUEST_CAMERA_CODE = 9;
    public static final int REQUEST_ALBUM_CODE = 10;
    public static final int REQUEST_FILEPICKER_CODE = 11;

    /**
     * 打开系统相机拍摄照片
     *
     * @param webView    浏览器
     * @param jsCallback 结果回传
     */
    public static void openCamera(WebView webView, final JsCallback jsCallback)
    {
        GalleryFinal.openCamera(REQUEST_CAMERA_CODE, new GalleryFinal.OnHanlderResultCallback()
        {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList)
            {
                if (reqeustCode == REQUEST_CAMERA_CODE)
                {
                    try
                    {
                        JSONArray photoArray = new JSONArray();
                        for (PhotoInfo photo : resultList)
                        {
                            File file = new File(photo.getPhotoPath());
                            JSONObject json = new JSONObject();
                            json.put("name", file.getName());
                            json.put("size", file.length());
                            json.put("path", file.getAbsolutePath());
                            photoArray.put(json);
                        }
                        String arrayJson = photoArray.toString().replaceAll(Matcher.quoteReplacement("\\"), "");
                        jsCallback.onClickCallback(arrayJson.replaceAll("\"", "'"));
                    } catch (JsCallback.JsCallbackException | JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg)
            {
                try
                {
                    jsCallback.onClickCallback(errorMsg);
                } catch (JsCallback.JsCallbackException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 打开系统相册选择多张照片
     *
     * @param webView    浏览器
     * @param jsCallback 结果回传
     */
    public static void openAlbum(WebView webView, final JsCallback jsCallback)
    {
        GalleryFinal.openGalleryMuti(REQUEST_ALBUM_CODE, GlideImageLoader.MAX_SELECT_PHOTO_SIZE, new GalleryFinal.OnHanlderResultCallback()
        {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList)
            {
                if (reqeustCode == REQUEST_ALBUM_CODE)
                {
                    try
                    {
                        JSONArray photoArray = new JSONArray();
                        for (PhotoInfo photo : resultList)
                        {
                            File file = new File(photo.getPhotoPath());
                            JSONObject json = new JSONObject();
                            json.put("name", file.getName());
                            json.put("size", file.length());
                            json.put("path", file.getAbsolutePath());
                            photoArray.put(json);
                        }
                        String arrayJson = photoArray.toString().replaceAll(Matcher.quoteReplacement("\\"), "");
                        jsCallback.onClickCallback(arrayJson.replaceAll("\"", "'"));
                    } catch (JsCallback.JsCallbackException | JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg)
            {
                try
                {
                    jsCallback.onClickCallback(errorMsg);
                } catch (JsCallback.JsCallbackException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 调取文件管理器。<i>注意：调用该方法必须在先调用{@link #openFormPage(WebView, String, String)}方法之后<i/>
     *
     * @param webView    浏览器
     * @param jsCallback 结果回传
     */
    public static void openFilePicker(WebView webView, JsCallback jsCallback)
    {
        Context context = webView.getContext();
        if (context != null && context instanceof Activity)
        {
            HybridFormActivity.setJsCallback(jsCallback);
            new MaterialFilePicker()
                    .withActivity((Activity) context)
                    .withRequestCode(REQUEST_FILEPICKER_CODE)
                    .withHiddenFiles(false)
                    .start();
        }
    }

    /**
     * 上传文件
     *
     * @param webView        浏览器
     * @param jsCallback     结果回传
     * @param isShowProgress 是否要显示一个原生的进度提示对话框
     * @param message        进度提示对话框中要显示的消息文字
     * @param url            上传文件的路径（不含基础域名）
     * @param params         上传文件所需的参数集合（JSON对象字符串，不含file）
     * @param files          要上传的文件集合
     */
    public static void uploadFiles(WebView webView, JsCallback jsCallback, boolean isShowProgress, String message, String url, String params, String files)
    {
        final Context context = webView.getContext();
        final JsCallback mJsCallback = jsCallback;
        final ProgressDialog mProgressDialog = new ProgressDialog(context);
        if (isShowProgress)
        {
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setMessage(message);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }
        try
        {
            PostFormBuilder mPostBuilder = OkHttpUtils.post();
            JSONObject requestParams = new JSONObject(params);
            for (Iterator<String> iter = requestParams.keys(); iter.hasNext(); )
            {
                String key = iter.next();
                String value = requestParams.getString(key);
                mPostBuilder.addParams(key, value);
            }
            for (String path : files.split(","))
            {
                File file = new File(path);
                mPostBuilder.addFile("file", file.getName(), file);
            }
            if (url.startsWith("http://") || url.startsWith("https://"))
                mPostBuilder.url(url);
            else
                mPostBuilder.url(getBaseRequestUrl(webView).concat(url));
            long timeout = 2 * 60 * 1000;
            RequestCall requestCall = mPostBuilder.build();
            requestCall.connTimeOut(timeout);
            requestCall.execute(new StringCallback()
            {
                @Override
                public void onError(Call call, Exception e, int id)
                {
                    try
                    {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        mJsCallback.onClickCallback(e.getMessage());
                    } catch (JsCallback.JsCallbackException e1)
                    {
                        e1.printStackTrace();
                    }
                }

                @Override
                public void onResponse(String response, int id)
                {
                    try
                    {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        mJsCallback.onClickCallback(response.replaceAll("\"", "'"));
                    } catch (JsCallback.JsCallbackException e1)
                    {
                        e1.printStackTrace();
                    }
                }
            });
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}