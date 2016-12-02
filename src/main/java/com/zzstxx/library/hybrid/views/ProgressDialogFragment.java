package com.zzstxx.library.hybrid.views;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

/**
 * 作者：孙博
 * <br>
 * 创建时间：Hiboboo on 2016/11/30.
 * <br>
 * 描述：
 * <br>
 * 版本：
 */
public class ProgressDialogFragment extends DialogFragment
{
    public static ProgressDialogFragment newInstance(CharSequence message, boolean cancelable)
    {
        ProgressDialogFragment f = new ProgressDialogFragment();
        Bundle data = new Bundle();
        data.putCharSequence("progress.dialog", message);
        data.putBoolean("progress.cancelable", cancelable);
        f.setArguments(data);
        return f;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        Bundle data = getArguments();
        ProgressDialog mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage(data.getCharSequence("progress.dialog"));
        mProgressDialog.setCancelable(data.getBoolean("progress.cancelable", true));
        return mProgressDialog;
    }
}
