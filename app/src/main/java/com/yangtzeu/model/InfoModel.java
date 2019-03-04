package com.yangtzeu.model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.text.InputType;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.textfield.TextInputEditText;
import com.lib.chat.common.UserManager;
import com.yangtzeu.R;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.model.imodel.IChatInfoModel;
import com.yangtzeu.ui.view.InfoView;
import com.yangtzeu.utils.MyUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class InfoModel implements IChatInfoModel {

    @Override
    public void showChangeHeaderView(final Activity activity, final InfoView chatInfoView) {

        LayoutInflater inflater = LayoutInflater.from(activity);
        @SuppressLint("InflateParams")
        View MeDialog = inflater.inflate(R.layout.view_change_header, null);

        final AlertDialog dialog = new AlertDialog.Builder(activity, R.style.style_dialog)
                .setView(MeDialog)
                .create();
        dialog.show();

        TextView QQ_Head = MeDialog.findViewById(R.id.QQ_Head);
        QQ_Head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                dialog.dismiss();

                @SuppressLint("InflateParams") final View view = activity.getLayoutInflater().inflate(R.layout.view_input_qq, null);
                final TextInputEditText inputEditText = view.findViewById(R.id.text);
                TextView textView = view.findViewById(R.id.trip);
                textView.setText(R.string.input_qq);

                String qq = SPUtils.getInstance("user_info").getString("qq");
                inputEditText.setTextSize(15);
                inputEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                inputEditText.setText(qq);
                inputEditText.setSelectAllOnFocus(true);
                inputEditText.setFocusable(true);

                AlertDialog dialog = new AlertDialog.Builder(activity)
                        .setView(view)
                        .setNegativeButton(R.string.clear, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                MyUtils.canCloseDialog(dialogInterface, true);
                            }
                        })
                        .setNeutralButton(R.string.clean, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //自动弹出键盘
                                KeyboardUtils.showSoftInput(inputEditText);
                                inputEditText.setText(null);
                                MyUtils.canCloseDialog(dialogInterface, false);
                            }
                        })
                        .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String qq = Objects.requireNonNull(inputEditText.getText()).toString().trim();
                                if (!qq.isEmpty()) {

                                    KeyboardUtils.hideSoftInput(activity);
                                    SPUtils.getInstance("user_info").put("qq", qq);
                                    MyUtils.canCloseDialog(dialogInterface, true);

                                    loadHeader(activity, chatInfoView);
                                } else {
                                    MyUtils.canCloseDialog(dialogInterface, false);
                                    ToastUtils.showShort(R.string.input_qq);
                                }
                            }
                        }).create();
                dialog.show();

            }
        });
        TextView Clear = MeDialog.findViewById(R.id.clear);
        Clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        Window window = dialog.getWindow();
        Objects.requireNonNull(window).setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setWindowAnimations(R.style.BottomToBottom);  //添加动画
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = display.getWidth(); //设置宽度
        dialog.getWindow().setAttributes(lp);

    }

    @Override
    public void loadHeader(Activity activity, final InfoView view) {
        String qq = SPUtils.getInstance("user_info").getString("qq", "default_header");
        String qqHeader = MyUtils.getQQHeader(qq);
        if (!qq.equals("default_header")) {
            Glide.with(activity).asBitmap().load(qqHeader).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    view.getHeader().setImageBitmap(resource);
                }
            });
        } else {
            view.getHeader().setImageDrawable(activity.getDrawable(R.mipmap.holder));
        }

    }

    @Override
    public void loadMineInfo(Activity activity, InfoView view) {
        final LinearLayout rootView = view.getMineInfoLayout();
        final TextView info_number = rootView.findViewById(R.id.info_number);
        final TextView info_sex = rootView.findViewById(R.id.info_sex);
        final TextView info_work = rootView.findViewById(R.id.info_work);
        final TextView info_class = rootView.findViewById(R.id.info_class);
        final TextView info_major = rootView.findViewById(R.id.info_major);
        final TextView info_yuan = rootView.findViewById(R.id.info_yuan);
        final TextView info_school = rootView.findViewById(R.id.info_school);


        OkHttp.do_Get(view.getUrl(), new OnResultStringListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                rootView.setVisibility(View.VISIBLE);
                Document document = Jsoup.parse(response);
                Elements trs = document.select("#studentInfoTb > tbody > tr");

                try {
                    //英文名字|性别
                    Elements tr2_tds = trs.get(2).select("tr > td");
                    //项目|学历
                    Elements tr4_tds = trs.get(4).select("tr > td");
                    //类别|院系
                    Elements tr5_tds = trs.get(5).select("tr > td");
                    //专业|方向
                    Elements tr6_tds = trs.get(6).select("tr > td");
                    //校区|班级
                    Elements tr11_tds = trs.get(11).select("tr > td");

                    String sex = tr2_tds.get(3).text();
                    String work = tr4_tds.get(3).text();
                    String mClass = tr11_tds.get(3).text();
                    String major = tr6_tds.get(1).text();
                    String yuan = tr5_tds.get(3).text();
                    String school = tr11_tds.get(1).text();

                    info_number.setText("学号：" + UserManager.getInstance().getAccount());
                    info_sex.setText("性别：" + sex);
                    info_work.setText("学历：" + work);
                    info_class.setText("班级：" + mClass);
                    info_major.setText("专业：" + major);
                    info_yuan.setText("院系：" + yuan);
                    info_school.setText("校区：" + school + "校区");
                } catch (Exception e) {
                    LogUtils.e("学籍信息解析失败",response);
                }

            }

            @Override
            public void onFailure(String error) {

            }
        });
    }

}
