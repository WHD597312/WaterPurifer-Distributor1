package ph.com.waterpurifer_distributor.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import ph.com.waterpurifer_distributor.R;
import ph.com.waterpurifer_distributor.adapter.JournalAdapter;
import ph.com.waterpurifer_distributor.base.BaseActivity;
import ph.com.waterpurifer_distributor.base.MyApplication;
import ph.com.waterpurifer_distributor.pojo.DeviceListData;
import ph.com.waterpurifer_distributor.pojo.JournalData;
import ph.com.waterpurifer_distributor.util.HttpUtils;
import ph.com.waterpurifer_distributor.util.ToastUtil;
import ph.com.waterpurifer_distributor.volley.VolleyListenerInterface;
import ph.com.waterpurifer_distributor.volley.VolleyRequestUtil;

public class JournalActivity extends BaseActivity {
    MyApplication application;
    @BindView(R.id.rv_day_time)
    RecyclerView rv_day_time;
    @BindView(R.id.tv_day_time)
    TextView tv_day_time;
    private TimePickerView pvCustomTime;
    List<JournalData> journalData = new ArrayList<>();
    JournalAdapter journalAdapter;
    String deviceMac;

    @Override
    public void initParms(Bundle parms) {
        deviceMac = parms.getString("deviceMac");
    }

    @Override
    public int bindLayout() {
        setSteepStatusBar(true);
        return R.layout.activity_daylist;
    }

    @Override
    public void initView(View view) {
        if (application == null) {
            application = (MyApplication) getApplication();
        }
        application.addActivity(this);
        initCustomTimePicker();
        journalAdapter = new JournalAdapter(this, journalData);
        rv_day_time.setLayoutManager(new LinearLayoutManager(this));
        rv_day_time.setAdapter(journalAdapter);

        progressDialog = new ProgressDialog(this);
    }

    @Override
    public void doBusiness(Context mContext) {

        boolean isConn = true;
        if (isConn) {
            showProgressDialog("正在加载，请稍后...");
            new getDeviceListAsynTask().execute();
        } else {
            toast("无网络可用，请检查网络");
        }
    }

    @Override
    public void widgetClick(View v) {

    }

    @OnClick({R.id.iv_main_memu, R.id.rl_day_time})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_main_memu:
                finish();
                break;

            case R.id.rl_day_time:
                pvCustomTime.show();

                break;

        }
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }


    private void initCustomTimePicker() {

        /**
         * @description
         *
         * 注意事项：
         * 1.自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针.
         * 具体可参考demo 里面的两个自定义layout布局。
         * 2.因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
         * setRangDate方法控制起始终止时间(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
         */
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(2014, 1, 23);
        Calendar endDate = Calendar.getInstance();
        endDate.set(selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DAY_OF_MONTH));
        //时间选择器 ，自定义布局
        pvCustomTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                tv_day_time.setText(getTime(date));
                new getDeviceListAsynTask().execute();
            }
        })
                /*.setType(TimePickerView.Type.ALL)//default is all
                .setCancelText("Cancel")
                .setSubmitText("Sure")
                .setContentTextSize(18)
                .setTitleSize(20)
                .setTitleText("Title")
                .setTitleColor(Color.BLACK)
               /*.setDividerColor(Color.WHITE)//设置分割线的颜色
                .setTextColorCenter(Color.LTGRAY)//设置选中项的颜色
                .setLineSpacingMultiplier(1.6f)//设置两横线之间的间隔倍数
                .setTitleBgColor(Color.DKGRAY)//标题背景颜色 Night mode
                .setBgColor(Color.BLACK)//滚轮背景颜色 Night mode
                .setSubmitColor(Color.WHITE)
                .setCancelColor(Color.WHITE)*/
                /*.animGravity(Gravity.RIGHT)// default is center*/
                .setTitleBgColor(Color.WHITE)
                .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.pickerview_custom_time, new CustomListener() {

                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        TextView ivCancel = (TextView) v.findViewById(R.id.iv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.returnData();
                                pvCustomTime.dismiss();
                            }
                        });
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.dismiss();
                            }
                        });
                    }
                })
                .setContentTextSize(18)
                .setDividerColor(Color.LTGRAY)//设置分割线的颜色
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("年", "月", "日", "时", "分", "秒")
                .setLineSpacingMultiplier(1.2f)
                .setTextXOffset(0, 0, 0, 0, 0, 0)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .build();

    }


    private ProgressDialog progressDialog;

    //显示dialog
    public void showProgressDialog(String message) {

        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();

    }


    String returnMsg;

    class getDeviceListAsynTask extends AsyncTask<Map<String, Object>, Void, String> {

        @Override
        protected String doInBackground(Map<String, Object>... maps) {
            String code = "";

            String result = ph.com.waterpurifer_distributor.util.http.HttpUtils.requestGet(HttpUtils.ipAddress + "/data/searchDeviceData?deviceMac=" + deviceMac + "&faultDate=" + tv_day_time.getText().toString());
            Log.e("qqqqBB", "data/searchDeviceData?deviceMac=" + deviceMac + "&faultDate=" + tv_day_time.getText().toString());
            Log.e("back", "--->" + result);
            if (!ToastUtil.isEmpty(result)) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("returnCode");
                    returnMsg = jsonObject.getString("returnMsg");
//                    JSONObject returnData = jsonObject.getJSONObject("returnData");
                    if ("100".equals(code)) {
                        journalData.clear();
                        JsonObject content = new JsonParser().parse(result.toString()).getAsJsonObject();
                        JsonArray list = content.getAsJsonArray("returnData");
                        Gson gson = new Gson();
                        journalData.clear();
                        for (int i = 0; i < list.size(); i++) {
                            //通过反射 得到UserBean.class
                            JournalData userList = gson.fromJson(list.get(i), JournalData.class);
                            journalData.add(userList);
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
                    journalAdapter.notifyDataSetChanged();
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
