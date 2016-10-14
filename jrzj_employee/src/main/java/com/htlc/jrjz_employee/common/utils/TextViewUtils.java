package com.htlc.jrjz_employee.common.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

/**
 * Created by John_Libo on 2016/8/15.
 */
public class TextViewUtils {
    /**
     * TextView 图片的位置 放在左边
     */
    public static final int DRAWABLE_LEFT = 0;
    /**
     * TextView 图片位置放在右边
     */
    public static final int DRAWABLE_RIGHT = 1;
    /**
     * TextView 图片位置放在下面
     */
    public static final int DRAWABLE_BOTTOM = 2;
    /**
     * TextView 图片位置放在上面
     */
    public static final int DRAWABLE_TOP = 3;

    /**
     * 设置TextView的图标
     *
     * @param context
     *            上下文对象
     * @param view
     *            TextView
     * @param resId
     *            图片ID
     * @param width
     *            宽度ID R.dimen.xx
     * @param height
     *            高度ID R.dimen.xx
     * @param where
     *            位置 DRAWABLE_
     */
    public static void setTextViewIcon(Context context, TextView view,
                                       int resId, int width, int height, int where) {
        Resources rs = context.getResources();
        // 初始化返回按钮图片大小
        Drawable d = ContextCompat.getDrawable(context,resId);
        d.setBounds(0, 0, rs.getDimensionPixelSize(width),
                rs.getDimensionPixelSize(height));
        switch (where) {
            case DRAWABLE_BOTTOM:
                view.setCompoundDrawables(null, null, null, d);
                break;
            case DRAWABLE_LEFT:
                view.setCompoundDrawables(d, null, null, null);
                break;
            case DRAWABLE_RIGHT:
                view.setCompoundDrawables(null, null, d, null);
                break;
            case DRAWABLE_TOP:
                view.setCompoundDrawables(null, d, null, null);
                break;
            default:
                break;
        }
    }


}
