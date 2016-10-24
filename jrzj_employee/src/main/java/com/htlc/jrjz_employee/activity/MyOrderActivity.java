package com.htlc.jrjz_employee.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.Address;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
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
    private TextView tv_exit, tv_modit;
    //viewpager的集合
    private List<BasePager> viewPagerDatas;
    private LayoutInflater mInflater;
    private List<String> mTitleList = new ArrayList<>();//页卡标题集合

    private DoingOrder doingOrder;
    private HistoryOrder historyOrder;
    public LocationClient mLocationClient = null;

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
        //位置权限
        startForLocate();

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.vp_view);
        mInflater = LayoutInflater.from(this);
        tv_exit = (TextView) findViewById(R.id.tv_exit);
        tv_modit = (TextView) findViewById(R.id.tv_modit);

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


        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        requestLocationInfo();//发请定位
    }

    /**
     * 获取位置权限
     */
    private void startForLocate() {
        if (Build.VERSION.SDK_INT >= 23) {
            int readSDPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
            if (readSDPermission != PackageManager.PERMISSION_GRANTED) {
                LogUtils.e("readSDPermission", "" + readSDPermission);
                ActivityCompat.requestPermissions(this, new String[]{
                                Manifest.permission.CAMERA,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.READ_PHONE_STATE},

                        123);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode, grantResults);
    }

    private void doNext(int requestCode, int[] grantResults) {
        if (requestCode == 1) {
        }
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
            case R.id.tv_exit:
                //跳转到登陆页面,并清除用户信息
                AppContext.set("uid", "");
                AppContext.set("accessToken", "");
                AppContext.set("IS_LOGIN", false);
                startActivity(new Intent(MyOrderActivity.this, LoginActivity.class));
                finish();
                break;
            case R.id.tv_modit:
                //跳转到修改密码页面
                Intent intent = new Intent(MyOrderActivity.this, ForgetPwdActivity.class);
                MyOrderActivity.this.startActivity(intent);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DoingOrderAdapter.CREATEORDER_REQUEST && resultCode == 1001) {
            doingOrder.loadForCreateOrderFinished();
            historyOrder.loadForCreateOrderFinished();

            LogUtils.i("更新页面啦~~~~~~~~~");
        }
    }

    /**
     * 发起定位
     */
    public void requestLocationInfo() {
        LogUtils.e("requestLocationInfo----", "requestLocationInfo");
        mLocationClient.registerLocationListener(new MyLocationListener());    //注册监听函数
        setLocationOption();
        if (mLocationClient != null && !mLocationClient.isStarted()) {
            mLocationClient.start();
        }
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.requestLocation();
        }
    }


    /**
     * 设置相关参数
     */
    private void setLocationOption() {
        LogUtils.e("setLocationOption----", "setLocationOption");

        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(false);        //是否打开GPS
        option.setCoorType("bd09ll");       //设置返回值的坐标类型。
        option.setAddrType("all");//返回的定位结果包含地址信息
//        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setProdName("Cuohe"); //设置产品线名称。强烈建议您使用自定义的产品线名称，方便我们以后为您提供更高效准确的定位服务。
        option.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms
        option.disableCache(true);//禁止启用缓存定位

        mLocationClient.setLocOption(option);
    }

    class MyLocationListener implements BDLocationListener {
        StringBuffer sb = new StringBuffer(256);

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null) {
                mLocationClient.start();
                LogUtils.e("location_failed----", "location_failed");
                return;
            } else {
                LogUtils.e("getLocType----", "" + location.getLocType());
                if (location.getLocType() == BDLocation.TypeGpsLocation) {
                    LogUtils.e("TypeGpsLocation----", "gps定位成功");
                    int locType = location.getLocType();
                    LogUtils.e("locType:", "" + locType);
                    LogUtils.e("getLatitude:", "" + location.getLatitude());
                    LogUtils.e("getLongitude:", "" + location.getLongitude());
                    if (TextUtils.isEmpty(String.valueOf(location.getLatitude()))) {

                        mLocationClient.start();
                    } else {
                        initNavigato(location);

                    }


                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    sb.append("\naddr : ");
                    sb.append(location.getAddrStr());
                    //运营商信息
                    sb.append("\noperationers : ");
                    sb.append(location.getOperators());
                    sb.append("\ndescribe : ");
                    sb.append("网络定位成功");
                    initNavigato(location);
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    sb.append("\ndescribe : ");
                    sb.append("离线定位成功，离线定位结果也是有效的");
                    initNavigato(location);
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    sb.append("\ndescribe : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    sb.append("\ndescribe : ");
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    sb.append("\ndescribe : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                }
                LogUtils.e("sb----------:", "" + sb.toString());

            }

        }

    }

    private double myLat; //我的纬度
    private double myLog; //我的经度
    private String address;
    private void initNavigato(BDLocation location) {
        myLat = location.getLatitude();
        myLog = location.getLongitude();
        address = location.getAddrStr();
        LogUtils.e("我的位置myLat==" + myLat);
        LogUtils.e("我的位置myLog==" + myLog);
        LogUtils.e("我的位置address==" + address);
        AppContext.set("myLat",String.valueOf(myLat));
        AppContext.set("myLog",String.valueOf(myLog));
        AppContext.set("address",address);
        mLocationClient.stop();
    }

}
