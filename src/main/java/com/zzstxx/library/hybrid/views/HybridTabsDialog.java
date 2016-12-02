package com.zzstxx.library.hybrid.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.zzstxx.library.hybrid.R;
import com.zzstxx.library.hybrid.actions.fragments.TabFragment;

/**
 * 作者：孙博
 * <br>
 * 创建时间：Hiboboo on 2016/10/8.
 * <br>
 * 描述：带有Tab标签的Dialog
 * <br>
 * 版本：
 */
public class HybridTabsDialog extends DialogFragment
{
    public static final String KEY_TAB_TITLES = "com.zzstxx.hybrid.KEY_TAB_TITLES";
    public static final String KEY_TAB_URLS = "com.zzstxx.hybrid.KEY_TAB_URLS";

    public static HybridTabsDialog newInstance(Bundle data)
    {
        HybridTabsDialog f = new HybridTabsDialog();
        f.setArguments(data);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setCancelable(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.fragment_tabs_dialog_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        FragmentPagerItems.Creator creator = FragmentPagerItems.with(getActivity());
        String[] tabTitles = getArguments().getStringArray(KEY_TAB_TITLES);
        String[] tabUrls = getArguments().getStringArray(KEY_TAB_URLS);
        for (int i = 0; i < tabTitles.length; i++)
        {
            Bundle data = new Bundle();
            data.putString(TabFragment.KEY_TAB_URL, tabUrls[i].trim());
            creator.add(tabTitles[i].trim(), TabFragment.class, data);
        }
        ViewPager mViewPager = (ViewPager) view.findViewById(R.id.dialog_pagers);
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(getChildFragmentManager(), creator.create());
        mViewPager.setAdapter(adapter);
        final SmartTabLayout mSmartTabLayout = (SmartTabLayout) view.findViewById(R.id.dialog_tabs);
        mSmartTabLayout.setViewPager(mViewPager);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        DisplayMetrics outMetrics = getResources().getDisplayMetrics();
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.width = (int) (outMetrics.widthPixels * 0.9);
        params.height = (int) (outMetrics.heightPixels * 0.65);
        getDialog().getWindow().setAttributes(params);
    }

    @Override
    public void onStop()
    {
        super.onStop();
        this.dismissAllowingStateLoss();
    }
}
