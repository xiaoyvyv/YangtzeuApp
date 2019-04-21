package com.yangtzeu.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yangtzeu.R;
import com.yangtzeu.entity.GameBean;
import com.yangtzeu.utils.GoogleUtils;
import com.yangtzeu.utils.MyUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2018/4/12.
 *
 * @author 王怀玉
 * @explain MoreAdapter
 */

public class GameAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<GameBean.GamesBean> appBeans = new ArrayList<>();

    public GameAdapter(Context context) {
        this.context = context;
    }

    public void setData(GameBean appBean) {
        this.appBeans.addAll(appBean.getGames());
    }

    public void addData(int position, GameBean.GamesBean bean) {
        this.appBeans.add(position, bean);
        notifyItemInserted(position);

    }

    public void clear() {
        appBeans.clear();
    }


    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_game_item, parent, false));
        } else {
            return new AdViewHolder(LayoutInflater.from(context).inflate(R.layout.view_ad_container, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        GameBean.GamesBean itemBean = appBeans.get(position);
        if (itemBean.isAd()) {
            final AdViewHolder holder = (AdViewHolder) viewHolder;

            //final AdView adView = GoogleUtils.newBannerView((Activity) context, AdSize.LARGE_BANNER);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    holder.adView.loadAd(GoogleUtils.getRequest());
                }
            }, 500);
        } else {
            ViewHolder holder = (ViewHolder) viewHolder;
            final String name = itemBean.getName();
            final String url = itemBean.getUrl();
            final String image = itemBean.getImage();

            holder.name.setText(name);
            MyUtils.loadImage(context, holder.imageView, image);
            holder.onClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyUtils.openUrl(context, url, true);
                }
            });
        }

    }

    @Override
    public long getItemId(int i) {
        return super.getItemId(i);
    }

    @Override
    public int getItemCount() {
        return appBeans.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (appBeans.get(position).isAd()) {
            return 1;
        } else {
            return 0;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        FrameLayout googleView;
        TextView name;
        View onClick;
        FloatingActionButton fab;

        ViewHolder(View view) {
            super(view);
            fab = view.findViewById(R.id.right);
            name = view.findViewById(R.id.name);
            googleView = view.findViewById(R.id.googleView);
            imageView = view.findViewById(R.id.imageView);
            onClick = view.findViewById(R.id.onClick);
        }
    }

    class AdViewHolder extends RecyclerView.ViewHolder {
        ViewGroup cardView;
        AdView adView;

        AdViewHolder(View view) {
            super(view);
            adView = view.findViewById(R.id.adView);
            cardView = view.findViewById(R.id.cardView);
        }
    }
}