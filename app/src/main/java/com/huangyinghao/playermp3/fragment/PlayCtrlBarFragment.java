package com.huangyinghao.playermp3.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huangyinghao.playermp3.R;
import com.huangyinghao.playermp3.adapter.PlayCtrlBarPagerAdapter;
import com.huangyinghao.playermp3.ui.PlaybarPagerTransformer;

/**
 * Created by deny on 2016/1/3.
 */
public class PlayCtrlBarFragment extends Fragment {


    ViewPager mViewPager;
    PlayCtrlBarPagerAdapter mAdapter;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_play_ctrl_bar, container, false);
        ;
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mAdapter = new PlayCtrlBarPagerAdapter(getActivity());
        mViewPager.setAdapter(mAdapter);

        PlaybarPagerTransformer transformer = new PlaybarPagerTransformer();
//        transformer.addTransformer(new ReaderViewPagerTransformer(ReaderViewPagerTransformer.TransformType.ZOOM));
        mViewPager.setPageTransformer(true, transformer);

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public static PlayCtrlBarFragment instance() {
        Bundle b = new Bundle();
        PlayCtrlBarFragment fragment = new PlayCtrlBarFragment();
        fragment.setArguments(b);
        return fragment;
    }
}

