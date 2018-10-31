package com.yangtzeu.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.yangtzeu.R;
import com.yangtzeu.entity.TripBean;
import com.yangtzeu.ui.activity.base.BaseActivity;
import com.yangtzeu.utils.MyUtils;
import com.yangtzeu.utils.YangtzeuUtils;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;


public class TripActivity extends BaseActivity {
    private BGABanner mForegroundBanner;
    private TripBean tripBean;
    public List<String> title = new ArrayList<>();
    public List<String> image = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        tripBean = (TripBean) getIntent().getSerializableExtra("tripBean");
        super.onCreate(savedInstanceState);
        ScreenUtils.setFullScreen(TripActivity.this);
        setContentView(R.layout.activity_trip);
        init();
    }

    @Override
    public void findViews() {
        mForegroundBanner = findViewById(R.id.banner_guide_foreground);
    }

    @Override
    public void setEvents() {
        if (ObjectUtils.isEmpty(tripBean)) {
            YangtzeuUtils.getTripInfo(this, true);
            super.onBackPressed();
            return;
        }
        List<TripBean.DataBean> datas = tripBean.getData();
        for (TripBean.DataBean dataBean : datas) {
            title.add(dataBean.getTitle());
            image.add(dataBean.getImage());
        }

        mForegroundBanner.setEnterSkipViewIdAndDelegate(R.id.btn_guide_enter, R.id.tv_guide_skip, new BGABanner.GuideDelegate() {
            @Override
            public void onClickEnterOrSkip() {
                TripActivity.this.finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });


        mForegroundBanner.setData(image, title);
        mForegroundBanner.setAdapter(new BGABanner.Adapter<ImageView, String>() {
            @Override
            public void fillBannerItem(BGABanner banner, ImageView itemView, String model, int position) {
                itemView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                Glide.with(TripActivity.this)
                        .load(model)
                        .into(itemView);
            }
        });

        mForegroundBanner.setDelegate(new BGABanner.Delegate() {
            @Override
            public void onBannerItemClick(BGABanner banner, View itemView, Object model, int position) {
                MyUtils.openImage(TripActivity.this, image.get(position));
            }
        });
    }


    @Override
    public void onBackPressed() {
        ToastUtils.showShort(R.string.please_read_trip);
    }
}
