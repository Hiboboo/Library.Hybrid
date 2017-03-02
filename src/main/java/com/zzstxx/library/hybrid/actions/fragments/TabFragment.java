package com.zzstxx.library.hybrid.actions.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zzstxx.library.hybrid.R;
import com.zzstxx.library.hybrid.util.HostJsScope;
import com.zzstxx.library.hybrid.views.ProgressWebView;

/**
 * 作者：孙博
 * <br>
 * 创建时间：Hiboboo on 2016/10/8.
 * <br>
 * 描述：
 * <br>
 * 版本：
 */
public class TabFragment extends Fragment
{
    public static final String KEY_TAB_URL = "com.zzstxx.hybrid.KEY_TAB_URL";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_tabs_dialog_show_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        ProgressWebView mWebView = (ProgressWebView) view.findViewById(R.id.webview);
        String url = getArguments().getString(KEY_TAB_URL);
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = HostJsScope.getBaseRequestUrl(mWebView).concat(url);
        mWebView.loadUrl(url);
        mWebView.setWebViewClientUrl(url);
    }
}
