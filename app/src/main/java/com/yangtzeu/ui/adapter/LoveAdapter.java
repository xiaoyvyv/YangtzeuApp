package com.yangtzeu.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lib.subutil.GsonUtils;
import com.yangtzeu.R;
import com.yangtzeu.entity.LoveBean;
import com.yangtzeu.entity.MessageBean;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.ui.activity.LoveDetailsActivity;
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
                    MyUtils.getAlert(context, context.getString(R.string.is_delete_shop), new DialogInterface.OnClickListener() {
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

        addReplay(viewHolder, replay);
    }

    private void addReplay(LoveAdapter.ViewHolder viewHolder, List<LoveBean.DataBean.ReplayBean> replay) {
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

        ViewHolder(View ll_class) {
            super(ll_class);
            replay_message = ll_class.findViewById(R.id.replay_message);
            name = ll_class.findViewById(R.id.name);
            time = ll_class.findViewById(R.id.time);
            header = ll_class.findViewById(R.id.header);
            des = ll_class.findViewById(R.id.des);
            image = ll_class.findViewById(R.id.image);
            onclick = ll_class.findViewById(R.id.onclick);
        }
    }

}