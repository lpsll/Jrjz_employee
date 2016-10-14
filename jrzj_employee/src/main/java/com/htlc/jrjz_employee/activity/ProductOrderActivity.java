package com.htlc.jrjz_employee.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.htlc.jrjz_employee.AppConfig;
import com.htlc.jrjz_employee.AppContext;
import com.htlc.jrjz_employee.R;
import com.htlc.jrjz_employee.common.entity.BaseEntity;
import com.htlc.jrjz_employee.common.http.CallBack;
import com.htlc.jrjz_employee.common.http.CommonApiClient;
import com.htlc.jrjz_employee.common.utils.LogUtils;
import com.htlc.jrjz_employee.common.utils.TDevice;
import com.htlc.jrjz_employee.common.utils.TimeUtils;
import com.htlc.jrjz_employee.common.utils.ToastUtils;
import com.htlc.jrjz_employee.dto.CreateOrderDTO;
import com.htlc.jrjz_employee.util.EditInputFilter;
import com.htlc.jrjz_employee.util.StringUtils;


public class ProductOrderActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText et_service_price; //服务费
    private EditText et_material_price;    //主材料费
    private EditText et_other_price;    //增项费
    private CheckBox cb_service_price;
    private CheckBox cb_material;
    private CheckBox cb_other;

    private TextView tv_totle_price; //合计总价格

    private double servicePrice; //服务价格
    private double materialPrice; //材料价格
    private double otherPrice;   //增项价格

    private boolean isService = true; //是否选中服务
    private boolean isMaterial = true; //是否选中价格
    private boolean isOther = true;   //是否选中增项

    private double totalPrice = 0; //总价格

    private TextView tv_commit; //提交按钮
    private ImageView img_back; //后退

    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_order);

        initView();

        initData();
    }

    /**
     * 获取订单编号
     */
    private void initData() {
        Intent intent = getIntent();
        orderId = intent.getStringExtra("orderId");

    }

    private void initView() {

        et_service_price = (EditText) findViewById(R.id.et_service_price);
        et_material_price = (EditText) findViewById(R.id.et_material_price);
        et_other_price = (EditText) findViewById(R.id.et_other_price);
        tv_totle_price = (TextView) findViewById(R.id.tv_totle_price);
        cb_service_price = (CheckBox) findViewById(R.id.cb_service_price);
        cb_material = (CheckBox) findViewById(R.id.cb_material);
        cb_other = (CheckBox) findViewById(R.id.cb_other);
        tv_commit = (TextView) findViewById(R.id.tv_commit);
        img_back = (ImageView) findViewById(R.id.img_back);

        //设置editText的过滤
        setEditTextFilter();
        //设置editText失去焦点时自动补全数字
        setAddZero();

        addTotalNumForCheckBoxChange();
        addTotalNumForEditTextChange();
        img_back.setOnClickListener(this);
        tv_commit.setOnClickListener(this);


    }

    /**
     * 设置editText失去焦点时自动补全数字
     */
    private void setAddZero() {
        et_service_price.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                } else {
                    String price = StringUtils.addTwoZero(et_service_price.getText().toString());
                    et_service_price.setText(price);
                }
            }
        });
        et_material_price.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                } else {
                    String price = StringUtils.addTwoZero(et_material_price.getText().toString());
                    et_material_price.setText(price);
                }
            }
        });
        et_other_price.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                } else {
                    String price = StringUtils.addTwoZero(et_other_price.getText().toString());
                    et_other_price.setText(price);
                }
            }
        });
    }

    /**
     * 当用户输入改变时重新计算总价格
     */
    private void addTotalNumForEditTextChange() {

        et_service_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = et_service_price.getText().toString().trim();
//                if (str.indexOf('0') == 0) {
//                    Toast.makeText(ProductOrderActivity.this, "首位不能为0", Toast.LENGTH_SHORT).show();
//                    et_service_price.setText("");
//                }
                if (str.indexOf('.') == 0) {
//                    Toast.makeText(ProductOrderActivity.this, "首位不能为.", Toast.LENGTH_SHORT).show();
                    et_service_price.setText("");
                }

                if (isService) {
                    //重新计算
                    getTotalPrice();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        et_material_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = et_material_price.getText().toString().trim();
//                if (str.indexOf('0') == 0) {
//                    Toast.makeText(ProductOrderActivity.this, "首位不能为0", Toast.LENGTH_SHORT).show();
//                    et_material_price.setText("");
//                }
                if (str.indexOf('.') == 0) {
//                    Toast.makeText(ProductOrderActivity.this, "首位不能为.", Toast.LENGTH_SHORT).show();
                    et_material_price.setText("");
                }

                if (isMaterial) {
                    //重新计算
                    getTotalPrice();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        et_other_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String str = et_other_price.getText().toString().trim();
//                if (str.indexOf('0') == 0) {
//                    Toast.makeText(ProductOrderActivity.this, "首位不能为0", Toast.LENGTH_SHORT).show();
//                    et_other_price.setText("");
//                }
                if (str.indexOf('.') == 0) {
//                    Toast.makeText(ProductOrderActivity.this, "首位不能为.", Toast.LENGTH_SHORT).show();
                    et_other_price.setText("");
                }

                if (isOther) {
                    //重新计算
                    getTotalPrice();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }


    /**
     * 当checkbox改变时重新计算价格
     */
    private void addTotalNumForCheckBoxChange() {
        cb_service_price.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //选中时，重新计算合计
                    isService = true;
                    getTotalPrice();

                } else {
                    //未选中时，重新计算合计
                    isService = false;
                    getTotalPrice();
                }
            }
        });
        cb_material.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //选中时，重新计算合计
                    isMaterial = true;
                    getTotalPrice();

                } else {
                    //未选中时，重新计算合计
                    isMaterial = false;
                    getTotalPrice();
                }
            }
        });
        cb_other.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //选中时，重新计算合计
                    isOther = true;
                    getTotalPrice();

                } else {
                    //未选中时，重新计算合计
                    isOther = false;
                    getTotalPrice();
                }
            }
        });

    }

    /**
     * 计算总价格,并显示
     */
    private void getTotalPrice() {
        totalPrice = 0;
        if (isService) {
            if (!TextUtils.isEmpty(et_service_price.getText().toString())) {
                servicePrice = Double.parseDouble(et_service_price.getText().toString());

                totalPrice = StringUtils.add(totalPrice, servicePrice);

//                totalPrice += servicePrice;
            }else{
                servicePrice = 0;
            }
        } else {
            servicePrice = 0;
        }


        if (isMaterial) {
            if (!TextUtils.isEmpty(et_material_price.getText().toString())) {
                materialPrice = Double.parseDouble(et_material_price.getText().toString());

                totalPrice = StringUtils.add(totalPrice, materialPrice);

//                totalPrice += materialPrice;
            }else{
                materialPrice = 0;
            }
        }else {
            materialPrice = 0;
        }


        if (isOther) {
            if (!TextUtils.isEmpty(et_other_price.getText().toString())) {
                otherPrice = Double.parseDouble(et_other_price.getText().toString());

                totalPrice = StringUtils.add(totalPrice, otherPrice);

//                totalPrice += otherPrice;
            }else{
                    otherPrice = 0;
                }
        }else {
            otherPrice = 0;
        }


        //先保留2位小数，再解析为double型，当0.x  +  0.x  的时候直接加会有精度问题
//        String format = new DecimalFormat("0.00").format(totalPrice);
//        totalPrice = Double.parseDouble(format);
        LogUtils.d("总价格========" + totalPrice);
        tv_totle_price.setText(totalPrice + "元");

    }

    /**
     * 设置editText的过滤
     */
    private void setEditTextFilter() {
        InputFilter[] filters = {new EditInputFilter(this)};
        et_service_price.setFilters(filters);
        et_material_price.setFilters(filters);
        et_other_price.setFilters(filters);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_commit:
                //先检查网络是否可用
                if (!TDevice.hasInternet(ProductOrderActivity.this)) {
                    ToastUtils.showShort(ProductOrderActivity.this, "请检查您的网络！");
                    break;
                }
                //判断金额是否大于0
                if (totalPrice <= 0) {
                    ToastUtils.showShort(ProductOrderActivity.this, "总金额必须大于0！");
                    break;
                }

                new AlertDialog.Builder(ProductOrderActivity.this).setTitle("温馨提示").setMessage("确定生成订单吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        createOrder();
                    }
                }).setNegativeButton("取消", null).show();
                break;
        }
    }

    /**
     * 创建订单
     * "accessToken": "",
     * "uid": "",
     * "timestamp": "",
     * "random": "",
     * "orderId": 0,
     * "serviceFee": 0,
     * "materialFee": 0,
     * "otherFee": 0,
     * "totalFee": 0,
     * "sign": ""
     */
    private void createOrder() {
        CreateOrderDTO createOrderDTO = new CreateOrderDTO();
        long time = TimeUtils.getSignTime();
        String random = TimeUtils.genNonceStr();
        createOrderDTO.setAccessToken(AppContext.get("accessToken", ""));
        createOrderDTO.setUid(AppContext.get("uid", ""));
        createOrderDTO.setTimestamp(time);
        createOrderDTO.setRandom(random);
        createOrderDTO.setOrderId(orderId);
        createOrderDTO.setServiceFee(servicePrice + "");
        createOrderDTO.setMaterialFee(materialPrice + "");
        createOrderDTO.setOtherFee(otherPrice + "");
        createOrderDTO.setTotalFee(totalPrice + "");
        createOrderDTO.setSign(AppContext.get("uid", "") + time + random);

        CommonApiClient.createOrder(this, createOrderDTO, new CallBack<BaseEntity>() {
            @Override
            public void onSuccess(BaseEntity result) {
                LogUtils.d("创建订单===========" + result.getMsg());
                if (AppConfig.SUCCESS.equals(result.getCode())) {
                    LogUtils.d("创建订单成功");
                    //此时应通知订单页刷新
                    setResult(1001);
                    ToastUtils.showShort(ProductOrderActivity.this, "创建成功");
                    finish();
                } else {
                    ToastUtils.showShort(ProductOrderActivity.this, result.getMsg());
                }
            }
        });
    }
}