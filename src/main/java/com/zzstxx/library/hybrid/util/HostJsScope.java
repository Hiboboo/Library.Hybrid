package com.zzstxx.library.hybrid.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.webkit.WebView;
import android.widget.Toast;

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zzstxx.library.hybrid.R;
import com.zzstxx.library.hybrid.actions.HybridFormActivity;
import com.zzstxx.library.hybrid.actions.HybridNewActivity;
import com.zzstxx.library.hybrid.safebridge.JsCallback;

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
     * 打开一个新页面
     *
     * @param webView     浏览器对象
     * @param pageClsName 要被打开的新页面类全名
     * @param title       新页面的标题
     * @param url         在页面中要打开的链接地址
     */
    public static void openNewpage(WebView webView, String pageClsName, String title, String url)
    {
        openNewpage(webView, pageClsName, title, url, null);
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
     * @param webView     浏览器对象
     * @param pageClsName 要被打开的新页面类全名
     * @param title       新页面标题
     * @param url         在页面中要打开的链接地址
     * @param params      自定义的参数
     */
    public static void openNewpage(WebView webView, String pageClsName, String title, String url, String params)
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
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 获取当前登录用户的基本信息
     *
     * @param webView    浏览器对象
     * @param jsCallback 结果回传
     */
    public static void getCurrentUserInfo(WebView webView, JsCallback jsCallback) throws JsCallback.JsCallbackException
    {
        Context context = webView.getContext();
        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String message = context.getResources().getString(R.string.notfound_userinfo);
        String userinfo = mPreferences.getString("com.zzstxx.library.hybrid.USERINFO", message);
        jsCallback.onClickCallback(userinfo);
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
    public static void uploadFiles(WebView webView, final JsCallback jsCallback, boolean isShowProgress, String message, String url, String params, String files)
    {
        final Context context = webView.getContext();
        final ProgressDialog mProgressDialog = new ProgressDialog(context);
        if (isShowProgress)
        {
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setMessage(message);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }
        String requestUrl = getBaseRequestUrl(webView).concat(url);
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
            mPostBuilder.url(requestUrl);
            mPostBuilder.build().connTimeOut(60 * 1000).execute(new StringCallback()
            {
                @Override
                public void onError(Call call, Exception e, int id)
                {
                    try
                    {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        jsCallback.onClickCallback(e.getMessage());
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
                        jsCallback.onClickCallback(response);
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