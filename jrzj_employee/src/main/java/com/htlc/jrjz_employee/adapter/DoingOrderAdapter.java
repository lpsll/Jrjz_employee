package com.htlc.jrjz_employee.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.htlc.jrjz_employee.R;
import com.htlc.jrjz_employee.activity.BaiduMapActivity;
import com.htlc.jrjz_employee.activity.ProductOrderActivity;
import com.htlc.jrjz_employee.entity.OrderBookedEntity;
import com.htlc.jrjz_employee.util.StringUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/8/19.
 */
public class DoingOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity context;
    private List<OrderBookedEntity> list;
    List<OrderBookedEntity> booker = new ArrayList<>();

    public static final int CREATEORDER_REQUEST = 0x1101; //跳转到生成订单页请求码

    public DoingOrderAdapter(Activity context, List<OrderBookedEntity> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.item_doing_order, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final MyViewHolder myHolder = (MyViewHolder) holder;
        final OrderBookedEntity entity = list.get(position);
        booker.add(position, entity);
        myHolder.tv_product_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到生成订单页,携带的信息=====订单编号orderId
                Intent intent = new Intent(context, ProductOrderActivity.class);
                intent.putExtra("orderId", booker.get(position).getOrderId());
                context.startActivityForResult(intent, DoingOrderAdapter.CREATEORDER_REQUEST);
//                context.startActivity(intent);
            }
        });


        myHolder.tv_doing_order_todoorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = booker.get(position).getAddress();
                //跳转到地图页
                Intent intent = new Intent(context, BaiduMapActivity.class);
                intent.putExtra("address",address);
                context.startActivity(intent);


                //去服务接口
//                new AlertDialog.Builder(context).setTitle("温馨提示").setMessage("确定要去服务吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    TextView tv_doing_order_todoorder = myHolder.tv_doing_order_todoorder;
//                    TextView tv_product_order = myHolder.tv_product_order;
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        TodoDTO todoDTO = new TodoDTO();
//                        String time = TimeUtils.getSignTime();
//                        String random = TimeUtils.genNonceStr();
//                        todoDTO.setAccessToken(AppContext.get("accessToken", ""));
//                        todoDTO.setUid(AppContext.get("uid", ""));
//                        todoDTO.setTimestamp(time);
//                        todoDTO.setRandom(random);
//                        todoDTO.setOrderId(booker.get(position).getOrderId());
//                        todoDTO.setSign(AppContext.get("uid", "") + time + random);
//
//                        CommonApiClient.todoOrder( context, todoDTO, new CallBack<BaseEntity>() {
//                            @Override
//                            public void onSuccess(BaseEntity result) {
//                                LogUtils.d("去服务===========" + result.getMsg());
//                                if (AppConfig.SUCCESS.equals(result.getCode())) {
//                                    LogUtils.d("去服务请求成功");
//                                    ToastUtils.showShort(context, "请求成功");
//                                    tv_doing_order_todoorder.setVisibility(View.GONE);
//                                    tv_product_order.setVisibility(View.VISIBLE);
//
//
//                                } else {
//                                    ToastUtils.showShort(context, result.getMsg());
//                                }
//                            }
//                        });
//                    }
//                }).setNegativeButton("取消", null).show();
            }
        });

        myHolder.tv_fuwu_dec.setText(entity.getServiceName());
        myHolder.tv_bianhao_dec.setText(entity.getOrderNo());
        myHolder.tv_address_dec.setText(entity.getAddress());
//        myHolder.tv_time_dec.setText(entity.getServiceTime());
        String serviceTime = entity.getServiceTime();
        //把time截成日期和时间
        List<String> dateAndTime = StringUtils.getDateAndTime(serviceTime);
        myHolder.tv_date_dec.setText(dateAndTime.get(0));
        myHolder.tv_time_dec.setText(dateAndTime.get(1));
        myHolder.tv_doing_phone_dec.setText(entity.getPhone());

        String status = entity.getStatus();
        String empId = entity.getEmpId();

//        myHolder.tv_product_order.setVisibility(View.GONE);
//        myHolder.tv_doing_order_todoorder.setVisibility(View.GONE);


//        if (TextUtils.isEmpty(empId)) {
//            //是去服务状态,显示去服务，隐藏生成订单
//            myHolder.tv_doing_order_todoorder.setVisibility(View.VISIBLE);
//        } else {
//            //是生成订单状态，显示生成订单，隐藏去服务
//            myHolder.tv_product_order.setVisibility(View.VISIBLE);
//        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_product_order;
        private TextView tv_fuwu_dec;
        private TextView tv_bianhao_dec;
        private TextView tv_date_dec;  //日期
        private TextView tv_time_dec;   //时间
        private TextView tv_address_dec;
        private TextView tv_doing_order_todoorder;
        private TextView tv_doing_phone_dec;

        public MyViewHolder(View view) {
            super(view);
            tv_product_order = (TextView) view.findViewById(R.id.tv_product_order);
            tv_doing_order_todoorder = (TextView) view.findViewById(R.id.tv_doing_order_todoorder);
            tv_bianhao_dec = (TextView) view.findViewById(R.id.tv_bianhao_dec);
            tv_time_dec = (TextView) view.findViewById(R.id.tv_time_dec);
            tv_fuwu_dec = (TextView) view.findViewById(R.id.tv_fuwu_dec);
            tv_address_dec = (TextView) view.findViewById(R.id.tv_address_dec);
            tv_date_dec = (TextView) view.findViewById(R.id.tv_date_dec);
            tv_doing_phone_dec = (TextView) view.findViewById(R.id.tv_doing_phone_dec);
        }
    }

    public void append(List<OrderBookedEntity> itemBean) {
        if (itemBean != null) {
            list.addAll(itemBean);
            notifyDataSetChanged();
        }
    }


}
