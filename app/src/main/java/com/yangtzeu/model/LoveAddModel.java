package com.yangtzeu.model;

import android.app.Activity;
import android.app.ProgressDialog;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lib.subutil.GsonUtils;
import com.yangtzeu.R;
import com.yangtzeu.entity.MessageBean;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.listener.OnUpLoadListener;
import com.yangtzeu.model.imodel.ILoveAddModel;
import com.yangtzeu.ui.view.LoveAddView;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;
import com.yangtzeu.utils.UpLoadUtils;

import okhttp3.Request;

public class LoveAddModel implements ILoveAddModel {
    @Override
    public void sendLove(final Activity activity, final LoveAddView view) {
        final String nameTa = view.getNameTa().getText().toString().trim();
        final String des = view.getDes().getText().toString().trim();
        final String qqTa = view.getQQTa().getText().toString().trim();
        String path = view.getImagePath();
        final String music = view.getMusic();
        final String isHide = String.valueOf(view.isHide());

        if (ObjectUtils.isEmpty(nameTa)) {
            view.getNameTa().setError(activity.getString(R.string.input_love_name));
            view.getNameTa().requestFocus();
            return;
        }
        if (ObjectUtils.isEmpty(des)) {
            view.getDes().setError(activity.getString(R.string.input_love_des));
            view.getDes().requestFocus();
            return;
        }
        if (ObjectUtils.isEmpty(qqTa)) {
            view.getQQTa().setError(activity.getString(R.string.input_love_qq));
            view.getQQTa().requestFocus();
            return;
        }
        if (ObjectUtils.isEmpty(path)) {
            ToastUtils.showShort(activity.getString(R.string.choose_love_image));
            return;
        }
        if (ObjectUtils.isEmpty(music)) {
            ToastUtils.showShort(activity.getString(R.string.choose_love_music));
            return;
        }
        view.getSend().setEnabled(false);
        view.getSend().setText("表白发布中");
        KeyboardUtils.hideSoftInput(activity);


        UpLoadUtils.upLoadFile(path, "image_love", new OnUpLoadListener() {
            @Override
            public void onUploading(int progress) {
                view.getProgressView().setProgress(progress);
            }

            @Override
            public void onSuccess(String path) {
                ToastUtils.showShort("图片上传成功，即将发布表白");
                String master = SPUtils.getInstance("user_info").getString("name");
                String master_id = SPUtils.getInstance("user_info").getString("number");
                String qq = SPUtils.getInstance("user_info").getString("qq", "default_header");

                Request request = Url.getAddLoveUrl(nameTa, des, master, master_id, music, qqTa, qq, isHide, path);

                final ProgressDialog dialog = MyUtils.getProgressDialog(activity, "表白发布中呢");
                dialog.show();

                OkHttp.do_Post(request, new OnResultStringListener() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        activity.onBackPressed();
                        MessageBean bean = GsonUtils.fromJson(response, MessageBean.class);
                        ToastUtils.showShort(bean.getInfo());
                    }

                    @Override
                    public void onFailure(String error) {
                        dialog.dismiss();
                        view.getSend().setEnabled(true);
                        view.getSend().setText("表白发布失败，重新发布");
                        ToastUtils.showShort(error);
                    }
                });
            }

            @Override
            public void onFailure(String error) {
                view.getSend().setEnabled(true);
                view.getSend().setText("表白发布失败，重新发布");
                ToastUtils.showShort(error);
            }
        });

    }
}

