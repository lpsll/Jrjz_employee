package com.htlc.jrjz_employee.dto;


import com.htlc.jrjz_employee.common.dto.BaseDTO;
import com.htlc.jrjz_employee.common.utils.SecurityUtils;

/**
 * Created by Administrator on 2016/8/24.
 * 忘记密码接口
 * <p/>
 * uid:用户ID，默认为手机号码
 * <p/>
 * timestamp:当前时间戳
 * <p/>
 * random:随机数
 * <p/>
 * smsverifycode:短信验证码
 * <p/>
 * password:新密码，MD5加密
 * <p/>
 * sign:签名【生成规则 uid+password(MD5)+timestamp+random 后md5加密串】
 */
public class ForgetPwdDTO extends BaseDTO {


    private String oriPassword;
    private String newPassword;

    public String getOriPassword() {
        return oriPassword;
    }

    public void setOriPassword(String oriPassword) {
        this.oriPassword = SecurityUtils.md5(oriPassword);
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = SecurityUtils.md5(newPassword);
    }



}
