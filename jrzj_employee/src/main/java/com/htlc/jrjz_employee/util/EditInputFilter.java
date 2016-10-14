package com.htlc.jrjz_employee.util;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/8/19.
 * 此类用于监听只能输入2位小数，且最大额不能超过10000
 */
public class EditInputFilter implements InputFilter {

    private Context context;
    /**
     * 最大数字
     */
    public static final int MAX_VALUE = 100000;
    /**
     * 小数点后的数字的位数
     */
    public static final int PONTINT_LENGTH = 2;
    Pattern p;
    public EditInputFilter(Context context){
        p = Pattern.compile("[0-9]*");   //除数字外的其他的
        this.context = context;
    }

    /**
     *  source    新输入的字符串
     *  start    新输入的字符串起始下标，一般为0
     *  end    新输入的字符串终点下标，一般为source长度-1
     *  dest    输入之前文本框内容
     *  dstart    原内容起始坐标，一般为0
     *  dend    原内容终点坐标，一般为dest长度-1
     */

    @Override
    public CharSequence filter(CharSequence src, int start, int end,
                               Spanned dest, int dstart, int dend) {
        String oldtext =  dest.toString();
        System.out.println(oldtext);
        //验证删除等按键
        if ("".equals(src.toString())) {
            return null;
        }
        //验证非数字或者小数点的情况
        Matcher m = p.matcher(src);
        if(oldtext.contains(".")){
            //已经存在小数点的情况下，只能输入数字
            if(!m.matches()){
                return null;
            }
        }else{
            //未输入小数点的情况下，可以输入小数点和数字
            if(!m.matches() && !src.equals(".") ){
                return null;
            }
        }
        //验证输入金额的大小
        if(!"".equals(src.toString())&&!".".equals(src.toString())) {
            double dold = Double.parseDouble(oldtext+src.toString());
            if(dold > MAX_VALUE){
                Toast.makeText(context, "输入的最大金额不能大于"+MAX_VALUE, Toast.LENGTH_SHORT).show();
                return dest.subSequence(dstart, dend);
            }else if(dold == MAX_VALUE){
                if(src.toString().equals(".")){
                    Toast.makeText(context, "输入的最大金额不能大于"+MAX_VALUE, Toast.LENGTH_SHORT).show();
                    return dest.subSequence(dstart, dend);
                }
            }
        }
        //验证小数位精度是否正确
        if(oldtext.contains(".")){
            int index = oldtext.indexOf(".");
            int len = dend - index;
            //小数位只能2位
            if(len > PONTINT_LENGTH){
                CharSequence newText = dest.subSequence(dstart, dend);
                return newText;
            }
        }
        return dest.subSequence(dstart, dend) +src.toString();
    }
}