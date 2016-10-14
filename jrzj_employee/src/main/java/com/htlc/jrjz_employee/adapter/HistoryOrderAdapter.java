package com.htlc.jrjz_employee.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.htlc.jrjz_employee.R;
import com.htlc.jrjz_employee.entity.OrderFinishedEntity;
import com.htlc.jrjz_employee.util.StringUtils;

import java.util.List;


/**
 * Created by Administrator on 2016/8/19.
 */
public class HistoryOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private List<OrderFinishedEntity> list;

    public HistoryOrderAdapter(Context context, List<OrderFinishedEntity> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.item_histroy_order, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MyViewHolder myHolder = (MyViewHolder) holder;

        OrderFinishedEntity entity = list.get(position);

        String serviceTime = entity.getServiceTime();
        List<String> dateAndTime = StringUtils.getDateAndTime(serviceTime);

        myHolder.tv_fuwu_dec.setText(entity.getServiceName());
        myHolder.tv_address_dec.setText(entity.getAddress());
        myHolder.tv_bianhao_dec.setText(entity.getOrderNo());
        myHolder.tv_price_dec.setText(entity.getPaidAmount()+"元");
        myHolder.tv_time_dec.setText(dateAndTime.get(1));
        myHolder.tv_date_dec.setText(dateAndTime.get(0));
        myHolder.tv_phone_histroy_dec.setText(entity.getPhone());

        String status = entity.getStatus();
        myHolder.tv_history_cancel.setVisibility(View.GONE);
        //如果状态是2就显示“已取消”
        if("2".equals(status)) {
            myHolder.tv_history_cancel.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_fuwu_dec;
        private TextView tv_bianhao_dec;
        private TextView tv_time_dec;
        private TextView tv_address_dec;
        private TextView tv_price_dec;
        private TextView tv_date_dec;
        private TextView tv_history_cancel;
        private TextView tv_phone_histroy_dec;


        public MyViewHolder(View view) {
            super(view);

            tv_fuwu_dec = (TextView) view.findViewById(R.id.tv_fuwu_dec);
            tv_bianhao_dec = (TextView) view.findViewById(R.id.tv_bianhao_dec);
            tv_time_dec = (TextView) view.findViewById(R.id.tv_time_dec);
            tv_address_dec = (TextView) view.findViewById(R.id.tv_address_dec);
            tv_price_dec = (TextView) view.findViewById(R.id.tv_price_dec);
            tv_date_dec = (TextView) view.findViewById(R.id.tv_date_dec);
            tv_history_cancel = (TextView) view.findViewById(R.id.tv_history_cancel);
            tv_phone_histroy_dec = (TextView) view.findViewById(R.id.tv_phone_histroy_dec);

        }
    }


    public void append(List<OrderFinishedEntity> itemBean) {
        if (itemBean != null) {
            list.addAll(itemBean);
            notifyDataSetChanged();
        }
    }
}
