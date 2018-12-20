package ph.com.waterpurifer_distributor.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
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
import ph.com.waterpurifer_distributor.R;
import ph.com.waterpurifer_distributor.adapter.xqRepairAdapter;
import ph.com.waterpurifer_distributor.base.BaseFragment;
import ph.com.waterpurifer_distributor.base.MyApplication;
import ph.com.waterpurifer_distributor.pojo.RepireList;
import ph.com.waterpurifer_distributor.util.NetWorkUtil;
import ph.com.waterpurifer_distributor.util.SharedPreferencesHelper;
import ph.com.waterpurifer_distributor.util.ToastUtil;
import ph.com.waterpurifer_distributor.util.http.HttpUtils;
import ph.com.waterpurifer_distributor.view.SpaceItemDecoration;


public class XqRepairFragment extends BaseFragment {
    @BindView(R.id.rv_xqrepair)
    RecyclerView rv_xqrepair;
    List <RepireList> repireLists ;
    xqRepairAdapter xqRepairAdapter;
    RefreshLayout refreshLayou;
    int pageNum = 1;
    int pos  ;
    int flag;
    int sellerId;
    @Override
    public int bindLayout() {
        return R.layout.activity_xqrepair;
    }

    @Override
    public void initView(View view) {
        repireLists = new ArrayList<>();
        SharedPreferencesHelper sharedPreferencesHelper=new SharedPreferencesHelper(getActivity(),"my");
        sellerId= (int) sharedPreferencesHelper.getSharedPreference("sellerId",0);
//        getRepairList();
        refreshLayou =view. findViewById(R.id.refreshLayout_xq);

        refreshLayou.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {

                boolean isConn = NetWorkUtil.isConn(MyApplication.getContext());
                if (isConn){
                    repireLists.clear();
                    Log.e("FFFFSDDD", "onRefresh: -->"+repireLists.size() );
                    pageNum=1;
                    getRepairList();
                }else {
                    ToastUtil.showShort( getActivity(),"无网络可用，请检查网络");
                }

            }

        });

        refreshLayou.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                boolean isConn = NetWorkUtil.isConn(MyApplication.getContext());
                if (isConn){
                    refreshLayout.finishLoadMore(5000,false,false);
                    pageNum++;
                    getRepairList();
                }else {
                    ToastUtil.showShort( getActivity(),"无网络可用，请检查网络");
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        getRepairList();
    }

    public void GetData(Map<String,Object>  param){
        setRepairTypeAsyncTask = new SetRepairTypeAsyncTask();
        setRepairTypeAsyncTask.execute(param);
        new Thread(){

            public void run() {
                try {
                    setRepairTypeAsyncTask.get(5, TimeUnit.SECONDS);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                    Message message = new Message();
                    message.obj="TimeOut";
                    handler.sendMessage(message);
                }
            }
        }.start();

    }
    /*
    * 获取维修列表分页获取
    * */
    public void getRepairList (){
         getRepairListAsyncTask = new GetRepairListAsyncTask();
         getRepairListAsyncTask.execute();
         new Thread(){

             public void run() {
                 try {
                     getRepairListAsyncTask.get(5,TimeUnit.SECONDS);
                 } catch (InterruptedException | ExecutionException e) {
                     e.printStackTrace();
                 } catch (TimeoutException e) {
                     e.printStackTrace();
                     Message message = new Message();
                     message.obj="TimeOut";
                     handler.sendMessage(message);
                 }
             }
         };

    }

    @Override
    public void doBusiness(Context mContext) {
        xqRepairAdapter = new xqRepairAdapter(getActivity(),repireLists);
        rv_xqrepair.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_xqrepair.addItemDecoration(new SpaceItemDecoration(0,35));
        rv_xqrepair.setAdapter(xqRepairAdapter);
        xqRepairAdapter.SetOnItemClick(new  xqRepairAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                flag = repireLists.get(position).getRepairFlag();
                pos = position;
                if (flag==0){
                    flag=1;
                    int repairId =  repireLists.get(position).getRepairId();
                    Map <String,Object> param = new HashMap<>();
                    param.put("repairId",repairId);
                    param.put("repairFlag",1);
                    GetData(param);
                }else if (flag==1){
                    flag=2;
                    int repairId =  repireLists.get(position).getRepairId();
                    Map <String,Object> param = new HashMap<>();
                    param.put("repairId",repairId);
                    param.put("repairFlag",2);
                    GetData(param);
                }


            }

            @Override
            public void onLongClick(View view, int posotion) {

            }
        });

    }

    @Override
    public void widgetClick(View v) {

    }

    @OnClick({})
    public void onClick(View view){
        switch (view.getId()){

        }
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if ("TimeOut".equals(msg)){
                ToastUtil.showShort(getActivity(),"请求超时，请重试。。。");
            }
        }
    };
    GetRepairListAsyncTask getRepairListAsyncTask;
    class GetRepairListAsyncTask extends AsyncTask  <Void,Void,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            String code = "";
//            String result = HttpUtils.getOkHpptRequest(HttpUtils.ipAddress+"/app/user/getRepairList?sellerId=1");
            String result=  HttpUtils.requestGet( HttpUtils.baseUrl+"app/user/getRepairList?sellerId="+sellerId+"&pageNum="+pageNum);
            Log.e("result", "doInBackground: --》"+result );
            try {
                if (!TextUtils.isEmpty(result)){
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");
                    JSONArray jsonArray = jsonObject.getJSONArray("returnData");
                    for (int i =0;i<jsonArray.length();i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        int repairId = jsonObject1.getInt("repairId");
                        String repairDeviceMac = jsonObject1.getString("repairDeviceMac");
                        String repairDeviceType = jsonObject1.getString("repairDeviceType");
                        String repairTime = jsonObject1.getString("repairTime");
                        String repairAddress = jsonObject1.getString("repairAddress");
                        String repairDesc = jsonObject1.getString("repairDesc");
                        String repairPhone = jsonObject1.getString("repairPhone");
                        int repairCreatorId =jsonObject1.getInt("repairCreatorId");
                        long repairCreatTime = jsonObject1.getLong("repairCreatTime");
                        int repairFlag = jsonObject1.getInt("repairFlag");
                        RepireList repireList = new RepireList();
                        repireList.setRepairId(repairId);
                        repireList.setRepairDeviceMac(repairDeviceMac);
                        repireList.setRepairDeviceType(repairDeviceType);
                        repireList.setRepairTime(repairTime);
                        repireList.setRepairAddress(repairAddress);
                        repireList.setRepairDesc(repairDesc);
                        repireList.setRepairPhone(repairPhone);
                        repireList.setRepairCreatorId(repairCreatorId);
                        repireList.setRepairCreatTime(repairCreatTime);
                        repireList.setRepairFlag(repairFlag);
                        repireLists.add(repireList);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return code;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            switch (s){
                case "100":
                    Log.e("FFFFSDDD", "onRefresh: -->"+repireLists.size() );
                    refreshLayou.finishLoadMore(true);
                    refreshLayou.finishRefresh(true);
                    xqRepairAdapter.Refrash(repireLists);
                    xqRepairAdapter.notifyDataSetChanged();
                    break;

                case "20010":
                    refreshLayou.finishLoadMore(false);
                    ToastUtil.showShort(getActivity(),"没有更多的报修信息");
                    break;

                case "20009":
                    refreshLayou.finishRefresh(false);
                    ToastUtil.showShort(getActivity(),"暂时无报修设备");
                    break;
            }
        }
    }
    SetRepairTypeAsyncTask setRepairTypeAsyncTask;
    class SetRepairTypeAsyncTask extends AsyncTask<Map<String,Object>,Void,String>{


        @Override
        protected String doInBackground(Map<String, Object>... maps) {
            String code = "";
            Map<String,Object> params = maps[0];
            String result = HttpUtils.requestPost(HttpUtils.baseUrl+"app/user/updateRepair",params);

                try {
                    if (!TextUtils.isEmpty(result)){
                    JSONObject jsonObject = new JSONObject(result);
                     code = jsonObject.getString("returnCode");
                     if ("100".equals(code)){
                         RepireList repireList = repireLists.get(pos);
                         repireList.setRepairFlag(flag);
                         repireLists.set(pos,repireList);
                     }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            return code;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            switch (s){
                case "100":
                    xqRepairAdapter.Refrash(repireLists);
                    xqRepairAdapter.notifyDataSetChanged();
                    break;

                    default:
                        ToastUtil.showShort(getActivity(),"处理失败，请重试");
                        break;
            }
        }
    }

}

