package com.zzstxx.library.hybrid.actions;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.zzstxx.library.hybrid.R;
import com.zzstxx.library.hybrid.util.ActivityManager;
import com.zzstxx.library.hybrid.util.HostJsScope;
import com.zzstxx.library.hybrid.views.ProgressWebView;

/**
 * 作者：孙博
 * <br>
 * 创建时间：Hiboboo on 2016/9/18.
 * <br>
 * 描述：一个新的混合显示页面，子类可以直接继承并处理复杂的业务逻辑。另：子类无须重写{@link #setContentView(int)}方法
 * <br>
 * 版本：v1.3
 */
public class HybridNewActivity extends AppCompatActivity
{
    private ProgressWebView mWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ActivityManager.addActivity(this);
        this.setContentView(R.layout.activity_newpage_layout);
        Toolbar mToolbar = (Toolbar) this.findViewById(R.id.toolbar);
        this.setSupportActionBar(mToolbar);
        ActionBar mActionbar = getSupportActionBar();
        if (mActionbar != null)
            mActionbar.setDisplayHomeAsUpEnabled(true);
        mWebView = (ProgressWebView) this.findViewById(R.id.webview);
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
    protected void onDestroy()
    {
        super.onDestroy();
        ActivityManager.removeActivity(this);
        mWebView.clearCacheCookie();
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
