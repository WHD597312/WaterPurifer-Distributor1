package ph.com.waterpurifer_distributor.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class ManageActivity extends BaseActivity {
    MyApplication application;
    SharedPreferences preferences;
    @BindView(R.id.et_man_pass)
    EditText et_man_pass;
    @BindView(R.id.view1)
    View view1;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.rl_heard)
    RelativeLayout rlHeard;
    @BindView(R.id.tv_man_bz1)
    TextView tvManBz1;
    @BindView(R.id.tv_man_bz2)
    TextView tvManBz2;
    @BindView(R.id.et_set_pass)
    EditText etSetPass;
    @BindView(R.id.bt_man_qd)
    Button btManQd;
    @BindView(R.id.bt_man_jc)
    Button btManJc;

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindLayout() {
        setSteepStatusBar(true);
        return R.layout.activity_manage;
    }

    @Override
    public void initView(View view) {
        if (application == null) {
            application = (MyApplication) getApplication();
        }
        preferences = getSharedPreferences("my", MODE_PRIVATE);
        application.addActivity(this);
        progressDialog = new ProgressDialog(this);
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @Override
    public void widgetClick(View v) {

    }

    @OnClick({R.id.iv_back, R.id.bt_man_qd,R.id.bt_man_jc})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.bt_man_qd:
                if (etSetPass.getText().toString().length() < 4) {
                    ToastUtil.showShort(this, "请输入四位数的数字密码");
                } else if (etSetPass.getText().toString().length() == 4) {
                    //                boolean isConn = NetWorkUtil.isConn(MyApplication.getContext());
                    boolean isConn=true;
                    if (isConn){
                        showProgressDialog("正在修改，请稍后...");
                        Map<String, Object> params = new HashMap<>();
                        params.put("userId", getSellerId());
                        params.put("sellerPassword", et_man_pass.getText().toString());
                        params.put("sellerManagePassword", etSetPass.getText().toString());
                        new updateSellerAsynTask().execute(params);
                    }else {
                        ToastUtil.showShort(this, "无网络可用，请检查网络");
                    }
                }
                break;
            case R.id.bt_man_jc:
                boolean isConn=true;
                if(et_man_pass.getText().toString().equals(getSellerPassword())) {
                    if (isConn) {
                        showProgressDialog("正在修改，请稍后...");
                        Map<String, Object> params = new HashMap<>();
                        params.put("userId", getSellerId());
                        params.put("sellerPassword", et_man_pass.getText().toString());
                        params.put("sellerManagePassword", "");
                        new updateSellerAsynTask().execute(params);
                    } else {
                        ToastUtil.showShort(this, "无网络可用，请检查网络");
                    }
                }else
                    toast("解除绑定需填写正确登录密码");
                break;
        }
    }


    String returnMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    class updateSellerAsynTask extends AsyncTask<Map<String, Object>, Void, String> {

        @Override
        protected String doInBackground(Map<String, Object>... maps) {
            String code = "";
            Map<String, Object> prarms = maps[0];
            String result = HttpUtils.postOkHpptRequest(HttpUtils.ipAddress + "/app/seller/updateSeller", prarms);
            Log.e("back", "--->" + result);
            if (!ToastUtil.isEmpty(result)) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");
                    returnMsg = jsonObject.getString("returnMsg");
//                    JSONObject returnData = jsonObject.getJSONObject("returnData");
                    if ("100".equals(code)) {

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
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("sellerPassword", etSetPass.getText().toString());
                    editor.commit();
                    finish();
                    toast("修改成功");

                    break;
                default:
                    if (progressDialog != null && progressDialog.isShowing())
                        progressDialog.dismiss();
                    toast(returnMsg);
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
