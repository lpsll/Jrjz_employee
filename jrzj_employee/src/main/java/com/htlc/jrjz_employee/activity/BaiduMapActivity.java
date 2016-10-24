package com.htlc.jrjz_employee.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.htlc.jrjz_employee.DrivingRouteOverlay;
import com.htlc.jrjz_employee.OverlayManager;
import com.htlc.jrjz_employee.R;
import com.htlc.jrjz_employee.TransitRouteOverlay;
import com.htlc.jrjz_employee.WalkingRouteOverlay;
import com.htlc.jrjz_employee.common.utils.LogUtils;

public class BaiduMapActivity extends AppCompatActivity {

    private MapView mMapView = null;
    private GeoCoder mSearch;
    private BaiduMap mBaiduMap;

    private LatLng location;  //获取的位置（经纬度信息）

    private String address;   //返回的地址

    private TextView tv_address;

    /**
     * 定位
     */
    private LocationClient locationClient = null;
    private static final int UPDATE_TIME = 60 * 1000;
    private static int LOCATION_COUTNS = 0;
    private double myLat; //我的纬度
    private double myLog; //我的经度
    private String myAddress;  //我现在的位置
    private RoutePlanSearch rSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baidu_map);

        Intent intent = getIntent();
        address = intent.getStringExtra("address");
        LogUtils.d("地址======" + address);

        mMapView = (MapView) findViewById(R.id.bmapView);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_address.setText(address);

        mBaiduMap = mMapView.getMap();


//        //普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
//
//        // 设置可改变地图位置

        locationClient = new LocationClient(getApplicationContext());     //声明LocationClient类


        rSearch = RoutePlanSearch.newInstance();
        rSearch.setOnGetRoutePlanResultListener(mlistener);
        mBaiduMap.setMyLocationEnabled(true);  //设置可改变地图中心位置
        mSearch = GeoCoder.newInstance();
        mSearch.geocode(new GeoCodeOption()
                .city("北京")
                .address(address));

        mSearch.setOnGetGeoCodeResultListener(listener);


        //定位，获取当前经纬度
//        getMyLocation();
//
//        //开始定位
//        locationClient.start();
//        locationClient.requestLocation();

    }


    private void initLocation() {
        LogUtils.e("initLocation", "initLocation");
        requestLocationInfo();//发请定位
    }

    /**
     * 发起定位
     */
    public void requestLocationInfo() {
        LogUtils.e("requestLocationInfo----", "requestLocationInfo");
        locationClient.registerLocationListener(new MyLocationListener());    //注册监听函数
        setLocationOption();
        if (locationClient != null && !locationClient.isStarted()) {
            locationClient.start();
        }
        if (locationClient != null && locationClient.isStarted()) {
            locationClient.requestLocation();
        }
    }

    class MyLocationListener implements BDLocationListener {
        StringBuffer sb = new StringBuffer(256);

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null) {
                locationClient.start();
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

                        locationClient.start();
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

    private void initNavigato(BDLocation location) {
        myLat = location.getLatitude();
        myLog = location.getLongitude();
        LogUtils.d("我的位置myLat==" + myLat);
        LogUtils.d("我的位置myLog==" + myLog);
        locationClient.stop();
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

        locationClient.setLocOption(option);
    }


    /**
     * 定位，获取当前经纬度
     */
    private void getMyLocation() {
        locationClient = new LocationClient(this);
        // 设置定位条件
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 是否打开GPS
        option.setCoorType("bd09ll"); // 设置返回值的坐标类型。
        option.setPriority(LocationClientOption.NetWorkFirst); // 设置定位优先级
        option.setProdName("LocationDemo"); // 设置产品线名称。强烈建议您使用自定义的产品线名称，方便我们以后为您提供更高效准确的定位服务。
        option.setScanSpan(UPDATE_TIME);// 设置定时定位的时间间隔。单位毫秒
        locationClient.setLocOption(option);

        // 注册位置监听器
        locationClient.registerLocationListener(new BDLocationListener() {

            @Override
            public void onReceiveLocation(BDLocation location) {
                // TODO Auto-generated method stub
                if (location == null) {
                    return;
                }
                StringBuffer sb = new StringBuffer(256);
                sb.append("Time : ");
                sb.append(location.getTime());
                sb.append("\nError code : ");
                sb.append(location.getLocType());
                sb.append("\nLatitude : ");
                sb.append(location.getLatitude());
                sb.append("\nLontitude : ");
                sb.append(location.getLongitude());
                sb.append("\nRadius : ");
                myLat = location.getLatitude();
                myLog = location.getLongitude();

                LogUtils.d("获取位置信息myLat=====" + myLat);
                LogUtils.d("获取位置信息myLog=====" + myLog);
                myAddress = location.getAddrStr();

                sb.append(location.getRadius());
                if (location.getLocType() == BDLocation.TypeGpsLocation) {
                    sb.append("\nSpeed : ");
                    sb.append(location.getSpeed());
                    sb.append("\nSatellite : ");
                    sb.append(location.getSatelliteNumber());
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                    sb.append("\nAddress : ");
                    sb.append(location.getAddrStr());

                }
                LOCATION_COUTNS++;
                sb.append("\n检查位置更新次数：");
                sb.append(String.valueOf(LOCATION_COUTNS));

            }


        });


    }


    /**
     * 规划路线(驾车)
     */
    public void drive(View view) {
        route = null;
        mBaiduMap.clear();
        PlanNode stNode = PlanNode.withLocation(new LatLng(myLat, myLog));
        PlanNode enNode = PlanNode.withLocation(location);
//        PlanNode stNode = PlanNode.withCityNameAndPlaceName("北京","龙泽");
//        PlanNode enNode = PlanNode.withCityNameAndPlaceName("北京","西单");

//        rSearch.setOnGetRoutePlanResultListener(mlistener);
        rSearch.drivingSearch((new DrivingRoutePlanOption())
                .from(stNode)
                .to(enNode));


    }

    public void walk(View view) {
        route = null;
        mBaiduMap.clear();
        PlanNode stNode = PlanNode.withLocation(new LatLng(myLat, myLog));
        PlanNode enNode = PlanNode.withLocation(location);
        rSearch.walkingSearch(new WalkingRoutePlanOption()
                .from(stNode)
                .to(enNode));
    }

    public void bus(View view) {
        route = null;
        mBaiduMap.clear();
        PlanNode stNode = PlanNode.withLocation(new LatLng(myLat, myLog));
        PlanNode enNode = PlanNode.withLocation(location);
        rSearch.transitSearch((new TransitRoutePlanOption())
                .from(stNode)
                .city("北京")
                .to(enNode));


    }

    RouteLine route = null;
    OverlayManager routeOverlay = null;
    OnGetRoutePlanResultListener mlistener = new OnGetRoutePlanResultListener() {
        public void onGetWalkingRouteResult(WalkingRouteResult result) {
            //获取步行线路规划结果

            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(BaiduMapActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
            }
            if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                //result.getSuggestAddrInfo()
                return;
            }

            if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                LogUtils.d("获取步行线路规划结果==" + result.getRouteLines().get(0));
                route = result.getRouteLines().get(0);
                WalkingRouteOverlay overlay = new WalkingRouteOverlay(mBaiduMap);
                mBaiduMap.setOnMarkerClickListener(overlay);
                routeOverlay = overlay;
//                overlay.setData(nowResultdrive.getRouteLines().get(position));
                overlay.setData(result.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();
//                TransitRouteOverlay overlay = new MyTransitRouteOverlay(mBaidumap);
//                mBaidumap.setOnMarkerClickListener(overlay);
//                overlay.setData(result.getRouteLines().get(0));
//                overlay.addToMap();
//                overlay.zoomToSpan();
            }

        }

        public void onGetTransitRouteResult(TransitRouteResult result) {
            //获取公交换乘路径规划结果
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(BaiduMapActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
            }
            if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                //result.getSuggestAddrInfo()
                return;
            }
            if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                TransitRouteOverlay overlay = new TransitRouteOverlay(mBaiduMap);
                mBaiduMap.setOnMarkerClickListener(overlay);
                overlay.setData(result.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();
            }
        }

        public void onGetDrivingRouteResult(DrivingRouteResult result) {
            //获取驾车线路规划结果
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(BaiduMapActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
            }
            if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                //result.getSuggestAddrInfo()
                return;
            }
            if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                LogUtils.d("获取驾车线路规划结果==" + result.getRouteLines().get(0));
                route = result.getRouteLines().get(0);
                DrivingRouteOverlay overlay = new DrivingRouteOverlay(mBaiduMap);
                mBaiduMap.setOnMarkerClickListener(overlay);
                routeOverlay = overlay;
//                overlay.setData(nowResultdrive.getRouteLines().get(position));
                overlay.setData(result.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();
//                TransitRouteOverlay overlay = new MyTransitRouteOverlay(mBaidumap);
//                mBaidumap.setOnMarkerClickListener(overlay);
//                overlay.setData(result.getRouteLines().get(0));
//                overlay.addToMap();
//                overlay.zoomToSpan();
            }


        }

        @Override
        public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

        }
    };

    private void initMyLocation() {
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
        LatLng ll = new LatLng(location.latitude, location.longitude);
        //MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll,f);
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, f - 7);//设置缩放比例
        mBaiduMap.animateMapStatus(u);
    }


    OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
        public void onGetGeoCodeResult(GeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                //没有检索到结果
                LogUtils.d("没有检索到结果====" + result);
                tv_address.setText("没有检索到结果");

            } else {
                //获取地理编码结果
                location = result.getLocation();
                LogUtils.d("获取地理编码结果====" + location.toString());

                initMyLocation();

                //构建Marker图标
//                BitmapDescriptor bitmap = BitmapDescriptorFactory
//                        .fromResource(R.drawable.dingwei);
//                //构建MarkerOption，用于在地图上添加Marker
//                OverlayOptions option = new MarkerOptions()
//                        .position(location)
//                        .icon(bitmap);
//                mBaiduMap.addOverlay(option);

                initLocation();//初始化定位
                //开始定位
//                locationClient.start();
//                locationClient.requestLocation();


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
        if (mBaiduMap != null) {
            mMapView.onDestroy();
        }
        if (mSearch != null) {
            mSearch.destroy();
        }

        if (locationClient != null && locationClient.isStarted()) {
            locationClient.stop();
            locationClient = null;
        }
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
