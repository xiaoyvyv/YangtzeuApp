package com.yangtzeu.model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.view.View;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.lib.subutil.ClipboardUtils;
import com.yangtzeu.R;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.listener.OnResultListener;
import com.yangtzeu.model.imodel.IKGMode;
import com.yangtzeu.ui.view.KGView;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MediaPlayUtil;
import com.yangtzeu.utils.MyUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import okhttp3.Request;

public class KGModel implements IKGMode {
    @Override
    public void playMusic(final Activity activity, final KGView view) {
        final String text = Objects.requireNonNull(view.getKgLink());
        if (ObjectUtils.isEmpty(text)) {
            ToastUtils.showShort(R.string.input_share_link);
            return;
        }

        Request request = new Request.Builder()
                .addHeader("Referer", "http://3g.gljlw.com/diy/kge.php")
                .url(Url.Url_Music_Kg+text)
                .build();
        OkHttp.do_Post(request, new OnResultStringListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(final String response) {
                ToastUtils.showShort(R.string.load_success);
                Document document = Jsoup.parse(response);

                String[] titles = document.select(".content").text().split(" ");
                String title = titles[1] + "\n" + titles[2];
                final String url = document.select(".content > audio > source").attr("src");
                String image_url = document.select(".content > img").attr("src");

                view.getTruePath().requestFocus();
                view.getTruePath().setText(title + "\n点击复制下载地址");
                view.getTruePath().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtils.showShort(R.string.copy_link_right);
                        ClipboardUtils.copyText(url);
                    }
                });
                Glide.with(activity).load(image_url).into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        view.getMusicView().setVisibility(View.VISIBLE);
                        Bitmap bitmap = ImageUtils.drawable2Bitmap(resource);
                        view.getMusicView().setPictureResource(bitmap);
                        view.getMusicView().setPlaying(true);
                    }
                });

                view.getMusicView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (MediaPlayUtil.getInstance().isPlaying()) {
                            MediaPlayUtil.getInstance().pause();
                            view.getMusicView().setPlaying(false);
                        } else {
                            MediaPlayUtil.getInstance().start();
                            view.getMusicView().setPlaying(true);
                        }
                    }
                });

                final String finalImage_url = image_url;
                view.getMusicView().setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        MyUtils.saveFile(activity, finalImage_url, "A_Tool/Download/Image/", FileUtils.getFileName(finalImage_url));
                        return true;
                    }
                });
            }
            @Override
            public void onFailure(String error) {
                ToastUtils.showShort(R.string.load_error);
            }
        });
        loadMusic(activity, view, 1);
    }

    @Override
    public void downloadMusic(final Activity activity, KGView view) {
        loadMusic(activity, view, 0);
    }


    @Override
    public void loadMusic(final Activity activity, final KGView view, final int which) {
        final String text = Objects.requireNonNull(view.getKgLink());
        if (ObjectUtils.isEmpty(text)) {
            ToastUtils.showShort(R.string.input_share_link);
            return;
        }
        if (!MediaPlayUtil.getInstance().isPlaying() || which == 0) {
            ToastUtils.showLong(R.string.loading);

            Request request = new Request.Builder()
                    .addHeader("Referer", "http://3g.gljlw.com/diy/kge.php")
                    .url(Url.Url_Music_Kg+text)
                    .build();
            OkHttp.do_Post(request, new OnResultStringListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(String response) {
                    Document document = Jsoup.parse(response);

                    String[] titles = document.select(".content").text().split(" ");
                    String title = titles[1] + "\n" + titles[2];
                    final String url = document.select(".content > audio > source").attr("src");
                    String image_url = document.select(".content > img").attr("src");

                    LogUtils.e( document.select(".content").toString(),title, url, image_url);

                    view.getTruePath().setText(title + "\n点击复制下载地址");
                    view.getTruePath().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ToastUtils.showShort(R.string.copy_link_right);
                            ClipboardUtils.copyText(url);
                        }
                    });
                    switch (which) {
                        case 0:
                            //下载
                            MyUtils.saveFile(activity, url, "A_Tool/Download/Music/", FileUtils.getFileName(url) + ".mp3");
                            break;
                        case 1:
                            //播放
                            MediaPlayUtil.getInstance().play(url);
                            MediaPlayUtil.getInstance().setPlayOnCompleteListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    ToastUtils.showShort(R.string.load_success);
                                }
                            });
                            break;
                    }
                }

                @Override
                public void onFailure(String error) {
                    ToastUtils.showShort(R.string.load_error);
                }
            });
        } else {
            ToastUtils.showShort(R.string.is_playing);
        }
    }

}
