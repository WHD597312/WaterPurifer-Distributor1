package ph.com.waterpurifer_distributor.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.OnClick;
import ph.com.waterpurifer_distributor.R;
import ph.com.waterpurifer_distributor.base.BaseActivity;
import ph.com.waterpurifer_distributor.base.MyApplication;
import ph.com.waterpurifer_distributor.util.ToastUtil;

public class PassWordActivity extends BaseActivity {
    SharedPreferences preferences;
    MyApplication application;
    @BindView(R.id.et_man_pass)
    EditText et_man_pass;
    String passWord;
    int type;
    String deviceMac;
    @Override
    public void initParms(Bundle parms) {
        deviceMac=parms.getString("deviceMac");
    }

    @Override
    public int bindLayout() {
        setSteepStatusBar(true);
        return R.layout.activity_password;
    }

    @Override
    public void initView(View view) {
        if (application == null) {
            application = (MyApplication) getApplication();
        }
        application.addActivity(this);
      preferences =getSharedPreferences("my",MODE_PRIVATE);
        passWord= preferences.getString("sellerManagePassword","");
        type=getIntent().getIntExtra("type",0);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

          finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @Override
    public void widgetClick(View v) {

    }
    @OnClick({R.id.iv_back,R.id.bt_pass_qd})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_back:
//                if (type==1){
//                    Intent intent = new Intent(this,MainActivity.class);
//                    intent.putExtra("type",0);
//                    startActivity(intent);
//                }
//                if (type==2){
//                    Intent intent = new Intent(this,MainActivity.class);
//                    intent.putExtra("type",2);
//                    startActivity(intent);
//                }
                finish();
                break;
            case R.id.bt_pass_qd:
                if (!et_man_pass.getText().toString().equals(passWord)){
                    ToastUtil.showShort(this,"密码错误请重新输入");
                }else {
                  switch (type){
                      case 1:
                          //查看用户信息
                          
                          application.removeActivity(this);
                          Intent intent = new Intent(this, UserdetailsActivity.class);
                          intent.putExtra("deviceMac",deviceMac);
                          startActivity(intent);
                          break;

                      case 2:
                          //修改密码
                          application.removeActivity(this);
                          startActivity(ManageActivity.class);
                          break;
                  }
                }

                break;
        }
    }
}
