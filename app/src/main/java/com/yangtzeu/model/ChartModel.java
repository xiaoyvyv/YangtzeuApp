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

        // 设置图表是可以交互的（拖拽，缩放等效果的前提）
        mChartView.setInteractive(true);
        // 设置只能水平缩放
        mChartView.setZoomType(ZoomType.HORIZONTAL);

        // 最大放大比例
        mChartView.setMaxZoom((float) 20);
        mChartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);

        // 设置X轴文字
        List<AxisValue> valuesX = new ArrayList<>();
        for (int i = 0; i < data1.size(); i++) {
            String x_str = data1.get(i).getCourseName();
            if (x_str.length() > 12) {
                x_str = x_str.substring(0, 12) + "...";
            }

            AxisValue xValue = new AxisValue(i)
                    .setValue(i)
                    .setLabel(x_str);
            valuesX.add(xValue);
        }

        // 设置Y轴文字
        List<AxisValue> valuesY = new ArrayList<>();
        for (int i = 0; i <= 20; i++) {
            valuesY.add(new AxisValue(i).setValue(i * 5).setLabel(String.valueOf(i * 5)));
        }

        // X轴
        final Axis axisX = new Axis(valuesX);
        // Y轴
        Axis axisY = new Axis(valuesY);
        // 是否显示X轴网格线
        axisX.setHasLines(true);
        // 是否显示Y轴网格线
        axisY.setHasLines(true);

        // 折线上的点集合
        ArrayList<PointValue> values = new ArrayList<>();
        for (int i = 0; i < data1.size(); i++) {
            String grade = data1.get(i).getCourseZuiZhong();
            float _grade = 0;
            try {
                _grade = Float.parseFloat(grade);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            values.add(new PointValue(i, _grade));
        }

        // 将点连成线
        Line line = new Line(values)
                //设置线宽度
                .setStrokeWidth(1)
                //设置点的半径
                .setPointRadius(2)
                //设置线是否是平滑
                .setCubic(true)
                //设置线颜色
                .setColor(activity.getResources().getColor(R.color.colorPrimary));

        // 将连填充到集合
        ArrayList<Line> lines = new ArrayList<>();
        lines.add(line);

        // 设置图标数据源：X轴|Y轴|数据源
        LineChartData data = new LineChartData();
        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);
        data.setLines(lines);

        // 给图表设置数据
        mChartView.setLineChartData(data);

        // 固定Y轴的范围,如果没有这个,Y轴的范围会根据数据的最大值和最小值决定,这不是我想要的
        Viewport v = new Viewport(mChartView.getMaximumViewport());
        v.bottom = 0f;
        v.top = 100f;
        mChartView.setMaximumViewport(v);

        //设置初始的可见数据多少  0-6 ，必须在 setMaximumViewport() 之后调用
        v.left = 0;
        v.right = 6;
        mChartView.setCurrentViewport(v);

        //设置点的监听
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

}
