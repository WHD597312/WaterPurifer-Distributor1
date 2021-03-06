package ph.com.waterpurifer_distributor.pojo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

@Entity
public class Equipment implements Serializable{
    @Id(autoincrement = false)
    private long id;/*设备id*/
    private int type;/*设备类型*/
    private String name;/*设备名称*/
    private boolean online;
    private String deviceMac;/*设备mac*/
    private String deviceMCU;/*设备mcu*/
    private int deviceUserId;/*设备使用人id*/
    private int wPurifierState;/*净水器状态*/
    private int bussinessmodule;/*商业模式*/
    /*净水器滤芯寿命 1-5*/
    private  int wPurifierfilter, wPurifierfilter1, wPurifierfilter2, wPurifierfilter3, wPurifierfilter4, wPurifierfilter5;
    private  int wTrueFlowmeter;/*净水器流量计实际值*/
    private int wPurifierPrimaryQuqlity;/*净水器原生水质*/
    private int FlowmeterWarm;/**净水器流量计报警*/
    private int wPurifierOutQuqlity;/*净水器出水水质,净水TDS*/
    private  int wTotalProductionTime;/*净水器累计制水时间*/
    private int wContinuiProductionTime;/**净水器连续制水时间*/
    private int wWaterStall;/*净水器售水量档位*/
    private int wMobileSignal;/*净水器移动信号*/
    private int IsOpen ;/**净水器是否开机*/
    private int HavaWater;/**净水器是否有水*/
    private  int WaterWash;/**净水器是否冲洗*/
    private  int MakeWater;/**净水器是否制水*/
    private  int IsFull;/**净水器是否冲满*/
    private int isReset;/**是否清除流量计量*/
    private int isReset2;/**是否清除计水时间*/
    private int Repair;/**净水器检修*/
    private int IsLeakage;/**净水器是否漏水*/
    private  int Alarming;/**净水器流量报警*/
    private  int Warming;/**净水器温度值*/
    private  int AlarmState;/**净水器设备报警状态*/
    private   int AlarmIsLeakage;/**净水器报警漏水*/
    private  int ContinuProduction ;/**净水器连续制水*/
    private  int AlarmFlowmeter ;/**净水器报警流量计错误*/
    private   int AlarmWash ;/**净水器报警冲洗电磁阀错误*/
    private  int RechargeTime;/**净水器租凭充值时间*/
    private  int RechargeFlow;/**净水器剩余充值流量*/
    private  int BackwaterInterval;/**净水器回水间隔时间*/
    private  int BackwashTime;/**净水器回水冲洗时间*/
    private  int BackwashInterval;/**净水器冲洗间隔*/
    private   int MachineType;/**净水机器类型*/
    private  int WashTime;/*净水机冲洗时间*/
    private int gear;/**设置档位*/
    public int getGear() {
        return this.gear;
    }
    public void setGear(int gear) {
        this.gear = gear;
    }
    public int getWashTime() {
        return this.WashTime;
    }
    public void setWashTime(int WashTime) {
        this.WashTime = WashTime;
    }
    public int getMachineType() {
        return this.MachineType;
    }
    public void setMachineType(int MachineType) {
        this.MachineType = MachineType;
    }
    public int getBackwashInterval() {
        return this.BackwashInterval;
    }
    public void setBackwashInterval(int BackwashInterval) {
        this.BackwashInterval = BackwashInterval;
    }
    public int getBackwashTime() {
        return this.BackwashTime;
    }
    public void setBackwashTime(int BackwashTime) {
        this.BackwashTime = BackwashTime;
    }
    public int getBackwaterInterval() {
        return this.BackwaterInterval;
    }
    public void setBackwaterInterval(int BackwaterInterval) {
        this.BackwaterInterval = BackwaterInterval;
    }
    public int getRechargeFlow() {
        return this.RechargeFlow;
    }
    public void setRechargeFlow(int RechargeFlow) {
        this.RechargeFlow = RechargeFlow;
    }
    public int getRechargeTime() {
        return this.RechargeTime;
    }
    public void setRechargeTime(int RechargeTime) {
        this.RechargeTime = RechargeTime;
    }
    public int getAlarmWash() {
        return this.AlarmWash;
    }
    public void setAlarmWash(int AlarmWash) {
        this.AlarmWash = AlarmWash;
    }
    public int getAlarmFlowmeter() {
        return this.AlarmFlowmeter;
    }
    public void setAlarmFlowmeter(int AlarmFlowmeter) {
        this.AlarmFlowmeter = AlarmFlowmeter;
    }
    public int getContinuProduction() {
        return this.ContinuProduction;
    }
    public void setContinuProduction(int ContinuProduction) {
        this.ContinuProduction = ContinuProduction;
    }
    public int getAlarmIsLeakage() {
        return this.AlarmIsLeakage;
    }
    public void setAlarmIsLeakage(int AlarmIsLeakage) {
        this.AlarmIsLeakage = AlarmIsLeakage;
    }
    public int getAlarmState() {
        return this.AlarmState;
    }
    public void setAlarmState(int AlarmState) {
        this.AlarmState = AlarmState;
    }
    public int getWarming() {
        return this.Warming;
    }
    public void setWarming(int Warming) {
        this.Warming = Warming;
    }
    public int getAlarming() {
        return this.Alarming;
    }
    public void setAlarming(int Alarming) {
        this.Alarming = Alarming;
    }
    public int getIsLeakage() {
        return this.IsLeakage;
    }
    public void setIsLeakage(int IsLeakage) {
        this.IsLeakage = IsLeakage;
    }
    public int getRepair() {
        return this.Repair;
    }
    public void setRepair(int Repair) {
        this.Repair = Repair;
    }
    public int getIsReset2() {
        return this.isReset2;
    }
    public void setIsReset2(int isReset2) {
        this.isReset2 = isReset2;
    }
    public int getIsReset() {
        return this.isReset;
    }
    public void setIsReset(int isReset) {
        this.isReset = isReset;
    }
    public int getIsFull() {
        return this.IsFull;
    }
    public void setIsFull(int IsFull) {
        this.IsFull = IsFull;
    }
    public int getMakeWater() {
        return this.MakeWater;
    }
    public void setMakeWater(int MakeWater) {
        this.MakeWater = MakeWater;
    }
    public int getWaterWash() {
        return this.WaterWash;
    }
    public void setWaterWash(int WaterWash) {
        this.WaterWash = WaterWash;
    }
    public int getHavaWater() {
        return this.HavaWater;
    }
    public void setHavaWater(int HavaWater) {
        this.HavaWater = HavaWater;
    }
    public int getIsOpen() {
        return this.IsOpen;
    }
    public void setIsOpen(int IsOpen) {
        this.IsOpen = IsOpen;
    }
    public int getWMobileSignal() {
        return this.wMobileSignal;
    }
    public void setWMobileSignal(int wMobileSignal) {
        this.wMobileSignal = wMobileSignal;
    }
    public int getWWaterStall() {
        return this.wWaterStall;
    }
    public void setWWaterStall(int wWaterStall) {
        this.wWaterStall = wWaterStall;
    }
    public int getWContinuiProductionTime() {
        return this.wContinuiProductionTime;
    }
    public void setWContinuiProductionTime(int wContinuiProductionTime) {
        this.wContinuiProductionTime = wContinuiProductionTime;
    }
    public int getWTotalProductionTime() {
        return this.wTotalProductionTime;
    }
    public void setWTotalProductionTime(int wTotalProductionTime) {
        this.wTotalProductionTime = wTotalProductionTime;
    }
    public int getWPurifierOutQuqlity() {
        return this.wPurifierOutQuqlity;
    }
    public void setWPurifierOutQuqlity(int wPurifierOutQuqlity) {
        this.wPurifierOutQuqlity = wPurifierOutQuqlity;
    }
    public int getFlowmeterWarm() {
        return this.FlowmeterWarm;
    }
    public void setFlowmeterWarm(int FlowmeterWarm) {
        this.FlowmeterWarm = FlowmeterWarm;
    }
    public int getWPurifierPrimaryQuqlity() {
        return this.wPurifierPrimaryQuqlity;
    }
    public void setWPurifierPrimaryQuqlity(int wPurifierPrimaryQuqlity) {
        this.wPurifierPrimaryQuqlity = wPurifierPrimaryQuqlity;
    }
    public int getWTrueFlowmeter() {
        return this.wTrueFlowmeter;
    }
    public void setWTrueFlowmeter(int wTrueFlowmeter) {
        this.wTrueFlowmeter = wTrueFlowmeter;
    }
    public int getWPurifierfilter5() {
        return this.wPurifierfilter5;
    }
    public void setWPurifierfilter5(int wPurifierfilter5) {
        this.wPurifierfilter5 = wPurifierfilter5;
    }
    public int getWPurifierfilter4() {
        return this.wPurifierfilter4;
    }
    public void setWPurifierfilter4(int wPurifierfilter4) {
        this.wPurifierfilter4 = wPurifierfilter4;
    }
    public int getWPurifierfilter3() {
        return this.wPurifierfilter3;
    }
    public void setWPurifierfilter3(int wPurifierfilter3) {
        this.wPurifierfilter3 = wPurifierfilter3;
    }
    public int getWPurifierfilter2() {
        return this.wPurifierfilter2;
    }
    public void setWPurifierfilter2(int wPurifierfilter2) {
        this.wPurifierfilter2 = wPurifierfilter2;
    }
    public int getWPurifierfilter1() {
        return this.wPurifierfilter1;
    }
    public void setWPurifierfilter1(int wPurifierfilter1) {
        this.wPurifierfilter1 = wPurifierfilter1;
    }
    public int getWPurifierfilter() {
        return this.wPurifierfilter;
    }
    public void setWPurifierfilter(int wPurifierfilter) {
        this.wPurifierfilter = wPurifierfilter;
    }
    public int getBussinessmodule() {
        return this.bussinessmodule;
    }
    public void setBussinessmodule(int bussinessmodule) {
        this.bussinessmodule = bussinessmodule;
    }
    public int getWPurifierState() {
        return this.wPurifierState;
    }
    public void setWPurifierState(int wPurifierState) {
        this.wPurifierState = wPurifierState;
    }
    public int getDeviceUserId() {
        return this.deviceUserId;
    }
    public void setDeviceUserId(int deviceUserId) {
        this.deviceUserId = deviceUserId;
    }
    public String getDeviceMCU() {
        return this.deviceMCU;
    }
    public void setDeviceMCU(String deviceMCU) {
        this.deviceMCU = deviceMCU;
    }
    public String getDeviceMac() {
        return this.deviceMac;
    }
    public void setDeviceMac(String deviceMac) {
        this.deviceMac = deviceMac;
    }
    public boolean getOnline() {
        return this.online;
    }
    public void setOnline(boolean online) {
        this.online = online;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    @Generated(hash = 2026222139)
    public Equipment(long id, int type, String name, boolean online, String deviceMac, String deviceMCU, int deviceUserId,
            int wPurifierState, int bussinessmodule, int wPurifierfilter, int wPurifierfilter1, int wPurifierfilter2,
            int wPurifierfilter3, int wPurifierfilter4, int wPurifierfilter5, int wTrueFlowmeter,
            int wPurifierPrimaryQuqlity, int FlowmeterWarm, int wPurifierOutQuqlity, int wTotalProductionTime,
            int wContinuiProductionTime, int wWaterStall, int wMobileSignal, int IsOpen, int HavaWater, int WaterWash,
            int MakeWater, int IsFull, int isReset, int isReset2, int Repair, int IsLeakage, int Alarming, int Warming,
            int AlarmState, int AlarmIsLeakage, int ContinuProduction, int AlarmFlowmeter, int AlarmWash, int RechargeTime,
            int RechargeFlow, int BackwaterInterval, int BackwashTime, int BackwashInterval, int MachineType, int WashTime,
            int gear) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.online = online;
        this.deviceMac = deviceMac;
        this.deviceMCU = deviceMCU;
        this.deviceUserId = deviceUserId;
        this.wPurifierState = wPurifierState;
        this.bussinessmodule = bussinessmodule;
        this.wPurifierfilter = wPurifierfilter;
        this.wPurifierfilter1 = wPurifierfilter1;
        this.wPurifierfilter2 = wPurifierfilter2;
        this.wPurifierfilter3 = wPurifierfilter3;
        this.wPurifierfilter4 = wPurifierfilter4;
        this.wPurifierfilter5 = wPurifierfilter5;
        this.wTrueFlowmeter = wTrueFlowmeter;
        this.wPurifierPrimaryQuqlity = wPurifierPrimaryQuqlity;
        this.FlowmeterWarm = FlowmeterWarm;
        this.wPurifierOutQuqlity = wPurifierOutQuqlity;
        this.wTotalProductionTime = wTotalProductionTime;
        this.wContinuiProductionTime = wContinuiProductionTime;
        this.wWaterStall = wWaterStall;
        this.wMobileSignal = wMobileSignal;
        this.IsOpen = IsOpen;
        this.HavaWater = HavaWater;
        this.WaterWash = WaterWash;
        this.MakeWater = MakeWater;
        this.IsFull = IsFull;
        this.isReset = isReset;
        this.isReset2 = isReset2;
        this.Repair = Repair;
        this.IsLeakage = IsLeakage;
        this.Alarming = Alarming;
        this.Warming = Warming;
        this.AlarmState = AlarmState;
        this.AlarmIsLeakage = AlarmIsLeakage;
        this.ContinuProduction = ContinuProduction;
        this.AlarmFlowmeter = AlarmFlowmeter;
        this.AlarmWash = AlarmWash;
        this.RechargeTime = RechargeTime;
        this.RechargeFlow = RechargeFlow;
        this.BackwaterInterval = BackwaterInterval;
        this.BackwashTime = BackwashTime;
        this.BackwashInterval = BackwashInterval;
        this.MachineType = MachineType;
        this.WashTime = WashTime;
        this.gear = gear;
    }
    @Generated(hash = 748305627)
    public Equipment() {
    }

 
   

   

}
