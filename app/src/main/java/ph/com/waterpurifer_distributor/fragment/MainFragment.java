package ph.com.waterpurifer_distributor.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.view.menu.MenuAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import ph.com.waterpurifer_distributor.R;
import ph.com.waterpurifer_distributor.activity.LoginActivity;
import ph.com.waterpurifer_distributor.activity.MainActivity;
import ph.com.waterpurifer_distributor.activity.ManageActivity;
import ph.com.waterpurifer_distributor.activity.PassWordActivity;
import ph.com.waterpurifer_distributor.activity.SearchActivity;
import ph.com.waterpurifer_distributor.activity.UserdetailsActivity;
import ph.com.waterpurifer_distributor.adapter.MainAdapter;
import ph.com.waterpurifer_distributor.adapter.MemuAdapter;
import ph.com.waterpurifer_distributor.base.BaseFragment;
import ph.com.waterpurifer_distributor.pojo.DeviceListData;
import ph.com.waterpurifer_distributor.util.HttpUtils;
import ph.com.waterpurifer_distributor.util.ToastUtil;
import ph.com.waterpurifer_distributor.view.SpaceItemDecoration;

import static android.content.Context.MODE_PRIVATE;

public class MainFragment extends BaseFragment {
    @BindView(R.id.rv_main)
    RecyclerView rv_main;
    @BindView(R.id.rv_main_top)
    RecyclerView rv_main_top;
    SharedPreferences preferences;
    String pass;
    String [] title = {"全部","最近添加","租赁设备","非租赁设备","激活设备","未激活设备","剩余天数","剩余流量"};
    List<DeviceListData> deviceListData=new ArrayList<>();
    List<DeviceListData> allListData=new ArrayList<>();
    MainAdapter mainAdapter;
    @Override
    public int bindLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(View view) {
        preferences = getActivity().getSharedPreferences("my", MODE_PRIVATE);
        pass = preferences.getString("sellerManagePassword","");
        List<String> list1 = new ArrayList<>(Arrays.asList(title));
        mainAdapter = new MainAdapter(getActivity(),deviceListData);
        rv_main.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_main.addItemDecoration(new SpaceItemDecoration(0,32));
        rv_main.setAdapter(mainAdapter);
        mainAdapter.SetOnItemClick(new MainAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (pass.length()>0){
                    Intent intent = new Intent(getActivity(), PassWordActivity.class);
                    intent.putExtra("deviceMac",deviceListData.get(position).getDeviceMac());
                    intent.putExtra("type",1);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getActivity(), UserdetailsActivity.class);
                    intent.putExtra("deviceMac",deviceListData.get(position).getDeviceMac());
                    startActivity(intent);
                }

            }

            @Override
            public void onLongClick(View view, int posotion) {

            }
        });
        final MemuAdapter menuAdapter = new MemuAdapter(getActivity(),list1);
        LinearLayoutManager  layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_main_top.setLayoutManager(layoutManager);
        rv_main_top.setAdapter(menuAdapter);
        menuAdapter.SetOnItemClick(new MemuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int myposition = position;
                //传到适配器  （适配器调用方法）
                menuAdapter.getIndex(myposition);
                //刷新适配器
                menuAdapter.notifyDataSetChanged();

                switch(position){
                    case 0:
                        deviceListData.clear();
                        for (DeviceListData allListDatum : allListData) {
                            deviceListData.add(allListDatum);
                        }
                        mainAdapter.notifyDataSetChanged();
                        break;
                    case 1:
                        long timeStamp = System.currentTimeMillis();
                        deviceListData.clear();
                        for (DeviceListData allListDatum : allListData) {
                            if(timeStamp-allListDatum.getDeviceCreatTime()<7*24*60*60*1000)
                            deviceListData.add(allListDatum);
                        }
                        mainAdapter.notifyDataSetChanged();
                        break;
                    case 2:
                        deviceListData.clear();
                        for (DeviceListData allListDatum : allListData) {
                            if(allListDatum.getDeviceType()!=4)
                            deviceListData.add(allListDatum);
                        }
                        mainAdapter.notifyDataSetChanged();
                        break;
                    case 3:
                        deviceListData.clear();
                        for (DeviceListData allListDatum : allListData) {
                            if(allListDatum.getDeviceType()==4)
                            deviceListData.add(allListDatum);
                        }
                        mainAdapter.notifyDataSetChanged();
                        break;
                    case 4:
                        deviceListData.clear();
                        for (DeviceListData allListDatum : allListData) {
                            if (allListDatum.getDeviceUserId() != 0&&allListDatum.getDeviceFlag() == 1)
                            deviceListData.add(allListDatum);
                        }
                        mainAdapter.notifyDataSetChanged();
                        break;
                    case 5:
                        deviceListData.clear();
                        for (DeviceListData allListDatum : allListData) {
                            if (allListDatum.getDeviceUserId() == 0||allListDatum.getDeviceFlag()== 0)
                                deviceListData.add(allListDatum);
                        }
                        mainAdapter.notifyDataSetChanged();
                        break;

                    case 6:
                        deviceListData.clear();
                        for (DeviceListData allListDatum : allListData) {
                                if(allListDatum.getDeviceLeaseType()==2)
                                deviceListData.add(allListDatum);
                        }

                        Collections.sort(deviceListData,comparator);
                        mainAdapter.notifyDataSetChanged();
                        break;

                    case 7:
                        deviceListData.clear();
                        for (DeviceListData allListDatum : allListData) {
                            if(allListDatum.getDeviceLeaseType()==1|| allListDatum.getDeviceLeaseType()==3)
                                deviceListData.add(allListDatum);
                        }

                        Collections.sort(deviceListData,comparator);
                        mainAdapter.notifyDataSetChanged();
                        break;
                }

            }

            @Override
            public void onLongClick(View view, int posotion) {

            }
        });

        final RefreshLayout refreshLayout =view. findViewById(R.id.refreshLayout);

            refreshLayout.setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefresh(@NonNull RefreshLayout refreshLayout) {

                    refreshLayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败

                }
            });

        progressDialog = new ProgressDialog(getContext());


    }


    @Override
    public void doBusiness(Context mContext) {

        boolean isConn=true;
        if (isConn){
            showProgressDialog("正在加载，请稍后...");
            Map<String, Object> params = new HashMap<>();
            params.put("deviceUserId",((MainActivity)getActivity()).getUserId());
            params.put("roleFlag", 2);
            new getDeviceListAsynTask().execute(params);
        }else {
            ToastUtil.showShort(getActivity(), "无网络可用，请检查网络");
        }

    }

    @Override
    public void widgetClick(View v) {

    }
    @OnClick({R.id.et_main_search
    })
    public void onClick(View view){
        switch (view.getId()){
            case R.id.et_main_search:
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;

        }
    }








    private ProgressDialog progressDialog;
    //显示dialog
    public void showProgressDialog(String message) {

        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();

    }



    String returnMsg;
    class getDeviceListAsynTask extends AsyncTask<Map<String,Object>,Void,String> {

        @Override
        protected String doInBackground(Map<String, Object>... maps) {
            String code = "";
            Map<String, Object> prarms = maps[0];
            String result = HttpUtils.postOkHpptRequest(HttpUtils.ipAddress + "/app/device/getDeviceList", prarms);
            Log.e("back", "--->" + result);
            if (!ToastUtil.isEmpty(result)) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");
                    returnMsg=jsonObject.getString("returnMsg");
//                    JSONObject returnData = jsonObject.getJSONObject("returnData");
                    if ("100".equals(code)) {
                        JsonObject content = new JsonParser().parse(result.toString()).getAsJsonObject();
                        JsonArray list = content.getAsJsonArray("returnData");
                        Gson gson = new Gson();
                        deviceListData.clear();
                        allListData.clear();
                        for (int i = 0; i < list.size(); i++) {
                            //通过反射 得到UserBean.class
                            DeviceListData userList = gson.fromJson(list.get(i), DeviceListData.class);
                            deviceListData.add(userList);
                            allListData.add(userList);
                        }
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
                    mainAdapter.notifyDataSetChanged();
                    break;
                default:
                    if (progressDialog != null && progressDialog.isShowing())
                        progressDialog.dismiss();
                    ToastUtil.showShort(getActivity(), returnMsg);
                    break;

            }
        }
    }

    Comparator<DeviceListData> comparator = new Comparator<DeviceListData>() {
        public int compare(DeviceListData s1, DeviceListData s2) {
            // 先排年龄
            if (s1.getDeviceNum() != s2.getDeviceNum()) {
                return s2.getDeviceNum() - s1.getDeviceNum();
            } else  {
                // 年龄相同则按姓名排序
                return (s1.getDeviceCreatTime()+"").compareTo((s2.getDeviceCreatTime()+""));
            }
        }
    };
}
