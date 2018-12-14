package ph.com.waterpurifer_distributor.database.dao.daoImp;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import java.util.List;
import ph.com.waterpurifer_distributor.database.dao.DBManager;
import ph.com.waterpurifer_distributor.database.dao.DaoMaster;
import ph.com.waterpurifer_distributor.database.dao.DaoSession;
import ph.com.waterpurifer_distributor.database.dao.EquipmentDao;
import ph.com.waterpurifer_distributor.pojo.Equipment;


public class EquipmentImpl {
    private Context context;
    private SQLiteDatabase db;
    private DaoMaster master;
    private EquipmentDao equipmentDao;
    private DaoSession session;
    public EquipmentImpl(Context context) {
        this.context = context;
        db= DBManager.getInstance(context).getWritableDasebase();
        master=new DaoMaster(db);
        session=master.newSession();
        equipmentDao = session.getEquipmentDao();
    }

    /**
     * 添加信息
     * @param equipment
     */
    public void insert(Equipment equipment){
        equipmentDao.insert(equipment);
    }

    /**
     * 删除信息
     * @param equipment
     */
    public void delete(Equipment equipment){
        equipmentDao.delete(equipment);
    }

    /**
     * 更新信息
     * @param equipment
     */
    public void update(Equipment equipment){
        equipmentDao.update(equipment);
    }

    public Equipment findById(long Id){
        return equipmentDao.load(Id);
    }
    public List<Equipment> findAll(){
        return equipmentDao.loadAll();
    }

    public void  deleteAll(){
        equipmentDao.deleteAll();
    }

    /**
     * 根据macAddress来查询设备
     * @param macAddress
     * @return
     */
    public List<Equipment> findDeviceByMacAddress(String macAddress){
        return equipmentDao.queryBuilder().where(EquipmentDao.Properties.DeviceMac.eq(macAddress)).list();
    }

    /**
     * 根据macAddress来查询设备
     * @param macAddress
     * @return
     */
    public Equipment findDeviceByMacAddress2(String macAddress){
        return equipmentDao.queryBuilder().where(EquipmentDao.Properties.DeviceMac.eq(macAddress)).unique();
    }

}
