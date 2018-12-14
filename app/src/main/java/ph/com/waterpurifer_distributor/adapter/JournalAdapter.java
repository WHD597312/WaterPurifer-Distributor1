package ph.com.waterpurifer_distributor.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import ph.com.waterpurifer_distributor.R;
import ph.com.waterpurifer_distributor.pojo.JournalData;

public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.MyViewHolder> {

    private List<JournalData> mData;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public JournalAdapter(Context context, List<JournalData> list) {
        this.context = context;
        this.mData = list;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_daylog, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {


        holder.tv_type.setText(mData.get(position).getFaultType());

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        int now = (int) (cal.getTimeInMillis() / 1000);

        if (mData.get(position).getFaultTime() > now) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(mData.get(position).getFaultTime());
            SimpleDateFormat fmat = new SimpleDateFormat("HH:mm");
            String time = fmat.format(calendar.getTime());
            holder.tv_time.setText("今天 " + time);
        }else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(mData.get(position).getFaultTime());
            SimpleDateFormat fmat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String time = fmat.format(calendar.getTime());
            holder.tv_time.setText( time);
        }

//              holder.itemView.setOnClickListener(new View.OnClickListener() {
//                  @Override
//                  public void onClick(View v) {
//                      onItemClickListener.onItemClick(v, position);
//                  }
//              });
//              holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                  @Override
//                  public boolean onLongClick(View v) {
//
//                      onItemClickListener.onLongClick(v, position);
//                      return false;
//                  }
//              });

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
        TextView tv_type, tv_time;

        //        RelativeLayout rl_sequitem;
        public MyViewHolder(View itemView) {
            super(itemView);
//            iv_seqp_pic = (ImageView) itemView.findViewById(R.id.iv_seqp_pic);
            tv_type = (TextView) itemView.findViewById(R.id.tv_type);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
//            rl_sequitem=(RelativeLayout) itemView.findViewById(R.id.rl_sequitem);
        }
    }


}
