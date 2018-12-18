package ph.com.waterpurifer_distributor.activity;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import ph.com.waterpurifer_distributor.R;
import ph.com.waterpurifer_distributor.base.BaseActivity;
import ph.com.waterpurifer_distributor.base.MyApplication;
import ph.com.waterpurifer_distributor.util.HttpUtils;
import ph.com.waterpurifer_distributor.util.Mobile;
import ph.com.waterpurifer_distributor.util.NetWorkUtil;
import ph.com.waterpurifer_distributor.util.ToastUtil;
import ph.com.waterpurifer_distributor.util.view.ScreenSizeUtils;


public class LoginActivity extends BaseActivity {
    MyApplication application;
    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.et_pswd)
    EditText et_pswd;
    @BindView(R.id.image_seepwd)
    ImageView image_seepwd;
    SharedPreferences preferences;
    boolean isHideFirst;
    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindLayout() {
        setSteepStatusBar(true);
        return R.layout.activity_login;
    }

    @Override
    public void initView(View view) {
        if (application == null) {
            application = (MyApplication) getApplication();
        }
        application.addActivity(this);
        preferences = getSharedPreferences("my", MODE_PRIVATE);

        progressDialog = new ProgressDialog(this);
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @Override
    public void widgetClick(View v) {


    }
    @OnClick({R.id.btn_login,  R.id.tv_forget_pswd, R.id.image_seepwd})
    public void onClick(View view){
        switch (view.getId()) {


//                ToastUtil.showShort(this,"注册");



            case R.id.btn_login:
                String phone = et_name.getText().toString();
                String password = et_pswd.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    toast("账号码不能为空");
                    break;
                }/* else if (!Mobile.isMobile(phone)) {
                    toast( "手机号码不合法");
                    break;
                }*/
                if (TextUtils.isEmpty(password)) {
                    ToastUtil.showShort(this, "请输入密码");
                    break;
                }
                boolean isConn = NetWorkUtil.isConn(MyApplication.getContext());
                if (isConn){
                    showProgressDialog("正在登录，请稍后...");
                    Map<String, Object> params = new HashMap<>();
                    params.put("phone", phone);
                    params.put("password", password);
                    new LoginAsynTask().execute(params);
                }else {
                    ToastUtil.showShort(this, "无网络可用，请检查网络");
                }

                break;
            case R.id.tv_forget_pswd:
                ShareDialog();
                break;
            case R.id.image_seepwd:

                if (isHideFirst) {
                    image_seepwd.setImageResource(R.mipmap.login_see);
                    //密文
                    HideReturnsTransformationMethod method1 = HideReturnsTransformationMethod.getInstance();
                    et_pswd.setTransformationMethod(method1);
                    isHideFirst = false;
                } else {
                    image_seepwd.setImageResource(R.mipmap.login_seeno);
                    //密文
                    TransformationMethod method = PasswordTransformationMethod.getInstance();
                    et_pswd.setTransformationMethod(method);
                    isHideFirst = true;

                }
                // 光标的位置
                int index = et_pswd.getText().toString().length();
                et_pswd.setSelection(index);
                break;

        }

    }
    /**
     * 自定义对话框
     */
    private void ShareDialog() {
        final Dialog dialog = new Dialog(this, R.style.MyDialog);
        View view = View.inflate(this, R.layout.dialog_forgtpassword, null);
        dialog.setContentView(view);
        //使得点击对话框外部不消失对话框
        dialog.setCanceledOnTouchOutside(true);
        //设置对话框的大小
        view.setMinimumHeight((int) (ScreenSizeUtils.getInstance(this).getScreenHeight() * 0.23f));
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (ScreenSizeUtils.getInstance(this).getScreenWidth() * 0.75f);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialogWindow.setAttributes(lp);
        dialog.show();
    }



    @Override
    protected void onStart() {
        super.onStart();

        if (preferences.contains("phone") && !preferences.contains("password")) {
            String phone = preferences.getString("phone", "");
            et_name.setText(phone);
            et_pswd.setText("");
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    private ProgressDialog progressDialog;
    //显示dialog
    public void showProgressDialog(String message) {

        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();

    }



    String returnMsg;
    class LoginAsynTask extends AsyncTask<Map<String,Object>,Void,String> {

        @Override
        protected String doInBackground(Map<String, Object>... maps) {
            String code = "";
            Map<String, Object> prarms = maps[0];
            String result = HttpUtils.postOkHpptRequest(HttpUtils.ipAddress + "/app/seller/login", prarms);
            Log.e("back", "--->" + result);
            if (!ToastUtil.isEmpty(result)) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");
                    returnMsg=jsonObject.getString("returnMsg");
//                    JSONObject returnData = jsonObject.getJSONObject("returnData");
                    if ("100".equals(code)) {
                        JSONObject returnData = jsonObject.getJSONObject("returnData");
                        int sellerId = returnData.getInt("sellerId");
                        int sellerRole = returnData.getInt("sellerRole");
                        int sellerFlag = returnData.getInt("sellerFlag");
                        String sellerCoName = returnData.getString("sellerCoName");
                        String sellerName = returnData.getString("sellerName");
                        String sellerPhone = returnData.getString("sellerPhone");
                        String sellerPassword = returnData.getString("sellerPassword");
                        String sellerManagePassword = returnData.getString("sellerManagePassword");
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putInt("sellerId", sellerId);
                        editor.putInt("sellerRole", sellerRole);
                        editor.putInt("sellerFlag", sellerFlag);
                        editor.putString("sellerCoName", sellerCoName);
                        editor.putString("sellerName", sellerName);
                        editor.putString("sellerPhone", sellerPhone);
                        editor.putString("sellerPassword", sellerPassword);
                        editor.putString("sellerManagePassword", sellerManagePassword);
                        editor.commit();

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
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
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
}
