package com.yangtzeu.url;

import com.blankj.utilcode.util.LogUtils;

import okhttp3.FormBody;
import okhttp3.Request;

public class Url {
    /**
     * 基本配置参数
     */
    //App接口Host
    public static final String My_App_Home = "http://ll.xyll520.top";
    //App接口Host
    public static final String My_Home = "http://m.xyll520.top";
    //mob的Key secret
    public static final String key = "20588bd8fbea0";
    public static final String secret = "95c6d889c855e82689609cd192cfc6c8";

    //长江大学主页
    public static final String Yangtzeu_Url = "http://www.yangtzeu.edu.cn/";
    //长江大学教务系统主页
    public static final String Yangtzeu_Base_Url = "http://jwc3.yangtzeu.edu.cn";
    //旧教务处主页
    public static final String Yangtzeu_JWC = "http://jwc.yangtzeu.edu.cn/";

    /**
     * 教务各类网址
     */
    //默认的学期期
    public static final String Default_Term = "49";
    //登录接口
    public static final String Yangtzeu_Login_Path = Yangtzeu_Base_Url + "/eams/login.action";
    //验证码地址
    public static final String Yangtzeu_Login_Code = Yangtzeu_Base_Url + "/eams/captcha/image.action";
    //注销地址
    public static final String Yangtzeu_Out = Yangtzeu_Base_Url + "/eams/logout.action";
    //学籍信息
    public static final String Yangtzeu_XueJI = Yangtzeu_Base_Url + "/eams/stdDetail.action";
    //学生成绩
    public static final String Yangtzeu_Grade_Url = Yangtzeu_Base_Url + "/eams/teach/grade/course/person!search.action?semesterId=";
    //学生所有成绩
    public static final String Yangtzeu_AllGrade_Url = Yangtzeu_Base_Url + "/eams/teach/grade/course/person!historyCourseGrade.action?projectType=MAJOR";
    //修改密码
    public static final String Yangtzeu_Change_Password = Yangtzeu_Base_Url + "/eams/security/my!save.action";
    //控制面板
    public static final String Yangtzeu_Control = Yangtzeu_Base_Url + "/eams/security/my.action";
    //Cet查询
    public static final String Yangtzeu_Cet = Yangtzeu_Base_Url + "/eams/stdOtherExamSignUp.action";
    //Cet查询
    public static final String Yangtzeu_Guan_Cet = "http://cet.etest.net.cn/";
    //Cet报名
    public static final String Yangtzeu_Cet_Add = Yangtzeu_Base_Url + "/eams/stdOtherExamSignUp!configList.action";
    //课表查询
    public static final String Yangtzeu_Table = Yangtzeu_Base_Url + "/eams/courseTableForStd!courseTable.action";
    //课表查询ids
    public static final String Yangtzeu_Table_Ids = Yangtzeu_Base_Url + "/eams/courseTableForStd.action";
    //我的考试
    public static final String Yangtzeu_My_Test = Yangtzeu_Base_Url + "/eams/stdExamTable.action";
    //我的考试详情
    public static final String Yangtzeu_My_Details_Test = Yangtzeu_Base_Url + "/eams/stdExamTable!examTable.action?examBatch.id=";
    //评教
    public static final String Yangtzeu_Teacher = Yangtzeu_Base_Url + "/eams/quality/stdEvaluate.action";
    //选课查询
    public static final String Yangtzeu_ChooseClass = Yangtzeu_Base_Url + "/eams/stdElectCourse.action";
    //培养计划
    public static final String Yangtzeu_Personal_Plan = Yangtzeu_Base_Url + "/eams/myPlan.action";
    //专业培养计划
    public static final String Yangtzeu_Major_Mode = Yangtzeu_Base_Url + "/eams/stdMajorPlan!search.action";
    //制定培养计划
    public static final String Yangtzeu_Maker_Mode = Yangtzeu_Base_Url + "/eams/draftPersonalPlan.action";
    //我的培养方案
    public static final String Yangtzeu_Me_Mode = Yangtzeu_Base_Url + "/eams/myDraftPersonalPlan!search.action";
    //我的培养方案详情
    public static final String Yangtzeu_Me_Mode_Details = Yangtzeu_Base_Url + "/eams/myDraftPersonalPlan!info.action";
    //站内消息
    public static final String Yangtzeu_ZhanNei = Yangtzeu_Base_Url + "/eams/systemMessageForStd!search.action";
    //成绩导出
    public static final String Yangtzeu_Grade_Export = Yangtzeu_Base_Url + "/eams/postgraduate/midterm/stdExamine!export.action";
    //长大校历
    public static final String Yangtzeu_School_Plan = My_App_Home + "/yangtzeu/time/index.html";
    //长大新闻搜索
    public static final String Yangtzeu_News_Search = "http://news.yangtzeu.edu.cn/search_list.jsp?wbtreeid=1001&newskeycode2=6ICD56CU";
    //计算机二级成绩
    public static final String Yangtzeu_Computer_Grade = "http://cjcx.neea.edu.cn/html1/folder/1508/206-1.htm?sid=300";
    //一卡通
    public static final String Yangtzeu_Card_Home = "http://10.10.220.77";
    //一卡通登录
    public static final String Yangtzeu_CenterCard_Login = Yangtzeu_Card_Home + "/Login.aspx";
    //教务通知
    public static final String Yangtzeu_JWTZ = "http://jwc.yangtzeu.edu.cn/jwxw/jwtz.htm";
    //本周事务
    public static final String Yangtzeu_BZSW = "http://jwc.yangtzeu.edu.cn/jwxw/bzsw.htm";
    //教学动态
    public static final String Yangtzeu_JXDT = "http://jwc.yangtzeu.edu.cn/jwxw/jxdt.htm";
    //教学简报
    public static final String Yangtzeu_JXJB = "http://jwc.yangtzeu.edu.cn/jwxw/jxjb.htm";
    //长江大学网站
    public static final String Yangtzeu_All_Web = My_App_Home + "/yangtzeu/json/yz_web.json";
    //软件集
    public static final String Yangtzeu_All_Web_Soft = My_App_Home + "/yangtzeu/json/yz_web_soft.json";
    //长江大学学院网站
    public static final String Yangtzeu_XueYuanWeb = "http://www.yangtzeu.edu.cn/xndh/jxdw.htm";
    //缴纳网费
    public static final String Yangtzeu_Fee = "http://58.50.120.1:89/Self/dashboard";


    /**
     * 物理实验中心
     */
    //主页
    public static String Yangtzeu_Physical_Home = "http://phylab.yangtzeu.edu.cn/jpkc/";
    //登录
    public static String Yangtzeu_Physical_Login = "http://10.10.16.16/index.php/Home/Index/login";
    //列表
    public static String Yangtzeu_Physical_List = "http://10.10.16.16/index.php/Home/Student/listexp";
    //删除预约
    public static String Yangtzeu_Physical_Delete = "http://10.10.16.16/index.php/Home/Student/delmyexp";
    //我的预约
    public static String Yangtzeu_Physical_Grade = "http://10.10.16.16/index.php/Home/Student/listmyexp";
    //我的预约
    public static String Yangtzeu_Physical_Add = "http://10.10.16.16/index.php/Home/Student/prelistexp";
    //验证码
    public static String Yangtzeu_Physical_Verify = "http://10.10.16.16/index.php/home/index/verify";




    /**
     * App各类接口相关
     */
    //最近节日
    public static final String Yangtzeu_Next_Holiday = "http://timor.tech/api/holiday/tts/next";
    //天气查询
    public static final String Yangtzeu_Weather = "http://apicloud.mob.com/v1/weather/query?key=" + key + "&province=湖北&city=";
    //课表默认背景
    public static final String Yangtzeu_Table_Background_White = My_App_Home + "/yangtzeu/image/white.jpg";
    //课表默认背景
    public static final String Yangtzeu_Table_Background = My_App_Home + "/yangtzeu/image/table_bg.jpg";
    //聊天背景
    public static final String Yangtzeu_Chat_Background = My_App_Home + "/image/chat_bg.jpeg";
    //爱心
    public static final String Yangtzeu_App_Love = My_App_Home + "/yangtzeu/image/love.png";
    //当前在线人数
    public static final String Yangtzeu_App_Online = My_App_Home + "/yangtzeu/api/yz_online.php";
    //当前在线人数
    public static final String Yangtzeu_App_Online_Show = My_App_Home + "/yangtzeu/api/yz_online_show.php";
    //长大App
    public static final String Yangtzeu_App_MyApp = My_App_Home + "/yangtzeu/json/yz_app.json";
    //功能页面滚动条通知
    public static final String Yangtzeu_App_Many_Notice = My_App_Home + "/yangtzeu/json/yz_many_notice.json";
    //功能页面Banner
    public static final String Yangtzeu_App_Many_Banner = My_App_Home + "/yangtzeu/json/yz_many_banner.json";
    //广告
    public static final String Yangtzeu_AD = My_App_Home + "/yangtzeu/json/yz_ad.json";
    //App酷安下载地址
    public static final String AppDownUrl = "https://www.coolapk.com/apk/com.yangtzeu";
    //App更新地址
    public static final String Yangtzeu_AppUp_Url = My_App_Home + "/yangtzeu/json/yz_update.json";
    //AppAlert通知地址
    public static final String Yangtzeu_AppAlertNotice = My_App_Home + "/yangtzeu/json/yz_main_alert.json";
    //封号相关
    public static final String Yangtzeu_FengHao = My_App_Home + "/yangtzeu/api/yz_ban.php?action=add_ban_user&submit=do";
    public static final String Yangtzeu_RemoveFengHao = My_App_Home + "/yangtzeu/api/yz_ban.php?action=delete_ban_user&submit=do&id=";
    public static final String Yangtzeu_ShowBanUser = My_App_Home + "/yangtzeu/api/yz_ban.php?action=query_ban_user&submit=do";
    //功能页面
    public static final String Yangtzeu_Many_Item = My_App_Home + "/yangtzeu/json/yz_many.json";
    //导航界面
    public static final String Yangtzeu_AppTripInfo = My_App_Home + "/yangtzeu/json/yz_trip.json";
    //锁屏白名单
    public static final String Yangtzeu_App_Lock_White = My_App_Home + "/yangtzeu/json/yz_lock_white.json";
    //开源地址
    public static final String Yangtzeu_Github = "https://github.com/xiaoyvyv/YangtzeuApp/blob/master/README.md";
    //翻译
    public static final String Yangtzeu_Translate = "http://api.guaqb.cn/api.php?fy=";
    //X5内核调试
    public static final String Yangtzeu_Debug_X5 = "http://debugtbs.qq.com";
    //App反馈地址
    public static String Yangtzeu_App_FeedBack = My_App_Home + "/yangtzeu/api/yz_feedback.php";
    //全民k歌解析
    public static String Url_Music_Kg = "http://3g.gljlw.com/diy/kge.php?url=";
    //上传图片
    public static String Url_Upload = "http://ll.xyll520.top/image/upload.php";
    //一言
    public static String Yangtzeu_App_YiYan = "https://api.uixsj.cn/hitokoto/w.php";
    //必应
    public static Object Yangtzeu_App_BiYin = "https://api.uixsj.cn/bing/bing.php";












    /**
     * 旧版接口
     */

    //发邮件
    public static String Yangtzeu_App_SendEmail = My_Home + "/php_mail/mail.php";
    //反馈
    public static String Yangtzeu_App_ShowFeedBack = My_Home + "/newyangtzeu/app_php/showfeedback.php";
    //元素周期表
    public static String Yangtzeu_Chem = My_Home + "/newyangtzeu/app_chm/index.html";
    //App留言板--发表
    public static String Yangtzeu_App_Message = My_Home + "/newyangtzeu/app_php/message.php";
    //App留言板--展示
    public static String Yangtzeu_App_ShowMessage = My_Home + "/newyangtzeu/app_php/showmessage.php?&which=30&start=";
    //App留言板--回复
    public static String Yangtzeu_App_Reply_Message = My_Home + "/newyangtzeu/app_php/reply.php";


    //失物招领
    public static String Yangtzeu_Things_FaBu = My_Home + "/newyangtzeu/app_php/things.php";
    //失物招领
    public static String Yangtzeu_Things_Show = My_Home + "/newyangtzeu/app_php/showthings.php";
    //失物招领 搜索
    public static String Yangtzeu_Things_Search = My_Home + "/newyangtzeu/app_php/searchthing.php";
    //删除物品
    public static String Yangtzeu_RemoveThing = My_Home + "/newyangtzeu/app_php/rmthing.php";


    //获取通知消息
    public static String getMessage(String to) {
        return My_App_Home + "/yangtzeu/api/yz_message.php?action=query_message&submit=do"
                + "&to=" + to;
    }

    //设置通知消息已读
    public static String getReadMessage(String id) {
        return My_App_Home + "/yangtzeu/api/yz_message.php?action=set_read&submit=do"
                + "&id=" + id;
    }

    //删除消息
    public static String deleteMessage(String id) {
        return My_App_Home + "/yangtzeu/api/yz_message.php?action=delete_message&submit=do"
                + "&id=" + id;
    }

    //发送消息
    public static String getSendMessage(String text, String from, String from_number, String to) {
        return My_App_Home + "/yangtzeu/api/yz_message.php?action=add_message&submit=do"
                + "&text=" + text
                + "&from=" + from
                + "&from_number=" + from_number
                + "&to=" + to;
    }


    //增加商品
    public static Request getAddGoodsUrl(String name, String description, String master,
                                         String master_id, String type, String price,
                                         String phone, String qq, String wechat, String image) {
        LogUtils.i(name, description, master, master_id, type, price, phone, qq, wechat, image);
        FormBody formBody = new FormBody.Builder()
                .add("name", name)
                .add("description", description)
                .add("master", master)
                .add("master_id", master_id)
                .add("price", price)
                .add("phone", phone)
                .add("qq", qq)
                .add("wechat", wechat)
                .add("type", type)
                .add("image", image)
                .build();
        return new Request.Builder()
                .url(My_App_Home + "/yangtzeu/api/yz_shop.php?action=add")
                .post(formBody)
                .build();
    }

    //删除商品
    public static String deleteGoods(String id) {
        return My_App_Home + "/yangtzeu/api/yz_shop.php?action=delete&id=" + id;
    }

    //查询商品
    public static String queryAllGoods(String query_type, String query_text, int page) {
        if (query_type == null || query_text == null) {
            return My_App_Home + "/yangtzeu/api/yz_shop.php?action=query" + "&page=" + page;
        } else {
            return My_App_Home + "/yangtzeu/api/yz_shop.php?action=query&query_type=" + query_type + "&query_text=" + query_text + "&page=" + page;
        }
    }

    //增加商品评论
    public static Request getAddGoodsReplayUrl(String good_id, String user_id, String user_name, String content) {
        FormBody formBody = new FormBody.Builder()
                .add("good_id", good_id)
                .add("user_id", user_id)
                .add("user_name", user_name)
                .add("content", content)
                .build();
        return new Request.Builder()
                .url(My_App_Home + "/yangtzeu/api/yz_shop_replay.php?action=add")
                .post(formBody)
                .build();
    }


    //添加用户
    public static String addUser(String master, String master_id, String mclass, String description, String wechat, String qq, String phone) {
        return My_App_Home + "/yangtzeu/api/yz_user.php?action=add"
                + "&master=" + master
                + "&phone=" + phone
                + "&class=" + mclass
                + "&wechat=" + wechat
                + "&description=" + description
                + "&master_id=" + master_id
                + "&qq=" + qq;
    }

    //查询用户
    public static String queryUser(String master_id) {
        return My_App_Home + "/yangtzeu/api/yz_user.php?action=query"
                + "&master_id=" + master_id;
    }

    public static String queryAllThings(String query_type, String query_text, int start) {
        if (query_type == null || query_text == null) {
            return My_App_Home + "/yangtzeu/api/yz_thing.php?action=query&page=" + start;
        } else {
            return My_App_Home + "/yangtzeu/api/yz_thing.php?action=query&query_type=" + query_type + "&query_text=" + query_text + "&page=" + start;
        }
    }

    //查询表白
    public static String queryAllLove(String query_type, String query_text, int page) {
        if (query_type == null || query_text == null) {
            return My_App_Home + "/yangtzeu/api/yz_love.php?action=query&page=" + page;
        } else {
            return My_App_Home + "/yangtzeu/api/yz_love.php?action=query&query_type=" + query_type + "&query_text=" + query_text + "&page=" + page;
        }
    }

    //增加表白
    public static Request getAddLoveUrl(String master_ta, String description,
                                        String master, String master_id,
                                        String music, String qq_ta,
                                        String qq, String isHide, String image) {
        LogUtils.i(master_ta, description, master, master_id, qq_ta, qq, image);
        FormBody formBody = new FormBody.Builder()
                .add("master_ta", master_ta)
                .add("description", description)
                .add("master", master)
                .add("master_id", master_id)
                .add("qq_ta", qq_ta)
                .add("qq", qq)
                .add("music", music)
                .add("hide", isHide)
                .add("image", image)
                .build();
        return new Request.Builder()
                .url(My_App_Home + "/yangtzeu/api/yz_love.php?action=add")
                .post(formBody)
                .build();
    }


    //增加表白评论
    public static Request getAddLoveReplayUrl(String love_id, String user_id, String user_name, String content) {
        FormBody formBody = new FormBody.Builder()
                .add("love_id", love_id)
                .add("user_id", user_id)
                .add("user_name", user_name)
                .add("content", content)
                .build();
        LogUtils.i(love_id, user_id, user_name, content);
        return new Request.Builder()
                .url(My_App_Home + "/yangtzeu/api/yz_love_replay.php?action=add")
                .post(formBody)
                .build();
    }

    //删除表白
    public static String deleteLove(String id) {
        return My_App_Home + "/yangtzeu/api/yz_love.php?action=delete&id=" + id;
    }

    //点击数统计
    public static String getTongJi(String key, String name, boolean isAdd) {
        if (isAdd) {
            return My_App_Home + "/yangtzeu/api/yz_statistics.php?action=do"
                    + "&key=" + key
                    + "&name=" + name;
        } else {
            return My_App_Home + "/yangtzeu/api/yz_statistics.php?action=do&type=find"
                    + "&key=" + key
                    + "&name=" + name;
        }
    }




}
