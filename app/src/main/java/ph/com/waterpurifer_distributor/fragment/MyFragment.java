package ph.com.waterpurifer_distributor.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ph.com.waterpurifer_distributor.R;
import ph.com.waterpurifer_distributor.activity.AddDeviceActivity;
import ph.com.waterpurifer_distributor.activity.ManageActivity;
import ph.com.waterpurifer_distributor.activity.PassWordActivity;
import ph.com.waterpurifer_distributor.activity.SettingActivity;
import ph.com.waterpurifer_distributor.activity.UserActivity;
import ph.com.waterpurifer_distributor.base.BaseFragment;
import ph.com.waterpurifer_distributor.util.SharedPreferencesHelper;

import static android.content.Context.MODE_PRIVATE;

public class MyFragment extends BaseFragment {
    SharedPreferences preferences;
    String pass;
    @BindView(R.id.rl_my_header)
    RelativeLayout rlMyHeader;
    @BindView(R.id.iv_my_pic)
    ImageView ivMyPic;
    @BindView(R.id.tv_my_name)
    TextView tvMyName;
    @BindView(R.id.tv_my_phone)
    TextView tvMyPhone;
    @BindView(R.id.rl_my_to)
    RelativeLayout rlMyTo;
    @BindView(R.id.rl_my_top)
    RelativeLayout rlMyTop;
    @BindView(R.id.iv_my_pz)
    ImageView ivMyPz;
    @BindView(R.id.rl_my_name)
    RelativeLayout rlMyName;
    @BindView(R.id.rl_my_main)
    RelativeLayout rlMyMain;
    @BindView(R.id.iv_my_mm)
    ImageView ivMyMm;
    @BindView(R.id.rl_my_adress)
    RelativeLayout rlMyAdress;
    @BindView(R.id.iv_my_syzn)
    ImageView ivMySyzn;
    @BindView(R.id.rl_my_xxadress)
    RelativeLayout rlMyXxadress;
    @BindView(R.id.rl_my_bootom)
    RelativeLayout rlMyBootom;
    @BindView(R.id.iv_my_sz)
    ImageView ivMySz;
    @BindView(R.id.rl_my_sz)
    RelativeLayout rlMySz;
    @BindView(R.id.rl_my_last)
    RelativeLayout rlMyLast;
    Unbinder unbinder;

    @Override
    public int bindLayout() {
        return R.layout.fragment_my;

    }

    @Override
    public void initView(View view) {
        preferences = getActivity().getSharedPreferences("my", MODE_PRIVATE);
        pass = preferences.getString("pass", "");
    }

    @Override
    public void doBusiness(Context mContext) {

        SharedPreferencesHelper sharedPreferencesHelper=new SharedPreferencesHelper(getActivity(),"my");
        String sellerPhone= (String) sharedPreferencesHelper.getSharedPreference("sellerPhone","0");
        String sellerCoName= (String) sharedPreferencesHelper.getSharedPreference("sellerCoName","0");
        tvMyPhone.setText(sellerPhone);
        tvMyName.setText(sellerCoName);
    }

    @Override
    public void widgetClick(View v) {

    }

    @OnClick({R.id.rl_my_adress, R.id.rl_my_sz, R.id.rl_my_to, R.id.rl_my_name})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_my_adress:
                //管理密码
                if (pass.length() == 0) {
                    startActivity(new Intent(getActivity(), ManageActivity.class));
                } else {
                    Intent intent = new Intent(getActivity(), PassWordActivity.class);
                    intent.putExtra("type", 2);
                    startActivity(intent);
                }

                break;

            case R.id.rl_my_sz:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;

            case R.id.rl_my_to:
                startActivity(new Intent(getActivity(), UserActivity.class));
                break;
            case R.id.rl_my_name:
                startActivity(new Intent(getActivity(), AddDeviceActivity.class));
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
