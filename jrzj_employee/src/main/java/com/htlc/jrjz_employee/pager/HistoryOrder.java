package com.htlc.jrjz_employee.pager;

import android.app.Activity;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.htlc.jrjz_employee.AppConfig;
import com.htlc.jrjz_employee.AppContext;
import com.htlc.jrjz_employee.R;
import com.htlc.jrjz_employee.adapter.HistoryOrderAdapter;
import com.htlc.jrjz_employee.base.BasePager;
import com.htlc.jrjz_employee.common.http.CallBack;
import com.htlc.jrjz_employee.common.http.CommonApiClient;
import com.htlc.jrjz_employee.common.utils.LogUtils;
import com.htlc.jrjz_employee.common.utils.TDevice;
import com.htlc.jrjz_employee.common.utils.TimeUtils;
import com.htlc.jrjz_employee.common.utils.ToastUtils;
import com.htlc.jrjz_employee.dto.OrderBookedDTO;
import com.htlc.jrjz_employee.entity.OrderFinishedEntity;
import com.htlc.jrjz_employee.entity.OrderFinishedResult;
import com.htlc.jrjz_employee.refresh.PullToRefreshRecycleView;

import java.util.List;


/**
 * Created by Administrator on 2016/8/16.
 * 历史订单页
 */
public class HistoryOrder extends BasePager implements View.OnClickListener {

    private PullToRefreshRecycleView recyclerview_history_order;
    private HistoryOrderAdapter adapter;

    private Handler mHandler = new Handler();


    private ImageView img_error_layout; //图片
    private TextView tv_error_layout; //加载失败
    private ProgressBar animProgress;
    private TextView tv_tv_error_layout; //加载中


    protected int mCurrentPage = 0;//当前页
    protected final static int PAGE_SIZE = 10;//每页条数
    private int pageCount;//总页数

    private List<OrderFinishedEntity> orderFinishedData;


    public HistoryOrder(Activity context) {
        super(context);
    }

    @Override
    public void initData() {

        rootView = LayoutInflater.from(context).inflate(R.layout.history_order, null, false);

//        rootView = View.inflate(context, R.layout.history_order, null);

        fl_basepager.removeAllViews();
        fl_basepager.addView(rootView);
        findView();

        //显示加载中视图
        showLoading();

        getData();

    }


    /**
     * 链接服务器获取数据
     */
    private void getOrdersFromService() {

        OrderBookedDTO dto = new OrderBookedDTO();
        long time = TimeUtils.getSignTime();
        String random = TimeUtils.genNonceStr();
        dto.setAccessToken(AppContext.get("accessToken", ""));
        dto.setRandom(random);
        dto.setUid(AppContext.get("uid", ""));
        dto.setTimestamp(time);
        dto.setSign(AppContext.get("uid", "") + time + random);
        dto.setPage(String.valueOf(mCurrentPage));
        dto.setSize(String.valueOf(PAGE_SIZE));

        CommonApiClient.orderFinished(context, dto, new CallBack<OrderFinishedResult>() {
            @Override
            public void onSuccess(OrderFinishedResult result) {
                if (AppConfig.SUCCESS.equals(result.getCode())) {
                    LogUtils.e("历史订单成功,size=====" + result.getData().getData().size()+"当前页===="+mCurrentPage);
                    //显示加载成功的视图
                    showSuccess();

                    if (result.getData() == null || result.getData().getData().size() == 0) {
                        //没有记录,显示视图
                        showEmpty();

                    } else {

                        pageCount = Integer.parseInt(result.getData().getPageCount());
                        orderFinishedData = result.getData().getData();
                        setAdapter();
                    }
                } else {
                    ToastUtils.showShort(context, result.getMsg());
                    //加载失败时显示失败的视图
                    showFail();
                }
            }

        });

    }


    private void setAdapter() {
        //设置adapter
        adapter = new HistoryOrderAdapter(context, orderFinishedData);
        recyclerview_history_order.setAdapter(adapter);

        recyclerview_history_order.setOnRefreshListener(new PullToRefreshRecycleView.OnRefreshListener() {
            @Override
            public void onPullDownRefresh() {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //下拉刷新的操作
                        mCurrentPage = 0;
                        getData();
//                        adapter.notifyDataSetChanged();
                        recyclerview_history_order.completeRefresh();
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore() {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //加载更多的操作
                        mCurrentPage++;
                        if (mCurrentPage < pageCount) {
                            getMore();
//                            adapter.notifyDataSetChanged();
                            recyclerview_history_order.completeLoadMore();

                        } else {
                            Toast.makeText(context, "没有更多数据了", Toast.LENGTH_SHORT).show();
                            recyclerview_history_order.completeLoadMore();
                        }
                    }
                }, 1000);
//
            }
        });

//        recyclerview_history_order.setLayoutManager(new LinearLayoutManager(context));


    }

    /**
     * 加载更多获取数据
     */
    private void getMore() {
        OrderBookedDTO dto = new OrderBookedDTO();
        long time = TimeUtils.getSignTime();
        String random = TimeUtils.genNonceStr();
        dto.setAccessToken(AppContext.get("accessToken", ""));
        dto.setRandom(random);
        dto.setUid(AppContext.get("uid", ""));
        dto.setTimestamp(time);
        dto.setSign(AppContext.get("uid", "") + time + random);
        dto.setPage(String.valueOf(mCurrentPage));
        dto.setSize(String.valueOf(PAGE_SIZE));

        CommonApiClient.orderFinished(context, dto, new CallBack<OrderFinishedResult>() {
            @Override
            public void onSuccess(OrderFinishedResult result) {
                LogUtils.d("历史订单成功,size========" + result.getData().getData().size()+"当前页===="+mCurrentPage);
                if (AppConfig.SUCCESS.equals(result.getCode())) {
                    LogUtils.e("历史订单成功");
                    //显示加载成功的视图
                    showSuccess();

                    if (result.getData() != null && result.getData().getData().size() != 0) {
                        pageCount = Integer.parseInt(result.getData().getPageCount());
                        List<OrderFinishedEntity> add = result.getData().getData();
                        adapter.append(add);
                    }


                } else {
                    ToastUtils.showShort(context, result.getMsg());
                    //加载失败时显示失败的视图
                    showFail();
                }
            }

        });

    }

    public void getData() {

        //判断是否有网络
        if (!TDevice.hasInternet(context)) {
            //没有网络，显示的视图
            hideLoading();
            showNoNetwork();
        } else {
            //链接服务器获取数据
            getOrdersFromService();
        }
    }


    private void findView() {
        recyclerview_history_order = (PullToRefreshRecycleView) rootView.findViewById(R.id.recyclerview_history_order);
        img_error_layout = (ImageView) rootView.findViewById(R.id.img_error_layout);
        tv_error_layout = (TextView) rootView.findViewById(R.id.tv_error_layout);
        animProgress = (ProgressBar) rootView.findViewById(R.id.animProgress);
        tv_tv_error_layout = (TextView) rootView.findViewById(R.id.tv_tv_error_layout);

        img_error_layout.setOnClickListener(this);
    }

    /**
     * 显示没有网络的视图
     */
    private void showNoNetwork() {
        recyclerview_history_order.setVisibility(View.GONE);
        img_error_layout.setVisibility(View.VISIBLE);
        img_error_layout.setBackgroundResource(R.drawable.page_icon_network);
        tv_error_layout.setVisibility(View.VISIBLE);
        tv_error_layout.setText("请检查您的网络，点击刷新");
        animProgress.setVisibility(View.GONE);
        tv_tv_error_layout.setVisibility(View.GONE);

    }

    /**
     * 显示加载中。。。
     */
    private void showLoading() {

        img_error_layout.setVisibility(View.GONE);
        tv_error_layout.setVisibility(View.GONE);
        animProgress.setVisibility(View.VISIBLE);
        tv_tv_error_layout.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏加载中
     */
    private void hideLoading() {
        animProgress.setVisibility(View.GONE);
        tv_tv_error_layout.setVisibility(View.GONE);
    }

    /**
     * 显示失败的视图
     */
    private void showFail() {
        hideLoading();
        recyclerview_history_order.setVisibility(View.GONE);
        img_error_layout.setVisibility(View.VISIBLE);
        img_error_layout.setBackgroundResource(R.drawable.pagefailed_bg);
        tv_error_layout.setVisibility(View.VISIBLE);
        tv_error_layout.setText("加载失败，点击刷新");

    }


    /**
     * 显示加载成功的视图
     */
    private void showSuccess() {
        hideLoading();
        recyclerview_history_order.setVisibility(View.VISIBLE);
        img_error_layout.setVisibility(View.GONE);
        tv_error_layout.setVisibility(View.GONE);
    }

    /**
     * 显示无
     */
    private void showEmpty() {
        hideLoading();
        recyclerview_history_order.setVisibility(View.GONE);
        img_error_layout.setVisibility(View.VISIBLE);
        img_error_layout.setBackgroundResource(R.drawable.siaieless1);
        tv_error_layout.setVisibility(View.VISIBLE);
        tv_error_layout.setText("暂无记录，点击刷新");

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_error_layout:

                //刷新操作
                refreshData();
                break;
        }
    }

    /**
     * 刷新操作
     */
    private void refreshData() {
        showLoading();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        }, 1000);

    }

    /**
     * 当生成订单页销毁时刷新该页面数据
     */
    public void loadForCreateOrderFinished() {
        mCurrentPage = 0;
        getData();

    }
}
