package com.yangtzeu.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.blankj.utilcode.util.LogUtils;
import com.yangtzeu.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by Administrator on 2018/4/12.
 *
 * @author 王怀玉
 * @explain MoreAdapter
 */

public class MatrixAddAdapter extends RecyclerView.Adapter<MatrixAddAdapter.ViewHolder> {
    private Context context;
    private List<EditText> editTexts = new ArrayList<>();
    private int width = 0;
    private int height = 0;

    public MatrixAddAdapter(Context context) {
        this.context = context;
    }

    public void setAdapterSize(int width,int  height) {
        this.width = width;
        this.height = height;
    }

    public void clear() {
        editTexts.clear();
    }

    public double[][] getEditTexts() {
        List<Double> d = new ArrayList<>();
        for (int i = 0; i < getItemCount(); i++) {
            String s = editTexts.get(i).getText().toString();
            if (s.isEmpty()) {
                s = "0";
            }
            Double double_s;
            try {
                double_s = Double.valueOf(s);
            } catch (NumberFormatException e) {
                double_s = 0.0;
            }
            d.add(double_s);
        }

        double[][] doubles = new double[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int which = i * width + j;
                double value = d.get(which);
                doubles[i][j] = value;
                LogUtils.e(which,value);
            }
        }
        return doubles;
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        EditText editText = (EditText) LayoutInflater.from(context).inflate(R.layout.activity_matrix_add_item, parent, false);
        editTexts.add(editText);
        return new ViewHolder(editText);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EditText editText = editTexts.get(position);

    }
    @Override
    public long getItemId(int i) {
        return super.getItemId(i);
    }

    @Override
    public int getItemCount() {
        return width * height;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(View view) {
            super(view);
        }
    }

}