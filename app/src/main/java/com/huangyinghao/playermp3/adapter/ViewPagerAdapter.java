package com.huangyinghao.playermp3.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by deny on 2015/12/24.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private List<Fragment> mFragments;


    public ViewPagerAdapter(FragmentManager fm , Context pContext , List<Fragment> pFragments) {
        super(fm);
        mContext = pContext;
        mFragments = pFragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}
