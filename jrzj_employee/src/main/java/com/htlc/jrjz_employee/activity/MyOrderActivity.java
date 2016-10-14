package com.htlc.jrjz_employee.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.htlc.jrjz_employee.AppContext;
import com.htlc.jrjz_employee.R;
import com.htlc.jrjz_employee.adapter.DoingOrderAdapter;
import com.htlc.jrjz_employee.adapter.MyPagerAdapter;
import com.htlc.jrjz_employee.base.BasePager;
import com.htlc.jrjz_employee.common.utils.LogUtils;
import com.htlc.jrjz_employee.pager.DoingOrder;
import com.htlc.jrjz_employee.pager.HistoryOrder;

import java.util.ArrayList;
import java.util.List;


public class MyOrderActivity extends AppCompatActivity implements View.OnClickListener {


    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private TextView tv_exit,tv_modit;
    //viewpager的集合
    private List<BasePager> viewPagerDatas;
    private LayoutInflater mInflater;
    private List<String> mTitleList = new ArrayList<>();//页卡标题集合

    private DoingOrder doingOrder;
    private HistoryOrder historyOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        initView();
    }


    /**
     * 初始化视图
     */
    private void initView() {

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.vp_view);
        mInflater = LayoutInflater.from(this);
        tv_exit = (TextView)findViewById(R.id.tv_exit);
        tv_modit = (TextView)findViewById(R.id.tv_modit);

        tv_exit.setOnClickListener(this);
        tv_modit.setOnClickListener(this);
        //添加页卡标题
        mTitleList.add("进行中订单");
        mTitleList.add("历史订单");

        mTabLayout.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(0)));//添加tab选项卡
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(1)));

        addViewPagerData();

        //设置适配器 MyPagerAdapter mAdapter =
        MyPagerAdapter mAdapter = new MyPagerAdapter(this, mTitleList, viewPagerDatas);
        mViewPager.setAdapter(mAdapter);//给ViewPager设置适配器
//        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                if(position==0) {
//                    doingOrder.initData();
//                }else {
//                    historyOrder.initData();
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
        mTabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来。
        mTabLayout.setTabsFromPagerAdapter(mAdapter);//给Tabs设置适配器

    }

    /**
     * 添加viewPager的数据
     */
    private void addViewPagerData() {
        viewPagerDatas = new ArrayList<BasePager>();
        doingOrder = new DoingOrder(this);
        historyOrder = new HistoryOrder(this);
        viewPagerDatas.add(doingOrder);
        viewPagerDatas.add(historyOrder);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case  R.id.tv_exit:
                //跳转到登陆页面,并清除用户信息
                AppContext.set("uid", "");
                AppContext.set("accessToken", "");
                AppContext.set("IS_LOGIN", false);
                startActivity(new Intent(MyOrderActivity.this,LoginActivity.class));
                finish();
                break;
            case  R.id.tv_modit:
                //跳转到修改密码页面
                Intent intent = new Intent(MyOrderActivity.this, ForgetPwdActivity.class);
                MyOrderActivity.this.startActivity(intent);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode== DoingOrderAdapter.CREATEORDER_REQUEST && resultCode == 1001) {
            doingOrder.loadForCreateOrderFinished();
            historyOrder.loadForCreateOrderFinished();

            LogUtils.i("更新页面啦~~~~~~~~~");
        }
    }

}
