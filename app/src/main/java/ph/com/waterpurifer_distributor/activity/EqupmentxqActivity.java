package ph.com.waterpurifer_distributor.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.formatter.IFillFormatter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import ph.com.waterpurifer_distributor.R;
import ph.com.waterpurifer_distributor.base.BaseActivity;
import ph.com.waterpurifer_distributor.base.MyApplication;
import ph.com.waterpurifer_distributor.database.dao.daoImp.EquipmentImpl;
import ph.com.waterpurifer_distributor.pojo.Equipment;
import ph.com.waterpurifer_distributor.util.mqtt.MQService;
import ph.com.waterpurifer_distributor.util.view.ScreenSizeUtils;

public class EqupmentxqActivity extends BaseActivity {
    MyApplication application;
    SharedPreferences preferences;
    @BindView(R.id.tv_line)
    TextView tv_line;//设备在线
    @BindView(R.id.tv_type)
    TextView tv_type;//机器类型
    @BindView(R.id.tv_state)
    TextView tv_state;//设备状态
    @BindView(R.id.tv_equxq_ys)
    TextView tv_equxq_ys;//原生TDS
    @BindView(R.id.tv_equxq_js)
    TextView tv_equxq_js;//净水TDS
    @BindView(R.id.tv_backwater_timer)
    TextView tv_backwater_timer;//回水间隔时间
    @BindView(R.id.tv_equxq_hscx)
    TextView tv_backwash_timer;//回水冲洗时间
    @BindView(R.id.tv_charge_day)
    TextView tv_charge_day;//充值天数
    @BindView(R.id.tv_equxq_jg)
    TextView tv_equxq_jg;//冲洗时间
    @BindView(R.id.tv_equxq_wd)
    TextView tv_equxq_wd;
    @BindView(R.id.tv_signal)
    TextView tv_signal;
    @BindView(R.id.iv_equxq_kg)
    ImageView iv_equxq_kg;//设备开关
    @BindView(R.id.tv_equxq_llql)
    ImageView tv_equxq_llql;//流量清0
    @BindView(R.id.tv_equxq_sl) TextView tv_equxq_sl;//累计纯水质水量
    @BindView(R.id.tv_use_day) TextView tv_use_day;//使用天数


    boolean isopen1 = true;
    boolean isopen2 = true;
    boolean isopen3 = true;
    @BindView(R.id.tv_equxq_lsjc)
    ImageView tv_equxq_lsjc;
    @BindView(R.id.iv_equxq_qzkg)
    ImageView iv_equxq_qzkg;
    private boolean clockisBound;
    private int OpenEqument;//设备开关  0X00不设置；0X01关机；0X02开机;0XFF复位
    private int Filter = 0;//滤芯时间及漏水检测功能开关 0无效；1（设置状态发1表示清0） 00:忽略，不设置;01:功能开启，有漏水检测;10:功能关闭，无漏水检测
    private int ZeroClear;//流量计实际值 0无效；1（设置状态发1表示清0）
    private int RawwaterTDS = 0;//原水TDS  0-65536
    private int WaterPurifTDS = 0;//净水TDS  0-255
    private int CumMakeTime = 0; //   累计治水时间 0无效；1（设置状态发1表示清0）
    private int ConMakeTime = 0;//连续治水时间 为0时忽略；为1时，不设置连续制水时间（无限制水）；其他值，连续制水的时间减一，例如发0X05，表示设置连续制水4小时后未满，检修报警。设置1~253小时
    private int Rentaltime = 0; //租赁时间 不支持添加
    private int Rentalll = 0; //租赁流量  不支持添加
    private int WaterStall = 0; //售水量档位
    private int WashTime = 0; //冲洗时间
    private int Singnal = 0;//移动信号强度
    private int Style = 0;  //机器类型
    String IEMI;
    private EquipmentImpl equipmentImpl;
    public static boolean running = false;

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindLayout() {
        setSteepStatusBar(true);
        return R.layout.activity_equpmentxq;
    }


    Equipment equipment;
    MessageReceiver receiver;

    @Override
    public void initView(View view) {
        if (application == null) {
            application = (MyApplication) getApplication();
        }
        application.addActivity(this);
        preferences = getSharedPreferences("my", MODE_PRIVATE);
        equipmentImpl = new EquipmentImpl(getApplicationContext());

        Intent intent = getIntent();
        String deviceMac = intent.getStringExtra("deviceMac");
        equipment = equipmentImpl.findDeviceByMacAddress2(deviceMac);
        setMode(equipment);
        IntentFilter filter = new IntentFilter("EqupmentxqActivity");
        receiver = new MessageReceiver();
        registerReceiver(receiver, filter);
        clockintent = new Intent(EqupmentxqActivity.this, MQService.class);
        clockisBound = bindService(clockintent, clockconnection, Context.BIND_AUTO_CREATE);
    }

    private int wPurifierfilter1, wPurifierfilter2, wPurifierfilter3, wPurifierfilter4, wPurifierfilter5;
    int[] wPurifierfilter = new int[5];

    @Override
    protected void onStart() {
        super.onStart();
        running = true;
    }

    private void setMode(Equipment equipment) {
        boolean online = equipment.getOnline();
        if (online) {
            tv_line.setText("在线");
        } else {
            tv_line.setText("离线");
        }
        int machineType = equipment.getMachineType();
        tv_type.setText("净水器");
        int wMobileSignal = equipment.getWMobileSignal();
        if (wMobileSignal >= 10) {
            tv_signal.setText("强");
        } else {
            tv_signal.setText("弱");
        }
        int wPurifierPrimaryQuqlity = equipment.getWPurifierPrimaryQuqlity();
        tv_equxq_ys.setText(wPurifierPrimaryQuqlity + "");
        int wPurifierOutQuqlity = equipment.getWPurifierOutQuqlity();
        tv_equxq_js.setText(wPurifierOutQuqlity + "");

        int wTotalProductionTime=equipment.getWTotalProductionTime();
        int day=wTotalProductionTime/24;
        tv_use_day.setText(day+"");
        int RechargeTime = equipment.getRechargeTime();
        if (RechargeTime > 1) {
            RechargeTime = (RechargeTime - 1) / 24;
            tv_charge_day.setText("" + RechargeTime);
        }
        int isOpen = equipment.getIsOpen();
        if (isOpen == 1) {
            iv_equxq_kg.setImageResource(R.mipmap.sz_kgk);
            isopen2 = true;
            OpenEqument = 2;
        } else if (isOpen == 0) {
            iv_equxq_kg.setImageResource(R.mipmap.sz_kgg);
            isopen2 = false;
            OpenEqument = 1;
        }
        int wTrueFlowmeter=equipment.getWTrueFlowmeter();
        tv_equxq_sl.setText(wTrueFlowmeter+"L");
        int temp = equipment.getWarming();
        if (temp >= 0) {
            tv_equxq_wd.setText(temp + "℃");
        }
        int BackwaterInterval = equipment.getBackwaterInterval() / 60;
        tv_backwater_timer.setText(BackwaterInterval + "h");
        int BackwashTime = equipment.getBackwashTime();
        tv_backwash_timer.setText(BackwashTime + "s");
        int BackwashInterval = equipment.getBackwashInterval();
        tv_equxq_jg.setText(BackwashInterval + "s");
        wPurifierfilter1 = equipment.getWPurifierfilter1();
        wPurifierfilter2 = equipment.getWPurifierfilter2();
        wPurifierfilter3 = equipment.getWPurifierfilter3();
        wPurifierfilter4 = equipment.getWPurifierfilter4();
        wPurifierfilter5 = equipment.getWPurifierfilter5();
        wPurifierfilter[0] = wPurifierfilter1;
        wPurifierfilter[1] = wPurifierfilter2;
        wPurifierfilter[2] = wPurifierfilter3;
        wPurifierfilter[3] = wPurifierfilter4;
        wPurifierfilter[4] = wPurifierfilter5;

       int isLeakage=equipment.getIsLeakage();
       if (isLeakage==1){
           tv_equxq_lsjc.setImageResource(R.mipmap.sz_kgk);
           isopen1=true;
       }else if (isLeakage==0){
           tv_equxq_lsjc.setImageResource(R.mipmap.sz_kgg);
           isopen1=false;
       }
    }

    Intent clockintent;
    MQService Equservice;
    boolean boundclock;
    ServiceConnection clockconnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MQService.LocalBinder binder = (MQService.LocalBinder) service;
            Equservice = binder.getService();
            boundclock = true;
            Log.e("QQQQQQQQQQQDDDDDDD", "onServiceConnected: ------->");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String macAddress = intent.getStringExtra("macAddress");
                Equipment equipment2 = (Equipment) intent.getSerializableExtra("equipment");
                if (equipment != null && macAddress.equals(equipment.getDeviceMac())) {
                    equipment = equipment2;
                    setMode(equipment);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        running = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (clockisBound) {
            unbindService(clockconnection);
        }
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        running = false;
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @Override
    public void widgetClick(View v) {

    }

    @OnClick({R.id.rl_equxq_maketime, R.id.rl_equxq_day, R.id.rl_equxq_lsjc, R.id.rl_equxq_kg, R.id.rl_equxq_qzkg,
            R.id.rl_equxq_lvx, R.id.iv_main_memu, R.id.rl_equxq_llql})

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_equxq_maketime:
                customDialog();
                break;
            case R.id.rl_equxq_llql://流量计清0
                customDialog2();
                break;
            case R.id.iv_main_memu:
                finish();
                break;
            case R.id.rl_equxq_day:
                startActivity(JournalActivity.class);
                break;
            case R.id.rl_equxq_lsjc:
                //漏水检查
                if (isopen1) {
                    tv_equxq_lsjc.setImageResource(R.mipmap.sz_kgg);
                    isopen1 = false;
                    equipment.setIsLeakage(0);
                } else {
                    tv_equxq_lsjc.setImageResource(R.mipmap.sz_kgk);
                    isopen1 = true;
                    equipment.setIsLeakage(1);
                }
                if (Equservice != null) {
                    Equservice.sendData(equipment);
                }
                break;

            case R.id.rl_equxq_kg:
                //设备开关
                if (isopen2) {
                    iv_equxq_kg.setImageResource(R.mipmap.sz_kgg);
                    isopen2 = false;
                    OpenEqument = 1;
                    equipment.setIsOpen(0);
                } else {
                    iv_equxq_kg.setImageResource(R.mipmap.sz_kgk);
                    isopen2 = true;
                    OpenEqument = 2;
                    equipment.setIsOpen(1);
                }
                if (Equservice != null) {
                    Equservice.sendData(equipment);
                }
                break;
            case R.id.rl_equxq_qzkg:
                //强制开关
                if (isopen3) {
                    iv_equxq_qzkg.setImageResource(R.mipmap.sz_kgg);
                    isopen3 = false;
                    equipment.setIsOpen(0);
                } else {
                    iv_equxq_qzkg.setImageResource(R.mipmap.sz_kgk);
                    isopen3 = true;
                    equipment.setIsOpen(1);
                }
                if (Equservice != null) {
                    Equservice.sendData(equipment);
                }
                break;
            case R.id.rl_equxq_lvx:
                Intent intent = new Intent(this, LvTypeActivity.class);
                intent.putExtra("wPurifierfilter", wPurifierfilter);
                startActivity(intent);
                break;

        }
    }

    /**
     * 自定义对话框
     */
    EditText et_make_sz;

    private void customDialog() {
        final Dialog dialog = new Dialog(EqupmentxqActivity.this, R.style.MyDialog);
        View view = View.inflate(EqupmentxqActivity.this, R.layout.dialog_makew, null);
        TextView tv_make_qx = (TextView) view.findViewById(R.id.tv_make_qx);
        TextView tv_make_qd = (TextView) view.findViewById(R.id.tv_make_qd);
        TextView tv_make_time = view.findViewById(R.id.tv_make_time);
        int time = equipment.getWContinuiProductionTime();
        tv_make_time.setText(time + "");
        et_make_sz = view.findViewById(R.id.et_make_sz);
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
                String ss = et_make_sz.getText().toString();
                int hour = Integer.parseInt(ss);
                if (hour >= 254) {

                } else if (hour >= 0 && hour < 254) {
                    if (Equservice != null) {
                        equipment.setWContinuiProductionTime(hour);
                        Equservice.sendData(equipment);
                    }
                    dialog.dismiss();
                }

            }
        });
        dialog.show();
    }

    /**
     * 是否清除流量
     */
    private void customDialog2() {
        final Dialog dialog = new Dialog(EqupmentxqActivity.this, R.style.MyDialog);
        View view = View.inflate(EqupmentxqActivity.this, R.layout.dialog_clear_flow, null);
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
                if (Equservice != null) {
                    equipment.setIsReset(1);
                    Equservice.sendData(equipment);
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void sendMessage() {
        int sum = 0;
        try {
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            int headCode = 90;//头码
            jsonArray.put(0, headCode);
            int busnessModeel = 0;//商业模式
            jsonArray.put(1, busnessModeel);
            int dataLength = 20;//数据长度
            jsonArray.put(2, dataLength);
            jsonArray.put(3, OpenEqument);
            jsonArray.put(4, Filter);
            jsonArray.put(5, ZeroClear);
            jsonArray.put(6, RawwaterTDS);
            jsonArray.put(7, RawwaterTDS);
            jsonArray.put(8, WaterPurifTDS);
            jsonArray.put(9, CumMakeTime);
            jsonArray.put(10, ConMakeTime);
            jsonArray.put(11, Rentaltime);
            jsonArray.put(12, Rentaltime);
            jsonArray.put(13, Rentalll);
            jsonArray.put(14, Rentalll);
            jsonArray.put(15, WaterStall);
            jsonArray.put(16, 0);
            jsonArray.put(17, 0);
            jsonArray.put(18, 0);
            jsonArray.put(19, 0);
            jsonArray.put(20, WashTime);
            jsonArray.put(21, Singnal);
            jsonArray.put(22, Style);
            for (int i = 0; i < jsonArray.length(); i++) {
                sum += jsonArray.getInt(i);
            }
            jsonArray.put(23, sum);
            jsonArray.put(24, 9);
            jsonObject.put("WPurifier", jsonArray);
            if (clockisBound) {
                String topicName = "p99/warmer/" + IEMI + "/set";
                String s = jsonObject.toString();
                boolean success = Equservice.publish(topicName, 1, s);
                if (!success) {
                    Equservice.publish(topicName, 1, s);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
