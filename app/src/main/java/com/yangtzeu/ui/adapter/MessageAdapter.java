package com.yangtzeu.ui.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lib.subutil.GsonUtils;
import com.yangtzeu.R;
import com.yangtzeu.entity.MessageBean;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.ui.activity.MessageActivity;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;

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

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private Context context;
    private List<MessageBean.DataBean> dataBeans = new ArrayList<>();

    public MessageAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<MessageBean.DataBean> dataBeans) {
        this.dataBeans = dataBeans;
    }

    public void clear() {
        dataBeans.clear();
    }


    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_message_item, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final MessageBean.DataBean bean = dataBeans.get(position);
        holder.title.setText(bean.getText());
        holder.time.setText("发布时间：" + bean.getTime());
        holder.from.setText("发布者：" + bean.getFrom());
        final String id = bean.getId();
        if (bean.getFrom().contains("admin")) {
            holder.from.setTextColor(context.getResources().getColor(R.color.red));
        } else {
            holder.from.setTextColor(context.getResources().getColor(R.color.black));
        }
        holder.onclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String from_number = SPUtils.getInstance("user_info").getString("number");
                final String name = SPUtils.getInstance("user_info").getString("name");
                if (bean.getFrom_number().equals(from_number)) {
                    ToastUtils.showShort("无法给自己回复");
                    return;
                }
                if (bean.getFrom().equals("admin")) {
                    ToastUtils.showShort("无法给管理员回复，请到用户反馈页面操作");
                    return;
                }

                final EditText edit = new EditText(context);
                int dp20 = ConvertUtils.dp2px(25);
                edit.setPadding(dp20, dp20 / 2, dp20, 0);
                edit.setTextSize(15);
                edit.setHint("回复" + bean.getFrom());
                edit.setBackgroundColor(context.getResources().getColor(R.color.translate));
                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setTitle("回复" + bean.getFrom())
                        .setMessage("请输入回复" + bean.getFrom() + "的内容")
                        .setView(edit)
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String s = edit.getText().toString().trim();
                                if (s.isEmpty()) {
                                    ToastUtils.showShort("未输入");
                                    return;
                                }
                                String sen_message = Url.getSendMessage(name + "回复你啦：" + s, name, from_number, bean.getFrom_number());
                                OkHttp.do_Get(sen_message, new OnResultStringListener() {
                                    @Override
                                    public void onResponse(String response) {
                                        MessageBean bean1 = GsonUtils.fromJson(response, MessageBean.class);
                                        if (bean1.getInfo().contains("留言成功")) {
                                            ToastUtils.showShort(R.string.replay_success_trip);
                                        } else {
                                            LogUtils.i(response, "回复消息发送失败，对方将不会收到提醒");
                                        }
                                    }

                                    @Override
                                    public void onFailure(String error) {
                                        LogUtils.i("回复消息发送失败，对方将不会收到提醒");
                                    }
                                });

                            }
                        }).create();
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
            }
        });
        holder.onclick.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (bean.getFrom().equals("admin")) {
                    ToastUtils.showShort("管理员消息无法删除");
                    return true;
                }

                MyUtils.getAlert(context, context.getString(R.string.is_delete_msg), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String url = Url.deleteMessage(id);
                        OkHttp.do_Get(url, new OnResultStringListener() {
                            @Override
                            public void onResponse(String response) {
                                ((MessageActivity) context).getData().remove(position);
                                notifyDataSetChanged();
                                ToastUtils.showShort(R.string.delete_success);
                            }

                            @Override
                            public void onFailure(String error) {
                                ToastUtils.showShort(R.string.delete_error);
                            }
                        });
                    }
                }).show();
                return true;
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
        TextView title;
        TextView time;
        TextView from;
        RelativeLayout onclick;

        ViewHolder(View view) {
            super(view);
            time = view.findViewById(R.id.time);
            onclick = view.findViewById(R.id.onclick);
            from = view.findViewById(R.id.from);
            title = view.findViewById(R.id.title);
        }
    }

}