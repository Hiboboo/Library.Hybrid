package com.zzstxx.library.hybrid.actions;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.zzstxx.library.hybrid.R;
import com.zzstxx.library.hybrid.util.ActivityManager;
import com.zzstxx.library.hybrid.util.HostJsScope;
import com.zzstxx.library.hybrid.views.ProgressWebView;

/**
 * 作者：孙博
 * <br>
 * 创建时间：Hiboboo on 2016/12/2.
 * <br>
 * 描述：带有H5播放器的页面
 * <br>
 * 版本：
 */
public class H5VideoPlayerActivity extends AppCompatActivity
{
    private ProgressWebView mWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ActivityManager.addActivity(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        this.setContentView(R.layout.activity_h5_video_player_layout);
        Toolbar mToolbar = (Toolbar) this.findViewById(R.id.toolbar);
        this.setSupportActionBar(mToolbar);
        ActionBar mActionbar = getSupportActionBar();
        if (mActionbar != null)
            mActionbar.setDisplayHomeAsUpEnabled(true);
        mWebView = (ProgressWebView) this.findViewById(R.id.video_webview);
        String title = getIntent().getStringExtra(HostJsScope.ExtraKey.KEY_TOOLBAR_TITLE);
        String url = getIntent().getStringExtra(HostJsScope.ExtraKey.KEY_TOOLBAR_URL);
        if (url != null)
        {
            mActionbar.setTitle(title);
            if (!url.startsWith("http://") && !url.startsWith("https://"))
                url = HostJsScope.getBaseRequestUrl(mWebView).concat(url);
            mWebView.loadUrl(url);
            mWebView.setWebViewClientUrl(url);
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mWebView.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mWebView.onPause();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        ActivityManager.removeActivity(this);
        mWebView.clearCacheCookie();
        mWebView.stopLoading();
        mWebView.destroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.hybrid_actionbar_menus, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int i = item.getItemId();
        if (i == android.R.id.home)
            this.finish();
        if (i == R.id.actionbar_item_refresh)
            mWebView.reload();
        return super.onOptionsItemSelected(item);
    }
}
