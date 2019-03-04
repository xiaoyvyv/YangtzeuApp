package com.yangtzeu.model.imodel;

import android.app.Activity;

import com.yangtzeu.ui.view.VoaView;

public interface IVoaModel {
    void getVoaTitleList(Activity activity, VoaView view);

    void getNormalVoaTitleList(Activity activity, VoaView view);

}
