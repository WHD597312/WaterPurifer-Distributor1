package ph.com.waterpurifer_distributor.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ph.com.waterpurifer_distributor.R;
import ph.com.waterpurifer_distributor.adapter.ClickViewPageAdapter;
import ph.com.waterpurifer_distributor.adapter.MainAdapter;
import ph.com.waterpurifer_distributor.base.BaseActivity;
import ph.com.waterpurifer_distributor.base.BaseFragment;
import ph.com.waterpurifer_distributor.base.MyApplication;
import ph.com.waterpurifer_distributor.database.dao.daoImp.EquipmentImpl;
import ph.com.waterpurifer_distributor.fragment.MainFragment;
import ph.com.waterpurifer_distributor.fragment.MyFragment;
import ph.com.waterpurifer_distributor.fragment.XqRepairFragment;
import ph.com.waterpurifer_distributor.pojo.DeviceListData;
import ph.com.waterpurifer_distributor.pojo.Equipment;
import ph.com.waterpurifer_distributor.util.HttpUtils;
import ph.com.waterpurifer_distributor.util.ToastUtil;
import ph.com.waterpurifer_distributor.util.mqtt.MQTTMessageReveiver;
import ph.com.waterpurifer_distributor.view.NoSrcollViewPage;
import ph.com.waterpurifer_distributor.view.SpaceItemDecoration;

public class MainActivity extends BaseActivity {
    MyApplication application;
    @BindView(R.id.vp_flower)
    NoSrcollViewPage vp_flower;
    @BindView(R.id.tl_flower)
    TabLayout tl_flower;
    List<String> circle = new ArrayList<>();
    List<BaseFragment> fragmentList = new ArrayList<>();
    private EquipmentImpl equipmentImpl;

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindLayout() {
        setSteepStatusBar(true);
        return R.layout.activity_mypaper;
    }

    @Override
    public void initView(View view) {
        if (application == null) {
            application = (MyApplication) getApplication();
        }
        application.addActivity(this);
        equipmentImpl=new EquipmentImpl(getApplicationContext());
        initView();
    }

    @Override
    public void doBusiness(Context mContext) {
        progressDialog = new ProgressDialog(this);
        boolean isConn=true;
        if (isConn){
            showProgressDialog("正在加载，请稍后...");
            Map<String, Object> params = new HashMap<>();
            params.put("deviceUserId",getUserId());
            params.put("roleFlag", 2);
            new getDeviceListAsynTask().execute(params);
        }else {
            toast( "无网络可用，请检查网络");
        }
    }

    @Override
    public void widgetClick(View v) {

    }

    ClickViewPageAdapter tabAdapter;
    MainFragment mainFragment;
    private void initView() {
        circle.add("首页");
        circle.add("维修");
        circle.add("我的");
        mainFragment=new MainFragment();
        fragmentList.add(mainFragment);
        fragmentList.add(new XqRepairFragment());
        fragmentList.add(new MyFragment());
        tabAdapter = new ClickViewPageAdapter(getSupportFragmentManager(), fragmentList, this);
        vp_flower.setAdapter(tabAdapter);
        tl_flower.setupWithViewPager(vp_flower);
        for (int i = 0; i < circle.size(); i++) {
            TabLayout.Tab tab = tl_flower.getTabAt(i);
            //注意！！！这里就是添加我们自定义的布局
            tab.setCustomView(tabAdapter.getCustomView(i));
            //这里是初始化时，默认item0被选中，setSelected（true）是为了给图片和文字设置选中效果，代码在文章最后贴出
//                ((ImageView) tab.getCustomView().findViewById(R.id.tab_iv)).setSelected(true);
//                ((TextView) tab.getCustomView().findViewById(R.id.tab_tv)).setTextColor(Color.parseColor("#33c62b"));
        }


        tl_flower.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//              选择时候调用
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //没有选择时候调用
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

}

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {


            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出经销商净水器",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
                return false;
            } else {
                application.removeAllActivity();
                return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    private long exitTime = 0;

    public int getUserId(){
        return getSellerId();
    }


















    private ProgressDialog progressDialog;
    //显示dialog
    public void showProgressDialog(String message) {

        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();

    }




    String returnMsg;
    List<DeviceListData> allListData=new ArrayList<>();
    class getDeviceListAsynTask extends AsyncTask<Map<String,Object>,Void,String> {

        @Override
        protected String doInBackground(Map<String, Object>... maps) {
            String code = "";
            Map<String, Object> prarms = maps[0];
            String result = HttpUtils.postOkHpptRequest(HttpUtils.ipAddress + "/app/device/getDeviceList", prarms);
            Log.e("back", "--->" + result);
            if (!ToastUtil.isEmpty(result)) {
                try {
                    equipmentImpl.deleteAll();
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");
                    returnMsg=jsonObject.getString("returnMsg");
//                    JSONObject returnData = jsonObject.getJSONObject("returnData");
                    if ("100".equals(code)) {
                        JsonObject content = new JsonParser().parse(result.toString()).getAsJsonObject();
                        JsonArray list = content.getAsJsonArray("returnData");
                        Gson gson = new Gson();
                        allListData.clear();
                        for (int i = 0; i < list.size(); i++) {
                            //通过反射 得到UserBean.class
                            DeviceListData userList = gson.fromJson(list.get(i), DeviceListData.class);
                            allListData.add(userList);
                            Equipment equipment=new Equipment();
                            equipment.setId(userList.getDeviceId());
                            equipment.setDeviceMac(userList.getDeviceMac());
                            equipment.setDeviceUserId(userList.getDeviceUserId());
                            equipment.setName(userList.getDeviceName());
                            equipmentImpl.insert(equipment);
                        }
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
                    if (mainFragment.isShow())
                        mainFragment.initData();
                    IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
                    filter.addAction("mqttmessage2");
                    myReceiver = new MQTTMessageReveiver();
                    MainActivity.this.registerReceiver(myReceiver, filter);
                    break;
                default:
                    if (progressDialog != null && progressDialog.isShowing())
                        progressDialog.dismiss();
                    toast( returnMsg);
                    break;

            }
        }
    }


    public List<DeviceListData> getAllListData() {
        return allListData;
    }



    MQTTMessageReveiver myReceiver;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myReceiver != null) {
            unregisterReceiver(myReceiver);
        }
    }
}
