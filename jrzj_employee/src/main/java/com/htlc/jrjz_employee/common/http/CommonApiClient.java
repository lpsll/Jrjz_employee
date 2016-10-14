package com.htlc.jrjz_employee.common.http;

import android.app.Activity;

import com.htlc.jrjz_employee.common.dto.BaseDTO;
import com.htlc.jrjz_employee.common.entity.BaseEntity;
import com.htlc.jrjz_employee.dto.CreateOrderDTO;
import com.htlc.jrjz_employee.dto.ForgetPwdDTO;
import com.htlc.jrjz_employee.dto.LoginDTO;
import com.htlc.jrjz_employee.dto.OrderBookedDTO;
import com.htlc.jrjz_employee.dto.TodoDTO;
import com.htlc.jrjz_employee.entity.LoginEntity;
import com.htlc.jrjz_employee.entity.OrderBookedResult;
import com.htlc.jrjz_employee.entity.OrderFinishedResult;
import com.htlc.jrjz_employee.entity.SmsVerifyEntity;

/**
 * Created by John_Libo on 2016/8/15.
 */
public class CommonApiClient extends BaseApiClient{

    /**
     * 用户名密码登录
     * @param act
     * @param dto
     * @param callback
     */
    public static void login(Activity act, LoginDTO
            dto, CallBack<LoginEntity> callback) {
        AsyncCallBack<LoginEntity> asyncCallBack = new AsyncCallBack<>(
                act, callback, LoginEntity.class);
        post(getAbsoluteUrl("/user/empLogin"), dto,
                asyncCallBack);
    }

    /**
     * 获取进行时订单数据
     * @param act
     * @param dto
     * @param callback
     */
    public static void orderBooked(Activity act, OrderBookedDTO
            dto, CallBack<OrderBookedResult> callback) {
        AsyncCallBack<OrderBookedResult> asyncCallBack = new AsyncCallBack<>(
                act, callback, OrderBookedResult.class);
        post(getAbsoluteUrl("/empServiceOrder/booked"), dto,
                asyncCallBack);
    }

    /**
     * 获取历史订单数据
     * @param act
     * @param dto
     * @param callback
     */
    public static void orderFinished(Activity act, OrderBookedDTO
            dto, CallBack<OrderFinishedResult> callback) {
        AsyncCallBack<OrderFinishedResult> asyncCallBack = new AsyncCallBack<>(
                act, callback, OrderFinishedResult.class);
        post(getAbsoluteUrl("/empServiceOrder/finished"), dto,
                asyncCallBack);
    }

    /**
     * 创建订单
     * @param act
     * @param dto
     * @param callback
     */
    public static void createOrder(Activity act, CreateOrderDTO
            dto, CallBack<BaseEntity> callback) {
        AsyncCallBack<BaseEntity> asyncCallBack = new AsyncCallBack<>(
                act, callback, BaseEntity.class);
        post(getAbsoluteUrl("/empServiceOrder/createOrder"), dto,
                asyncCallBack);
    }

    /**
     * 去服务
     * @param act
     * @param dto
     * @param callback
     */
    public static void todoOrder(Activity act, TodoDTO
            dto, CallBack<BaseEntity> callback) {
        AsyncCallBack<BaseEntity> asyncCallBack = new AsyncCallBack<>(
                act, callback, BaseEntity.class);
        post(getAbsoluteUrl("/empServiceOrder/todoOrder"), dto,
                asyncCallBack);
    }

    /**
     * 获取短信验证码
     * @param act
     * @param dto
     * @param callback
     */
    public static void verifyCode(Activity act, BaseDTO
            dto, CallBack<SmsVerifyEntity> callback) {
        AsyncCallBack<SmsVerifyEntity> asyncCallBack = new AsyncCallBack<>(
                act, callback, SmsVerifyEntity.class);
        post(getAbsoluteUrl("/user/getSmsVerifyCode"), dto,
                asyncCallBack);
    }

    /**
     * 修改密码
     * @param act
     * @param dto
     * @param callback
     */
    public static void forgetPwd(Activity act, ForgetPwdDTO
            dto, CallBack<BaseEntity> callback) {
        AsyncCallBack<BaseEntity> asyncCallBack = new AsyncCallBack<>(
                act, callback, BaseEntity.class);
        post(getAbsoluteUrl("/user/changePassword"), dto,
                asyncCallBack);
    }




}
