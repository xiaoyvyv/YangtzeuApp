package com.yangtzeu.model;

import android.app.Activity;

import com.blankj.utilcode.util.ToastUtils;
import com.lib.subutil.GsonUtils;
import com.yangtzeu.R;
import com.yangtzeu.entity.GameBean;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.model.imodel.IGameModel;
import com.yangtzeu.ui.adapter.GameAdapter;
import com.yangtzeu.ui.view.GameView;
import com.yangtzeu.url.Url;

public class GameModel implements IGameModel {
    @Override
    public void loadGames(final Activity activity, final GameView view) {
        OkHttp.do_Get(Url.Yangtzeu_App_Game, new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                GameBean gameBean = GsonUtils.fromJson(response, GameBean.class);
                GameAdapter adapter = view.getAdapter();
                adapter.setData(gameBean);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showShort(R.string.load_error);
            }
        });
    }
}
