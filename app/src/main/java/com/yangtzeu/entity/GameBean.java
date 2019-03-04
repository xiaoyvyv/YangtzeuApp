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
    }
}
