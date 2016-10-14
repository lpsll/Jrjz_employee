package com.htlc.jrjz_employee.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.htlc.jrjz_employee.AppConfig;
import com.htlc.jrjz_employee.AppContext;
import com.htlc.jrjz_employee.R;
import com.htlc.jrjz_employee.common.http.CallBack;
import com.htlc.jrjz_employee.common.http.CommonApiClient;
import com.htlc.jrjz_employee.common.utils.LogUtils;
import com.htlc.jrjz_employee.common.utils.PhoneUtils;
import com.htlc.jrjz_employee.common.utils.TDevice;
import com.htlc.jrjz_employee.common.utils.TimeUtils;
import com.htlc.jrjz_employee.common.utils.ToastUtils;
import com.htlc.jrjz_employee.dto.LoginDTO;
import com.htlc.jrjz_employee.entity.LoginEntity;

import cn.jpush.android.api.JPushInterface;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_ok;
    private EditText username;
    private EditText pwd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean isLogin = AppContext.get("IS_LOGIN", false);
        if (isLogin) {
            //跳转到我的订单
            startActivity(new Intent(LoginActivity.this, MyOrderActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.login);



        initView();


    }

    private void initView() {

        tv_ok = (TextView) findViewById(R.id.tv_ok);
        username = (EditText) findViewById(R.id.username);
        pwd = (EditText) findViewById(R.id.pwd);
        tv_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_ok:

                //判空
                //判断电话，密码格式和是否为空
                if (TextUtils.isEmpty(username.getText().toString().trim())) {
                    new AlertDialog.Builder(LoginActivity.this).setTitle("温馨提示").setMessage("请输入用户名").setPositiveButton("确定", null).show();
                    break;
                }
                boolean isValid = PhoneUtils.isPhoneNumberValid(username.getText().toString());
                if (!isValid) {
                    new AlertDialog.Builder(this).setTitle("温馨提示").setMessage("请输入正确的电话号码!").setPositiveButton("确定", null).show();
                    break;
                }
                if (TextUtils.isEmpty(pwd.getText().toString().trim())) {
                    new AlertDialog.Builder(this).setTitle("温馨提示").setMessage("请输入密码").setPositiveButton("确定", null).show();
                    break;
                }

                //先检查网络是否可用
                if (TDevice.hasInternet(LoginActivity.this)) {

                    Login();

                } else {
                    ToastUtils.showShort(LoginActivity.this, "请检查您的网络！");

                }


                break;
        }
    }

    /**
     * 认证授权请求
     * <p/>
     * uid：用户ID，默认为手机号码
     * <p/>
     * password：用户密码 md5加密32位字符串
     * <p/>
     * timestamp：当前时间戳
     * <p/>
     * random：随机数
     * <p/>
     * sign：签名【生成规则 uid+password+timestamp+random 后md5加密串】
     * <p/>
     * usertype:用户类型 1-普通用户 , 默认为1
     */
    private void Login() {

        final String phone = username.getText().toString().trim();
        final String password = pwd.getText().toString().trim();

        LoginDTO loginDTO = new LoginDTO();
        long time = TimeUtils.getSignTime();
        String random = TimeUtils.genNonceStr();
        loginDTO.setUid(phone);
        loginDTO.setPassword(password);
        loginDTO.setTimestamp(time);
        loginDTO.setRandom(random);
        loginDTO.setSign(phone + loginDTO.getPassword() + time + random);
        loginDTO.setUsertype("" + AppConfig.COMMON_USER_TYPE); //默认为普通用户


        CommonApiClient.login(this, loginDTO, new CallBack<LoginEntity>() {
            @Override
            public void onSuccess(LoginEntity result) {
                LogUtils.e("result登录========登录========" + result.getMsg());
                if (AppConfig.SUCCESS.equals(result.getCode())) {
                    LogUtils.e("登录成功");
                    ToastUtils.showShort(LoginActivity.this, "登录成功");
                    LogUtils.e("用户令牌======" + result.getData().getAccessToken());

                    //保存用户信息
                    AppContext.set("uid", phone);
                    AppContext.set("accessToken", result.getData().getAccessToken());
                    AppContext.set("IS_LOGIN", true);

                    //跳转到我的订单
                    startActivity(new Intent(LoginActivity.this, MyOrderActivity.class));

                    //注册成功后设置极光推送的别名和tag
                    JPushInterface.setAlias(LoginActivity.this, phone, null);


                    finish();
                } else {
                    ToastUtils.showShort(LoginActivity.this, result.getMsg());
                }
            }

        });


    }
}
