package com.yangtzeu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.lib.loading.LVBlock;
import com.yangtzeu.R;
import com.yangtzeu.listener.OnResultListener;
import com.yangtzeu.presenter.CetPresenter;
import com.yangtzeu.ui.activity.base.BaseActivity;
import com.yangtzeu.ui.view.CetView;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;
import com.yangtzeu.utils.YangtzeuUtils;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;


public class CetActivity extends BaseActivity implements CetView {
    private String CET_DATE;

    private Toolbar toolbar;
    private LinearLayout container;
    private CetPresenter presenter;
    private TextView cet4;
    private TextView cet6;
    private TextView title;
    private CardView cet_grade_view;

    private EditText et_name;
    private EditText et_number;
    private EditText et_yzm;
    private ImageView yzm_image;
    private Button query_cet_grade;

    private EditText card_name;
    private EditText card_number;
    private EditText card_yzm;
    private ImageView card_yzm_image;
    private Button query_cet_card;
    private LVBlock loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cet);
        init();
        MyUtils.setToolbarBackToHome(this, toolbar);
    }

    @Override
    public void findViews() {
        toolbar = findViewById(R.id.toolbar);
        container = findViewById(R.id.cet_container);
        loading = findViewById(R.id.loading);


        et_name = findViewById(R.id.et_name);
        et_number = findViewById(R.id.et_number);
        et_yzm = findViewById(R.id.et_yzm);
        yzm_image = findViewById(R.id.yzm);
        query_cet_grade = findViewById(R.id.query_cet_grade);


        card_name = findViewById(R.id.card_name);
        card_number = findViewById(R.id.card_number);
        card_yzm = findViewById(R.id.card_yzm);
        card_yzm_image = findViewById(R.id.card_yzm_image);
        query_cet_card = findViewById(R.id.query_cet_card);


        title = findViewById(R.id.title);
        cet4 = findViewById(R.id.cet4);
        cet6=findViewById(R.id.cet6);
        cet_grade_view=findViewById(R.id.cet_grade_view);
    }

    @Override
    public void setEvents() {
        index_url = Url.Yangtzeu_Cet_Index1;
        url = Url.Yangtzeu_Cet1;

        presenter = new CetPresenter(this, this);
        presenter.initData();
        presenter.getCetHistoryGrade();
        presenter.getCardYzm();

        yzm_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getGradeYzm();
            }
        });

        card_yzm_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShort("刷新验证码");
                presenter.getCardYzm();
            }
        });

        query_cet_grade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getNowTermGrade();
            }
        });

        query_cet_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getCardInfo();
            }
        });

        loading.startAnim();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cet_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.change:
                YangtzeuUtils.showChooseModel(new OnResultListener<Integer>() {
                    @Override
                    public void onResult(Integer projectId) {
                        switch (projectId) {
                            case 1:
                                index_url = Url.Yangtzeu_Cet_Index1;
                                url = Url.Yangtzeu_Cet1;
                                presenter.getCetHistoryGrade();
                                break;
                            case 2:
                                index_url = Url.Yangtzeu_Cet_Index2;
                                url = Url.Yangtzeu_Cet2;
                                presenter.getCetHistoryGrade();
                                break;
                        }
                    }
                });
                break;
            case R.id.add_cet:
                MyUtils.startActivity(new Intent(CetActivity.this, WebActivity.class)
                        .putExtra("from_url", Url.Yangtzeu_Cet_Add));
                break;
            case R.id.read_in_web:
                MyUtils.openUrl(CetActivity.this, url, true);
                break;
            case R.id.join_in_web:
                MyUtils.startActivity(new Intent(CetActivity.this, WebActivity.class)
                        .putExtra("from_url", Url.Yangtzeu_Guan_Cet));
                break;
            case R.id.g_web:
                MyUtils.startActivity(new Intent(CetActivity.this, WebActivity.class)
                        .putExtra("from_url", "http://cet.neea.edu.cn/cet"));
                break;
            case R.id.g_web2:
                MyUtils.startActivity(new Intent(CetActivity.this, WebActivity.class)
                        .putExtra("from_url", "https://www.chsi.com.cn/cet/"));
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public ImageView getYzmImage() {
        return yzm_image;
    }

    @Override
    public ImageView getCardYzmImage() {
        return card_yzm_image;
    }

    @Override
    public CardView getQueryGradeView() {
        return cet_grade_view;
    }

    @Override
    public EditText getCardName() {
        return card_name;
    }

    @Override
    public EditText getCardNumber() {
        return card_number;
    }

    @Override
    public EditText getCardYzm() {
        return card_yzm;
    }

    @Override
    public String getCetInfo() {
        return CET_DATE;
    }

    @Override
    public TextView getCet4DateView() {
        return cet4;
    }

    @Override
    public TextView getCetTitle() {
        return title;
    }

    @Override
    public TextView getCet6DateView() {
        return cet6;
    }

    @Override
    public void setCetInfo(String CET_DATE) {
        this.CET_DATE = CET_DATE;
    }

    @Override
    public LinearLayout getCetHistoryContainer() {
        return container;
    }

    @Override
    public EditText getStudentName() {
        return et_name;
    }

    @Override
    public EditText getCetNumber() {
        return et_number;
    }

    @Override
    public EditText getYzmEditView() {
        return et_yzm;
    }

    @Override
    public String getIndexUrl() {
        return index_url;
    }

    @Override
    public String getUrl() {
        return url;
    }
}
