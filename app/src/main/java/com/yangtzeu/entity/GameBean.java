package com.yangtzeu.entity;

import java.util.List;

public class GameBean {
    private List<GamesBean> games;

    public List<GamesBean> getGames() {
        return games;
    }

    public void setGames(List<GamesBean> games) {
        this.games = games;
    }

    public static class GamesBean {
        /**
         * name : 无尽之旅
         * url : http://cdn.games.imlianpu.com/platform/ellojump/index.html
         */

        private String name;
        private String url;
        private String image = "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=560302979,384745884&fm=26&gp=0.jpg";
        private boolean isAd = false;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public boolean isAd() {
            return isAd;
        }

        public void setAd(boolean ad) {
            isAd = ad;
        }
    }
}
