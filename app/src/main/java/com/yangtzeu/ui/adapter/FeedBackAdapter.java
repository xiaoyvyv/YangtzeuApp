package com.yangtzeu.ui.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.lib.subutil.GsonUtils;
import com.yangtzeu.R;
import com.yangtzeu.entity.FeedBackBean;
import com.yangtzeu.entity.MessageBean;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by Administrator on 2018/4/12.
 *
 * @author 王怀玉
 * @explain MoreAdapter
 */

public class FeedBackAdapter extends RecyclerView.Adapter<FeedBackAdapter.ViewHolder> {
    private Context context;
    private List<FeedBackBean.DataBean> dataBeans = new ArrayList<>();

    public FeedBackAdapter(Context context) {
        this.context = context;
    }

    public void addData(List<FeedBackBean.DataBean> appBean) {
        this.dataBeans.addAll(appBean);
    }

    public void clear() {
        dataBeans.clear();
        notifyDataSetChanged();
    }


    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_feedback_item, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FeedBackBean.DataBean itemBean = dataBeans.get(position);

        final String content = itemBean.getContent();
        final String email = itemBean.getEmail();
        final String master = itemBean.getMaster();
        final String master_id = itemBean.getMaster_id();
        final String classx = itemBean.getClassX();
        final String time = itemBean.getTime();
        final String state = itemBean.getState();

        holder.name.setText("姓名：" + master + "\n班级：" + classx + "\n学号：" + master_id);

        holder.content.setText(content);
        holder.email.setText(email);
        holder.time.setText(time);
        holder.state.setText(state);
        if (state.equals("账户状态：正常")) {
            holder.state.setTextColor(Color.GREEN);
        } else {
            holder.state.setTextColor(Color.RED);
        }
        holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                @SuppressLint("InflateParams")
                View inflate = LayoutInflater.from(context).inflate(R.layout.fragment_love_replay, null);
                TextView trip = inflate.findViewById(R.id.trip);
                TextView message = inflate.findViewById(R.id.message);
                trip.setText("回复" + master);
                message.setText("邮件回复");
                final TextInputEditText replayView = inflate.findViewById(R.id.replay);
                AlertDialog alert = MyUtils.getAlert(context, null, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String input = Objects.requireNonNull(replayView.getText()).toString().trim();
                        if (input.isEmpty()) {
                            ToastUtils.showShort(R.string.please_input);
                        } else {
                            String url = Url.Yangtzeu_App_SendEmail + "?address=" + email + "&text=" + input + "&title=新长大助手-用户反馈查阅通知回复";
                            OkHttp.do_Get(url, new OnResultStringListener() {
                                @Override
                                public void onResponse(String response) {
                                    ToastUtils.showShort(response);
                                }

                                @Override
                                public void onFailure(String error) {
                                    ToastUtils.showShort(R.string.send_error);
                                }
                            });
                        }
                    }
                });

                String bt_text;
                final String url;
                if (state.equals("账户状态：正常")) {
                    bt_text = "封停";
                    url = Url.Yangtzeu_FengHao + "&number=" + master_id + "&name=" + master;
                } else {
                    bt_text = "解封";
                    url = Url.Yangtzeu_RemoveFengHao + master_id;
                }
                alert.setButton(AlertDialog.BUTTON_NEUTRAL, bt_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        OkHttp.do_Get(url, new OnResultStringListener() {
                            @Override
                            public void onResponse(String response) {
                                MessageBean bean = GsonUtils.fromJson(response, MessageBean.class);
                                String info = bean.getInfo();
                                ToastUtils.showShort(info);
                            }

                            @Override
                            public void onFailure(String error) {
                                ToastUtils.showShort(error);
                            }
                        });
                    }
                });
                alert.setView(inflate);
                alert.setTitle(null);
                alert.show();
            }
        });
    }

    @Override
    public long getItemId(int i) {
        return super.getItemId(i);
    }

    @Override
    public int getItemCount() {
        return dataBeans.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView content;
        TextView state;
        TextView email;
        TextView time;
        LinearLayout mLinearLayout;

        ViewHolder(View view) {
            super(view);
            state = view.findViewById(R.id.state);
            content = view.findViewById(R.id.content);
            name = view.findViewById(R.id.name);
            email = view.findViewById(R.id.email);
            time = view.findViewById(R.id.time);
            mLinearLayout = view.findViewById(R.id.mLinearLayout);
        }
    }

}