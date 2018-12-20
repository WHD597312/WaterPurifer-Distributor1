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
    @BindView(R.id.et_add_id)
    TextView et_add_id;
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

    @OnClick({R.id.btn_ensure,R.id.et_add_tx,R.id.rl_add_2,R.id.rl_add_1,R.id.iv_main_memu,R.id.btn_add_qd})
    public void onClick(View view){
        switch (view.getId()) {

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
                    ToastUtil.showShort(this, "IEMI不能为空");
                    break;
                }
                Intent intent = new Intent(this, UserdetailsActivity.class);
                intent.putExtra("deviceMac",deviceMac);
                startActivity(intent);
                break;
            case R.id.btn_add_qd:
                deviceMac = et_add_id.getText().toString().trim();
                if (TextUtils.isEmpty(deviceMac)) {
                    ToastUtil.showShort(this, "请扫描二维码");
                    break;
                }
                Intent intent1 = new Intent(this, UserdetailsActivity.class);
                intent1.putExtra("deviceMac",deviceMac);
                startActivity(intent1);
                break;
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
