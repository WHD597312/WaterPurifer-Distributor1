package ph.com.waterpurifer_distributor.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ph.com.waterpurifer_distributor.R;
import ph.com.waterpurifer_distributor.base.BaseActivity;
import ph.com.waterpurifer_distributor.base.MyApplication;
import ph.com.waterpurifer_distributor.util.HttpUtils;
import ph.com.waterpurifer_distributor.util.ToastUtil;


public class ChangpassActivity extends BaseActivity {
    MyApplication application;
    @BindView(R.id.view_main_1)
    View viewMain1;
    @BindView(R.id.iv_main_memu)
    ImageView ivMainMemu;
    @BindView(R.id.li_main_bt)
    LinearLayout liMainBt;
    @BindView(R.id.et_change_xg1)
    EditText etChangeXg1;
    @BindView(R.id.et_change_xg2)
    EditText etChangeXg2;
    @BindView(R.id.tv_cha_bz1)
    TextView tvChaBz1;
    @BindView(R.id.bt_chan_qd)
    Button btChanQd;
    @BindView(R.id.rl_main_father)
    RelativeLayout rlMainFather;

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindLayout() {
        setSteepStatusBar(true);
        return R.layout.activity_changepass;
    }

    @Override
    public void initView(View view) {
        if (application == null) {
            application = (MyApplication) getApplication();
        }
        application.addActivity(this);
        progressDialog = new ProgressDialog(this);
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @Override
    public void widgetClick(View v) {

    }

    @OnClick({R.id.iv_main_memu, R.id.bt_chan_qd})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_main_memu:
                finish();
                break;

            case R.id.bt_chan_qd:
                String phone = getSellerPhone();
                String oldPassword = etChangeXg1.getText().toString();
                String password = etChangeXg2.getText().toString();
                if (TextUtils.isEmpty(password)) {
                    toast("新密码不能为空");
                    break;
                }

//                boolean isConn = NetWorkUtil.isConn(MyApplication.getContext());
                boolean isConn=true;
                if (isConn){
                    showProgressDialog("正在登录，请稍后...");
                    Map<String, Object> params = new HashMap<>();
                    params.put("phone", phone);
                    params.put("oldPassword", oldPassword);
                    params.put("password", password);
                    new resetAsynTask().execute(params);
                }else {
                    ToastUtil.showShort(this, "无网络可用，请检查网络");
                }

                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }




    String returnMsg;
    class resetAsynTask extends AsyncTask<Map<String,Object>,Void,String> {

        @Override
        protected String doInBackground(Map<String, Object>... maps) {
            String code = "";
            Map<String, Object> prarms = maps[0];
            String result = HttpUtils.postOkHpptRequest(HttpUtils.ipAddress + "/app/seller/resetPassword", prarms);
            Log.e("back", "--->" + result);
            if (!ToastUtil.isEmpty(result)) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");
                    returnMsg=jsonObject.getString("returnMsg");
//                    JSONObject returnData = jsonObject.getJSONObject("returnData");
                    if ("100".equals(code)) {
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
                    if (progressDialog != null && progressDialog.isShowing())
                        progressDialog.dismiss();
                    startActivity(new Intent(ChangpassActivity.this, MainActivity.class));
                    toast( "登录成功");

                    break;
                default:
                    if (progressDialog != null && progressDialog.isShowing())
                        progressDialog.dismiss();
                    toast( returnMsg);
                    break;

            }
        }
    }


    private ProgressDialog progressDialog;
    //显示dialog
    public void showProgressDialog(String message) {

        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();

    }
}
