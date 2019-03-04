package com.yangtzeu.entity;

import java.util.List;

public class CetDateBean {
    /**
     * sn : 2018年上半年全国大学英语四、六级考试（含口试）
     * subn : 仅限查询2018年上半年考试成绩。
     * qt : 2018/08/22 09:00:00
     * rdsub : [{"code":"CET6-D","tab":"CET6_181_DANGCI","name":"全国大学英语六级考试(CET6)","en":"2018年06月全国大学英语六级考试(CET6)"},{"code":"CRT6-D","tab":"CRT6_181_DANGCI","name":"全国大学俄语六级考试(CRT6)","en":"2018年06月全国大学俄语六级考试(CRT6)"},{"code":"PHS6-D","tab":"PHS6_181_DANGCI","name":"全国大学德语六级考试(PHS6)","en":"2018年06月全国大学德语六级考试(PHS6)"},{"code":"TFU4-D","tab":"TFU4_181_DANGCI","name":"全国大学法语四级考试(TFU4)","en":"2018年06月全国大学法语四级考试(TFU4)"},{"code":"CJT4-D","tab":"CJT4_181_DANGCI","name":"全国大学日语四级考试(CJT4)","en":"2018年06月全国大学日语四级考试(CJT4)"},{"code":"PHS4-D","tab":"PHS4_181_DANGCI","name":"全国大学德语四级考试(PHS4)","en":"2018年06月全国大学德语四级考试(PHS4)"},{"code":"CET4-D","tab":"CET4_181_DANGCI","name":"全国大学英语四级考试(CET4)","en":"2018年06月全国大学英语四级考试(CET4)"},{"code":"CJT6-D","tab":"CJT6_181_DANGCI","name":"全国大学日语六级考试(CJT6)","en":"2018年06月全国大学日语六级考试(CJT6)"},{"code":"CRT4-D","tab":"CRT4_181_DANGCI","name":"全国大学俄语四级考试(CRT4)","en":"2018年06月全国大学俄语四级考试(CRT4)"}]
     */

    private String sn;
    private String subn;
    private String qt;
    private List<RdsubBean> rdsub;

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getSubn() {
        return subn;
    }

    public void setSubn(String subn) {
        this.subn = subn;
    }

    public String getQt() {
        return qt;
    }

    public void setQt(String qt) {
        this.qt = qt;
    }

    public List<RdsubBean> getRdsub() {
        return rdsub;
    }

    public void setRdsub(List<RdsubBean> rdsub) {
        this.rdsub = rdsub;
    }

    public static class RdsubBean {
        /**
         * code : CET6-D
         * tab : CET6_181_DANGCI
         * name : 全国大学英语六级考试(CET6)
         * en : 2018年06月全国大学英语六级考试(CET6)
         */

        private String code;
        private String tab;
        private String name;
        private String en;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getTab() {
            return tab;
        }

        public void setTab(String tab) {
            this.tab = tab;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEn() {
            return en;
        }

        public void setEn(String en) {
            this.en = en;
        }
    }
}
