package com.yangtzeu.model;

import android.app.Activity;
import android.view.View;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lib.subutil.GsonUtils;
import com.yangtzeu.R;
import com.yangtzeu.entity.TranslateBean;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.model.imodel.ITranslateModel;
import com.yangtzeu.ui.view.TranslateView;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MediaPlayUtil;

public class TranslateModel implements ITranslateModel {

    @Override
    public void translate(Activity activity, final TranslateView view) {
        String fromText = view.getFromText().getText().toString().trim();
        if (StringUtils.isEmpty(fromText)) {
            ToastUtils.showShort(R.string.input_translate);
            return;
        }
        view.getDialog().show();
        OkHttp.do_Get(Url.Yangtzeu_Translate + fromText, new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                view.getDialog().dismiss();
                TranslateBean bean = GsonUtils.fromJson(response, TranslateBean.class);
                final String mp3 = bean.getMp3();

                String data = bean.getData();
                view.getToText().setText(data);

                view.doPlayView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MediaPlayUtil instance = MediaPlayUtil.getInstance();
                        instance.setLooping(false);
                        instance.play(mp3);
                    }
                });

            }
            @Override
            public void onFailure(String error) {
                ToastUtils.showShort(R.string.try_again);
                view.getDialog().dismiss();
            }
        });

    }
}
