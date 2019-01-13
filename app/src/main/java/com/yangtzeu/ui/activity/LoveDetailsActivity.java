package com.yangtzeu.ui.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lib.falling.FallObject;
import com.lib.falling.FallingView;
import com.lib.moving.MovingImageView;
import com.yangtzeu.R;
import com.yangtzeu.entity.LoveBean;
import com.yangtzeu.listener.OnResultListener;
import com.yangtzeu.ui.activity.base.BaseActivity;
import com.yangtzeu.ui.adapter.LoveAdapter;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MediaPlayUtil;
import com.yangtzeu.utils.MyUtils;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

public class LoveDetailsActivity extends BaseActivity {
    private Toolbar toolbar;
    private LoveBean.DataBean bean;
    private String id;
    private String description;
    private String name;
    private String name_ta;
    private String master_id;
    private String qq;
    private String qq_ta;
    private String image;
    private String music;
    private String time;
    private TextView nameView;
    private ImageView headerView;
    private TextView nameViewTa;
    private ImageView headerViewTa;
    private MovingImageView imageView;
    private TextView timeView;
    private TextView desView;
    private LinearLayout replay_message;
    private boolean hide;
    private FallingView fallingView;
    private StringBuilder share_text = new StringBuilder();
    private List<LoveBean.DataBean.ReplayBean> replay;
    private ImageView addReplay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        bean = (LoveBean.DataBean) getIntent().getSerializableExtra("data");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love_details);
        init();
        MyUtils.setToolbarBackToHome(this, toolbar);
    }

    @Override
    public void findViews() {
        id = bean.getId();
        description = bean.getDescription();
        name = bean.getMaster();
        name_ta = bean.getMaster_ta();
        master_id = bean.getMaster_id();
        qq = bean.getQq();
        qq_ta = bean.getQq_ta();
        image = bean.getImage();
        music = bean.getMusic();
        time = bean.getTime();
        replay = bean.getReplay();

        hide = Boolean.parseBoolean(bean.getHide());
        fallingView = findViewById(R.id.fallingView);
        toolbar = findViewById(R.id.toolbar);

        nameView = findViewById(R.id.name);
        headerView = findViewById(R.id.header);
        timeView = findViewById(R.id.time);
        desView = findViewById(R.id.des);
        imageView = findViewById(R.id.image);
        nameViewTa = findViewById(R.id.name_ta);
        headerViewTa = findViewById(R.id.header_ta);
        replay_message = findViewById(R.id.replay_message);
        addReplay = findViewById(R.id.addReplay);

    }

    @Override
    public void setEvents() {
        //添加评论
        LoveAdapter.addReplay(this, replay_message, replay, master_id);
        //添加评论监听
        LoveAdapter.addReplayListener(this, replay_message, addReplay, replay, id, master_id);

        MusicActivity.getMusicLink(music, new OnResultListener<String>() {
            @Override
            public void onResult(String s) {
                if (URLUtil.isNetworkUrl(s)) {
                    MediaPlayUtil.getInstance().play(s);
                }
            }
        });

        Glide.with(this).load(image).into(imageView);

        imageView.getMovingAnimator().setInterpolator(new LinearInterpolator());
        imageView.getMovingAnimator()
                .addCustomMovement()
                .addDiagonalMoveToDownRight()
                .addHorizontalMoveToLeft()
                .addDiagonalMoveToUpRight()
                .addVerticalMoveToDown()
                .addHorizontalMoveToLeft()
                .addVerticalMoveToUp().
                start();

        this.timeView.setText(time);
        this.desView.setText(description);

        share_text.append("长江大学表白墙")
                .append("\n")
                .append("\n");
        if (hide) {
            share_text.append("佚名表白者")
                    .append("\n");
            nameView.setText("佚名表白者");
            MyUtils.loadImage(this, headerView, R.mipmap.holder);
        } else {
            share_text.append("表白者：")
                    .append(name)
                    .append("\n");
            nameView.setText(name);
            MyUtils.loadImage(this, headerView, MyUtils.getQQHeader(qq));
        }
        nameViewTa.setText(name_ta);
        MyUtils.loadImage(this, headerViewTa, MyUtils.getQQHeader(qq_ta));

        share_text.append("表白对象：")
                .append(name_ta)
                .append("\n");
        share_text.append("表白宣言：")
                .append(description)
                .append("\n");
        share_text.append("表白时间：")
                .append(time)
                .append("\n\n\n");
        share_text.append("表白配图：")
                .append(image)
                .append("\n");
        share_text.append("表白配乐：")
                .append(music)
                .append("\n");


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUtils.openImage(LoveDetailsActivity.this, image);
            }
        });


        Animation circle_anim = AnimationUtils.loadAnimation(LoveDetailsActivity.this, R.anim.anim_round_rotate);
        LinearInterpolator interpolator = new LinearInterpolator();  //设置匀速旋转，在xml文件中设置会出现卡顿
        circle_anim.setInterpolator(interpolator);
        headerView.startAnimation(circle_anim);  //开始动画
        headerViewTa.startAnimation(circle_anim);  //开始动画

        //初始化一个雪花样式的fallObject
        FallObject.Builder builder = new FallObject.Builder(getResources().getDrawable(R.mipmap.heart));
        FallObject fallObject = builder
                .setSpeed(5, true)
                .setSize(60, 60, true)
                //设置风力等级、方向以及随机因素，
                // level：风力等级
                // isWindRandom：物体初始风向和风力大小比例是否随机
                // isWindChange：在物体下落过程中风的风向和风力是否会产生随机变化
                .setWind(5, true, true)
                .build();
        fallingView.addFallObject(fallObject, 50);//添加50个下落物体对象
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(R.string.share).setIcon(R.drawable.ic_share).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                MyUtils.shareText(LoveDetailsActivity.this, share_text.toString() + "\n\n数据来自：" + Url.AppDownUrl);
                return false;
            }
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }
}
