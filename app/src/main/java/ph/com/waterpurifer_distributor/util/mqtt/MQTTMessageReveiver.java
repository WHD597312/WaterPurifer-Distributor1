package ph.com.waterpurifer_distributor.util.mqtt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import ph.com.waterpurifer_distributor.base.MyApplication;
import ph.com.waterpurifer_distributor.util.NetWorkUtil;
import ph.com.waterpurifer_distributor.util.ToastUtil;


public class MQTTMessageReveiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isConn = NetWorkUtil.isConn(MyApplication.getContext());
        ConnectivityManager connectivityManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        Log.i("MQTTMessageReveiver","MQTTMessageReveiver");
        if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
            ToastUtil.showShort(context, "无网络可用");
            //改变背景或者 处理网络的全局变量
        }else if (mobNetInfo.isConnected() || wifiNetInfo.isConnected()){
//            Utils.showToast(context,"网络已连接");
//            DeviceChildDaoImpl deviceChildDao = new DeviceChildDaoImpl(context);
//            List<DeviceChild> deviceChildren = deviceChildDao.findAllDevice();

            Intent mqttIntent = new Intent(context,MQService.class);
            mqttIntent.putExtra("reconnect","reconnect");
            mqttIntent.putExtra("isClockFinish","isClockFinish");
            context.startService(mqttIntent);
        }

        if (!isConn) {
        }
    }
}
