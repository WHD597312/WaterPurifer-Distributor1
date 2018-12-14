package ph.com.waterpurifer_distributor.adapter;


import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ph.com.waterpurifer_distributor.R;

public class MemuAdapter extends RecyclerView.Adapter<MemuAdapter.MyViewHolder> {

    private List<String> mData;
    private Context context;
    private OnItemClickListener onItemClickListener;
    private int myposition=0;
    public MemuAdapter(Context context , List<String> list ) {
        this.context = context;
        this.mData = list;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_main_top,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
                 holder. tv_item_memu.setText(mData.get(position));
                 if (position==myposition){
                     holder. tv_item_memu.setTextColor(context.getResources().getColor(R.color.color_toblue));
                 }else {
                     holder. tv_item_memu.setTextColor(Color.parseColor("#333333"));
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
    public void SetOnItemClick(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener ;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onLongClick(View view, int posotion);
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void getIndex(int myposition) {
        this.myposition = myposition;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_item_memu;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_item_memu= (TextView)itemView.findViewById(R.id.tv_item_memu);

        }
    }


}
