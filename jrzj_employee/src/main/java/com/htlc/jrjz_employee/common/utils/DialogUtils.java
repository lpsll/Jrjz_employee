package com.htlc.jrjz_employee.common.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.htlc.jrjz_employee.R;


/**
 * Created by John_Libo on 2016/8/15.
 */
public class DialogUtils {
    /**
     * 加载中遮罩层
     *
     * @param context
     * @return
     */
    public static Dialog showLoading(Context context, String messge) {
        Dialog dialog = null;
        View mView = LayoutInflater.from(context).inflate(
                R.layout.base_loading, null);
        mView.setBackgroundResource(R.drawable.base_loading_bg);
        mView.setVisibility(View.VISIBLE);
        mView.setPadding(30, 30, 30, 30);
        TextView tv = (TextView) mView.findViewById(R.id.base_loading_msg);
        tv.setTextColor(ContextCompat.getColor(context,R.color.color_ff));
        if (!TextUtils.isEmpty(messge)) {
            tv.setText(messge);
        }
        dialog = new Dialog(context, R.style.CommonLoadingShadeDialog);
        dialog.setContentView(mView);
        dialog.show();
        return dialog;
    }



    public static Dialog showLoading(Context context) {
        return showLoading(context, "加载中...");
    }

    /**
     * 确认对话框
     *
     * @param context
     * @param message
     * @param listener
     */
    public static void confirm(final Context context, String message,
                               DialogInterface.OnClickListener listener) {
        AlertDialog.Builder bui = new AlertDialog.Builder(context);
        bui.setTitle("温馨提示");
        bui.setMessage(message);
        bui.setPositiveButton("是", listener);
        bui.setNegativeButton("否", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog mExitDialog = bui.create();
        mExitDialog.show();
    }

    /**
     * 确认对话框
     *
     * @param context
     * @param message
     */
    public static void confirm(final Context context, String message, String pStr, DialogInterface.OnClickListener pListener, String nStr,
                               DialogInterface.OnClickListener nListener) {
        AlertDialog.Builder bui = new AlertDialog.Builder(context);
        bui.setTitle("温馨提示");
        bui.setMessage(message);
        bui.setPositiveButton(pStr, pListener);
        bui.setNegativeButton(nStr, nListener);
        AlertDialog mExitDialog = bui.create();
        mExitDialog.show();
    }

    /**
     * 提示对话框
     *
     * @param context
     * @param mes
     * @param btn
     * @return
     */
    public static Dialog showPrompt(Context context, String mes,String st,String btn) {
        final Dialog dialog =  new Dialog(context, R.style.CommonLoadingShadeDialog);;
        View mView = LayoutInflater.from(context).inflate(
                R.layout.base_prompt, null);
//		mView.setBackgroundResource(R.drawable.base_loading_bg);
        mView.setVisibility(View.VISIBLE);
        mView.setPadding(30, 30, 30, 30);
        TextView tv = (TextView) mView.findViewById(R.id.prompt_tv);
        TextView text = (TextView) mView.findViewById(R.id.prompt_text);
        Button mTvYes = (Button) mView.findViewById(R.id.prompt_tv_yes);
        mTvYes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        tv.setTextColor(ContextCompat.getColor(context,R.color.color_00));
        if (!TextUtils.isEmpty(st)) {
            tv.setText(st);
        }
        if (!TextUtils.isEmpty(mes)) {
            text.setText(mes);
        }
        if (!TextUtils.isEmpty(btn)) {
            mTvYes.setText(btn);
        }
        dialog.setContentView(mView);
        dialog.show();
        return dialog;
    }



    /**
     * 创建单选对话框
     *
     * @param context
     *            上下文
     * @param title
     *            标题
     * @param items
     *            数据源
     * @param checkedItem
     *            选中条目
     * @return
     */
    public static Dialog createSingleChoiceDialog(Context context,
                                                  String title, String[] items, int checkedItem,
                                                  DialogInterface.OnClickListener listener) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(title);
        builder.setSingleChoiceItems(items, checkedItem, listener);
        builder.setPositiveButton("确定", listener);
        builder.setNegativeButton("取消", listener);
        dialog = builder.create();
        dialog.show();
        return dialog;
    }



    /**
     * 创建列表对话框
     *
     * @param context
     *            上下文
     * @param title
     *            标题
     * @param items
     *            数据源
     * @param listener
     *            确定事件
     * @return
     */
    public static Dialog createListDialog(Context context, String title,
                                          String[] items, DialogInterface.OnClickListener listener) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setItems(items, listener);
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
        return dialog;
    }

    /**
     * 从底部滑出popwindow
     * @param context
     * @param view
     * @return
     */
    public static Dialog showDialog(Context context, View view) {

        final Dialog dlg = new Dialog(context, R.style.AnimBottomPopwindow);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        WindowManager wm = ((Activity) context).getWindowManager();
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;//宽度
        int height = dm.heightPixels ;//高度

        dlg.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));


        Window w = dlg.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.x = 0;
        final int cMakeBottom = -1000;
        lp.y = cMakeBottom;
        lp.gravity = Gravity.BOTTOM;
        lp.width = width;
        lp.height=height;
        dlg.onWindowAttributesChanged(lp);
        dlg.show();
        dlg.setCanceledOnTouchOutside(true);
        return dlg;
    }

}
