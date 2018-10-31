package com.yangtzeu.model;

import android.app.Activity;

import com.blankj.utilcode.util.ToastUtils;
import com.yangtzeu.R;
import com.yangtzeu.entity.GradeBean;
import com.yangtzeu.model.imodel.IChartModel;
import com.yangtzeu.ui.view.ChartView;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;


public class ChartModel implements IChartModel {
    @Override
    public void setChart(Activity activity, ChartView view) {
        final List<GradeBean> data1 = view.getData();

        LineChartView mChartView = view.getLineChartView();

        mChartView.setInteractive(true);//设置图表是可以交互的（拖拽，缩放等效果的前提）
       // mChartView.setZoomType(ZoomType.VERTICAL);//设置缩放方向

        mChartView.setZoomType(ZoomType.HORIZONTAL);
        mChartView.setMaxZoom((float) 20);//最大方法比例
        mChartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);

        List<AxisValue> valuesX = new ArrayList<>();
        for (int i = 0; i < data1.size(); i++) {
            valuesX.add(new AxisValue(i).setValue(i).setLabel(data1.get(i).getCourseName()));
        }

        List<AxisValue> valuesY = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            valuesY.add(new AxisValue(i).setValue(i * 5).setLabel(String.valueOf(i * 5)));
        }

        final Axis axisX = new Axis(valuesX);//x轴
        Axis axisY = new Axis(valuesY);//y轴
        axisX.setHasLines(true);// 是否显示X轴网格线
        axisY.setHasLines(true);// 是否显示Y轴网格线

        ArrayList<PointValue> values = new ArrayList<>();//折线上的点
        for (int i = 0; i < data1.size(); i++) {
            String courseZuiZhong = data1.get(i).getCourseZuiZhong();
            if (courseZuiZhong.contains(".")) {
                courseZuiZhong = courseZuiZhong.substring(0, courseZuiZhong.lastIndexOf("."));
            }
            int grade = Integer.parseInt(courseZuiZhong);

            values.add(new PointValue(i, grade));
        }


        Line line = new Line(values)
                .setStrokeWidth(2)
                .setPointRadius(2)
                .setCubic(true)//设置是平滑的还是直的
                .setColor(activity.getResources().getColor(R.color.colorPrimary));//声明线并设置颜色


        ArrayList<Line> lines = new ArrayList<>();
        lines.add(line);


        LineChartData data = new LineChartData();
        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);
        data.setLines(lines);

        mChartView.setLineChartData(data);//给图表设置数据

        Viewport v = new Viewport(mChartView.getMaximumViewport());
        v.bottom = 0f;
        v.top = 100f;
        //固定Y轴的范围,如果没有这个,Y轴的范围会根据数据的最大值和最小值决定,这不是我想要的
        mChartView.setMaximumViewport(v);
        //这2个属性的设置一定要在lineChart.setMaximumViewport(v);这个方法之后,不然显示的坐标数据是不能左右滑动查看更多数据的
        v.left = 0 ;
       v.right = 12;
        mChartView.setCurrentViewport(v);


        mChartView.setOnValueTouchListener(new LineChartOnValueSelectListener() {
            @Override
            public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
                ToastUtils.showShort(data1.get(pointIndex).getCourseName() + "：" + value.getY());

            }

            @Override
            public void onValueDeselected() {

            }
        });

    }

    @Override
    public void setColumnChart(Activity activity, ChartView view) {


    }
}
