package ph.com.waterpurifer_distributor.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ph.com.waterpurifer_distributor.R;
import ph.com.waterpurifer_distributor.adapter.ClickViewPageAdapter;
import ph.com.waterpurifer_distributor.adapter.MainAdapter;
import ph.com.waterpurifer_distributor.base.BaseActivity;
import ph.com.waterpurifer_distributor.base.BaseFragment;
import ph.com.waterpurifer_distributor.base.MyApplication;
import ph.com.waterpurifer_distributor.fragment.MainFragment;
import ph.com.waterpurifer_distributor.fragment.MyFragment;
import ph.com.waterpurifer_distributor.fragment.XqRepairFragment;
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
        initView();
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @Override
    public void widgetClick(View v) {

    }

    ClickViewPageAdapter tabAdapter;
    private void initView() {
        circle.add("首页");
        circle.add("维修");
        circle.add("我的");
        fragmentList.add(new MainFragment());
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
}
