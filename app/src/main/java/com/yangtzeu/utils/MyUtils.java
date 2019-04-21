package com.yangtzeu.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.PathUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.blankj.utilcode.util.ZipUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.lib.chat.common.UserManager;
import com.lib.subutil.ClipboardUtils;
import com.lib.x5web.X5WebView;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.QbSdk;
import com.yangtzeu.R;
import com.yangtzeu.entity.ImageBean;
import com.yangtzeu.ui.activity.ADActivity;
import com.yangtzeu.ui.activity.ChatGroupInfoActivity;
import com.yangtzeu.ui.activity.ChatOpenActivity;
import com.yangtzeu.ui.activity.DownloadActivity;
import com.yangtzeu.ui.activity.ImageActivity;
import com.yangtzeu.ui.activity.InfoActivity;
import com.yangtzeu.ui.activity.LoginActivity;
import com.yangtzeu.ui.activity.LoveActivity;
import com.yangtzeu.ui.activity.LoveAddActivity;
import com.yangtzeu.ui.activity.LoveDetailsActivity;
import com.yangtzeu.ui.activity.SplashActivity;
import com.yangtzeu.ui.activity.WebActivity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 2016 on 2017/11/29.
 */

public class MyUtils {
    private final static String[] NameList = {"doc", "docx", "ppt", "pptx", "xls", "xlsx", "pdf", "txt", "epub"};
    private final static String[] ImageList = {"png", "jpg", "gif", "webp", "bmp", "jpeg"};


    public static void setToolbarBackToHome(final AppCompatActivity activity, Toolbar toolbar) {
        activity.setSupportActionBar(toolbar);
        Objects.requireNonNull(activity.getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    public static int getIntStringWeek() {
        int[] weekDays = {7, 1, 2, 3, 4, 5, 6};
        Calendar cal = Calendar.getInstance();
        cal.setTime(TimeUtils.getNowDate());
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    public static void setMyTheme(Activity activity) {
        if (activity instanceof LoginActivity || activity instanceof ImageActivity
                || activity instanceof SplashActivity || activity instanceof InfoActivity
                || activity instanceof ChatGroupInfoActivity ) {
            activity.setTheme(R.style.AppTheme_NoStateBar);
            return;
        }
        if (activity instanceof LoveActivity || activity instanceof LoveDetailsActivity || activity instanceof LoveAddActivity) {
            activity.setTheme(R.style.Love);
            return;
        }

        String theme = SPUtils.getInstance("app_info").getString("theme", "0");
        switch (theme) {
            case "0":
                activity.setTheme(R.style.AppTheme_NoActionBar);
                break;
            case "1":
                activity.setTheme(R.style.AppTheme_KuAnLv);
                break;
            case "2":
                activity.setTheme(R.style.AppTheme_YiMaHong);
                break;
            case "3":
                activity.setTheme(R.style.AppTheme_BiLiFen);
                break;
            case "4":
                activity.setTheme(R.style.AppTheme_YiDiLan);
                break;
            case "5":
                activity.setTheme(R.style.AppTheme_ShuiYaQing);
                break;
            case "6":
                activity.setTheme(R.style.AppTheme_YiTengCheng);
                break;
            case "7":
                activity.setTheme(R.style.AppTheme_JiLaoZi);
                break;
            case "8":
                activity.setTheme(R.style.AppTheme_ZhiHuLan);
                break;
            case "9":
                activity.setTheme(R.style.AppTheme_GuTongZong);
                break;
            case "10":
                activity.setTheme(R.style.AppTheme_DiDiaoHui);
                break;
            case "11":
                activity.setTheme(R.style.AppTheme_GaoDuanHei);
                break;
        }
    }

    /**
     * 保留几位小数
     *
     * @param big 要进行保留的大数
     * @return 保留后的数
     */
    public static double getScale(double big, int Accuracy) {
        double result = 0;
        try {
            BigDecimal bigDecimal = new BigDecimal(big);
            result = bigDecimal.setScale(Accuracy, RoundingMode.HALF_UP).doubleValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public static void startActivity(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Context context = ActivityUtils.getTopActivity();
        if (context == null) {
            context = Utils.getApp().getApplicationContext();
        }
        if (context == null) {
            ActivityUtils.startActivity(intent);
            return;
        }
        context.startActivity(intent);
        enterAnimation(context);
    }

    public static void startActivity(Class<? extends Activity> activity) {
        Intent intent = new Intent(Utils.getApp().getApplicationContext(), activity);
        startActivity(intent);
    }

    public static void startActivity(String cls) {
        try {
            Intent intent = new Intent();
            //前名一个参数是应用程序的包名,后一个是这个应用程序的主Activity名
            intent.setComponent(new ComponentName(AppUtils.getAppPackageName(), cls));
            startActivity(intent);
        } catch (Exception e) {
            ToastUtils.showShort(R.string.open_error);
        }
    }

    public static ProgressDialog getProgressDialog(Context context, String s) {
        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(context.getString(R.string.trip));
        progressDialog.setMessage(s);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        return progressDialog;
    }

    public static AlertDialog getAlert(Context context, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder build = new AlertDialog.Builder(context);
        build.setTitle(R.string.trip);
        build.setMessage(message);
        build.setPositiveButton(R.string.done, listener);
        build.setNegativeButton(R.string.clear, null);
        build.create();
        AlertDialog dialog = build.create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    public static AlertDialog getAlert(Context context, String message, DialogInterface.OnClickListener pos_listener, DialogInterface.OnClickListener neg_listener) {
        AlertDialog.Builder build = new AlertDialog.Builder(context);
        build.setTitle(R.string.trip);
        build.setMessage(message);
        build.setPositiveButton(R.string.done, pos_listener);
        build.setNegativeButton(R.string.clear, neg_listener);
        build.create();
        AlertDialog dialog = build.create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    //保存网络文件
    public static void saveFile(final Context context, String url, String path, String saveFileName) {
        if (saveFileName.length() > 25) {
            saveFileName = saveFileName.substring(saveFileName.length() - 20);
        }
        if (path == null) {
            path = "A_Tool/Download/";
        }
        createSDCardDir(path);
        ToastUtils.showShort(R.string.saving);

        final String finalPath = path;
        final String finalSaveFileName = saveFileName;
        DownloadUtils.get().download(url, path, saveFileName, new DownloadUtils.OnDownloadListener() {
            @Override
            public void onDownloadSuccess() {
                ToastUtils.showShort(R.string.save_success);
                if (finalSaveFileName.endsWith(".png") || finalSaveFileName.endsWith(".jpg") || finalSaveFileName.endsWith(".jpeg") || finalSaveFileName.endsWith(".gif")) {
                    MyUtils.saveImageToGallery(context, PathUtils.getExternalStoragePath() + "/" + finalPath + finalSaveFileName);
                }
            }

            @Override
            public void onDownloading(int progress) {

            }

            @Override
            public void onDownloadFailed(String error) {
                LogUtils.e(error);
                ToastUtils.showShort(R.string.save_error);
            }
        });
    }

    /**
     * 正则获取Host
     *
     * @param url 链接
     * @return Host
     */
    public static String getHost(String url) {
        if (url == null || url.trim().equals("")) {
            return "";
        }
        String host = "";
        Pattern p = Pattern.compile("(?<=//|)((\\w)+\\.)+\\w+");
        Matcher matcher = p.matcher(url);
        if (matcher.find()) {
            host = matcher.group();
        }
        return host;
    }

    /**
     * 规则：必须同时包含大小写字母及数字
     *
     * @param str 被判断字符串
     * @return 是否同时包含大小写字母及数字
     */
    public static boolean isContainAll(String str) {
        boolean isDigit = false;//定义一个boolean值，用来表示是否包含数字
        boolean isLowerCase = false;//定义一个boolean值，用来表示是否包含字母
        boolean isUpperCase = false;
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {   //用char包装类中的判断数字的方法判断每一个字符
                isDigit = true;
            } else if (Character.isLowerCase(str.charAt(i))) {  //用char包装类中的判断字母的方法判断每一个字符
                isLowerCase = true;
            } else if (Character.isUpperCase(str.charAt(i))) {
                isUpperCase = true;
            }
        }
        String regex = "^[a-zA-Z0-9]+$";
        return isDigit && isLowerCase && isUpperCase && str.matches(regex);
    }


    /**
     * 获得根目录
     *
     * @return 根目录
     */
    public static String rootPath() {
        File sdcardDir = Environment.getExternalStorageDirectory();
        //得到一个路径，内容是sdcard的文件夹路径和名字
        String path = sdcardDir.getPath();
        return path + "/";
    }

    /**
     * 文件大小换算
     *
     * @param target_size 字节大小
     * @return 文件大小
     */
    public static String FormatSize(Context context, String target_size) {
        return Formatter.formatFileSize(context, Long.valueOf(target_size));
    }


    /**
     * 在SD卡上创建一个文件夹"A_Tool"
     *
     * @param DirName 文件夹名称
     */
    public static String createSDCardDir(String DirName) {
        String path = "";
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            // 创建一个文件夹对象，赋值为外部存储器的目录
            File sdcardDir = Environment.getExternalStorageDirectory();
            //得到一个路径，内容是sdcard的文件夹路径和名字
            path = sdcardDir.getPath() + "/" + DirName;
            File path1 = new File(path);
            if (!path1.exists()) {
                //若不存在，创建目录，可以在应用启动的时候创建
                if (path1.mkdirs()) {
                    LogUtils.i("文件夹路径创建成功：" + path);
                }
            } else {
                Log.i("MyUtils", "createSDCardDir()：文件夹路径已存在：" + path);
            }
        } else {
            LogUtils.e("文件夹创建失败：" + DirName, "可能未取得读写权限");
        }
        return path;
    }

    /**
     * 根据扩展名获取文件Mime类型
     *
     * @param file 文件
     * @return 文件Mime类型
     */
    public static String getMimeType(File file) {
        String extension = FileUtils.getFileExtension(file);
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }


    /**
     * QB_SDK打开文件
     *
     * @param path 文件路径
     */
    public static void openFile(Context context, final String path) {

        if (path.endsWith(".zip")) {
            String zip_path = createSDCardDir("A_Tool/Download/Zip/");
            try {
                ZipUtils.unzipFile(path, zip_path);
                boolean delete = new File(path).delete();
                String activity = ActivityUtils.getTopActivity().getLocalClassName();
                if (activity.contains("WebActivity")) {
                    ToastUtils.showShort("解压成功");
                    ActivityUtils.startActivity(DownloadActivity.class);
                } else {
                    ToastUtils.showShort("解压成功，请刷新");
                }
            } catch (Exception e) {
                ToastUtils.showShort("不支持打开此文件,请使用第三方工具打开");
                openFileByPath(context, path);
            }
            return;
        }
        boolean QB_canOpen = false;
        boolean Image_canOpen = false;
        String name = FileUtils.getFileExtension(path);
        for (String Item : NameList) {
            if (name.equals(Item)) {
                QB_canOpen = true;
            }
        }
        if (QB_canOpen) {
            LogUtils.e("QB_SDK:" + name, "尝试打开：" + path);
            QbSdk.openFileReader(context, path, null, null);
        } else {
            for (String Item : ImageList) {
                if (name.equals(Item)) {
                    Image_canOpen = true;
                }
            }
            if (Image_canOpen) {
                openImage(context, path);
            } else {
                openFileByPath(context, path);
            }
        }

    }

    /**
     * 传入路径，打开文件
     *
     * @param path 文件路径
     */

    public static void openFileByPath(Context context, String path) {
        try {
            File file = new File(path);
            String mimeType = getMimeType(file);
            LogUtils.e("尝试打开文件", path, mimeType);

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            Uri uri;
            //判读版本是否在7.0以上
            if (Build.VERSION.SDK_INT >= 24) {
                uri = FileProvider.getUriForFile(context, "com.yangtzeu.fileprovider", file);
            } else {
                uri = Uri.fromFile(file);
            }
            intent.setDataAndType(uri, mimeType);

            context.startActivity(intent);
        } catch (Exception e) {
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setTitle("提示")
                    .setMessage("此App不支持打开此文件，请在手机文件管理器中打开路径\n\n\"A_Tool/Download/\"")
                    .setPositiveButton("知道了", null)
                    .create();
            dialog.show();
        }
    }

    /**
     * 适配api19以上,根据uri获取图片的绝对路径
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPathFromUri(Context context, Uri uri) {
        String filePath = null;
        String wholeID = DocumentsContract.getDocumentId(uri);         // 使用':'分割
        String id = wholeID.split(":")[1];
        String[] projection = {MediaStore.Images.Media.DATA};
        String selection = MediaStore.Images.Media._ID + "=?";
        String[] selectionArgs = {id};
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection, selection, selectionArgs, null);
        int columnIndex = Objects.requireNonNull(cursor).getColumnIndex(projection[0]);
        if (cursor.moveToFirst()) filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }

    /**
     * 获取取随机数
     *
     * @param num 最大数限制
     * @return 随机数`
     */
    public static String getRand(int num) {
        Random ran = new Random(System.currentTimeMillis());
        int refresh_int = ran.nextInt(num);
        String str = String.valueOf(refresh_int);
        LogUtils.i("获取的随机数为", str);
        return str;
    }

    /**
     * 跳转到微信
     */
    public static void openWeChat() {
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cmp);
            startActivity(intent);
        } catch (Exception e) {
            ToastUtils.showShort(R.string.no_wechat);
        }
    }

    public static void listFilesInDir(List<File> fileList, String path) {
        File[] allFiles = new File(path).listFiles();
        for (File file : allFiles) {
            if (file.isFile()) {
                fileList.add(file);
            } else {
                listFilesInDir(fileList, file.getAbsolutePath());
            }
        }
    }

    /**
     * 截屏
     *
     * @param decorView
     * @param mWebView  上下文
     * @return Bitmap
     */
    public static Bitmap getScreenViewPicture(Context context, View decorView, X5WebView mWebView) {
        // 这里的 mWebView 就是 X5 内核的 WebView ，代码中的 longImage 就是最后生成的长图
        mWebView.measure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        mWebView.layout(0, 0, mWebView.getMeasuredWidth(), mWebView.getMeasuredHeight());
        mWebView.setDrawingCacheEnabled(true);
        mWebView.buildDrawingCache();
        Bitmap longImage = Bitmap.createBitmap(mWebView.getMeasuredWidth(), mWebView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(longImage); // 画布的宽高和 WebView 的网页保持一致
        Paint paint = new Paint();
        canvas.drawBitmap(longImage, 0, mWebView.getMeasuredHeight(), paint);

        float scale = decorView.getResources().getDisplayMetrics().density;
        Bitmap x5Bitmap = Bitmap.createBitmap(mWebView.getWidth(), mWebView.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas x5Canvas = new Canvas(x5Bitmap);
        x5Canvas.drawColor(context.getResources().getColor(R.color.white));
        mWebView.getX5WebViewExtension().snapshotWholePage(x5Canvas, false, false); // 少了这行代码就无法正常生成长图
        Matrix matrix = new Matrix();
        matrix.setScale(scale, scale);

        return ImageUtils.addTextWatermark(x5Bitmap, "来自：新长大助手@酷安网@小玉",
                ConvertUtils.dp2px(20), context.getResources().getColor(R.color.black_20), 50, x5Bitmap.getHeight() - 100);
    }

    public static String getRandomString(int length) {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(3);
            long result = 0;
            switch (number) {
                case 0:
                    result = Math.round(Math.random() * 25 + 65);
                    sb.append((char) result);
                    break;
                case 1:
                    result = Math.round(Math.random() * 25 + 97);
                    sb.append((char) result);
                    break;
                case 2:
                    sb.append(new Random().nextInt(10));
                    break;
            }
        }
        return sb.toString();
    }

    public static void chatOnline(Context context, String to_number, String userType) {
        if (ObjectUtils.isEmpty(UserManager.getInstance().getStatus())) {
            ToastUtils.showShort("当前处于离线状态，请稍后再试");
            return;
        }
        if (StringUtils.equals(to_number, UserManager.getInstance().getAccount())) {
            ToastUtils.showShort(R.string.cannt_chat_myself);
            return;
        }

        Intent intent = new Intent(context, ChatOpenActivity.class);
        intent.putExtra("type", userType);
        intent.putExtra("id", to_number);
        MyUtils.startActivity(intent);
    }

    /**
     * 将文件按时间降序排列
     */
    public static class FileComparator2 implements Comparator<File> {
        @Override
        public int compare(File file1, File file2) {
            return Long.compare(file2.lastModified(), file1.lastModified());
        }
    }

    /**
     * 复制内容到剪切板
     */
    public static void putStringToClipboard(Context context, String clipboard) {
        ClipboardUtils.copyText(clipboard);
    }


    /**
     * 重写对话框关闭事件
     *
     * @param dialogInterface 对话框
     * @param close           是否能关闭
     */
    public static void canCloseDialog(DialogInterface dialogInterface, boolean close) {
        try {
            Field field = Objects.requireNonNull(dialogInterface.getClass().getSuperclass()).getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(dialogInterface, close);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断是否是数字
     *
     * @param str 被判断的字符串
     * @return 是否是数字
     */
    public static boolean isNumeric(String str) {
        if (ObjectUtils.isEmpty(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 在浏览器中打开链接
     *
     * @param targetUrl 要打开的网址
     */
    public static void openBrowser(Context context, String targetUrl) {
        if (!TextUtils.isEmpty(targetUrl) && targetUrl.startsWith("file://")) {
            ToastUtils.showShort(R.string.cant_open);
            return;
        }
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri url = Uri.parse(targetUrl);
        intent.setData(url);
        context.startActivity(intent);
    }

    /**
     * 将cookie同步到X5WebView
     *
     * @param host   WebView要加载的host
     * @param cookie 要同步的cookie
     */
    @SuppressLint("ObsoleteSdkInt")
    public static void syncX5Cookie(String host, String cookie) {
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setCookie(host, cookie);
    }

    /**
     * 分享文件
     *
     * @param what 分享内容
     */
    public static void shareText(Context context, String what) {
        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.putExtra(Intent.EXTRA_TEXT, "分享了：\n" + what);
        share.setType("text/plain");
        context.startActivity(Intent.createChooser(share, "分享到"));
        enterAnimation(context);
    }

    /**
     * 读取Assets内部文件
     *
     * @param fileName Assets内部文件路径
     * @return 文件内容
     */
    public static String readAssetsFile(Context context, String fileName) {
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            int read = is.read(buffer);
            is.close();
            return new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "读取错误，请检查文件";
    }

    /**
     * 获取App版本号
     *
     * @return App版本号
     */
    public static String getAPPVersionName(Context context) {
        String appVersionName = "";
        float currentVersionCode;
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            appVersionName = info.versionName; // 版本名
            currentVersionCode = info.versionCode; // 版本号
            System.out.println(currentVersionCode + " " + appVersionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appVersionName;
    }

    /**
     * 获取时间信息
     *
     * @param Calendar_What 信息种类
     * @return 时间信息
     */
    public static String getDateInfo(int Calendar_What) {
        int mYear, mMonth, mDay, mWay, mHour, mMinute;
        Calendar c = Calendar.getInstance();
        switch (Calendar_What) {
            case Calendar.YEAR:
                mYear = c.get(Calendar.YEAR); // 获取当前年份
                return String.valueOf(mYear);
            case Calendar.MONTH:
                mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
                return String.valueOf(mMonth);
            case Calendar.DAY_OF_MONTH:
                mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当日期
                return String.valueOf(mDay);
            case Calendar.DAY_OF_WEEK:
                mWay = c.get(Calendar.DAY_OF_WEEK);// 获取当前星期
                return String.valueOf(mWay);
            case Calendar.HOUR_OF_DAY:
                mHour = c.get(Calendar.HOUR_OF_DAY);//时
                return String.valueOf(mHour);
            case Calendar.MINUTE:
                mMinute = c.get(Calendar.MINUTE);//分
                return String.valueOf(mMinute);
            default:
                return (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH) + " " + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);
        }
    }

    /**
     * 加载图片
     *
     * @param imageView 图片容器
     * @param o         图片内容（链接、文件等等）
     */
    public static void loadImage(Context context, ImageView imageView, Object o) {
        if (ObjectUtils.isNotEmpty(o)) {
            if (context instanceof Activity) {
                if (((Activity) context).isDestroyed()) {
                    return;
                }
            }
            if (o.toString().contains("default_header")) {
                Glide.with(context).load(R.mipmap.holder).into(imageView);
                return;
            }
            Glide.with(context).load(o).into(imageView);
        }
    }

    /**
     * 加载图片,不缓存
     *
     * @param imageView 图片容器
     * @param o         图片内容（链接、文件等等）
     */
    public static void loadImageNoCache(Context context, ImageView imageView, Object o) {
        if (ObjectUtils.isNotEmpty(context)) {
            if (context instanceof Activity) {
                if (((Activity) context).isDestroyed()) {
                    return;
                }
            }
            RequestOptions options = new RequestOptions()
                    .skipMemoryCache(true)
                    .placeholder(R.mipmap.holder)
                    .diskCacheStrategy(DiskCacheStrategy.NONE);
            Glide.with(context).load(o).apply(options).into(imageView);
        }
    }

    /**
     * 加载图片,不缓存
     *
     * @param imageView 图片容器
     * @param url       图片内容（链接、文件等等）
     * @param key       Key
     */
    public static void loadImageNoCache(Context context, ImageView imageView, Object url, Object key) {
        if (ObjectUtils.isNotEmpty(context)) {
            if (context instanceof Activity) {
                if (((Activity) context).isDestroyed()) {
                    return;
                }
            }
            RequestOptions options = new RequestOptions()
                    .signature(new ObjectKey(key));
            Glide.with(context).load(url).apply(options).into(imageView);
        }
    }


    /**
     * 控制震动
     *
     * @param time 震动时间
     */
    public static void mVibrator(Context context, int time) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {0, time}; //0ms—500ms
        Objects.requireNonNull(vibrator).vibrate(pattern, -1);
    }

    /**
     * 十进制转为16进制
     *
     * @param a 10进制数
     * @return 16进制数
     */
    public static String formattingH(int a) {
        String i = Integer.toHexString(a);
        if (i.length() != 2) {
            i = "0" + i;
        }
        return i;
    }

    /**
     * 拨打电话
     *
     * @param phone 电话号码
     */
    public static void call(Context context, String phone) {
        Uri uri = Uri.parse("tel:" + phone); //设置要操作的路径
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);  //设置要操作的Action
        intent.setData(uri); //要设置的数据
        context.startActivity(intent);
        enterAnimation(context);
    }

    /**
     * QQ会话
     *
     * @param qq QQ号码
     */
    public static void chatQQ(Context context, String qq) {
        String qq_str = "mqqwpa://im/chat?chat_type=wpa&uin=" + qq + "&version=1";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(qq_str));
        MyUtils.startActivity(intent);
        enterAnimation(context);
    }

    /**
     * 拖动排序，第adapterPosition个调到第position个，然后其他的顺延
     *
     * @param list_Title   数组
     * @param fromPosition 从哪个位置调
     * @param toPosition   调到哪个位置
     */
    public static void ListSwap(List<?> list_Title, int fromPosition, int toPosition) {
        if (fromPosition > toPosition) {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(list_Title, i, i - 1);// 改变实际的数据集
            }
        } else {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(list_Title, i + 1, i);// 改变实际的数据集
            }
        }
    }

    public static void enterAnimation(Context context) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }

    /**
     * 过滤emoji 或者 其他非文字类型的字符
     *
     * @param source 被过滤文本
     * @return 过滤后文本
     */
    public static String filterEmoji(String source) {
        int len = source.length();
        StringBuilder buf = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (isNotEmojiCharacter(codePoint)) {
                buf.append(codePoint);
            }
        }
        return buf.toString();
    }

    private static boolean isNotEmojiCharacter(char codePoint) {
        return codePoint == 0x0 || codePoint == 0x9 || codePoint == 0xA || codePoint == 0xD || codePoint >= 0x20 && codePoint <= 0xD7FF || codePoint >= 0xE000 && codePoint <= 0xFFFD;
    }

    /**
     * 跳转网页
     *
     * @param url 网页链接
     */
    public static void openUrl(Context context, String url) {
        //浏览器中打开
        if (url.endsWith("openBrowser")) {
            openBrowser(context, url);
            return;
        }

        //无标题打开
        if (url.endsWith("noTitle")) {
            openUrl(context, url, true);
            return;
        }

        //广告招租介绍
        if (url.endsWith("ADActivity")) {
            MyUtils.startActivity(ADActivity.class);
            return;
        }

        //红包码
        if (url.contains(context.getResources().getString(R.string.apply_redbag_key)) || url.contains("showRedBag")) {
            showRedBag(context);
            return;
        }

        //支付宝二维码捐赠
        if (url.contains(context.getResources().getString(R.string.apply_me_key)) || url.contains("donateAlipayByPic")) {
            if (context instanceof Activity)
                AlipayUtil.donateAlipayByPic(((Activity) context));
            return;
        }

        //支付宝捐赠
        if (url.contains(context.getResources().getString(R.string.apply_me_key)) || url.contains("donateAlipay")) {
            if (context instanceof Activity)
                AlipayUtil.donateAlipay(((Activity) context));
            return;
        }


        //微信二维码捐赠
        if (url.contains(context.getResources().getString(R.string.apply_me_key)) || url.contains("donateWeiXin")) {
            if (context instanceof Activity)
                WeChatUtil.donateWeiXin(((Activity) context));
            return;
        }


        if (URLUtil.isNetworkUrl(url)) {
            context.startActivity(new Intent(context, WebActivity.class)
                    .putExtra("from_url", url));
            enterAnimation(context);
            return;
        }

        startActivity(url);
    }

    /**
     * 跳转网页
     *
     * @param url 网页链接
     */
    public static void openUrl(Context context, String url, boolean isNoTitle) {
        if (URLUtil.isNetworkUrl(url)) {
            context.startActivity(new Intent(context, WebActivity.class)
                    .putExtra("from_url", url)
                    .putExtra("isNoTitle", isNoTitle));
            enterAnimation(context);
        } else {
            startActivity(url);
        }
    }


    /**
     * 跳转网页
     *
     * @param url    网页链接
     * @param cookie Cookie
     */
    public static void openUrl(Context context, String url, String cookie) {
        context.startActivity(new Intent(context, WebActivity.class)
                .putExtra("from_url", url)
                .putExtra("cookie", cookie));
        enterAnimation(context);
    }

    /**
     * 跳转网页
     *
     * @param base_url  base_url
     * @param base_html base_html
     */
    public static void openBaseUrl(Context context, String base_title, String base_url, String
            base_html) {
        context.startActivity(new Intent(context, WebActivity.class)
                .putExtra("base_title", base_title)
                .putExtra("base_url", base_url)
                .putExtra("base_html", base_html));
        enterAnimation(context);
    }

    /**
     * 跳转网页
     *
     * @param base_url  base_url
     * @param base_html base_html
     */
    public static void openBaseUrl(Context context, String base_url, String base_html,
                                   boolean isNoTitle) {
        context.startActivity(new Intent(context, WebActivity.class)
                .putExtra("base_url", base_url)
                .putExtra("isNoTitle", isNoTitle)
                .putExtra("base_html", base_html));
        enterAnimation(context);
    }


    /**
     * 打开图片
     *
     * @param bean 图片资源
     */
    public static void openImage(Context context, ImageBean bean) {
        Intent intent = new Intent(context, ImageActivity.class);
        intent.putExtra("image_list", bean);
        context.startActivity(intent);
        enterAnimation(context);
    }

    public static void openImage(Context context, String url) {
        String[] trip = {url};
        openImage(context, ImageBean.getImageBean(trip, trip));
    }

    /**
     * @param string base64值
     * @return 返回类型
     */
    public static Bitmap base64ToBitmap(String string) {
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray = Base64.decode(string.split(",")[1], Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static void saveImageToGallery(Context context, String path) {
        LogUtils.e("图片更新路径" + path);
        File file = new File(path);
        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), file.getName(), "网页图片");
        } catch (Exception e) {
            ToastUtils.showShort("更新失败");
            e.printStackTrace();
        }
        //最后通知图库更新
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);//扫描单个文件
        intent.setData(Uri.fromFile(file));//给图片的绝对路径
        context.sendBroadcast(intent);
    }

    public static File geCacheDir() {
        String path = MyUtils.createSDCardDir("A_Tool/Cache/");
        return new File(path);
    }

    //正则表达式匹配两个指定字符串中间的内容
    public static List<String> getSubUtil(String soap, String regex) {
        List<String> list = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);// 匹配的模式
        Matcher m = pattern.matcher(soap);
        while (m.find()) {
            int i = 1;
            list.add(m.group(i));
        }
        return list;
    }

    //获取QQ头像
    public static String getQQHeader(String qq) {
        if (StringUtils.isEmpty(qq)) {
            return "default_header";
        }
        return "http://q1.qlogo.cn/g?b=qq&nk=" + qq + "&s=100";
    }


    /**
     * 跳转到应用市场app详情界面
     *
     * @param appPkg    App的包名
     * @param marketPkg 应用市场包名
     */
    public static void launchMarketApp(String appPkg, String marketPkg) {
        try {
            if (TextUtils.isEmpty(appPkg)) return;
            Uri uri = Uri.parse("market://details?id=" + appPkg);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (!TextUtils.isEmpty(marketPkg)) intent.setPackage(marketPkg);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            ToastUtils.showShort("您的手机没有安装 酷安 应用市场");
        }
    }

    /**
     * 检测辅助功能是否开启
     *
     * @return boolean
     */
    public static boolean isAccessibilitySettingsOn(Context mContext, Class<?> serviceClass) {
        int accessibilityEnabled = 0;
        // TestService为对应的服务
        final String service = AppUtils.getAppPackageName() + "/" + serviceClass.getCanonicalName();
        LogUtils.i("service:" + service);
        // com.z.buildingaccessibilityservices/android.accessibilityservice.AccessibilityService
        try {
            accessibilityEnabled = Settings.Secure.getInt(mContext.getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
            LogUtils.i("accessibilityEnabled = " + accessibilityEnabled);
        } catch (Settings.SettingNotFoundException e) {
            LogUtils.e("Error finding setting, default accessibility to not found: " + e.getMessage());
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            LogUtils.i("***ACCESSIBILITY IS ENABLED*** -----------------");
            String settingValue = Settings.Secure.getString(mContext.getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            // com.z.buildingaccessibilityservices/com.z.buildingaccessibilityservices.TestService
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessibilityService = mStringColonSplitter.next();

                    LogUtils.i("-------------- > accessibilityService :: " + accessibilityService + " " + service);
                    if (accessibilityService.equalsIgnoreCase(service)) {
                        LogUtils.i("We've found the correct setting - accessibility is switched on!");
                        return true;
                    }
                }
            }
        } else {
            LogUtils.e("***ACCESSIBILITY IS DISABLED***");
        }
        return false;
    }

    public static void showRedBag(final Context context) {
        @SuppressLint("InflateParams") final View view = LayoutInflater.from(context).inflate(R.layout.view_red_dialog, null);
        final TextView redBag = view.findViewById(R.id.gotoZFB);
        final View close = view.findViewById(R.id.close);
        final AlertDialog alert = new AlertDialog.Builder(context, R.style.translate_dialog)
                .setView(view)
                .create();
        alert.show();
        alert.setCanceledOnTouchOutside(false);
        alert.setCancelable(false);
        redBag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AlipayUtil.hasInstalledAlipayClient(context)) {
                    MyUtils.putStringToClipboard(context, context.getString(R.string.apply_redbag_key));
                    ToastUtils.showLong("请到搜索框粘贴后搜索，即可领红包！");
                    AlipayUtil.startAlipayClient(context);
                } else {
                    ToastUtils.showLong(R.string.no_apply_app);
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUtils.putStringToClipboard(context, context.getString(R.string.apply_redbag_key));
                alert.dismiss();
            }
        });
    }
}
