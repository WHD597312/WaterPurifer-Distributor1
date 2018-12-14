package ph.com.waterpurifer_distributor.activity;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import ph.com.waterpurifer_distributor.R;
import ph.com.waterpurifer_distributor.base.BaseActivity;
import ph.com.waterpurifer_distributor.base.MyApplication;
import ph.com.waterpurifer_distributor.pojo.Equipment;
import ph.com.waterpurifer_distributor.util.mqtt.MQService;
import ph.com.waterpurifer_distributor.util.view.ScreenSizeUtils;

public class EqupmentxqActivity extends BaseActivity {
    MyApplication application;
    SharedPreferences preferences;
    boolean isopen1 = true ;
    boolean  isopen2= true ;
    boolean  isopen3 = true;
    @BindView(R.id.iv_equxq_kg)
    ImageView iv_equxq_kg;
    @BindView(R.id.tv_equxq_lsjc)
    ImageView tv_equxq_lsjc;
    @BindView(R.id.iv_equxq_qzkg)
    ImageView iv_equxq_qzkg;
    private  boolean clockisBound;
    private int OpenEqument;//设备开关  0X00不设置；0X01关机；0X02开机;0XFF复位
    private int Filter=0;//滤芯时间及漏水检测功能开关 0无效；1（设置状态发1表示清0） 00:忽略，不设置;01:功能开启，有漏水检测;10:功能关闭，无漏水检测
    private int ZeroClear ;//流量计实际值 0无效；1（设置状态发1表示清0）
    private int RawwaterTDS = 0 ;//原水TDS  0-65536
    private int WaterPurifTDS = 0 ;//净水TDS  0-255
    private int CumMakeTime=0; //   累计治水时间 0无效；1（设置状态发1表示清0）
    private int ConMakeTime=0;//连续治水时间 为0时忽略；为1时，不设置连续制水时间（无限制水）；其他值，连续制水的时间减一，例如发0X05，表示设置连续制水4小时后未满，检修报警。设置1~253小时
    private int Rentaltime = 0; //租赁时间 不支持添加
    private int Rentalll = 0; //租赁流量  不支持添加
    private int WaterStall = 0 ; //售水量档位
    private int WashTime = 0; //冲洗时间
    private int Singnal =0 ;//移动信号强度
    private int Style = 0 ;  //机器类型
    String IEMI;
    String deviceMac;
    @Override
    public void initParms(Bundle parms) {
        deviceMac=parms.getString("deviceMac");
    }

    @Override
    public int bindLayout() {
        setSteepStatusBar(true);
        return R.layout.activity_equpmentxq;
    }

    @Override
    public void initView(View view) {
        if (application == null) {
            application = (MyApplication) getApplication();
        }
        application.addActivity(this);
        preferences = getSharedPreferences("my", MODE_PRIVATE);
        Intent intent = getIntent();
         IEMI = intent.getStringExtra("IEMI");
        clockintent = new Intent(EqupmentxqActivity.this, MQService.class);
        clockisBound = bindService(clockintent, clockconnection, Context.BIND_AUTO_CREATE);
    }
    Intent clockintent;
    MQService  Equservice;
    boolean boundclock;
    ServiceConnection clockconnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MQService.LocalBinder binder = (MQService.LocalBinder) service;
            Equservice = binder.getService();
            boundclock = true;
            Log.e("QQQQQQQQQQQDDDDDDD", "onServiceConnected: ------->" );
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };
    @Override
    public void doBusiness(Context mContext) {

    }

    @Override
    public void widgetClick(View v) {

    }
    @OnClick({R.id.rl_equxq_maketime,R.id.rl_equxq_day,R.id.rl_equxq_lsjc,R.id.rl_equxq_kg,R.id.rl_equxq_qzkg,
            R.id.rl_equxq_lvx,R.id.iv_main_memu})

    public void onClick(View view){
        switch (view.getId()){
            case R.id.rl_equxq_maketime:
                customDialog();
                break;

            case R.id.iv_main_memu:
                finish();
                break;
            case R.id.rl_equxq_day:
                Intent intent=new Intent(this,JournalActivity.class);
                intent.putExtra("deviceMac",deviceMac);
                startActivity(intent);
                break;
            case R.id.rl_equxq_lsjc:
                //漏水检查
                if (isopen1){
                    tv_equxq_lsjc.setImageResource(R.mipmap.sz_kgg);
                    isopen1=false;
                    int x [] = {0,1,0,0,0,0,0,0};
                    for (int i = 0; i<8;i++){
                        Filter += x[i]<<(7-i);
                    }
                }else {
                    tv_equxq_lsjc.setImageResource(R.mipmap.sz_kgk);
                    isopen1=true;
                    int x [] = {0,0,1,0,0,0,0,0};
                    for (int i = 0; i<8;i++){
                        Filter += x[i]<<(7-i);
                    }
                }
                sendMessage();
                break;

            case R.id.rl_equxq_kg:
                //设备开关
                if (isopen2){
                    iv_equxq_kg.setImageResource(R.mipmap.sz_kgg);
                    isopen2=false;
                    OpenEqument=1;
                }else {
                    iv_equxq_kg.setImageResource(R.mipmap.sz_kgk);
                    isopen2=true;
                    OpenEqument=2;
                }
                sendMessage();

                break;
            case R.id.rl_equxq_qzkg:
                //强制开关
                if (isopen3){
                    iv_equxq_qzkg.setImageResource(R.mipmap.sz_kgg);
                    isopen3=false;

                }else {
                    iv_equxq_qzkg.setImageResource(R.mipmap.sz_kgk);
                    isopen3=true;
                }
                break;
            case R.id.rl_equxq_lvx:
                startActivity(LvTypeActivity.class);

                break;

        }
    }

    /**
     * 自定义对话框
     */
    private void customDialog() {
        final Dialog dialog = new Dialog(EqupmentxqActivity.this, R.style.MyDialog);
        View view = View.inflate( EqupmentxqActivity.this, R.layout.dialog_makew, null);
        TextView tv_make_qx = (TextView) view.findViewById(R.id.tv_make_qx);
        TextView tv_make_qd = (TextView) view.findViewById(R.id.tv_make_qd);
        dialog.setContentView(view);
        //使得点击对话框外部不消失对话框
        dialog.setCanceledOnTouchOutside(true);
        //设置对话框的大小
//        view.setMinimumHeight((int) (ScreenSizeUtils.getInstance(this).getScreenHeight() * 0.23f));
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (ScreenSizeUtils.getInstance(this).getScreenWidth() * 0.90f);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialogWindow.setAttributes(lp);
        tv_make_qx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }

        });
        tv_make_qd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void  sendMessage(){
        int sum = 0;
        try {
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            int headCode= 90;//头码
            jsonArray.put(0,headCode);
            int busnessModeel=0;//商业模式
            jsonArray.put(1,busnessModeel);
            int dataLength = 20;//数据长度
            jsonArray.put(2,dataLength);
            jsonArray.put(3,OpenEqument);
            jsonArray.put(4,Filter);
            jsonArray.put(5,ZeroClear);
            jsonArray.put(6,RawwaterTDS);
            jsonArray.put(7,RawwaterTDS);
            jsonArray.put(8,WaterPurifTDS);
            jsonArray.put(9,CumMakeTime);
            jsonArray.put(10,ConMakeTime);
            jsonArray.put(11,Rentaltime);
            jsonArray.put(12,Rentaltime);
            jsonArray.put(13,Rentalll);
            jsonArray.put(14,Rentalll);
            jsonArray.put(15,WaterStall);
            jsonArray.put(16,0);
            jsonArray.put(17,0);
            jsonArray.put(18,0);
            jsonArray.put(19,0);
            jsonArray.put(20,WashTime);
            jsonArray.put(21,Singnal);
            jsonArray.put(22,Style);
            for (int i = 0;i<jsonArray.length();i++){
               sum += jsonArray.getInt(i);
            }
            jsonArray.put(23,sum);
            jsonArray.put(24,9);
            jsonObject.put("WPurifier",jsonArray);
            if (clockisBound){
                String topicName="p99/warmer/"+IEMI+"/set";
                String s=jsonObject.toString();
                boolean success=Equservice.publish(topicName,1,s);
                if (!success){
                    Equservice.publish(topicName,1,s);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
