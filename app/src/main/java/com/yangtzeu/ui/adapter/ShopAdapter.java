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
import com.google.gson.Gson;
import com.lib.mob.im.IMManager;
import com.lib.subutil.ClipboardUtils;
import com.lib.subutil.GsonUtils;
import com.mob.imsdk.model.IMConversation;
import com.yangtzeu.R;
import com.yangtzeu.entity.MessageBean;
import com.yangtzeu.entity.ShopBean;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.ui.activity.ChatDetailsActivity;
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

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ViewHolder> {
    private boolean is_manger;
    private Context context;
    private List<ShopBean.DataBean> beans = new ArrayList<>();

    public ShopAdapter(Context context, boolean is_manger) {
        this.context = context;
        this.is_manger = is_manger;
    }

    public void setData(List<ShopBean.DataBean> beans) {
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
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_shop_item, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
        ShopBean.DataBean bean = beans.get(position);

        final String id = bean.getId();
        final String title = bean.getName();
        final String description = bean.getDescription();
        final String name = bean.getMaster();
        final String master_id = bean.getMaster_id();
        final String phone = bean.getPhone();
        final String price = bean.getPrice();
        String type = bean.getType();
        final String qq = bean.getQq();
        final String wechat = bean.getWechat();
        final String image = bean.getImage();
        final String time = bean.getTime();
        final List<ShopBean.DataBean.ReplayBean> replay = bean.getReplay();


        viewHolder.time.setText(time);
        viewHolder.des.setText(description);
        viewHolder.title.setText(title);
        viewHolder.price.setText("￥" + price);
        viewHolder.type.setText(type);
        viewHolder.master.setText("发布者：" + name);
        viewHolder.name.setText(name);

        MyUtils.loadImage(context, viewHolder.image, image);
        MyUtils.loadImage(context, viewHolder.header, MyUtils.getQQHeader(qq));

        viewHolder.onclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_manger) {
                    MyUtils.getAlert(context, context.getString(R.string.is_delete_shop), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ToastUtils.showShort(R.string.delete_ing);
                            OkHttp.do_Get(Url.deleteGoods(id), new OnResultStringListener() {
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

                @SuppressLint("InflateParams")
                View view = LayoutInflater.from(context).inflate(R.layout.fragment_shop_dialog, null);
                final AlertDialog dialog = new AlertDialog.Builder(context)
                        .setView(view)
                        .create();
                TextView titleView = view.findViewById(R.id.title);
                TextView desView = view.findViewById(R.id.des);
                TextView nameView = view.findViewById(R.id.name);
                TextView timeView = view.findViewById(R.id.time);
                TextView priceView = view.findViewById(R.id.price);
                ImageView imageView = view.findViewById(R.id.image);
                LinearLayout phoneView = view.findViewById(R.id.phone);
                LinearLayout qqView = view.findViewById(R.id.qq);
                LinearLayout wechatView = view.findViewById(R.id.wechat);
                LinearLayout onlineView = view.findViewById(R.id.online);
                Button sendView = view.findViewById(R.id.send);
                final TextInputEditText replayView = view.findViewById(R.id.replay);

                MyUtils.loadImage(context, imageView, image);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyUtils.openImage(context, image);
                    }
                });

                priceView.setText("￥" + price);
                timeView.setText(time);
                desView.setText(description);
                titleView.setText(title);
                nameView.setText("发布者：" + name);
                titleView.setText(title);
                wechatView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ClipboardUtils.copyText(wechat);
                        ToastUtils.showShort(R.string.copy_wechat_right);
                        MyUtils.openWeChat();
                    }
                });
                onlineView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //targetId - 目标id（群聊为群的id，私聊为对方id）
                        IMManager.chat(master_id,IMConversation.TYPE_USER);
                    }
                });
                sendView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String input = Objects.requireNonNull(replayView.getText()).toString().trim();
                        if (input.isEmpty()) {
                            ToastUtils.showShort(R.string.please_input);
                        } else {
                            ShopBean.DataBean.ReplayBean replayBean = new ShopBean.DataBean.ReplayBean();
                            String my_name = SPUtils.getInstance("user_info").getString("name");
                            String my_number= SPUtils.getInstance("user_info").getString("number");
                            replayBean.setUser_name(my_name);
                            replayBean.setContent(input);
                            replay.add(replayBean);
                            addReplay(viewHolder, replay, master_id);

                            KeyboardUtils.hideSoftInput(replayView);
                            dialog.dismiss();

                            final ProgressDialog replay_dialog = MyUtils.getProgressDialog(context, "评论发布中");
                            replay_dialog.show();
                            Request request = Url.getAddGoodsReplayUrl(id, my_number, my_name, input);
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
                phoneView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyUtils.call(context, phone);
                    }
                });
                qqView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyUtils.chatQQ(context, qq);
                    }
                });


                dialog.show();
            }
        });

        addReplay(viewHolder, replay, master_id);
    }

    private void addReplay(ViewHolder viewHolder, List<ShopBean.DataBean.ReplayBean> replay, final String master_id) {
        if (replay.size() == 0) {
            viewHolder.replay_message.removeAllViews();
            View item = View.inflate(context, R.layout.activity_board_replay_item, null);
            viewHolder.replay_message.addView(item);
        } else {
            viewHolder.replay_message.removeAllViews();
        }
        for (int j = 0; j < replay.size(); j++) {
            //载入回复Item布局,并且加入回复容器布局
            View HuiFuItem = View.inflate(context, R.layout.activity_board_replay_item, null);
            viewHolder.replay_message.addView(HuiFuItem);
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
                    IMManager.chat(master_id,IMConversation.TYPE_USER);
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
        LinearLayout replay_message;
        TextView title;
        TextView price;
        TextView type;
        TextView master;
        TextView name;
        TextView time;
        TextView des;
        ImageView image;
        ImageView header;

        ViewHolder(View ll_class) {
            super(ll_class);
            name = ll_class.findViewById(R.id.name);
            time = ll_class.findViewById(R.id.time);
            header = ll_class.findViewById(R.id.header);
            des = ll_class.findViewById(R.id.des);
            title = ll_class.findViewById(R.id.title);
            price = ll_class.findViewById(R.id.price);
            type = ll_class.findViewById(R.id.type);
            master = ll_class.findViewById(R.id.master);
            image = ll_class.findViewById(R.id.image);
            onclick = ll_class.findViewById(R.id.onclick);
            replay_message = ll_class.findViewById(R.id.replay_message);
        }
    }

}