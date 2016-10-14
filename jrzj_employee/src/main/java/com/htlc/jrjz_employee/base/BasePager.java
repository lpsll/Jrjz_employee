package com.htlc.jrjz_employee.base;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;

import com.htlc.jrjz_employee.R;


/**
 * Created by Administrator on 2016/8/16.
 */
public class BasePager {

    public Activity context;


    public FrameLayout fl_basepager; //正文部分

    public View rootView; //此类要加载的页面

    public BasePager(Activity context) {

        this.context = context;
        rootView = initView();



    }

    /**
     * 初始化视图
     *
     * @return
     */
    public View initView() {
        View view = View.inflate(context, R.layout.basepager, null);
        //实例化
        fl_basepager = (FrameLayout) view.findViewById(R.id.fl_basepager);

        return view;
    }


    /**
     * 初始化数据
     */
    public void initData() {

    }

}
