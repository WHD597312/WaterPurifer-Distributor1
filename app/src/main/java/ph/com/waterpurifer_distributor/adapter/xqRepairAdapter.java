package ph.com.waterpurifer_distributor.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import ph.com.waterpurifer_distributor.R;
import ph.com.waterpurifer_distributor.pojo.RepireList;

public class xqRepairAdapter extends RecyclerView.Adapter<xqRepairAdapter.MyViewHolder> {

    private List<RepireList> mData;
    private Context context;
    private  OnItemClickListener onItemClickListener;
    public xqRepairAdapter(Context context , List<RepireList> list ) {

        this.context = context;
        this.mData = list;
    }

    public void Refrash (List<RepireList> lists){
        this.mData=lists;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_xqrepair,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.tv_xqre_phone1.setText(mData.get(position).getRepairPhone());
        holder.tv_xqre_type1.setText(mData.get(position).getRepairDeviceType());
        int flag = mData.get(position).getRepairFlag();
        if (flag==0){
            holder.tv_xqre_jdzt1.setText("等待接单");

        }else if (flag==1){
            holder.tv_xqre_jdzt1.setText("处理中");
            holder.bt_xqre_td.setText("处理中");
        }else if (flag==2){
            holder.tv_xqre_jdzt1.setText("处理完成");
            holder.bt_xqre_td.setText("删除");
        }
        holder.tv_xqre_yytime1.setText(mData.get(position).getRepairTime());
        holder.tv_xqre_yyadress1.setText(mData.get(position).getRepairAddress());
        holder.bt_xqre_td.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v, position);
            }
        });
        holder.bt_xqre_td.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                onItemClickListener.onLongClick(v, position);
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void SetOnItemClick( OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener ;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onLongClick(View view, int posotion);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_xqre_type1,tv_xqre_jdzt1,tv_xqre_yytime1,tv_xqre_phone1,tv_xqre_yyadress1;
        Button bt_xqre_td;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_xqre_type1= (TextView)itemView.findViewById(R.id.tv_xqre_type1);
            tv_xqre_jdzt1= (TextView)itemView.findViewById(R.id.tv_xqre_jdzt1);
            tv_xqre_yytime1= (TextView)itemView.findViewById(R.id.tv_xqre_yytime1);
            tv_xqre_phone1= (TextView)itemView.findViewById(R.id.tv_xqre_phone1);
            bt_xqre_td=(Button) itemView.findViewById(R.id.bt_xqre_td);
            tv_xqre_yyadress1= (TextView) itemView.findViewById(R.id.tv_xqre_yyadress1);
        }
    }


}
