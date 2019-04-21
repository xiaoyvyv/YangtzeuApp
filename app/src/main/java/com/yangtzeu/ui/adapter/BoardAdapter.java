package com.yangtzeu.ui.adapter;

/**
 * Created by Administrator on 2018/1/30.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.yangtzeu.R;
import com.yangtzeu.entity.BoardBean;
import com.yangtzeu.entity.ImageBean;
import com.yangtzeu.ui.activity.BoardActivity;
import com.yangtzeu.ui.activity.ImageActivity;
import com.yangtzeu.utils.MyUtils;

import java.util.ArrayList;
import java.util.List;


public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.ViewHolder> {
    private Context context;
    private List<BoardBean.ResultBean> mBigList = new ArrayList<>();

    public BoardAdapter(Context context) {
        this.context = context;
    }

    public void addDate(List<BoardBean.ResultBean> BigList) {
        this.mBigList.addAll(BigList);
    }

    public void clear() {
        mBigList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup view, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_board_item, view, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, @SuppressLint("RecyclerView") final int i) {
        if (ObjectUtils.isEmpty(mBigList.get(i).getId())) {
            return;
        }
        try {
            final String ID = mBigList.get(i).getId();
            String Text = mBigList.get(i).get留言内容();
            if (Text.contains("‖")) {
                Text = Text.replace("‖", "&");
            }

            final String Class = mBigList.get(i).get班级();
            final String HeadImg = mBigList.get(i).get头像();
            final String Name = mBigList.get(i).get昵称();
            final String number = mBigList.get(i).get个签();
            String Background = mBigList.get(i).get背景();
            List<BoardBean.ResultBean.回复Bean> replay = mBigList.get(i).get回复();

            if (replay.size() == 0) {
                viewHolder.LouZhuCommentView.setVisibility(View.GONE);
            } else {
                viewHolder.LouZhuCommentView.setVisibility(View.VISIBLE);
                viewHolder.LouZhuCommentView.removeAllViews();
            }
            for (int j = 0; j < replay.size(); j++) {
                //载入回复Item布局,并且加入回复容器布局
                View HuiFuItem = View.inflate(context, R.layout.activity_board_replay_item, null);
                viewHolder.LouZhuCommentView.addView(HuiFuItem);
                TextView HuiFuItemText = HuiFuItem.findViewById(R.id.HuiFuText);
                final String rText = replay.get(j).get回复内容();
                final String rName = "<font color=#00367a>" + replay.get(j).get昵称() + "</font>";
                HuiFuItemText.setText(Html.fromHtml(rName + ":\t\t" + rText));
            }


            if (Background.contains("‖")) {
                Background = Background.replace("‖", "&");
            }
            if (Background.contains("∩")) {
                String[] BG_list = Background.split("∩");
                try {
                    SetTextImg(viewHolder.OnClick, viewHolder.mImageView, BG_list[0], BG_list[1]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                SetTextImg(viewHolder.OnClick, viewHolder.mImageView, Background, "http://");
            }


            final String Time = mBigList.get(i).get时间();

            SetHeadImg(viewHolder.LouZhuHeadImg, HeadImg);

            viewHolder.LouZhuTextView.setText(Text);
            viewHolder.LouZhuNameView.setText(Name);

            //设置开发者高亮
            if (Name.equals("小编")) {
                viewHolder.LouZhuNameView.setTextColor(Color.parseColor("#dd0000"));
                viewHolder.LouZhuGeQinaView.setTextColor(Color.parseColor("#dd0000"));
                viewHolder.LouZhuClassView.setTextColor(Color.parseColor("#dd0000"));
                viewHolder.LouZhuTextView.setTextColor(Color.parseColor("#dd0000"));
            } else {
                viewHolder.LouZhuNameView.setTextColor(Color.parseColor("#000000"));
                viewHolder.LouZhuGeQinaView.setTextColor(Color.parseColor("#000000"));
                viewHolder.LouZhuClassView.setTextColor(Color.parseColor("#000000"));
                viewHolder.LouZhuTextView.setTextColor(Color.parseColor("#000000"));
            }

            viewHolder.LouZhuTimeView.setText(Time);
            viewHolder.LouZhuClassView.setText(Class);
            viewHolder.LouZhuGeQinaView.setText(number);
            viewHolder.LouZhuLouView.setText("第" + ID + "楼");

            final String finalBackground = Background;
            viewHolder.OnClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BoardActivity context = (BoardActivity) BoardAdapter.this.context;
                    if (context.getReplay_container().getVisibility() == View.VISIBLE) {
                        context.getReplay_container().setVisibility(View.GONE);
                    } else {
                        context.showReplayOrAdd(number, ID, Name);
                    }
                }
            });

            viewHolder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ImageActivity.class);
                    if (finalBackground.contains("∩")) {
                        String url = finalBackground.split("∩")[1];
                        ImageBean bean = ImageBean.getImageBean(ImageBean.toStringArray(url), ImageBean.toStringArray(url));
                        intent.putExtra("image_list", bean);
                        MyUtils.startActivity(intent);
                    }

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //头像设置方法
    private void SetHeadImg(ImageView imageView, String url) {
        if (URLUtil.isNetworkUrl(url)) {
            Glide.with(Utils.getApp()).load(url.trim()).into(imageView);
        } else if (MyUtils.isNumeric(url)) {
            Glide.with(Utils.getApp()).load("http://q.qlogo.cn/headimg_dl?bs=qq&dst_uin=" + url + "&src_uin=www.qqjike.com&fid=blog&spec=100").into(imageView);
        } else {
            RequestOptions option = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
            Glide.with(Utils.getApp()).load(R.mipmap.holder).apply(option).into(imageView);
        }
    }

    //背景设置方法
    private void SetTextImg(LinearLayout linearLayout, ImageView imageView, String bg, String url) {
        if (!url.trim().equals("http://")) {
            ((View) imageView.getParent()).setVisibility(View.VISIBLE);
            Glide.with(context).load(url.trim()).into(imageView);
        } else {
            ((View) imageView.getParent()).setVisibility(View.GONE);
        }

        Log.e("留言图片地址", url);

        if (bg.contains("@")) bg = bg.replace("@", "#");
        else bg = "#" + bg;
        linearLayout.setBackgroundColor(Color.parseColor(bg));
    }


    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return mBigList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView LouZhuHeadImg;
        ImageView mImageView;
        TextView LouZhuTextView;
        TextView LouZhuNameView;
        TextView LouZhuTimeView;
        TextView LouZhuClassView;
        TextView LouZhuGeQinaView;
        TextView LouZhuLouView;
        LinearLayout LouZhuCommentView;
        LinearLayout OnClick;

        ViewHolder(View ll_class) {
            super(ll_class);
            //绑定对应楼层楼主发布的内容控件StudentImg
            LouZhuHeadImg = ll_class.findViewById(R.id.studentHeader2);
            //绑定对应楼层楼主发布的内容控件StudentImg
            LouZhuTextView = ll_class.findViewById(R.id.Text);
            //绑定留言楼层楼主昵称控件
            LouZhuNameView = ll_class.findViewById(R.id.Name);
            //绑定留言楼层楼主留言时间控件
            LouZhuTimeView = ll_class.findViewById(R.id.Time);
            //绑定留言楼层楼主班级控件
            LouZhuClassView = ll_class.findViewById(R.id.Class);
            LouZhuGeQinaView = ll_class.findViewById(R.id.GeQian);
            LouZhuCommentView = ll_class.findViewById(R.id.PingLun);
            LouZhuLouView = ll_class.findViewById(R.id.Lou);
            OnClick = ll_class.findViewById(R.id.OnClick);
            mImageView = ll_class.findViewById(R.id.imageView);
        }
    }
}