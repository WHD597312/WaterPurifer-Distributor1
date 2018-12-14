package ph.com.waterpurifer_distributor.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ph.com.waterpurifer_distributor.R;
import ph.com.waterpurifer_distributor.pojo.SearchData;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {

    private List<SearchData> mData;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public SearchAdapter(Context context, List<SearchData> list) {
        this.context = context;
        this.mData = list;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_main, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {


        holder.tv_item_name.setText(mData.get(position).getDeviceName());
        holder.tv_item_adress.setText(mData.get(position).getDeviceUserAddress());
        if (mData.get(position).getDeviceUserId() == 0)
            holder.tv_item_flag.setText("未激活");
        else {
            if (mData.get(position).getDeviceFlag() == 0)
                holder.tv_item_flag.setText("未激活");
            else
                holder.tv_item_flag.setText("激活");
        }
        switch (mData.get(position).getDeviceLeaseType()) {
            case 1:
            case 3:
                holder.tv_item_num.setVisibility(View.VISIBLE);
                holder.tv_item_bz2.setVisibility(View.VISIBLE);
                holder.tv_item_num.setText(mData.get(position).getDeviceNum() + "");
                holder.tv_item_bz2.setText("升");
                break;
            case 2:
                holder.tv_item_num.setVisibility(View.VISIBLE);
                holder.tv_item_bz2.setVisibility(View.VISIBLE);
                holder.tv_item_num.setText(mData.get(position).getDeviceNum() / 24 + "");
                holder.tv_item_bz2.setText("天");
                break;
            case 4:
                holder.tv_item_num.setVisibility(View.GONE);
                holder.tv_item_bz2.setVisibility(View.GONE);
                break;

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v, position);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                onItemClickListener.onLongClick(v, position);
                return false;
            }
        });


    }

    public void SetOnItemClick(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onLongClick(View view, int posotion);
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        //        ImageView iv_seqp_pic;
        TextView tv_item_name, tv_item_adress, tv_item_flag, tv_item_bz2, tv_item_num;

        //        RelativeLayout rl_sequitem;
        public MyViewHolder(View itemView) {
            super(itemView);
//            iv_seqp_pic = (ImageView) itemView.findViewById(R.id.iv_seqp_pic);
            tv_item_name = (TextView) itemView.findViewById(R.id.tv_item_name);
            tv_item_adress = (TextView) itemView.findViewById(R.id.tv_item_adress);
            tv_item_flag = (TextView) itemView.findViewById(R.id.tv_item_flag);
            tv_item_bz2 = (TextView) itemView.findViewById(R.id.tv_item_bz2);
            tv_item_num = (TextView) itemView.findViewById(R.id.tv_item_num);

//            rl_sequitem=(RelativeLayout) itemView.findViewById(R.id.rl_sequitem);
        }
    }


}
