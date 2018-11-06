package com.yangtzeu.model;

import android.app.Activity;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lib.subutil.GsonUtils;
import com.yangtzeu.R;
import com.yangtzeu.entity.MessageBean;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.model.imodel.IFeedBackModel;
import com.yangtzeu.ui.view.FeedBackView;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;
import com.yangtzeu.utils.PostUtils;

import java.util.Objects;

import okhttp3.FormBody;
import okhttp3.Request;

public class FeedBackModel implements IFeedBackModel {
    @Override
    public void sendSuggestion(Activity activity, final FeedBackView view) {
        final String qq = Objects.requireNonNull(view.getQQ().getText()).toString().trim();
        String suggestion = Objects.requireNonNull(view.getSuggestionText().getText()).toString().trim();
        if (suggestion.isEmpty()) {
            view.getSuggestionText().setError(activity.getString(R.string.input_suggestion));
            view.getSuggestionText().requestFocus();
            return;
        }
        if (qq.isEmpty()) {
            view.getSuggestionText().setError(activity.getString(R.string.input_qq));
            view.getSuggestionText().requestFocus();
            return;
        }
        KeyboardUtils.hideSoftInput(view.getQQ());

        ToastUtils.showShort("建议派送中...");

        final String content = MyUtils.filterEmoji(suggestion);
        final String name = SPUtils.getInstance("user_info").getString("name");
        final String number = SPUtils.getInstance("user_info").getString("number");
        final String mClass = SPUtils.getInstance("user_info").getString("class");
        FormBody formBody = new FormBody.Builder()
                .add("master", name)
                .add("content", content)
                .add("master_id", number)
                .add("class", mClass)
                .add("email", qq + "@qq.com")
                .build();

        Request request = new Request.Builder()
                .post(formBody)
                .url(Url.Yangtzeu_App_FeedBack+"?action=add")
                .build();

        OkHttp.do_Post(request, new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                MessageBean bean = GsonUtils.fromJson(response, MessageBean.class);
                String info = bean.getInfo();
                if (info.equals("建议发送成功")) {
                    ToastUtils.showShort(R.string.suggestion_success);
                    String text = "<h4><b>新长大助手用户反馈</b></h4>" +
                            "<p><b>亲爱的：" + name + "同学，您好！</b></p><br>" +
                            "<p><b>你的反馈内容已经收到！我会在0个到n个工作日内回复你的遇到的问题或建议！</b></p><br>" +
                            "<p><b>谢谢支持！</b></p><br><br>" +
                            "<p><b>欢迎加入我的技术交流群：<a href=\"http://qm.qq.com/cgi-bin/qm/qr?k=dezJxL7E5dTPk0-q5Is6tKA8mPHgIfxy\">小玉开发中心交流群</a></b></p><br>" +
                            "<p><b>CopyRight@小玉</b></p>";
                    PostUtils.sendMessage(qq,text);

                    String to_xy = "<h4><b>新长大助手用户反馈</b></h4>" +
                            "<p><b>反馈人姓名：" + name + "</b></p>" +
                            "<p><b>反馈人班级：" + mClass + "</b></p>" +
                            "<p><b>反馈人学号：" + number + "</b></p>" +
                            "<p><b>反馈人企鹅：" + qq + "</b></p>" +
                            "<p><b>管理员操作：<a href=\"mailto:" + qq + "@qq.com?subject=新长大助手-用户反馈查阅通知回复\">给"+name+"回复邮件</a></b></p>"+
                            "<h4><b>反馈的内容：" + content + "</b></h4>" ;
                    PostUtils.sendMessage("1223414335",to_xy);

                    view.getSuggestionText().setText(null);
                    view.getQQ().setText(null);
                } else {
                    ToastUtils.showShort(info);
                }
            }
            @Override
            public void onFailure(String error) {
                ToastUtils.showShort(R.string.suggestion_error);
            }
        });

    }
}
