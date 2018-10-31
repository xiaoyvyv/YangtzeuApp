package com.yangtzeu.ui.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.yangtzeu.R;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.ui.activity.base.BaseActivity;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

public class BoardAddActivity extends BaseActivity {
    private final static String TAG = "【BoardAddActivity】";
    private EditText mess_text;
    private String InputColor = "#ffffff";
    private String InputImg = "http://";
    private Toolbar toolbar;


    private CardView Test;
    private TextView change_text_bg;
    private Button send;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_add);
        init();
        MyUtils.setToolbarBackToHome(this, toolbar);
    }


    @Override
    public void findViews() {
        toolbar = this.findViewById(R.id.toolbar);
        mess_text = findViewById(R.id.mess_write);
        Test = findViewById(R.id.TextBg);
        change_text_bg = findViewById(R.id.change_text_bg);
        send = findViewById(R.id.send);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setEvents() {
        change_text_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int red = Integer.parseInt(MyUtils.getRand(150)) + 100;
                int green = Integer.parseInt(MyUtils.getRand(150)) + 100;
                int blue = Integer.parseInt(MyUtils.getRand(150)) + 100;
                InputColor = "#" + MyUtils.formattingH(red) + MyUtils.formattingH(green) + MyUtils.formattingH(blue);
                Test.setCardBackgroundColor(Color.parseColor(InputColor));
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取出本地保存的路径
                String qq = SPUtils.getInstance("user_info").getString("qq", "1223414335");
                String student_name = SPUtils.getInstance("user_info").getString("number");
                String name = SPUtils.getInstance("user_info").getString("name");
                String mClass = SPUtils.getInstance("user_info").getString("class");
                if (student_name.equals("201603246")) {
                    name = "小编";
                }
                String my_text = mess_text.getText().toString().trim();
                if (my_text.isEmpty()) {
                    mess_text.setError(getString(R.string.please_input_text));
                    mess_text.requestFocus();
                } else {
                    my_text = my_text.replace("&", "‖");
                    my_text = my_text.replace("\n", "~");

                    KeyboardUtils.hideSoftInput(BoardAddActivity.this);

                    ToastUtils.showLong(R.string.sending);
                    mess_text.setText(null);
                    //开始发表
                    InputColor = InputColor.replace("#", "@");

                    sendBoardMessage(MyUtils.filterEmoji(my_text), name, mClass, student_name, qq, InputColor + "∩" + InputImg);
                }
            }
        });

    }


    private void sendBoardMessage(String luiYanText, String niChen, String Class, String geQian, String headImg, String inputColor) {
        String FootUrl = "?text=" + luiYanText + "&name=" + niChen + "&class=" + Class + "&geqian=" + geQian + "&image=" + headImg + "&background=" + inputColor;
        String url = Url.Yangtzeu_App_Message + FootUrl;
        OkHttp.do_Get(url, new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                if (response.isEmpty()) {
                    ToastUtils.showShort(R.string.send_success);
                    MyUtils.getAlert(BoardAddActivity.this, getString(R.string.send_success), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            BoardAddActivity.this.onBackPressed();
                        }
                    }).show();
                } else {
                    ToastUtils.showShort(R.string.send_error);
                }
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showShort(R.string.send_error);

            }
        });
    }

}
