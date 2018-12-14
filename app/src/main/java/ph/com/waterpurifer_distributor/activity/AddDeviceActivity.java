package ph.com.waterpurifer_distributor.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import ph.com.waterpurifer_distributor.R;
import ph.com.waterpurifer_distributor.base.BaseActivity;
import ph.com.waterpurifer_distributor.base.MyApplication;
import ph.com.waterpurifer_distributor.database.dao.daoImp.EquipmentImpl;
import ph.com.waterpurifer_distributor.pojo.Equipment;
import ph.com.waterpurifer_distributor.util.HttpUtils;
import ph.com.waterpurifer_distributor.util.NetWorkUtil;
import ph.com.waterpurifer_distributor.util.ToastUtil;

public class AddDeviceActivity extends BaseActivity {
    MyApplication application;
    Unbinder unbinder;
    @BindView(R.id.et_ssid)
    EditText et_ssid;//IEMI
    @BindView(R.id.iv_pz_wifi)
    ImageView iv_pz_wifi;
    @BindView(R.id.tv_pz_wifi)
    TextView tv_pz_wifi;
    @BindView(R.id.iv_pz_sm)
    ImageView iv_pz_sm;
    @BindView(R.id.tv_pz_sm)
    TextView tv_pz_sm;
    @BindView(R.id.view_pz_sm)
    View view_pz_sm;
    @BindView(R.id.view_pz_wifi)
    View view_pz_wifi;
    @BindView(R.id.rl_add_sm)
    RelativeLayout rl_add_sm;
    @BindView(R.id.rl_add_wifi)
    RelativeLayout rl_add_wifi;
    @BindView(R.id.iv_add_bs)
    ImageView iv_add_bs;
    private ProgressDialog mProgressDialog;
    String deviceMac,userId;
    EquipmentImpl equmentDao;
    Equipment equipment;
    List<Equipment> equipments;
    private ProgressDialog progressDialog;
    SharedPreferences preferences;
    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindLayout() {
        setSteepStatusBar(true);
        return R.layout.activity_add_device;
    }

    @Override
    public void initView(View view) {
        if (application == null) {
            application = (MyApplication) getApplication();
        }
        application.addActivity(this);
        iv_add_bs .setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(AddDeviceActivity.this,R.color.white)));
         equmentDao = new EquipmentImpl(getApplicationContext());
         equipment = new Equipment();
         equipments = new ArrayList<>();
         equipments = equmentDao.findAll();
        progressDialog = new ProgressDialog(this);
        preferences = getSharedPreferences("my", MODE_PRIVATE);
        userId = preferences.getString("userId","");
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @Override
    public void widgetClick(View v) {

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @OnClick({R.id.btn_ensure,R.id.et_add_tx,R.id.rl_add_2,R.id.rl_add_1,R.id.iv_main_memu})
    public void onClick(View view){
        switch (view.getId()){

            case R.id.et_add_tx:
                startActivity(QRScanerActivity.class);
                break;
            case R.id.rl_add_2:
                iv_pz_wifi.setImageResource(R.mipmap.equ_sdk);
                iv_pz_sm.setImageResource(R.mipmap.equ_smg);
                tv_pz_sm.setTextColor(getResources().getColor(R.color.color_gray2));
                tv_pz_wifi.setTextColor(getResources().getColor(R.color.color_toblue));
                view_pz_sm.setVisibility(View.GONE);
                view_pz_wifi.setVisibility(View.VISIBLE);
                rl_add_wifi.setVisibility(View.VISIBLE);
                rl_add_sm.setVisibility(View.GONE);
                break;
            case R.id.rl_add_1:

                iv_pz_wifi.setImageResource(R.mipmap.equ_sdg);
                iv_pz_sm.setImageResource(R.mipmap.equ_smk);
                tv_pz_sm.setTextColor(getResources().getColor(R.color.color_toblue));
                tv_pz_wifi.setTextColor(getResources().getColor(R.color.color_gray2));
                view_pz_sm.setVisibility(View.VISIBLE);
                view_pz_wifi.setVisibility(View.GONE);
                rl_add_wifi.setVisibility(View.GONE);
                rl_add_sm.setVisibility(View.VISIBLE);
                break;

            case R.id.iv_main_memu:
                finish();
                break;

            case R.id.btn_ensure:
                //手动添加确定
                deviceMac = et_ssid.getText().toString().trim();
                if (TextUtils.isEmpty(deviceMac)) {
                    ToastUtil.showShort(this, "账号码不能为空");
                    break;
                }
                for (int i = 0; i< equipments.size(); i++){
                     equipment = equipments.get(i);
                     String id = equipment.getId()+"";
                     if (id.equals(deviceMac)){
                         ToastUtil.showShort(this,"设备已添加");
                         break;
                     }
                }

//                boolean isConn = NetWorkUtil.isConn(MyApplication.getContext());
                boolean isConn =true;
                if (isConn){
                    showProgressDialog("正在配置，请稍后。。。");
                    Map<String, Object> params = new HashMap<>();
                    params.put("deviceMac", deviceMac);
                    params.put("deviceType", 8);
                    params.put("deviceName", deviceMac);
                    params.put("deviceUserId", userId);
                    try {
                        new addDeviceAsyncTask().execute(params).get(5, TimeUnit.SECONDS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                        Message message = new Message();
                        message.obj = "TimeOut";
                        handler.sendMessage(message);
                    }
                }else {
                    ToastUtil.showShort(this, "无网络可用，请检查网络");
                }
                break;


        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if ("TimeOut".equals(msg.obj)){
                if (progressDialog!=null&&progressDialog.isShowing())
                    progressDialog.dismiss();
                Toast.makeText(AddDeviceActivity.this,"请求超时,请重试",Toast.LENGTH_SHORT).show();
            }
        }
    };
//    Equipment equipment2;
    class addDeviceAsyncTask extends AsyncTask<Map<String,Object>,Void,String> {

        @Override
        protected String doInBackground(Map<String, Object>... maps) {
            String code = "";
            Map<String ,Object> prarms = maps[0];
            String result = HttpUtils.postOkHpptRequest(HttpUtils.ipAddress+"/app/device/addNewDevice",prarms);
            Log.e("back", "--->"+result);
            if (!ToastUtil.isEmpty(result)){
                try {
                    JSONObject jsonObject= new JSONObject(result);
                    code = jsonObject.getString("returnCode");
//                    JSONObject returnData = jsonObject.getJSONObject("returnData");
                    if ("100".equals(code)){
//                        Equipment equipment = new Equipment();
//                        equipment.setName(deviceMac);
//                        equipment.setType(8);
//                        equipment.setDeviceMac(deviceMac);
//                        equipment.setId(Long.valueOf(deviceMac));
//                        equmentDao.insert(equipment);
//                        equipment2 = equipment;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return code;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            switch (s) {

                case "100":
                    if (progressDialog!=null&&progressDialog.isShowing())
                        progressDialog.dismiss();

                    ToastUtil.showShort(AddDeviceActivity.this, "配置成功");
                    Intent intent=new Intent();
//                    intent.putExtra("equipment", equipment2);
                    setResult(600,intent);
                    finish();
                    break;
                default:
                    if (progressDialog!=null&&progressDialog.isShowing())
                        progressDialog.dismiss();
                    ToastUtil.showShort(AddDeviceActivity.this, "配置失败，请重试");
                    break;

            }
        }
    }

    //显示dialog
    public void showProgressDialog(String message) {

        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(unbinder!=null)
            unbinder.unbind();//解绑注解
    }
}
