package com.htlc.jrjz_employee.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.htlc.jrjz_employee.R;
import com.htlc.jrjz_employee.common.utils.LogUtils;

public class BaiduMapActivity extends AppCompatActivity {

    private MapView mMapView = null;
    private GeoCoder mSearch;
    private BaiduMap mBaiduMap;

    private LatLng location;  //获取的位置（经纬度信息）

    private String address;   //返回的地址

    private TextView tv_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baidu_map);

        Intent intent = getIntent();
        address = intent.getStringExtra("address");
        LogUtils.d("地址======"+address);


        mMapView = (MapView) findViewById(R.id.bmapView);
        tv_address = (TextView)findViewById(R.id.tv_address);

        tv_address.setText(address);

        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);  //设置可改变地图中心位置
        mSearch = GeoCoder.newInstance();
        mSearch.geocode(new GeoCodeOption()
                .city("北京")
                .address(address));

        mSearch.setOnGetGeoCodeResultListener(listener);

    }

    private void initMyLocation()
    {
        mBaiduMap.setMyLocationEnabled(true);
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(100)
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(90.0f)
                .latitude(location.latitude)
                .longitude(location.longitude).build();

        float f = mBaiduMap.getMaxZoomLevel();//19.0 最小比例尺
        //float m = mBaiduMap.getMinZoomLevel();//3.0 最大比例尺
        mBaiduMap.setMyLocationData(locData);
        mBaiduMap.setMyLocationEnabled(true);
        LatLng ll = new LatLng(location.latitude,location.longitude);
        //MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll,f);
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, f-7);//设置缩放比例
        mBaiduMap.animateMapStatus(u);
    }



    OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
        public void onGetGeoCodeResult(GeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                //没有检索到结果
                LogUtils.d("没有检索到结果===="+ result );
                tv_address.setText("没有检索到结果");

            }else {
                //获取地理编码结果
                location = result.getLocation();
                LogUtils.d("获取地理编码结果====" + location.toString());

                initMyLocation();

                //构建Marker图标
                BitmapDescriptor bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.dingwei);
                //构建MarkerOption，用于在地图上添加Marker
                OverlayOptions option = new MarkerOptions()
                        .position(location)
                        .icon(bitmap);
                mBaiduMap.addOverlay(option);

            }
        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                //没有找到检索结果
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        mSearch.destroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }



}
