package com.maruonan.garbagecollection.adapter;

import android.media.tv.TvContentRating;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maruonan.garbagecollection.R;
import com.maruonan.garbagecollection.bean.ApptBean;

import java.util.List;

/**
 * @author yf_zch
 * @version 1.0.0
 */
public class ApptAdapter extends RecyclerView.Adapter<ApptAdapter.ViewHolder> {
    private List<ApptBean> mApptList;

    public ApptAdapter(List<ApptBean> mApptList) {
        this.mApptList = mApptList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvID;
        TextView tvType;
        TextView tvWeight;
        TextView tvTime;

        public ViewHolder(View itemView) {
            super(itemView);
            tvID = itemView.findViewById(R.id.history_id);
            tvType = itemView.findViewById(R.id.history_type);
            tvWeight = itemView.findViewById(R.id.history_weight);
            tvTime = itemView.findViewById(R.id.history_time);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ApptBean apptBean = mApptList.get(position);
        holder.tvID.setText(String.valueOf(apptBean.getId()));
        holder.tvType.setText(apptBean.getType());
        holder.tvWeight.setText(apptBean.getWeight());
        holder.tvTime.setText(apptBean.getTime());
    }

    @Override
    public int getItemCount() {
        return mApptList.size();
    }


}
