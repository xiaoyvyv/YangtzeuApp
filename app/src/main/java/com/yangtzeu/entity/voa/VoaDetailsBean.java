package com.yangtzeu.entity.voa;

import java.util.List;
/**
 * Created by Administrator on 2018/2/26.
 * @author 王怀玉
 * @explain Voa听力详情的实体类
 */

public class VoaDetailsBean {

    private List<Data> data;
    private Title title;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }


    public class Data {
        private String EndTiming;
        private String ParaId;
        private String Sentence_cn;
        private String Timing;
        private String Sentence;

        public String getEndTiming() {
            return EndTiming;
        }

        public void setEndTiming(String endTiming) {
            EndTiming = endTiming;
        }

        public String getParaId() {
            return ParaId;
        }

        public void setParaId(String paraId) {
            ParaId = paraId;
        }

        public String getSentence_cn() {
            return Sentence_cn;
        }

        public void setSentence_cn(String sentence_cn) {
            Sentence_cn = sentence_cn;
        }

        public String getTiming() {
            return Timing;
        }

        public void setTiming(String timing) {
            Timing = timing;
        }

        public String getSentence() {
            return Sentence;
        }

        public void setSentence(String sentence) {
            Sentence = sentence;
        }
    }

    public class Title{

        private String Title_cn;
        private String Sound;
        private String Pic;
        private String Title;
        private String ReadCount;
        private String CreateTime;

        public String getTitle_cn() {
            return Title_cn;
        }

        public void setTitle_cn(String title_cn) {
            Title_cn = title_cn;
        }

        public String getSound() {
            return Sound;
        }

        public void setSound(String sound) {
            Sound = sound;
        }

        public String getPic() {
            return Pic;
        }

        public void setPic(String pic) {
            Pic = pic;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String title) {
            Title = title;
        }

        public String getReadCount() {
            return ReadCount;
        }

        public void setReadCount(String readCount) {
            ReadCount = readCount;
        }

        public String getCreateTime() {
            return CreateTime;
        }

        public void setCreateTime(String createTime) {
            CreateTime = createTime;
        }

    }
}
