package com.yangtzeu.model;

import android.app.Activity;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.yangtzeu.R;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.model.imodel.IFeedBackModel;
import com.yangtzeu.ui.view.FeedBackView;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;
import com.yangtzeu.utils.PostUtils;

import java.util.Objects;

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

        final String str = SPUtils.getInstance("user_info").getString("number") + "%" + MyUtils.filterEmoji(suggestion);
        String url = Url.Yangtzeu_App_FeedBack + "?text=" + str + "&email=" + qq + "@qq.com";
        OkHttp.do_Get(url, new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                if (response.contains("提交出错")) {
                    ToastUtils.showShort(R.string.suggestion_error);
                } else {
                    ToastUtils.showShort(R.string.suggestion_success);
                    String text = "<h2>新长大助手用户反馈结果</h2><br><br><h4>你的反馈已经收到，谢谢您的支持！</h4><br>欢迎加入我们<a href=\"http://qm.qq.com/cgi-bin/qm/qr?k=dezJxL7E5dTPk0-q5Is6tKA8mPHgIfxy\">小玉开发中心交流群</a>";
                    PostUtils.sendMessage(qq,text);
                    String to_xy = "<h2>新长大助手用户反馈</h2><h2>"+str+"</h2><h4><a href=\"tencent://message/?uin="+qq+"&amp;Site=tool.520101.com&amp;Menu=yes\">用户QQ："+qq+"</a><h4>";
                    PostUtils.sendMessage("1223414335",to_xy);
                    view.getSuggestionText().setText(null);
                    view.getQQ().setText(null);
                }
            }
            @Override
            public void onFailure(String error) {
                ToastUtils.showShort(R.string.suggestion_error);
            }
        });

    }
}
