package com.zzstxx.library.hybrid.actions;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Toast;

import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.zzstxx.library.hybrid.R;
import com.zzstxx.library.hybrid.safebridge.InjectedChromeClient;
import com.zzstxx.library.hybrid.safebridge.JsCallback;
import com.zzstxx.library.hybrid.util.ActivityManager;
import com.zzstxx.library.hybrid.util.HostJsScope;
import com.zzstxx.library.hybrid.views.ProgressWebView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.regex.Matcher;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * 作者：孙博
 * <br>
 * 创建时间：Hiboboo on 2016/9/19.
 * <br>
 * 描述：一个专用于表单显示的页面，提供有选择并上传附件的方法
 * <br>
 * 版本：v1.3
 */
@RuntimePermissions
public class HybridFormActivity extends AppCompatActivity
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
        mActionbar.setTitle(title);
        HybridFormActivityPermissionsDispatcher.doExternalStorageNeedsPermissionWithCheck(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        HybridFormActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void doExternalStorageNeedsPermission()
    {
        String url = getIntent().getStringExtra(HostJsScope.ExtraKey.KEY_TOOLBAR_URL);
        if (url != null)
        {
            if (!url.startsWith("http://") && !url.startsWith("https://") && !url.startsWith("file://"))
                url = HostJsScope.getBaseRequestUrl(mWebView).concat(url);
            mWebView.loadUrl(url);
            mWebView.setWebViewClient(url);
        }
    }

    @OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void onExternalStorageShowRationale(PermissionRequest request)
    {
        request.proceed();
    }

    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void OnExternalStoragePermissionDenied()
    {
        Toast.makeText(this, R.string.alert_permission_apply_message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        ActivityManager.removeActivity(this);
        mWebView.clearCacheCookie();
    }

    private int index = 0;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == HostJsScope.REQUEST_FILEPICKER_CODE)
        {
            if (data != null)
            {
                String path = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
                try
                {
                    JSONArray photoArray = new JSONArray();
                    File file = new File(path);
                    JSONObject json = new JSONObject();
                    json.put("name", file.getName());
                    json.put("size", file.length());
                    json.put("path", file.getAbsolutePath());
                    photoArray.put(json);
                    String arrayJson = photoArray.toString().replaceAll(Matcher.quoteReplacement("\\"), "");
                    new JsCallback(mWebView, InjectedChromeClient.INJECTEDNAME, index++).onClickCallback(arrayJson.replaceAll("\"", "'"));
                } catch (JsCallback.JsCallbackException | JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }
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
