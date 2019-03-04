package com.yangtzeu.ui.view;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yangtzeu.ui.view.base.BaseView;

import androidx.cardview.widget.CardView;

public interface CetView extends BaseView {
    String getCetInfo();

    TextView getCet4DateView();

    TextView getCetTitle();

    TextView getCet6DateView();

    LinearLayout getCetHistoryContainer();

    EditText getStudentName();

    EditText getCetNumber();

    EditText getYzmEditView();

    ImageView getYzmImage();

    ImageView getCardYzmImage();

    CardView getQueryGradeView();

    EditText getCardName();

    EditText getCardNumber();

    EditText getCardYzm();

    void setCetInfo(String cet);


}
