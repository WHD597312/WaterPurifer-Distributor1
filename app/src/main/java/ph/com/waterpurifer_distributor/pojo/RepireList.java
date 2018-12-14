package ph.com.waterpurifer_distributor.pojo;

public class RepireList {
    private int repairId ;
     private  String repairDeviceMac;
     private  String repairDeviceType;//维修类型
     private  String repairTime;//维修时间
     private  String   repairAddress;    //维修地点
     private  String    repairDesc; //详细描述
     private String repairPhone;//维修电话
    private int   repairCreatorId;//维修者id
    private long repairCreatTime;
    private int  repairFlag;
    public int getRepairId() {
        return repairId;
    }

    public void setRepairId(int repairId) {
        this.repairId = repairId;
    }

    public String getRepairDeviceMac() {
        return repairDeviceMac;
    }

    public void setRepairDeviceMac(String repairDeviceMac) {
        this.repairDeviceMac = repairDeviceMac;
    }

    public String getRepairDeviceType() {
        return repairDeviceType;
    }

    public void setRepairDeviceType(String repairDeviceType) {
        this.repairDeviceType = repairDeviceType;
    }

    public String getRepairTime() {
        return repairTime;
    }

    public void setRepairTime(String repairTime) {
        this.repairTime = repairTime;
    }

    public String getRepairAddress() {
        return repairAddress;
    }

    public void setRepairAddress(String repairAddress) {
        this.repairAddress = repairAddress;
    }

    public String getRepairDesc() {
        return repairDesc;
    }

    public void setRepairDesc(String repairDesc) {
        this.repairDesc = repairDesc;
    }

    public String getRepairPhone() {
        return repairPhone;
    }

    public void setRepairPhone(String repairPhone) {
        this.repairPhone = repairPhone;
    }

    public int getRepairCreatorId() {
        return repairCreatorId;
    }

    public void setRepairCreatorId(int repairCreatorId) {
        this.repairCreatorId = repairCreatorId;
    }

    public long getRepairCreatTime() {
        return repairCreatTime;
    }

    public void setRepairCreatTime(long repairCreatTime) {
        this.repairCreatTime = repairCreatTime;
    }

    public int getRepairFlag() {
        return repairFlag;
    }

    public void setRepairFlag(int repairFlag) {
        this.repairFlag = repairFlag;
    }



}
