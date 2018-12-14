package ph.com.waterpurifer_distributor.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

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
import ph.com.waterpurifer_distributor.adapter.SearchAdapter;
import ph.com.waterpurifer_distributor.base.BaseActivity;
import ph.com.waterpurifer_distributor.fragment.MainFragment;
import ph.com.waterpurifer_distributor.pojo.SearchData;
import ph.com.waterpurifer_distributor.util.HttpUtils;
import ph.com.waterpurifer_distributor.util.ToastUtil;
import ph.com.waterpurifer_distributor.view.SpaceItemDecoration;

public class SearchActivity extends BaseActivity {
    @BindView(R.id.rv_search_data)
    RecyclerView rv_search_data;

    List<SearchData> searchDataLis = new ArrayList<>();
    SearchAdapter searchAdapter;
    @BindView(R.id.view_main_1)
    View viewMain1;
    @BindView(R.id.iv_serch_fh)
    ImageView ivSerchFh;
    @BindView(R.id.et_main_search)
    EditText etMainSearch;
    @BindView(R.id.iv_main_search)
    ImageView ivMainSearch;
    @BindView(R.id.head_title)
    RelativeLayout headTitle;

    String pass;

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindLayout() {
        setSteepStatusBar(true);
        return R.layout.activity_search;
    }

    @Override
    public void initView(View view) {
        progressDialog = new ProgressDialog(this);
        searchAdapter = new SearchAdapter(this, searchDataLis);
        rv_search_data.setLayoutManager(new LinearLayoutManager(this));
        rv_search_data.addItemDecoration(new SpaceItemDecoration(0, 35));
        rv_search_data.setAdapter(searchAdapter);


        pass =getSellerManagePassword();
        searchAdapter.SetOnItemClick(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (pass.length()>0){
                    Intent intent = new Intent(SearchActivity.this, PassWordActivity.class);
                    intent.putExtra("deviceMac",searchDataLis.get(position).getDeviceMac());
                    intent.putExtra("type",1);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(SearchActivity.this, UserdetailsActivity.class);
                    intent.putExtra("deviceMac",searchDataLis.get(position).getDeviceMac());
                    startActivity(intent);
                }

            }

            @Override
            public void onLongClick(View view, int posotion) {

            }
        });

    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @Override
    public void widgetClick(View v) {

    }

    @OnClick({R.id.iv_serch_fh, R.id.iv_main_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_serch_fh:
                finish();
                break;
            case R.id.iv_main_search:
                boolean isConn = true;
                if (isConn) {
                    showProgressDialog("正在加载，请稍后...");
                    Map<String, Object> params = new HashMap<>();
                    params.put("deviceSellerId", getSellerId());
                    params.put("searchText", etMainSearch.getText().toString());
                    new searchDeviceAsynTask().execute(params);
                } else {
                    toast( "无网络可用，请检查网络");
                }
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
            String result = HttpUtils.postOkHpptRequest(HttpUtils.ipAddress + "/app/device/searchDevice", prarms);
            Log.e("back", "--->" + result);
            if (!ToastUtil.isEmpty(result)) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");
                    returnMsg = jsonObject.getString("returnMsg");
//                    JSONObject returnData = jsonObject.getJSONObject("returnData");
                    if ("100".equals(code)) {
                        JsonObject content = new JsonParser().parse(result.toString()).getAsJsonObject();
                        JsonArray list = content.getAsJsonArray("returnData");
                        Gson gson = new Gson();
                        searchDataLis.clear();
                        for (int i = 0; i < list.size(); i++) {
                            //通过反射 得到UserBean.class
                            SearchData userList = gson.fromJson(list.get(i), SearchData.class);
                            searchDataLis.add(userList);
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
                    searchAdapter.notifyDataSetChanged();
                    break;
                default:
                    if (progressDialog != null && progressDialog.isShowing())
                        progressDialog.dismiss();
                    toast(returnMsg);
                    break;

            }
        }
    }

}
