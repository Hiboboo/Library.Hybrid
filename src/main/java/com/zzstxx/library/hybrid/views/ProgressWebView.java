package com.zzstxx.library.hybrid.views;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.zzstxx.library.hybrid.safebridge.InjectedChromeClient;
import com.zzstxx.library.hybrid.util.HostJsScope;

/**
 * 带有进度条的网页
 * <p/>
 * Created by 博博 on 2015/5/25.
 */
public class ProgressWebView extends WebView
{
    public ProgressWebView(Context context)
    {
        super(context);
        this.initViews(context, null);
    }

    public ProgressWebView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.initViews(context, attrs);
    }

    public ProgressWebView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        this.initViews(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ProgressWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.initViews(context, attrs);
    }

    private NumberProgressBar mProgressbar;

    @SuppressLint("SetJavaScriptEnabled")
    private void initViews(Context context, AttributeSet attrs)
    {
        mProgressbar = new NumberProgressBar(context);
        mProgressbar.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, dp2px(context, 8)));
        this.addView(mProgressbar);
        this.enterTextSelection();
        this.setBackgroundColor(Color.WHITE);
        WebSettings setting = this.getSettings();
        setting.setJavaScriptEnabled(true);
        setting.setBuiltInZoomControls(false);
        setting.setAppCacheEnabled(true);
        setting.setSupportZoom(true);
        setting.setDefaultTextEncodingName("UTF-8");
        // 设置加载进来的页面自适应手机屏幕
        setting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        setting.setCacheMode(WebSettings.LOAD_DEFAULT);
        this.setWebChromeClient(new EvWebChromeClient(InjectedChromeClient.INJECTEDNAME, HostJsScope.class));
    }

    int dp2px(Context context, int dp)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context
                .getResources().getDisplayMetrics());
    }

    private EvWebViewClient mWebClicnet;

    public void setWebViewClient(String url)
    {
        mWebClicnet = new EvWebViewClient(url);
        this.setWebViewClient(mWebClicnet);
    }

    public WebViewClient getWebClient()
    {
        return mWebClicnet;
    }

    private class EvWebChromeClient extends InjectedChromeClient
    {
        public EvWebChromeClient(String injectedName, Class injectedCls)
        {
            super(injectedName, injectedCls);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result)
        {
            return super.onJsAlert(view, url, message, result);
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, JsResult result)
        {
            return super.onJsConfirm(view, url, message, result);
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result)
        {
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress)
        {
            if (newProgress == 100)
                mProgressbar.setVisibility(GONE);
            else
            {
                if (mProgressbar.getVisibility() == GONE)
                    mProgressbar.setVisibility(VISIBLE);
                mProgressbar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt)
    {
        LayoutParams lp = (LayoutParams) mProgressbar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        mProgressbar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }

    private class EvWebViewClient extends WebViewClient
    {
        private final String requestUrl;

        private EvWebViewClient(String requestUrl)
        {
            this.requestUrl = requestUrl;
        }

        @Override
        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event)
        {
            view.loadUrl(requestUrl);
            return true;
        }
    }

    private void enterTextSelection()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1)
            return;
        try
        {
            WebView.class.getMethod("selectText").invoke(this);
        } catch (Exception e)
        {
            try
            {
                WebView.class.getMethod("emulateShiftHeld").invoke(this);
            } catch (Exception ee)
            {
                KeyEvent shiftPressEvent = new KeyEvent(0, 0, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_SHIFT_LEFT, 0, 0);
                shiftPressEvent.describeContents();
            }
        }
    }
}
