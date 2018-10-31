package com.yangtzeu.entity;

import java.util.List;

public class WeatherBean {

    /**
     * msg : success
     * result : [{"airCondition":"良","airQuality":{"aqi":84,"city":"荆州","district":"荆州","fetureData":[{"aqi":60,"date":"2018-10-08","quality":"良"},{"aqi":69,"date":"2018-10-09","quality":"良"},{"aqi":90,"date":"2018-10-10","quality":"良"},{"aqi":97,"date":"2018-10-11","quality":"良"},{"aqi":103,"date":"2018-10-12","quality":"轻度污染"},{"aqi":70,"date":"2018-10-13","quality":"良"}],"hourData":[{"aqi":84,"dateTime":"2018-10-07 11:00:00"},{"aqi":85,"dateTime":"2018-10-07 10:00:00"},{"aqi":84,"dateTime":"2018-10-07 09:00:00"},{"aqi":82,"dateTime":"2018-10-07 08:00:00"},{"aqi":82,"dateTime":"2018-10-07 07:00:00"},{"aqi":82,"dateTime":"2018-10-07 06:00:00"},{"aqi":84,"dateTime":"2018-10-07 05:00:00"},{"aqi":90,"dateTime":"2018-10-07 04:00:00"},{"aqi":100,"dateTime":"2018-10-07 03:00:00"},{"aqi":113,"dateTime":"2018-10-07 02:00:00"},{"aqi":114,"dateTime":"2018-10-07 01:00:00"},{"aqi":105,"dateTime":"2018-10-07 00:00:00"},{"aqi":98,"dateTime":"2018-10-06 23:00:00"},{"aqi":94,"dateTime":"2018-10-06 22:00:00"},{"aqi":86,"dateTime":"2018-10-06 21:00:00"},{"aqi":70,"dateTime":"2018-10-06 20:00:00"},{"aqi":60,"dateTime":"2018-10-06 19:00:00"},{"aqi":90,"dateTime":"2018-10-06 18:00:00"},{"aqi":105,"dateTime":"2018-10-06 17:00:00"},{"aqi":107,"dateTime":"2018-10-06 16:00:00"},{"aqi":107,"dateTime":"2018-10-06 15:00:00"},{"aqi":104,"dateTime":"2018-10-06 14:00:00"},{"aqi":89,"dateTime":"2018-10-06 13:00:00"},{"aqi":71,"dateTime":"2018-10-06 12:00:00"}],"no2":52,"pm10":117,"pm25":59,"province":"湖北","quality":"良","so2":21,"updateTime":"2018-10-07 12:00:00"},"city":"荆州","coldIndex":"","date":"2018-10-07","distrct":"荆州","dressingIndex":"","exerciseIndex":"","future":[{"date":"2018-10-07","dayTime":"多云","night":"小雨","temperature":"27°C / 17°C","week":"今天","wind":"东北风 小于3级"},{"date":"2018-10-08","dayTime":"小雨","night":"小雨","temperature":"23°C / 16°C","week":"星期一","wind":"北风 小于3级"},{"date":"2018-10-09","dayTime":"小雨","night":"多云","temperature":"21°C / 13°C","week":"星期二","wind":"西北风 小于3级"},{"date":"2018-10-10","dayTime":"多云","night":"多云","temperature":"21°C / 12°C","week":"星期三","wind":"东北风 3～4级"},{"date":"2018-10-11","dayTime":"晴","night":"晴","temperature":"21°C / 11°C","week":"星期四","wind":"东北风 小于3级"},{"date":"2018-10-12","dayTime":"晴","night":"多云","temperature":"23°C / 11°C","week":"星期五","wind":"东北风 小于3级"},{"date":"2018-10-13","dayTime":"阵雨","night":"阵雨","temperature":"21°C / 13°C","week":"星期六","wind":"东北偏北风 2级"},{"date":"2018-10-14","dayTime":"阵雨","night":"阵雨","temperature":"21°C / 14°C","week":"星期日","wind":"北风 2级"},{"date":"2018-10-15","dayTime":"阵雨","night":"阴天","temperature":"20°C / 14°C","week":"星期一","wind":"北风 3级"},{"date":"2018-10-16","dayTime":"阴天","night":"零散阵雨","temperature":"22°C / 14°C","week":"星期二","wind":"北风 3级"}],"humidity":"湿度：37%","pollutionIndex":"84","province":"湖北","sunrise":"05:42","sunset":"19:34","temperature":"25℃","time":"12:42","updateTime":"20181007130153","washIndex":"","weather":"晴","week":"周日","wind":"北风2级"}]
     * retCode : 200
     */

    private String msg;
    private String retCode;
    private List<ResultBean> result;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * airCondition : 良
         * airQuality : {"aqi":84,"city":"荆州","district":"荆州","fetureData":[{"aqi":60,"date":"2018-10-08","quality":"良"},{"aqi":69,"date":"2018-10-09","quality":"良"},{"aqi":90,"date":"2018-10-10","quality":"良"},{"aqi":97,"date":"2018-10-11","quality":"良"},{"aqi":103,"date":"2018-10-12","quality":"轻度污染"},{"aqi":70,"date":"2018-10-13","quality":"良"}],"hourData":[{"aqi":84,"dateTime":"2018-10-07 11:00:00"},{"aqi":85,"dateTime":"2018-10-07 10:00:00"},{"aqi":84,"dateTime":"2018-10-07 09:00:00"},{"aqi":82,"dateTime":"2018-10-07 08:00:00"},{"aqi":82,"dateTime":"2018-10-07 07:00:00"},{"aqi":82,"dateTime":"2018-10-07 06:00:00"},{"aqi":84,"dateTime":"2018-10-07 05:00:00"},{"aqi":90,"dateTime":"2018-10-07 04:00:00"},{"aqi":100,"dateTime":"2018-10-07 03:00:00"},{"aqi":113,"dateTime":"2018-10-07 02:00:00"},{"aqi":114,"dateTime":"2018-10-07 01:00:00"},{"aqi":105,"dateTime":"2018-10-07 00:00:00"},{"aqi":98,"dateTime":"2018-10-06 23:00:00"},{"aqi":94,"dateTime":"2018-10-06 22:00:00"},{"aqi":86,"dateTime":"2018-10-06 21:00:00"},{"aqi":70,"dateTime":"2018-10-06 20:00:00"},{"aqi":60,"dateTime":"2018-10-06 19:00:00"},{"aqi":90,"dateTime":"2018-10-06 18:00:00"},{"aqi":105,"dateTime":"2018-10-06 17:00:00"},{"aqi":107,"dateTime":"2018-10-06 16:00:00"},{"aqi":107,"dateTime":"2018-10-06 15:00:00"},{"aqi":104,"dateTime":"2018-10-06 14:00:00"},{"aqi":89,"dateTime":"2018-10-06 13:00:00"},{"aqi":71,"dateTime":"2018-10-06 12:00:00"}],"no2":52,"pm10":117,"pm25":59,"province":"湖北","quality":"良","so2":21,"updateTime":"2018-10-07 12:00:00"}
         * city : 荆州
         * coldIndex :
         * date : 2018-10-07
         * distrct : 荆州
         * dressingIndex :
         * exerciseIndex :
         * future : [{"date":"2018-10-07","dayTime":"多云","night":"小雨","temperature":"27°C / 17°C","week":"今天","wind":"东北风 小于3级"},{"date":"2018-10-08","dayTime":"小雨","night":"小雨","temperature":"23°C / 16°C","week":"星期一","wind":"北风 小于3级"},{"date":"2018-10-09","dayTime":"小雨","night":"多云","temperature":"21°C / 13°C","week":"星期二","wind":"西北风 小于3级"},{"date":"2018-10-10","dayTime":"多云","night":"多云","temperature":"21°C / 12°C","week":"星期三","wind":"东北风 3～4级"},{"date":"2018-10-11","dayTime":"晴","night":"晴","temperature":"21°C / 11°C","week":"星期四","wind":"东北风 小于3级"},{"date":"2018-10-12","dayTime":"晴","night":"多云","temperature":"23°C / 11°C","week":"星期五","wind":"东北风 小于3级"},{"date":"2018-10-13","dayTime":"阵雨","night":"阵雨","temperature":"21°C / 13°C","week":"星期六","wind":"东北偏北风 2级"},{"date":"2018-10-14","dayTime":"阵雨","night":"阵雨","temperature":"21°C / 14°C","week":"星期日","wind":"北风 2级"},{"date":"2018-10-15","dayTime":"阵雨","night":"阴天","temperature":"20°C / 14°C","week":"星期一","wind":"北风 3级"},{"date":"2018-10-16","dayTime":"阴天","night":"零散阵雨","temperature":"22°C / 14°C","week":"星期二","wind":"北风 3级"}]
         * humidity : 湿度：37%
         * pollutionIndex : 84
         * province : 湖北
         * sunrise : 05:42
         * sunset : 19:34
         * temperature : 25℃
         * time : 12:42
         * updateTime : 20181007130153
         * washIndex :
         * weather : 晴
         * week : 周日
         * wind : 北风2级
         */

        private String airCondition;
        private AirQualityBean airQuality;
        private String city;
        private String coldIndex;
        private String date;
        private String distrct;
        private String dressingIndex;
        private String exerciseIndex;
        private String humidity;
        private String pollutionIndex;
        private String province;
        private String sunrise;
        private String sunset;
        private String temperature;
        private String time;
        private String updateTime;
        private String washIndex;
        private String weather;
        private String week;
        private String wind;
        private List<FutureBean> future;

        public String getAirCondition() {
            return airCondition;
        }

        public void setAirCondition(String airCondition) {
            this.airCondition = airCondition;
        }

        public AirQualityBean getAirQuality() {
            return airQuality;
        }

        public void setAirQuality(AirQualityBean airQuality) {
            this.airQuality = airQuality;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getColdIndex() {
            return coldIndex;
        }

        public void setColdIndex(String coldIndex) {
            this.coldIndex = coldIndex;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getDistrct() {
            return distrct;
        }

        public void setDistrct(String distrct) {
            this.distrct = distrct;
        }

        public String getDressingIndex() {
            return dressingIndex;
        }

        public void setDressingIndex(String dressingIndex) {
            this.dressingIndex = dressingIndex;
        }

        public String getExerciseIndex() {
            return exerciseIndex;
        }

        public void setExerciseIndex(String exerciseIndex) {
            this.exerciseIndex = exerciseIndex;
        }

        public String getHumidity() {
            return humidity;
        }

        public void setHumidity(String humidity) {
            this.humidity = humidity;
        }

        public String getPollutionIndex() {
            return pollutionIndex;
        }

        public void setPollutionIndex(String pollutionIndex) {
            this.pollutionIndex = pollutionIndex;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getSunrise() {
            return sunrise;
        }

        public void setSunrise(String sunrise) {
            this.sunrise = sunrise;
        }

        public String getSunset() {
            return sunset;
        }

        public void setSunset(String sunset) {
            this.sunset = sunset;
        }

        public String getTemperature() {
            return temperature;
        }

        public void setTemperature(String temperature) {
            this.temperature = temperature;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getWashIndex() {
            return washIndex;
        }

        public void setWashIndex(String washIndex) {
            this.washIndex = washIndex;
        }

        public String getWeather() {
            return weather;
        }

        public void setWeather(String weather) {
            this.weather = weather;
        }

        public String getWeek() {
            return week;
        }

        public void setWeek(String week) {
            this.week = week;
        }

        public String getWind() {
            return wind;
        }

        public void setWind(String wind) {
            this.wind = wind;
        }

        public List<FutureBean> getFuture() {
            return future;
        }

        public void setFuture(List<FutureBean> future) {
            this.future = future;
        }

        public static class AirQualityBean {
            /**
             * aqi : 84
             * city : 荆州
             * district : 荆州
             * fetureData : [{"aqi":60,"date":"2018-10-08","quality":"良"},{"aqi":69,"date":"2018-10-09","quality":"良"},{"aqi":90,"date":"2018-10-10","quality":"良"},{"aqi":97,"date":"2018-10-11","quality":"良"},{"aqi":103,"date":"2018-10-12","quality":"轻度污染"},{"aqi":70,"date":"2018-10-13","quality":"良"}]
             * hourData : [{"aqi":84,"dateTime":"2018-10-07 11:00:00"},{"aqi":85,"dateTime":"2018-10-07 10:00:00"},{"aqi":84,"dateTime":"2018-10-07 09:00:00"},{"aqi":82,"dateTime":"2018-10-07 08:00:00"},{"aqi":82,"dateTime":"2018-10-07 07:00:00"},{"aqi":82,"dateTime":"2018-10-07 06:00:00"},{"aqi":84,"dateTime":"2018-10-07 05:00:00"},{"aqi":90,"dateTime":"2018-10-07 04:00:00"},{"aqi":100,"dateTime":"2018-10-07 03:00:00"},{"aqi":113,"dateTime":"2018-10-07 02:00:00"},{"aqi":114,"dateTime":"2018-10-07 01:00:00"},{"aqi":105,"dateTime":"2018-10-07 00:00:00"},{"aqi":98,"dateTime":"2018-10-06 23:00:00"},{"aqi":94,"dateTime":"2018-10-06 22:00:00"},{"aqi":86,"dateTime":"2018-10-06 21:00:00"},{"aqi":70,"dateTime":"2018-10-06 20:00:00"},{"aqi":60,"dateTime":"2018-10-06 19:00:00"},{"aqi":90,"dateTime":"2018-10-06 18:00:00"},{"aqi":105,"dateTime":"2018-10-06 17:00:00"},{"aqi":107,"dateTime":"2018-10-06 16:00:00"},{"aqi":107,"dateTime":"2018-10-06 15:00:00"},{"aqi":104,"dateTime":"2018-10-06 14:00:00"},{"aqi":89,"dateTime":"2018-10-06 13:00:00"},{"aqi":71,"dateTime":"2018-10-06 12:00:00"}]
             * no2 : 52
             * pm10 : 117
             * pm25 : 59
             * province : 湖北
             * quality : 良
             * so2 : 21
             * updateTime : 2018-10-07 12:00:00
             */

            private int aqi;
            private String city;
            private String district;
            private int no2;
            private int pm10;
            private int pm25;
            private String province;
            private String quality;
            private int so2;
            private String updateTime;
            private List<FetureDataBean> fetureData;
            private List<HourDataBean> hourData;

            public int getAqi() {
                return aqi;
            }

            public void setAqi(int aqi) {
                this.aqi = aqi;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getDistrict() {
                return district;
            }

            public void setDistrict(String district) {
                this.district = district;
            }

            public int getNo2() {
                return no2;
            }

            public void setNo2(int no2) {
                this.no2 = no2;
            }

            public int getPm10() {
                return pm10;
            }

            public void setPm10(int pm10) {
                this.pm10 = pm10;
            }

            public int getPm25() {
                return pm25;
            }

            public void setPm25(int pm25) {
                this.pm25 = pm25;
            }

            public String getProvince() {
                return province;
            }

            public void setProvince(String province) {
                this.province = province;
            }

            public String getQuality() {
                return quality;
            }

            public void setQuality(String quality) {
                this.quality = quality;
            }

            public int getSo2() {
                return so2;
            }

            public void setSo2(int so2) {
                this.so2 = so2;
            }

            public String getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(String updateTime) {
                this.updateTime = updateTime;
            }

            public List<FetureDataBean> getFetureData() {
                return fetureData;
            }

            public void setFetureData(List<FetureDataBean> fetureData) {
                this.fetureData = fetureData;
            }

            public List<HourDataBean> getHourData() {
                return hourData;
            }

            public void setHourData(List<HourDataBean> hourData) {
                this.hourData = hourData;
            }

            public static class FetureDataBean {
                /**
                 * aqi : 60
                 * date : 2018-10-08
                 * quality : 良
                 */

                private int aqi;
                private String date;
                private String quality;

                public int getAqi() {
                    return aqi;
                }

                public void setAqi(int aqi) {
                    this.aqi = aqi;
                }

                public String getDate() {
                    return date;
                }

                public void setDate(String date) {
                    this.date = date;
                }

                public String getQuality() {
                    return quality;
                }

                public void setQuality(String quality) {
                    this.quality = quality;
                }
            }

            public static class HourDataBean {
                /**
                 * aqi : 84
                 * dateTime : 2018-10-07 11:00:00
                 */

                private int aqi;
                private String dateTime;

                public int getAqi() {
                    return aqi;
                }

                public void setAqi(int aqi) {
                    this.aqi = aqi;
                }

                public String getDateTime() {
                    return dateTime;
                }

                public void setDateTime(String dateTime) {
                    this.dateTime = dateTime;
                }
            }
        }

        public static class FutureBean {
            /**
             * date : 2018-10-07
             * dayTime : 多云
             * night : 小雨
             * temperature : 27°C / 17°C
             * week : 今天
             * wind : 东北风 小于3级
             */

            private String date;
            private String dayTime;
            private String night;
            private String temperature;
            private String week;
            private String wind;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getDayTime() {
                return dayTime;
            }

            public void setDayTime(String dayTime) {
                this.dayTime = dayTime;
            }

            public String getNight() {
                return night;
            }

            public void setNight(String night) {
                this.night = night;
            }

            public String getTemperature() {
                return temperature;
            }

            public void setTemperature(String temperature) {
                this.temperature = temperature;
            }

            public String getWeek() {
                return week;
            }

            public void setWeek(String week) {
                this.week = week;
            }

            public String getWind() {
                return wind;
            }

            public void setWind(String wind) {
                this.wind = wind;
            }
        }
    }
}
