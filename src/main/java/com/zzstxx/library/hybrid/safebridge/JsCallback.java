package com.zzstxx.library.hybrid.safebridge;

import android.util.Log;
import android.webkit.WebView;

import java.lang.ref.WeakReference;
import java.util.Locale;

/**
 * 异步回调页面JS函数管理对象
 */
public class JsCallback
{
    private static final String CALLBACK_JS_FORMAT = "javascript:%s.callback(%d, %d %s);";
    private static final String ONCLICK_CALLBACK_JS_FORMAT = "javascript:%s.callback(%d, %d);";
    private int mIndex;
    private boolean mCouldGoOn;
    private WeakReference<WebView> mWebViewRef;
    private int mIsPermanent;
    private String mInjectedName;

    public JsCallback(WebView view, String injectedName, int index)
    {
        mCouldGoOn = true;
        mWebViewRef = new WeakReference<>(view);
        mInjectedName = injectedName;
        mIndex = index;
    }

    public void onClickCallback(Object... args) throws JsCallbackException
    {
        if (mWebViewRef.get() == null)
        {
            throw new JsCallbackException("the WebView related to the JsCallback has been recycled");
        }
        if (!mCouldGoOn)
        {
            throw new JsCallbackException("the JsCallback isn't permanent,cannot be called more than once");
        }
        StringBuilder sb = new StringBuilder();
        for (Object arg : args)
        {
            sb.append(",");
            boolean isStrArg = arg instanceof String;
            if (isStrArg)
            {
                sb.append("\"");
            }
            sb.append(String.valueOf(arg));
            if (isStrArg)
            {
                sb.append("\"");
            }
        }
        String execJs = String.format(Locale.getDefault(), CALLBACK_JS_FORMAT, mInjectedName, mIndex, mIsPermanent, sb.toString());
        Log.d("JsCallBack", execJs);
        mWebViewRef.get().loadUrl(execJs);
        mCouldGoOn = mIsPermanent > 0;
    }

    public void onClickCallback() throws JsCallbackException
    {
        if (mWebViewRef.get() == null)
        {
            throw new JsCallbackException("the WebView related to the JsCallback has been recycled");
        }
        if (!mCouldGoOn)
        {
            throw new JsCallbackException("the JsCallback isn't permanent,cannot be called more than once");
        }
        String execJs = String.format(Locale.getDefault(), ONCLICK_CALLBACK_JS_FORMAT, mInjectedName, mIndex, mIsPermanent);
        Log.d("JsCallBack", execJs);
        mWebViewRef.get().loadUrl(execJs);
        mCouldGoOn = mIsPermanent > 0;
    }

    public void setPermanent(boolean value)
    {
        mIsPermanent = value ? 1 : 0;
    }

    public static class JsCallbackException extends Exception
    {
        public JsCallbackException(String msg)
        {
            super(msg);
        }
    }
}
