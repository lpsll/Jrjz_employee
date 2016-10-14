package com.htlc.jrjz_employee.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.htlc.jrjz_employee.R;
import com.htlc.jrjz_employee.view.LoadingView;


/**
 * Created by mjc on 2015/12/26.
 */
public class CustomHeaderView extends IHeaderView {

    private TextView mTipTv;
    private LoadingView mImageView;

    private ProgressBar cycler;
    private Animation operatingAnim;

    public CustomHeaderView(Context context) {
        super(context);
    }

    public CustomHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
    }

    @Override
    public void onPullToRefresh(int pullheight) {
      mImageView.updateView((float)pullheight/getHeight());
        mTipTv.setText("下拉刷新");
        mImageView.rotateAnimation(false);
        mImageView.setVisibility(VISIBLE);
        cycler.setVisibility(GONE);
    }

    @Override
    public void onReleaseToRefresh(int pullheight) {
        mImageView.updateView((float)pullheight/getHeight());
        mTipTv.setText("手松刷新");
        mImageView.rotateAnimation(false);
        mImageView.setVisibility(VISIBLE);
        cycler.setVisibility(GONE);
    }

    @Override
    public void onRefreshing() {
        mTipTv.setText("正在加载中...");
        mImageView.updateView(1);
        mImageView.rotateAnimation(true);
        cycler.setVisibility(VISIBLE);
        mImageView.setVisibility(GONE);
    }

    @Override
    public void onNormal() {
        mTipTv.setText("刷新完成");
        mImageView.rotateAnimation(false);
        cycler.setVisibility(GONE);
        mImageView.setVisibility(VISIBLE);
    }

    @Override
    public View inflateView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_header_view, null);
        mTipTv = (TextView) view.findViewById(R.id.text);
        mImageView = (LoadingView) view.findViewById(R.id.imageView);
        cycler = (ProgressBar)view.findViewById(R.id.cycler);
        return view;
    }

}
