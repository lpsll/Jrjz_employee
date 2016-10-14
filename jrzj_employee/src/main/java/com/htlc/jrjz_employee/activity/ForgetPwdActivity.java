package com.htlc.jrjz_employee.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.htlc.jrjz_employee.AppConfig;
import com.htlc.jrjz_employee.AppContext;
import com.htlc.jrjz_employee.R;
import com.htlc.jrjz_employee.common.base.BaseTitleActivity;
import com.htlc.jrjz_employee.common.dto.BaseDTO;
import com.htlc.jrjz_employee.common.entity.BaseEntity;
import com.htlc.jrjz_employee.common.eventbus.ErrorEvent;
import com.htlc.jrjz_employee.common.http.CallBack;
import com.htlc.jrjz_employee.common.http.CommonApiClient;
import com.htlc.jrjz_employee.common.utils.LogUtils;
import com.htlc.jrjz_employee.common.utils.PhoneUtils;
import com.htlc.jrjz_employee.common.utils.TimeUtils;
import com.htlc.jrjz_employee.common.utils.ToastUtils;
import com.htlc.jrjz_employee.dto.ForgetPwdDTO;


/**
 * 获取验证码倒计时
 */
public class ForgetPwdActivity extends BaseTitleActivity{

    EditText etForgetpwdPhone;
    EditText etOld;
    EditText etNew;
    TextView tvOk;

    private String phone,CodeOld,CodeNew;

    public void initView() {
        setTitleText("修改密码");
        etForgetpwdPhone = (EditText) findViewById(R.id.et_forgetpwd_phone);
        etOld = (EditText) findViewById(R.id.et_old);
        etNew = (EditText) findViewById(R.id.et_new);
        tvOk = (TextView) findViewById(R.id.tv_ok);
        tvOk.setOnClickListener(this);
    }


    public void initData() {
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_ok:
                dataVerify();
                break;
        }
    }

    @Override
    protected int getContentResId() {
        return R.layout.activity_forget_pwd;
    }


    private void dataVerify() {
        phone = etForgetpwdPhone.getText().toString().trim();
        CodeOld = etOld.getText().toString().trim();
        CodeNew = etNew.getText().toString().trim();
        //手机号码格式验证
        boolean valid = PhoneUtils.isPhoneNumberValid(phone);
        if (!valid) {
            new AlertDialog.Builder(this).setTitle("温馨提示").setMessage("请输入正确的电话号码!").setPositiveButton("确定", null).show();
            return;
        }

        if (TextUtils.isEmpty(CodeOld)) {
            new AlertDialog.Builder(this).setTitle("温馨提示").setMessage("旧密码不能为空!").setPositiveButton("确定", null).show();
            return;
        }

        if (TextUtils.isEmpty(CodeNew)) {
            new AlertDialog.Builder(this).setTitle("温馨提示").setMessage("新密码不能为空!").setPositiveButton("确定", null).show();
            return;
        }

        setNewPwd();
    }

    private void setNewPwd() {
        long time = TimeUtils.getSignTime();
        String random = TimeUtils.genNonceStr();
        ForgetPwdDTO forgetPwdDTO = new ForgetPwdDTO();
        forgetPwdDTO.setUid(phone);
        forgetPwdDTO.setTimestamp(time);
        forgetPwdDTO.setRandom(random);
        forgetPwdDTO.setSign(phone+ time + random);
        forgetPwdDTO.setAccessToken(AppContext.get("accessToken",""));
        forgetPwdDTO.setNewPassword(CodeNew);
        forgetPwdDTO.setOriPassword(CodeOld);

        CommonApiClient.forgetPwd(this, forgetPwdDTO, new CallBack<BaseEntity>() {
            @Override
            public void onSuccess(BaseEntity result) {
                if (AppConfig.SUCCESS.equals(result.getCode())) {
                    LogUtils.e("修改密码成功");
                    ToastUtils.showShort(ForgetPwdActivity.this, "修改密码成功");
                    finish();
                }
            }
        });
    }


}