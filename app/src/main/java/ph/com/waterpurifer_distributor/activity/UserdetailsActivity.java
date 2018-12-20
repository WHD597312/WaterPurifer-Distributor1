package ph.com.waterpurifer_distributor.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ph.com.waterpurifer_distributor.R;
import ph.com.waterpurifer_distributor.base.BaseActivity;
import ph.com.waterpurifer_distributor.base.MyApplication;
import ph.com.waterpurifer_distributor.util.HttpUtils;
import ph.com.waterpurifer_distributor.util.NetWorkUtil;
import ph.com.waterpurifer_distributor.util.ToastUtil;

public class UserdetailsActivity extends BaseActivity {
    MyApplication application;
    String deviceMac;
    @BindView(R.id.iv_user_fh)
    ImageView ivUserFh;
    @BindView(R.id.rl_user_header)
    RelativeLayout rlUserHeader;
    @BindView(R.id.iv_user_pic)
    ImageView ivUserPic;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.rl_user_top)
    RelativeLayout rlUserTop;
    @BindView(R.id.tv_user_equname)
    TextView tvUserEquname;
    @BindView(R.id.rl_user_name)
    RelativeLayout rlUserName;
    @BindView(R.id.iv_user_equxq)
    ImageView ivUserEquxq;
    @BindView(R.id.rl_user_equxq)
    RelativeLayout rlUserEquxq;
    @BindView(R.id.tv_user_time)
    TextView tvUserTime;
    @BindView(R.id.rl_user_time)
    RelativeLayout rlUserTime;
    @BindView(R.id.rl_user_main)
    RelativeLayout rlUserMain;
    @BindView(R.id.tv_user_adress)
    TextView tvUserAdress;
    @BindView(R.id.rl_user_adress)
    RelativeLayout rlUserAdress;
    @BindView(R.id.tv_user_xqadress)
    TextView tvUserXqadress;
    @BindView(R.id.rl_user_xxadress)
    RelativeLayout rlUserXxadress;
    @BindView(R.id.tv_user_sytime)
    TextView tvUserSytime;
    @BindView(R.id.rl_user_sytime)
    RelativeLayout rlUserSytime;
    @BindView(R.id.rl_user_bootom)
    RelativeLayout rlUserBootom;
    @BindView(R.id.bt_user_bd)
    Button btUserBd;
    @BindView(R.id.tv_type)
    TextView tvType;


    boolean isMine=true;

    @Override
    public void initParms(Bundle parms) {
        deviceMac = parms.getString("deviceMac");
    }

    @Override
    public int bindLayout() {
        setSteepStatusBar(true);
        return R.layout.activity_user;
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
   boolean isConn = NetWorkUtil.isConn(MyApplication.getContext());
        if (isConn) {
            showProgressDialog("正在加载，请稍后...");
            Map<String, Object> params = new HashMap<>();
            params.put("deviceMac", deviceMac);
            new searchDeviceAsynTask().execute(params);
        } else {
            ToastUtil.showShort(this, "无网络可用，请检查网络");
        }
    }

    @Override
    public void widgetClick(View v) {

    }

    @OnClick({R.id.iv_user_fh, R.id.rl_user_equxq,R.id.bt_user_bd})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_user_fh:
                finish();
                break;

            case R.id.rl_user_equxq:
                if (isMine&&returnData!=null) {
                    Intent intent = new Intent(this, EqupmentxqActivity.class);
                    intent.putExtra("deviceMac", deviceMac);
                    startActivity(intent);
                }else
                toast("该设备不属于你");
                break;
            case R.id.bt_user_bd:
                if(isMine&&returnData!=null) {
                    if (devicePayType != -1) {
                        boolean isConn = true;
                        if (isConn) {
                            showProgressDialog("正在加载，请稍后...");
                            Map<String, Object> params = new HashMap<>();
                            params.put("deviceMac", deviceMac);
                            params.put("deviceSellerId", getSellerId());
                            params.put("deviceSellerFlag", devicePayType);

                            new updateDeviceByFlagAsynTask().execute(params);
                        } else {
                            ToastUtil.showShort(this, "无网络可用，请检查网络");
                        }
                    }
                }else
                    toast("该设备不属于你");

                break;

        }

    }


    String returnMsg;
    JSONObject returnData;
    int devicePayType;
    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    class searchDeviceAsynTask extends AsyncTask<Map<String, Object>, Void, String> {

        @Override
        protected String doInBackground(Map<String, Object>... maps) {
            String code = "";
            Map<String, Object> prarms = maps[0];
            String result = HttpUtils.postOkHpptRequest(HttpUtils.ipAddress + "/app/device/getDeviceBasicData", prarms);
            Log.e("back", "--->" + result);
            if (!ToastUtil.isEmpty(result)) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");
                    returnMsg = jsonObject.getString("returnMsg");
//                    JSONObject returnData = jsonObject.getJSONObject("returnData");
                    if ("100".equals(code)) {
                        returnData = jsonObject.getJSONObject("returnData");

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
                    try {
                        if (returnData != null) {

                            tvUserName.setText(returnData.getString("deviceUserName"));
                            tvUserEquname.setText(returnData.getString("deviceName"));
                            Date date = new Date(returnData.getLong("deviceCreatTime"));
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                            tvUserTime.setText(format.format(date));
                            String address = returnData.getString("deviceUserAddress");
                            tvUserAdress.setText(address.substring(0, address.indexOf("市") + 1));
                            if (address.indexOf("市") + 1 == address.length())
                                tvUserXqadress.setText("暂无");
                            else
                                tvUserXqadress.setText(address.substring((address.indexOf("市") + 1), address.length()));

                            if (returnData.getInt("deviceLeaseType") == 1 || returnData.getInt("deviceLeaseType") == 3) {
                                tvType.setText("使用水量");
                                tvUserSytime.setText(returnData.getInt("deviceNum") + "L");
                            } else if (returnData.getInt("deviceLeaseType") == 2) {
                                tvType.setText("使用天数");
                                tvUserSytime.setText(returnData.getInt("deviceNum") / 24 + "天");
                            } else {
                                tvType.setVisibility(View.GONE);
                                tvUserSytime.setVisibility(View.GONE);
                            }

                            if (returnData.getInt("deviceLeaseType") == 4) {
                                btUserBd.setBackgroundResource(R.drawable.bg_fill_gray25);
                                devicePayType = -1;
                            } else {
                                if (returnData.getInt("deviceUserId") == 0) {
                                    btUserBd.setBackgroundResource(R.drawable.bg_fill_gray25);
                                    devicePayType = -1;
                                } else {
                                    if (returnData.getInt("devicePayType") == 1) {
                                        btUserBd.setText("解除绑定");
                                        devicePayType = 1;
                                    } else {
                                        btUserBd.setText("绑定");
                                        devicePayType = 0;
                                    }
                                }
                            }

                            if (returnData.getInt("deviceSellerId") == getSellerId())
                                isMine = true;
                            else
                                isMine = false;
                        }else {
                            toast("无此设备IMEI，将在三秒后自动退出");
                             countDownTimer = new CountDownTimer(3000,1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {

                                }

                                @Override
                                public void onFinish() {
                                    application.removeActivity(UserdetailsActivity.this);
                                }
                            }.start();
                        }
                        } catch(JSONException e){
                            e.printStackTrace();
                        }

                    break;
                default:
                    if (progressDialog != null && progressDialog.isShowing())
                        progressDialog.dismiss();
                    toast(returnMsg);
                    break;

            }
        }
    }



    class updateDeviceByFlagAsynTask extends AsyncTask<Map<String, Object>, Void, String> {

        @Override
        protected String doInBackground(Map<String, Object>... maps) {
            String code = "";
            Map<String, Object> prarms = maps[0];
            String result = HttpUtils.postOkHpptRequest(HttpUtils.ipAddress + "/app/device/updateDeviceByFlag", prarms);
            Log.e("back", "--->" + result);
            if (!ToastUtil.isEmpty(result)) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");
                    returnMsg = jsonObject.getString("returnMsg");
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
                    if (progressDialog != null && progressDialog.isShowing())
                        progressDialog.dismiss();
                    if(devicePayType==1) {
                        toast("解绑成功");
                        devicePayType=0;
                        btUserBd.setText("绑定");
                    }else {
                        toast("绑定成功");
                        devicePayType=1;
                        btUserBd.setText("解除绑定");
                    }

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

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer!=null)
            countDownTimer.cancel();

    }
}
