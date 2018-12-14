package ph.com.waterpurifer_distributor.activity;

import android.content.Context;
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
        vp_progress1.setProgress(1);
        vp_progress2.setProgress(2);
        vp_progress3.setProgress(3);
        vp_progress4.setProgress(5);
        vp_progress5.setProgress(10);
        tv_progress1.setText("1");
        tv_progress2.setText("50");
        tv_progress3.setText("70");
        tv_progress4.setText("94");
        tv_progress5.setText("100");

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
