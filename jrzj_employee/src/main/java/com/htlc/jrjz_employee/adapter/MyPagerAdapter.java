package com.htlc.jrjz_employee.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.htlc.jrjz_employee.base.BasePager;

import java.util.List;


/**
 * Created by Administrator on 2016/8/19.
 */
public class MyPagerAdapter extends PagerAdapter {
    private Context context;
    private List<String> mTitleList;
    private List<BasePager> viewPagerDatas;

    public MyPagerAdapter(Context context, List<String> mTitleList, List<BasePager> viewPagerDatas) {
        this.context = context;
        this.mTitleList = mTitleList;
        this.viewPagerDatas = viewPagerDatas;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleList.get(position);//页卡标题
    }


    @Override
    public int getCount() {
        return viewPagerDatas.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        BasePager basePager = viewPagerDatas.get(position);
        View view = basePager.rootView;

        //加载每个视图时要初始化每个pager中的数据
        //屏蔽预加载页面
        basePager.initData();

        container.addView(view);

        return view;
    }

}
