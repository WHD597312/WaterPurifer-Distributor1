package ph.com.waterpurifer_distributor.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import ph.com.waterpurifer_distributor.R;
import ph.com.waterpurifer_distributor.base.BaseActivity;
import ph.com.waterpurifer_distributor.view.VerticalProgressBar;


public class LvTypeActivity extends BaseActivity {

    @BindView(R.id.vp_progress1)
    VerticalProgressBar vp_progress1;
    @BindView(R.id.vp_progress2)
    VerticalProgressBar vp_progress2;
    @BindView(R.id.vp_progress3)
    VerticalProgressBar vp_progress3;
    @BindView(R.id.vp_progress4)
    VerticalProgressBar vp_progress4;
    @BindView(R.id.vp_progress5)
    VerticalProgressBar vp_progress5;
    @BindView(R.id.tv_progress1)
    TextView tv_progress1;
    @BindView(R.id.tv_progress2)
    TextView tv_progress2;
    @BindView(R.id.tv_progress3)
    TextView tv_progress3;
    @BindView(R.id.tv_progress4)
    TextView tv_progress4;
    @BindView(R.id.tv_progress5)
    TextView tv_progress5;
    @Override
    public void initParms(Bundle parms) {


    }

    @Override
    public int bindLayout() {
        setSteepStatusBar(true);
        return R.layout.activity_lvtype;
    }

    @Override
    public void initView(View view) {
        Intent intent=getIntent();
        int[] wPurifierfilter=intent.getIntArrayExtra("wPurifierfilter");
        vp_progress1.setProgress(wPurifierfilter[0]);
        vp_progress2.setProgress(wPurifierfilter[1]);
        vp_progress3.setProgress(wPurifierfilter[2]);
        vp_progress4.setProgress(wPurifierfilter[3]);
        vp_progress5.setProgress(wPurifierfilter[4]);
        tv_progress1.setText(wPurifierfilter[0]+"");
        tv_progress2.setText(wPurifierfilter[1]+"");
        tv_progress3.setText(wPurifierfilter[2]+"");
        tv_progress4.setText(wPurifierfilter[3]+"");
        tv_progress5.setText(wPurifierfilter[4]+"");

    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @Override
    public void widgetClick(View v) {

    }
  @OnClick({R.id.iv_main_memu})
public  void  onClick(View view){
        switch (view.getId()){
            case R.id.iv_main_memu:
                finish();
                break;
        }
  }



}
