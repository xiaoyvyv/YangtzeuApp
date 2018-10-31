package com.yangtzeu.model;


import android.app.Activity;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lib.subutil.GsonUtils;
import com.yangtzeu.R;
import com.yangtzeu.entity.BoardBean;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.model.imodel.IBoardModel;
import com.yangtzeu.ui.view.BoardView;
import com.yangtzeu.url.Url;

public class BoardModel implements IBoardModel {

    @Override
    public void loadMessageData(Activity activity, final BoardView view) {
         final int old_page = view.getAdapter().getItemCount();
        OkHttp.do_Get(Url.Yangtzeu_App_ShowMessage + view.getPage(), new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                view.getRefresh().finishRefresh();
                view.getRefresh().finishLoadMore();
                BoardBean mess = GsonUtils.fromJson(response, BoardBean.class);
                if (!mess.getCode().equals("404")) {
                    if (!mess.getResult().isEmpty()) {
                        if (view.isRefresh()) {
                            view.getAdapter().clear();
                            view.getBoardData().clear();
                            view.getAdapter().notifyDataSetChanged();

                            view.getBoardData().addAll(mess.getResult());
                            view.getAdapter().SetDate(view.getBoardData());
                            view.getAdapter().notifyItemRangeChanged(0, view.getAdapter().getItemCount());
                        } else {
                            view.getBoardData().addAll(mess.getResult());
                            view.getAdapter().SetDate(view.getBoardData());
                            view.getAdapter().notifyItemRangeChanged(old_page, view.getAdapter().getItemCount());
                        }

                    } else {
                        view.setPage(view.getPage() - 30);
                        ToastUtils.showShort("没有更多啦");
                    }
                }
            }

            @Override
            public void onFailure(String error) {
                LogUtils.e(error);
                view.setPage(view.getPage() - 30);
                ToastUtils.showShort(R.string.try_again);
                view.getRefresh().finishRefresh();
                view.getRefresh().finishLoadMore();
            }
        });

    }
}
