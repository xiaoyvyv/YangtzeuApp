package com.yangtzeu.model;

import android.app.Activity;
import android.app.ProgressDialog;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lib.subutil.GsonUtils;
import com.yangtzeu.R;
import com.yangtzeu.entity.MessageBean;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.listener.OnUpLoadListener;
import com.yangtzeu.model.imodel.IShopAddModel;
import com.yangtzeu.ui.view.ShopAddView;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;
import com.yangtzeu.utils.UpLoadUtils;

import okhttp3.Request;

public class ShopAddModel implements IShopAddModel {

    @Override
    public void sendGoods(final Activity activity, final ShopAddView view) {
        final String title = view.getMyTitle().getText().toString().trim();
        final String des = view.getDes().getText().toString().trim();
        final String price = view.getPrice().getText().toString().trim();
        String path = view.getImagePath();
        final String kind = view.getKind();

        if (ObjectUtils.isEmpty(title)) {
            view.getMyTitle().setError(activity.getString(R.string.input_goods_title));
            view.getMyTitle().requestFocus();
            return;
        }
        if (ObjectUtils.isEmpty(des)) {
            view.getDes().setError(activity.getString(R.string.input_goods_des));
            view.getDes().requestFocus();
            return;
        }
        if (ObjectUtils.isEmpty(price)) {
            view.getPrice().setError(activity.getString(R.string.input_goods_price));
            view.getPrice().requestFocus();
            return;
        }
        if (ObjectUtils.isEmpty(path)) {
            ToastUtils.showShort(activity.getString(R.string.choose_goods_image));
            return;
        }

        if (ObjectUtils.isEmpty(kind)) {
            ToastUtils.showShort(activity.getString(R.string.choose_goods_kind));
            return;
        }
        view.getSend().setEnabled(false);
        view.getSend().setText("宝贝发布中");
        KeyboardUtils.hideSoftInput(activity);

        UpLoadUtils.upLoadFile( path,"image_shop", new OnUpLoadListener() {
            @Override
            public void onUploading(int progress) {
                view.getProgressView().setProgress(progress);
            }

            @Override
            public void onSuccess(String path) {
                ToastUtils.showShort("图片上传成功，即将发布宝贝");
                String master = SPUtils.getInstance("user_info").getString("name");
                String master_id = SPUtils.getInstance("user_info").getString("number");
                String wechat = SPUtils.getInstance("user_info").getString("wechat","whysbelief");
                String qq = SPUtils.getInstance("user_info").getString("qq", "default_header");
                String phone = SPUtils.getInstance("user_info").getString("phone","0");
                Request request = Url.getAddGoodsUrl(title, des, master, master_id, kind, price, phone, qq, wechat, path);

                final ProgressDialog dialog = MyUtils.getProgressDialog(activity, "宝贝发布中");
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
                        ToastUtils.showShort(R.string.send_error);
                    }
                });
            }

            @Override
            public void onFailure(String error) {
                view.getSend().setEnabled(true);
                view.getSend().setText("发布失败，重新发布");
                ToastUtils.showShort(error);
            }
        });
    }




}
