package ph.com.waterpurifer_distributor.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import butterknife.OnClick;
import ph.com.waterpurifer_distributor.R;
import ph.com.waterpurifer_distributor.base.BaseActivity;

/*
* 制水折线图（暂时不用）
* **/
public class MakeWaterActivity extends BaseActivity {
    private LineChart lineChart;
    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindLayout() {
        setSteepStatusBar(true);
        return R.layout.activity_makewater;
    }

    @Override
    public void initView(View view) {
        lineChart = findViewById(R.id.lineChart);
        setData();
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @Override
    public void widgetClick(View v) {

    }
    private void setData() {
        lineChart.getDescription().setEnabled(false);
        //1.设置x轴和y轴的点
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < 24; i++)
            entries.add(new Entry(i, new Random().nextInt(300)));

        LineDataSet dataSet = new LineDataSet(entries, "制水中"); // add entries to dataset
//        dataSet.setColor(Color.parseColor("#ff5500"));//线条颜色
//        dataSet.setCircleColor(Color.parseColor("#ff5500"));//圆点颜色
//        dataSet.setLineWidth(1f);//线条宽度


        dataSet.setDrawCircles(true);//允许设置平滑曲线
        dataSet.setCubicIntensity(9.0f);//设置折线的平滑度
        lineChart.setDrawGridBackground(false);

        lineChart.setGridBackgroundColor(Color.TRANSPARENT);

        lineChart.setBackgroundColor(Color.TRANSPARENT);// 设置背景
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置x轴的显示位置
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                String value1;
                if (value<9){
                    value1= "0"+(int)value  + ":00";
                }else {
                    value1= (int) value  + ":00";
                }
                return value1;
            }
        });
        xAxis.setDrawGridLines(false);
        YAxis leftAxis = lineChart.getAxisLeft();
        YAxis axisRight = lineChart.getAxisRight();
        axisRight.setEnabled(false);   //设置是否使用 Y轴右边的
        leftAxis.setEnabled(false);     //设置是否使用 Y轴左边的
        leftAxis.setDrawGridLines(false);
        //3.chart设置数据
        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.invalidate(); // refresh

        lineChart.animateY(2000);//动画效果，MPAndroidChart中还有很多动画效果可以挖掘
    }

    @OnClick({R.id.iv_main_memu})
    public  void onClick(View view){
        switch (view.getId()){
            case R.id.iv_main_memu:
                finish();
                break;
        }
    }
}
