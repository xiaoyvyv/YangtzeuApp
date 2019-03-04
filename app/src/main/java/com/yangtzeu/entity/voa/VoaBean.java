package com.yangtzeu.entity.voa;

import java.util.List;
/**
 * Created by Administrator on 2018/2/26.
 * @author 王怀玉
 * @explain Voa听力列表的实体类
 */

public class VoaBean {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<date_dteails> getData() {
        return data;
    }

    public void setData(List<date_dteails> data) {
        this.data = data;
    }

    private List<date_dteails> data;

    public class date_dteails{

        private String Title;

        public String getTitle() {
            return Title;
        }

        public void setTitle(String title) {
            Title = title;
        }

        private String Title_cn;
        private String DescCn;
        private String CreateTime;
        private String CategoryName;
        private String Id;
        private String Pic;
        private String ReadCount;

        public String getTitle_cn() {
            return Title_cn;
        }

        public void setTitle_cn(String title_cn) {
            Title_cn = title_cn;
        }

        public String getDescCn() {
            return DescCn;
        }

        public void setDescCn(String descCn) {
            DescCn = descCn;
        }

        public String getCreateTime() {
            return CreateTime;
        }

        public void setCreateTime(String createTime) {
            CreateTime = createTime;
        }

        public String getCategoryName() {
            return CategoryName;
        }

        public void setCategoryName(String categoryName) {
            CategoryName = categoryName;
        }

        public String getId() {
            return Id;
        }

        public void setId(String id) {
            Id = id;
        }

        public String getPic() {
            return Pic;
        }

        public void setPic(String pic) {
            Pic = pic;
        }

        public String getReadCount() {
            return ReadCount;
        }

        public void setReadCount(String readCount) {
            ReadCount = readCount;
        }
    }
}