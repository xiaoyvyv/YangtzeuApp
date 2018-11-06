package com.yangtzeu.ui.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.lib.subutil.GsonUtils;
import com.mob.imsdk.model.IMConversation;
import com.yangtzeu.R;
import com.yangtzeu.entity.LoveBean;
import com.yangtzeu.entity.MessageBean;
import com.yangtzeu.entity.ShopBean;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.ui.activity.ChatActivity;
import com.yangtzeu.ui.activity.ChatDetailsActivity;
import com.yangtzeu.ui.activity.LoveDetailsActivity;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Request;


/**
 * Created by Administrator on 2018/4/12.
 *
 * @author 王怀玉
 * @explain MoreAdapter
 */

public class LoveAdapter extends RecyclerView.Adapter<LoveAdapter.ViewHolder> {
    private Context context;
    private List<LoveBean.DataBean> beans = new ArrayList<>();
    private boolean is_manger;

    public LoveAdapter(Context context, boolean is_manger) {
        this.context = context;
        this.is_manger = is_manger;
    }

    public void setData(List<LoveBean.DataBean> beans) {
        this.beans = beans;
    }

    public void clear() {
        beans.clear();
        notifyDataSetChanged();
    }


    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_love_item, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
        final LoveBean.DataBean bean = beans.get(position);

        final String id = bean.getId();
        final String description = bean.getDescription();
        final String name = bean.getMaster();
        final String master_id = bean.getMaster_id();
        final String qq = bean.getQq();
        final String image = bean.getImage();
        final String music = bean.getMusic();
        final String time = bean.getTime();
        final boolean hide = Boolean.parseBoolean(bean.getHide());
        final List<LoveBean.DataBean.ReplayBean> replay = bean.getReplay();

        if (hide) {
            viewHolder.name.setText("佚名表白者");
            MyUtils.loadImage(context, viewHolder.header, R.mipmap.holder);
        } else {
            viewHolder.name.setText(name);
            MyUtils.loadImage(context, viewHolder.header, MyUtils.getQQHeader(qq));
        }

        viewHolder.time.setText(time);
        viewHolder.des.setText(description);
        MyUtils.loadImage(context, viewHolder.image, image);


        viewHolder.onclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_manger) {
                    MyUtils.getAlert(context, context.getString(R.string.is_delete_love), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ToastUtils.showShort(R.string.delete_ing);
                            OkHttp.do_Get(Url.deleteLove(id), new OnResultStringListener() {
                                @Override
                                public void onResponse(String response) {
                                    MessageBean messageBean = GsonUtils.fromJson(response, MessageBean.class);
                                    ToastUtils.showShort(messageBean.getInfo());
                                }
                                @Override
                                public void onFailure(String error) {
                                    ToastUtils.showShort(R.string.delete_error);
                                }
                            });
                        }
                    }).show();
                    return;
                }

                Intent intent = new Intent(context, LoveDetailsActivity.class);
                intent.putExtra("data", bean);
                MyUtils.startActivity(intent);
            }
        });

        addReplay(context, viewHolder.replay_message, replay, master_id);
        addReplayListener(context, viewHolder.replay_message, viewHolder.addReplay, replay, id, master_id);
    }

    public static void addReplayListener(final Context context, final LinearLayout container, final View view, final List<LoveBean.DataBean.ReplayBean> replay, final String love_id, final String master_id) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                @SuppressLint("InflateParams")
                View inflate = LayoutInflater.from(context).inflate(R.layout.fragment_love_replay, null);
                TextView trip = inflate.findViewById(R.id.trip);
                trip.setText("聊一聊");
                final TextInputEditText replayView = inflate.findViewById(R.id.replay);
                AlertDialog alert = MyUtils.getAlert(context, null, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String input = Objects.requireNonNull(replayView.getText()).toString().trim();
                        if (input.isEmpty()) {
                            ToastUtils.showShort(R.string.please_input);
                        } else {
                            LoveBean.DataBean.ReplayBean replayBean = new LoveBean.DataBean.ReplayBean();
                            String my_name = SPUtils.getInstance("user_info").getString("name");
                            String my_number = SPUtils.getInstance("user_info").getString("number");
                            replayBean.setUser_name(my_name);
                            replayBean.setContent(input);
                            replay.add(replayBean);
                            addReplay(context, container, replay, master_id);

                            KeyboardUtils.hideSoftInput(replayView);
                            dialog.dismiss();

                            final ProgressDialog replay_dialog = MyUtils.getProgressDialog(context, "评论发布中");
                            replay_dialog.show();
                            Request request = Url.getAddLoveReplayUrl(love_id, my_number, my_name, input);
                            OkHttp.do_Post(request, new OnResultStringListener() {
                                @Override
                                public void onResponse(String response) {
                                    replay_dialog.dismiss();
                                    ToastUtils.showShort(R.string.replay_success);
                                }

                                @Override
                                public void onFailure(String error) {
                                    replay.remove(replay.size() - 1);
                                    replay_dialog.dismiss();
                                    ToastUtils.showShort(R.string.replay_error);
                                }
                            });

                        }
                    }
                });
                alert.setView(inflate);
                alert.setTitle(null);
                alert.show();
            }
        });
    }

    public static void addReplay(final Context context, LinearLayout container, List<LoveBean.DataBean.ReplayBean> replay, final String master_id) {
        if (replay.size() == 0) {
            container.removeAllViews();
            View item = View.inflate(context, R.layout.activity_board_replay_item, null);
            container.addView(item);
        } else {
            container.removeAllViews();
        }
        for (int j = 0; j < replay.size(); j++) {
            //载入回复Item布局,并且加入回复容器布局
            View HuiFuItem = View.inflate(context, R.layout.activity_board_replay_item, null);
            container.addView(HuiFuItem);
            TextView HuiFuItemText = HuiFuItem.findViewById(R.id.HuiFuText);
            final String rText = replay.get(j).getContent();
            final String rName = "<font color=#00367a>" + replay.get(j).getUser_name() + "</font>";
            LogUtils.i(rText, rName);
            HuiFuItemText.setText(Html.fromHtml(rName + ":\t\t" + rText));

            //点击评论跳转聊天
            HuiFuItemText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //targetId - 目标id（群聊为群的id，私聊为对方id）
                    Intent intent = new Intent(context, ChatDetailsActivity.class);
                    intent.putExtra("id", master_id);
                    intent.putExtra("type", IMConversation.TYPE_USER);
                    MyUtils.startActivity(intent);
                }
            });
        }
    }

    @Override
    public long getItemId(int i) {
        return super.getItemId(i);
    }

    @Override
    public int getItemCount() {
        return beans.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout onclick;
        TextView name;
        TextView time;
        LinearLayout replay_message;
        TextView des;
        ImageView image;
        ImageView header;
        ImageView addReplay;

        ViewHolder(View ll_class) {
            super(ll_class);
            replay_message = ll_class.findViewById(R.id.replay_message);
            name = ll_class.findViewById(R.id.name);
            time = ll_class.findViewById(R.id.time);
            addReplay = ll_class.findViewById(R.id.addReplay);
            header = ll_class.findViewById(R.id.header);
            des = ll_class.findViewById(R.id.des);
            image = ll_class.findViewById(R.id.image);
            onclick = ll_class.findViewById(R.id.onclick);
        }
    }

}