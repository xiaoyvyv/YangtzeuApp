package com.yangtzeu.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.lib.x5web.WebViewProgressBar;
import com.yangtzeu.R;
import com.yangtzeu.presenter.ShopAddPresenter;
import com.yangtzeu.ui.activity.base.BaseActivity;
import com.yangtzeu.ui.view.ShopAddView;
import com.yangtzeu.utils.MyUtils;
import com.yangtzeu.utils.UriPathUtils;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

public class ShopAddActivity extends BaseActivity implements ShopAddView {
    private Toolbar toolbar;
    private LinearLayout add_image_trip;
    private ImageView image;
    private TextView des;
    private TextView title;
    private TextView kind;
    private TextView price;
    private Button send;
    private ShopAddPresenter president;
    private String imagePath;
    private String imageKind;
    private WebViewProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_add);
        init();
        MyUtils.setToolbarBackToHome(this, toolbar);
    }

    @Override
    public void findViews() {
        toolbar = findViewById(R.id.toolbar);
        title = findViewById(R.id.title);
        des = findViewById(R.id.des);
        image = findViewById(R.id.image);
        add_image_trip = findViewById(R.id.add_image_trip);
        price = findViewById(R.id.price);
        kind = findViewById(R.id.kind);
        send = findViewById(R.id.send);
        progressBar = findViewById(R.id.progressBar);

    }
    @Override
    public void setEvents() {
        president = new ShopAddPresenter(this, this);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                president.sendGoods();
            }
        });

        add_image_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickIntent = new Intent(Intent.ACTION_PICK);
                pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(pickIntent, 123);
            }
        });

        kind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                @SuppressLint("InflateParams")
                View view = getLayoutInflater().inflate(R.layout.view_change_theme, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(ShopAddActivity.this);
                builder.setView(view).create();
                final AlertDialog dialog = builder.show();
                LinearLayout layout = view.findViewById(R.id.container);
                for (int i = 1; i < ShopActivity.SHOP_KIND.length; i++) {
                    @SuppressLint("InflateParams")
                    View m_item = getLayoutInflater().inflate(R.layout.view_choose_term_item, null);
                    TextView title = m_item.findViewById(R.id.title);
                    TextView bt = m_item.findViewById(R.id.bt);
                    title.setText( ShopActivity.SHOP_KIND[i]);
                    final int finalI = i;
                    bt.setOnClickListener(new View.OnClickListener() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onClick(View v) {
                            ToastUtils.showShort("你选择了：" +  ShopActivity.SHOP_KIND[finalI]);
                            imageKind = ShopActivity.SHOP_KIND[finalI];
                            kind.setText("分类：" + ShopActivity.SHOP_KIND[finalI]);
                            dialog.dismiss();
                        }
                    });
                    layout.addView(m_item);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 123) {
            imagePath = UriPathUtils.getPath(this, Objects.requireNonNull(data).getData());
            Glide.with(this).load(imagePath).into(image);
            add_image_trip.removeAllViews();
        } else if (resultCode == Activity.RESULT_CANCELED) {
            ToastUtils.showShort(R.string.clear_set);
        } else ToastUtils.showShort(R.string.load_file_error);
    }

    @Override
    public LinearLayout getAdd_image_trip() {
        return add_image_trip;
    }

    @Override
    public Button getSend() {
        return send;
    }


    @Override
    public String getImagePath() {
        return imagePath;
    }

    @Override
    public WebViewProgressBar getProgressView() {
        return progressBar;
    }

    @Override
    public TextView getDes() {
        return des;
    }

    @Override
    public TextView getMyTitle() {
        return title;
    }

    @Override
    public String getKind() {
        return imageKind;
    }

    @Override
    public TextView getPrice() {
        return price;
    }
}
