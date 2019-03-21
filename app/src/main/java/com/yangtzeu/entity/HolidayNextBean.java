package com.yangtzeu.entity;

import java.util.List;

public class HolidayNextBean {

    /**
     * countdown : 283.4379513888889
     * holiday : [{"name":"元旦","desc":"2018年12月30日至2019年1月1日放假调休，共3天。2018年12月29日（星期六）上班。","festival":1546272000000,"days":4},{"name":"除夕","desc":"除夕","festival":1549209600000,"days":1},{"name":"春节","desc":"2月4日至10日放假调休，共7天。2月2日（星期六）、2月3日（星期日）上班","festival":1549296000000,"days":9}]
     * holidaylist : [{"name":"元旦","startday":"2019-1-1"},{"name":"除夕","startday":"2019-2-4"},{"name":"春节","startday":"2019-2-4"},{"name":"清明节","startday":"2019-4-5"},{"name":"劳动节","startday":"2019-5-1"},{"name":"端午节","startday":"2019-6-7"},{"name":"中秋节","startday":"2019-9-13"},{"name":"国庆节","startday":"2019-10-1"}]
     */

    private double countdown;
    private List<HolidayBean> holiday;
    private List<HolidaylistBean> holidaylist;

    public double getCountdown() {
        return countdown;
    }

    public void setCountdown(double countdown) {
        this.countdown = countdown;
    }

    public List<HolidayBean> getHoliday() {
        return holiday;
    }

    public void setHoliday(List<HolidayBean> holiday) {
        this.holiday = holiday;
    }

    public List<HolidaylistBean> getHolidaylist() {
        return holidaylist;
    }

    public void setHolidaylist(List<HolidaylistBean> holidaylist) {
        this.holidaylist = holidaylist;
    }

    public static class HolidayBean {
        /**
         * name : 元旦
         * desc : 2018年12月30日至2019年1月1日放假调休，共3天。2018年12月29日（星期六）上班。
         * festival : 1546272000000
         * days : 4
         */

        private String name;
        private String desc;
        private long festival;
        private int days;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public long getFestival() {
            return festival;
        }

        public void setFestival(long festival) {
            this.festival = festival;
        }

        public int getDays() {
            return days;
        }

        public void setDays(int days) {
            this.days = days;
        }
    }

    public static class HolidaylistBean {
        /**
         * name : 元旦
         * startday : 2019-1-1
         */

        private String name;
        private String startday;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStartday() {
            return startday;
        }

        public void setStartday(String startday) {
            this.startday = startday;
        }
    }
}
