package ph.com.waterpurifer_distributor.util.mqtt;


import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import ph.com.waterpurifer_distributor.activity.EqupmentxqActivity;
import ph.com.waterpurifer_distributor.database.dao.daoImp.EquipmentImpl;
import ph.com.waterpurifer_distributor.pojo.Equipment;
import ph.com.waterpurifer_distributor.util.TenTwoUtil;
import ph.com.waterpurifer_distributor.util.Utils;

public class MQService extends Service {

    private String host = "tcp://47.98.131.11:1883";
    /**
     * 主机名称
     */
    private String userName = "admin";
    /**
     * 用户名
     */
    private String passWord = "Xr7891122";
    /**
     * 密码
     */
    private Context mContext = this;
    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private MqttClient client;
    private MqttConnectOptions options;
    String clientId = "";
    /***
     * 头码
     */
    private int headCode = 0X90;

    /***
     * 商业模块00：忽略；11：按水流量租
     凭；22：按时间租赁；33：
     按售水量售水型；FF：常规
     机型
     */
    private int[] bussinessmodule = {0X00, 0X11, 0X22, 0X33, 0XFF};
    EquipmentImpl equipmentDao;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("MQService", "-->onCreate");
        init();
        equipmentDao = new EquipmentImpl(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("MQService", "-->onStartCommand");
        connect();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    /**
     * 初始化MQTT
     */
    private void init() {
        try {
            //host为主机名，test为clientid即连接MQTT的客户端ID，一般以客户端唯一标识符表示，MemoryPersistence设置clientid的保存形式，默认为以内存保存

            client = new MqttClient(host, clientId,
                    new MemoryPersistence());
            //MQTT的连接设置
            options = new MqttConnectOptions();
            //设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
            options.setCleanSession(true);
            //设置连接的用户名
            options.setUserName(userName);
            //设置连接的密码
            options.setPassword(passWord.toCharArray());
            // 设置超时时间 单位为秒
            options.setConnectionTimeout(15);
            // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
            options.setKeepAliveInterval(60);


            //设置回调
            client.setCallback(new MqttCallback() {

                @Override
                public void connectionLost(Throwable cause) {
                    //连接丢失后，一般在这里面进行重连
                    System.out.println("connectionLost----------");
                    startReconnect();
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    //publish后会执行到这里
                    System.out.println("deliveryComplete---------"
                            + token.isComplete());
                }

                @Override
                public void messageArrived(String topicName, MqttMessage message) {
                    try {
                        Log.i("topicName", "topicName:" + topicName);
                        String msg = message.toString();
                        new LoadAsyncTask().execute(topicName, message.toString());
                        Log.i("message", "message:" + msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getData(String macAddress) {
        try {
            Calendar calendar = Calendar.getInstance();
            int headCode = 0x55;
            int effectTime = 1;
            int year = calendar.get(Calendar.YEAR);
            int yearHigh = year / 256;
            int yearLow = year % 256;
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int week = calendar.get(Calendar.DAY_OF_WEEK);
            week = Utils.getWeek(week);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int min = calendar.get(Calendar.MINUTE);
            int second = calendar.get(Calendar.SECOND);
            int funCode = 2;


            int endCode = 136;
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(0, headCode);
            jsonArray.put(1, effectTime);
            jsonArray.put(2, yearHigh);
            jsonArray.put(3, yearLow);
            jsonArray.put(4, month);
            jsonArray.put(5, day);
            jsonArray.put(6, week);
            jsonArray.put(7, hour);
            jsonArray.put(8, min);
            jsonArray.put(9, second);
            jsonArray.put(10, funCode);
            int sum = 0;
            for (int i = 0; i < 11; i++) {
                sum = sum + jsonArray.getInt(i);
            }
            int checkCode = sum % 256;
            jsonArray.put(11, checkCode);
            jsonArray.put(12, endCode);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("WPurifier", jsonArray);
            String topicName = "p99/wPurifier1" + macAddress + "/set";
            String s = jsonObject.toString();
            boolean success = publish(topicName, 1, s);
            if (!success) {
                publish(topicName, 1, s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void sendData(Equipment equipment){
        try {
            JSONObject jsonObject=new JSONObject();
            JSONArray jsonArray=new JSONArray();
            int headCode=0x90;/**头码*/
            int busMode=equipment.getBussinessmodule();/**商业模式*/
            int dataLength=20;/**数据长度*/
            int deviceState=equipment.getIsOpen();/**设备状态*/
//            int wPurifierfilter1=equipment.getWPurifierfilter1();/**滤芯1-5*/
//            int wPurifierfilter2=equipment.getWPurifierfilter2();
//            int wPurifierfilter3=equipment.getWPurifierfilter3();
//            int wPurifierfilter4=equipment.getWPurifierfilter4();
//            int wPurifierfilter5=equipment.getWPurifierfilter5();
            int isLeakage=equipment.getIsLeakage();/**净水器是否漏水*/
            int[] x=new int[8];
            x[0]=0;
            x[1]=0;
            x[2]=0;
            x[3]=0;
            x[4]=0;
            if (isLeakage==0){
                x[5]=0;
                x[6]=1;
            } else if (isLeakage==1){
                x[5]=1;
                x[6]=0;
            }else {
                x[5]=0;
                x[6]=0;
            }
            x[0]=0;
            int content=TenTwoUtil.changeToTen(x);
            int isReset=equipment.getIsReset();/**是否清除流量计量*/
            int wPurifierPrimaryQuqlity=equipment.getWPurifierPrimaryQuqlity();/**原生水质TDS值*/
            int highWPurifierPrimaryQuqlity=wPurifierPrimaryQuqlity/256;
            int lowWPurifierPrimaryQuqlity=wPurifierPrimaryQuqlity%256;
            int wPurifierOutQuqlity=equipment.getWPurifierOutQuqlity();/**净水TDS*/
            int isReset2=equipment.getIsReset2();/**是否清除累计制水时间*/
            int wContinuiProductionTime=equipment.getWContinuiProductionTime();/**连续计水时间*/
            int RechargeTime=equipment.getRechargeTime();/**租赁充值时间*/

            int highRechargeTime=RechargeTime/256;
            int lowRechargeTime=RechargeTime%256;
            int RechargeFlow=equipment.getRechargeFlow();/**租赁充值流量*/
            int highRechargeFlow=RechargeFlow/256;
            int lowRechargeFlow=RechargeFlow%256;
            int gear=equipment.getGear();/**售水量档位*/
            int washTime=0;
            int signal=1;/**查询信号强度*/
            int type=equipment.getType();/**设备类型*/
            int checkCode;/**校验码*/
            int endCode=9;/**结束码*/

            jsonArray.put(0,headCode);
            jsonArray.put(1,busMode);
            jsonArray.put(2,dataLength);
            jsonArray.put(3,deviceState);
            jsonArray.put(4,content);
            jsonArray.put(5,isReset);
            jsonArray.put(6,highWPurifierPrimaryQuqlity);
            jsonArray.put(7,lowWPurifierPrimaryQuqlity);
            jsonArray.put(8,wPurifierOutQuqlity);
            jsonArray.put(9,isReset2);
            jsonArray.put(10,wContinuiProductionTime);
            jsonArray.put(11,highRechargeTime);
            jsonArray.put(12,lowRechargeTime);
            jsonArray.put(13,highRechargeFlow);
            jsonArray.put(14,lowRechargeFlow);
            jsonArray.put(15,gear);
            jsonArray.put(16,0);
            jsonArray.put(17,0);
            jsonArray.put(18,0);
            jsonArray.put(19,0);
            jsonArray.put(20,washTime);
            jsonArray.put(21,signal);
            jsonArray.put(22,type);
            int sum=0;
            for (int i = 0; i <23 ; i++) {
                sum+=jsonArray.getInt(i);
            }
            checkCode=sum%256;
            jsonArray.put(23,checkCode);
            jsonArray.put(24,endCode);

            jsonObject.put("WPurifier",jsonArray);
            String payload=jsonObject.toString();
            String macAddress=equipment.getDeviceMac();
            String topicName = "p99/wPurifier1/" + macAddress + "/set";
            boolean success=publish(topicName,1,payload);
            if (!success)
                publish(topicName,1,payload);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @SuppressLint("StaticFieldLeak")
    class LoadAsyncTask extends AsyncTask<String, Void, Object> {

        @Override
        protected Object doInBackground(String... strings) {

            String topicName = strings[0];/**收到的主题*/
            String message = strings[1];/**收到的消息*/
            Log.i("topicName", "-->:" + topicName);
            String macAddress = null;
            if (topicName.startsWith("p99/wPurifier1")) {
                macAddress = topicName.substring(15, topicName.lastIndexOf("/"));
            }
            JSONArray messageJsonArray = null;
            JSONObject messageJsonObject = null;
            try {
                if (!TextUtils.isEmpty(message) && message.startsWith("{") && message.endsWith("}")) {
                    messageJsonObject = new JSONObject(message);
                }
                if (messageJsonObject != null && messageJsonObject.has("WPurifier")) {
                    messageJsonArray = messageJsonObject.getJSONArray("WPurifier");
                }
                if (messageJsonArray != null) {
                    int wPurifierState;/*净水器状态*/
                    int bussinessmodule;/*商业模式*/
                    /*净水器滤芯寿命 1-5*/
                    int wPurifierfilter1, wPurifierfilter2, wPurifierfilter3, wPurifierfilter4, wPurifierfilter5;
                    String deviceMCU;
                    int wTrueFlowmeter;/*净水器流量计实际值*/
                    int wPurifierPrimaryQuqlity;/*净水器原生水质*/
                    int FlowmeterWarm;/**净水器流量计报警*/
                    int wPurifierOutQuqlity;/*净水器出水水质*/
                    int wTotalProductionTime;/*净水器累计制水时间*/
                    int wContinuiProductionTime;/**净水器连续制水时间*/
                    int wWaterStall;/*净水器售水量档位*/
                    int wMobileSignal;/*净水器移动信号*/
                    int IsOpen;/**净水器是否开机*/
                    int HavaWater;/**净水器是否有水*/
                    int WaterWash;/**净水器是否冲洗*/
                    int MakeWater;/**净水器是否制水*/
                    int IsFull;/**净水器是否冲满*/
                    int Repair;/**净水器检修*/
                    int IsLeakage;/**净水器是否漏水*/
                    int Warming;/**净水器温度值*/
                    int AlarmState;/**净水器设备报警状态*/
                    int AlarmIsLeakage;/**净水器报警漏水*/
                    int ContinuProduction;/**净水器连续制水*/
                    int AlarmFlowmeter;/**净水器报警流量计错误*/
                    int AlarmWash;/**净水器报警冲洗电磁阀错误*/
                    int RechargeTime;/**净水器租凭充值时间*/
                    int RechargeFlow;/**净水器剩余充值流量*/
                    int BackwaterInterval;/**净水器回水间隔时间*/
                    int BackwashTime;/**净水器回水冲洗时间*/
                    int BackwashInterval;/**净水器冲洗间隔*/
                    int MachineType;/**净水机器类型*/
                    int WashTime;/*净水机冲洗时间*/

                    bussinessmodule = messageJsonArray.getInt(1);
                    deviceMCU = messageJsonArray.getString(2);
                    wPurifierState = messageJsonArray.getInt(4);
                    /*bit0:0表示关机，1表示开机;bit1:0表示无缺水，1表示有缺水； bit2:制水；bit3:冲洗；bit4:水满；bit5:
                    检修；(以上各位为0是表示无报警，1表示有报警）；bit6：漏水检测状态，1功能开启，有漏水检测；0功能关闭，无漏水检测*/
                    int[] y = TenTwoUtil.changeToTwo(wPurifierState);
                    IsOpen = y[0];
                    HavaWater = y[1];
                    MakeWater = y[2];
                    WaterWash = y[3];
                    IsFull = y[4];
                    Repair = y[5];
                    IsLeakage = y[6];
                    wPurifierfilter1 = messageJsonArray.getInt(5);
                    wPurifierfilter2 = messageJsonArray.getInt(6);
                    wPurifierfilter3 = messageJsonArray.getInt(7);
                    wPurifierfilter4 = messageJsonArray.getInt(8);
                    wPurifierfilter5 = messageJsonArray.getInt(9);
                    FlowmeterWarm = messageJsonArray.getInt(10);
                    wTrueFlowmeter = messageJsonArray.getInt(11)*256 + messageJsonArray.getInt(12);
                    Warming = messageJsonArray.getInt(13);
                    wPurifierPrimaryQuqlity=messageJsonArray.getInt(14)*256+messageJsonArray.getInt(15);
                    wPurifierOutQuqlity = messageJsonArray.getInt(16);
                    wTotalProductionTime = messageJsonArray.getInt(17)*256 + messageJsonArray.getInt(18);
                    AlarmState = messageJsonArray.getInt(19);
                    int[] x = TenTwoUtil.changeToTwo(AlarmState);
                    ContinuProduction = x[0];
                    AlarmIsLeakage = x[1];
                    AlarmFlowmeter = x[2];
                    AlarmWash = x[3];
                    wContinuiProductionTime = messageJsonArray.getInt(20);
                    RechargeTime = messageJsonArray.getInt(21)*256  + messageJsonArray.getInt(22);
                    RechargeFlow = messageJsonArray.getInt(23)*256 + messageJsonArray.getInt(24);
                    BackwaterInterval = messageJsonArray.getInt(25)*256 + messageJsonArray.getInt(26);
                    BackwashTime = messageJsonArray.getInt(27);
                    BackwashInterval = messageJsonArray.getInt(28)*256 + messageJsonArray.getInt(29);
                    wWaterStall = messageJsonArray.getInt(30);
                    WashTime = messageJsonArray.getInt(35);
                    wMobileSignal = messageJsonArray.getInt(36);
                    MachineType = messageJsonArray.getInt(37);

                    if (equipment != null) {
                        equipment.setWPurifierState(wPurifierState);
                        equipment.setBussinessmodule(bussinessmodule);
                        equipment.setWPurifierfilter1(wPurifierfilter1);
                        equipment.setWPurifierfilter2(wPurifierfilter2);
                        equipment.setWPurifierfilter3(wPurifierfilter3);
                        equipment.setWPurifierfilter4(wPurifierfilter4);
                        equipment.setWPurifierfilter5(wPurifierfilter5);
                        equipment.setDeviceMCU(deviceMCU);
                        equipment.setIsOpen(IsOpen);
                        equipment.setHavaWater(HavaWater);
                        equipment.setMakeWater(MakeWater);
                        equipment.setWaterWash(WaterWash);
                        equipment.setIsFull(IsFull);
                        equipment.setRepair(Repair);
                        equipment.setIsLeakage(IsLeakage);
                        equipment.setAlarmIsLeakage(AlarmIsLeakage);
                        equipment.setFlowmeterWarm(FlowmeterWarm);
                        equipment.setWTrueFlowmeter(wTrueFlowmeter);
                        equipment.setWarming(Warming);
                        equipment.setWPurifierPrimaryQuqlity(wPurifierPrimaryQuqlity);
                        equipment.setWPurifierOutQuqlity(wPurifierOutQuqlity);
                        equipment.setWTotalProductionTime(wTotalProductionTime);
                        equipment.setContinuProduction(ContinuProduction);
                        equipment.setAlarmFlowmeter(AlarmFlowmeter);
                        equipment.setAlarmWash(AlarmWash);
                        equipment.setWContinuiProductionTime(wContinuiProductionTime);
                        equipment.setRechargeTime(RechargeTime);
                        equipment.setRechargeFlow(RechargeFlow);
                        equipment.setBackwashInterval(BackwashInterval);
                        equipment.setBackwashTime(BackwashTime);
                        equipment.setBackwaterInterval(BackwaterInterval);
                        equipment.setWWaterStall(wWaterStall);
                        equipment.setWashTime(WashTime);
                        equipment.setWMobileSignal(wMobileSignal);
                        equipment.setMachineType(MachineType);
                        equipment.setOnline(true);
                        equipmentDao.update(equipment);
                        Intent mqttIntent = new Intent("MainActivity");
                        mqttIntent.putExtra("msg", macAddress);
                        mqttIntent.putExtra("msg1", equipment);
                        sendBroadcast(mqttIntent);
                    }
                    if (EqupmentxqActivity.running){
                        Intent mqttIntent = new Intent("EqupmentxqActivity");
                        mqttIntent.putExtra("macAddress", macAddress);
                        mqttIntent.putExtra("equipment", equipment);
                        sendBroadcast(mqttIntent);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public List<String> getTopicNames() {
        List<String> list = new ArrayList<>();
        String onlineTopicName = "";
        String offlineTopicName = "";
//        List<Equipment> equipments = equipmentDao.findAll();
//        for (int i = 0; i < equipments.size(); i++) {
//            Equipment equipment = equipments.get(i);
//            String macAddress = equipment.getDeviceMac();
//            onlineTopicName = "p99/wPurifier1/" + macAddress + "/transfer";
//            offlineTopicName = "p99/wPurifier1/" + macAddress + "/lwt";
//            list.add(onlineTopicName);
//            list.add(offlineTopicName);
//        }
        if (equipment!=null){
            String macAddress = equipment.getDeviceMac();
            onlineTopicName = "p99/wPurifier1/" + macAddress + "/transfer";
            offlineTopicName = "p99/wPurifier1/" + macAddress + "/lwt";
            list.add(onlineTopicName);
            list.add(offlineTopicName);
        }
        return list;
    }

    /***
     * 连接MQTT
     */
    public void connect() {
        try {
            if (client != null && !client.isConnected()) {
                client.connect(options);
            }
            List<String> topicNames = getTopicNames();
            new ConAsync().execute(topicNames);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 重新连接MQTT
     */
    private void startReconnect() {

        scheduler.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                if (!client.isConnected()) {
                    connect();
                }
            }
        }, 0 * 1000, 1 * 1000, TimeUnit.MILLISECONDS);
    }

    /**
     * 发送MQTT主题
     */

    public boolean publish(String topicName, int qos, String payload) {
        boolean flag = false;
        if (client != null && client.isConnected()) {
            try {
                MqttMessage message = new MqttMessage(payload.getBytes("utf-8"));
                qos = 1;
                message.setQos(qos);
                client.publish(topicName, message);
                flag = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    /**
     * 订阅MQTT主题
     *
     * @param topicName
     * @param qos
     * @return
     */
    public boolean subscribe(String topicName, int qos) {
        boolean flag = false;
        if (client != null && client.isConnected()) {
            try {

                client.subscribe(topicName, qos);
                flag = true;
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }


    private LocalBinder binder = new LocalBinder();
    public class LocalBinder extends Binder {

        public MQService getService() {
            Log.i("Binder", "Binder");
            return MQService.this;
        }
    }


    /**
     * @param topicName
     */
    public void unsubscribe(String topicName) {
        if (client != null && client.isConnected()) {
            try {
                client.unsubscribe(topicName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    class ConAsync extends AsyncTask<List<String>, Void, Void> {

        @Override
        protected Void doInBackground(List<String>... lists) {
            try {
                List<String> topicNames = lists[0];
                if (client.isConnected() && !topicNames.isEmpty()) {
                    for (String topicName : topicNames) {
                        if (!TextUtils.isEmpty(topicName)) {
                            client.subscribe(topicName, 1);
                            String macAddress=topicName.substring(15,topicName.lastIndexOf("/"));
                            getData(macAddress);
                            Log.i("client", "-->" + topicName);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    Equipment equipment;
    public void setEquiment(Equipment equiment){
        this.equipment=equiment;
    }

    public Equipment getEquipment() {
        return equipment;
    }
}
