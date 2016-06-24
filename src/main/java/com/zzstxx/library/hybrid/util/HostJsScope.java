package com.zzstxx.library.hybrid.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.webkit.WebView;
import android.widget.Toast;

import com.zzstxx.library.hybrid.R;
import com.zzstxx.library.hybrid.safebridge.JsCallback;

import java.io.File;

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
            intent.putExtra("com.zzstxx.hybrid.TITLE", title);
            intent.putExtra("com.zzstxx.hybrid.URL", url);
            if (params != null)
                intent.putExtra("com.zzstxx.hybrid.PARAMS", params);
            context.startActivity(intent);
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
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

    /**
     * SD卡根路径
     */
    public static final String SDCARD_PATH = Environment.getExternalStorageDirectory() + File.separator;
    /**
     * 照片存放路径
     */
    public static final String CAMERA_IMAGE_PATH = SDCARD_PATH + "zzstxx/hybrid/photos/";
    /**
     * 相机
     */
    public static final int SELECT_PIC_BY_TACK_PHOTO = 0XABC1;
    /**
     * 相册
     */
    public static final int SELECT_PIC_BY_CHOOSE_PHOTO = 0XABC2;

    /**
     * 打开设备相机
     *
     * @param webView 浏览器对象
     * @return 返回拍摄后的照片本地绝对路径
     */
    public static String openCamera(WebView webView)
    {
        Context context = webView.getContext();
        if (context instanceof Activity)
        {
            String mSDState = Environment.getExternalStorageState();
            if (mSDState.equals(Environment.MEDIA_MOUNTED))
            {
                File imageFolder = new File(CAMERA_IMAGE_PATH);
                if (!imageFolder.exists())
                    imageFolder.mkdirs();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File imagePath = new File(imageFolder, "stxx_image_" + System.currentTimeMillis() + ".jpg");
                Uri imageUri = Uri.fromFile(imagePath);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                ((Activity) context).startActivityForResult(intent, SELECT_PIC_BY_TACK_PHOTO);
                return imagePath.getAbsolutePath();
            }
        }
        return null;
    }

    /**
     * 打开相册选择照片
     *
     * @param webView 浏览器
     */
    public static void openAlbum(WebView webView)
    {
        Context context = webView.getContext();
        if (context instanceof Activity)
        {
            Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
            openAlbumIntent.setType("image/*");
            ((Activity) context).startActivityForResult(openAlbumIntent, SELECT_PIC_BY_CHOOSE_PHOTO);
        }
    }
}