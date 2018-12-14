/**
 * Copyright 2018 bejson.com
 */
package ph.com.waterpurifer_distributor.pojo;

/**
 * Auto-generated: 2018-12-14 14:54:50
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class JournalData {

    private int faultId;
    private String faultDeviceMac;
    private String faultType;
    private long faultTime;
    public void setFaultId(int faultId) {
        this.faultId = faultId;
    }
    public int getFaultId() {
        return faultId;
    }

    public void setFaultDeviceMac(String faultDeviceMac) {
        this.faultDeviceMac = faultDeviceMac;
    }
    public String getFaultDeviceMac() {
        return faultDeviceMac;
    }

    public void setFaultType(String faultType) {
        this.faultType = faultType;
    }
    public String getFaultType() {
        return faultType;
    }

    public void setFaultTime(long faultTime) {
        this.faultTime = faultTime;
    }
    public long getFaultTime() {
        return faultTime;
    }

}