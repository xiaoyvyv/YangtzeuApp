package com.lib.calculator.calculator;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.lib.calculator.expression.Constants;
import com.lib.calculator.expression.ExpressionHandler;
import com.yangtzeu.R;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

public class CalculatorActivity extends BaseActivity {

    @SuppressLint("StaticFieldLeak")
    public static CalculatorActivity activity;
    private Context context;
    private Toolbar toolbar;
    private EditText inText;
    private TextView stateText;
    private TextView outText;
    private ViewPager drawerPager;
    private DrawerLayout drawer;
    private ArrayList<View> drawerPageList;
    public FrameLayout delete;
    private TextView close;
    private TextView clear;

    private static final int[] XX = {1, 3, 1, 3};
    private static final int[] YY = {6, 4, 5, 5};

    private static final String[] OPERATOR = {"÷", "×", "-", "+", "%", ",", "i"};
    private static final String[] OPERATOR_VICE = {"√", "^", "!", "()", "°", "∞", "x"};

    private static final String[][] BUTTON = {
            {"sqrt", "cbrt", "root", "rand", "randInt", "abs", "lg", "ln", "log",
                    "min", "max", "fact", "sin", "cos", "tan", "asin", "acos",
                    "atan", "re", "im", "arg", "norm", "reg", "conj", "diff",
                    "sum", "lim", "eval", "fzero", "integ", "exp", "gcd", "lcm",
                    "perm", "comb", "gamma", "round", "floor", "ceil", "sign",
                    "remn", "prime", "isPrime", "prec", "base"},
            {"ans", "reg", "π", "e", "F", "h", "ћ", "γ", "φ", "c",
                    "N", "R", "K", "k", "G", "Φ", "true", "false", "me", "mn", "mp"}};

    private static final String[][] BUTTON_VICE = {
            {"平方根", "立方根", "开方", "随机复数", "随机整数", "绝对值", "常用对数", "自然对数", "对数",
                    "最小", "最大", "阶乘", "正弦", "余弦", "正切", "反正弦", "反余弦", "反正切", "实部",
                    "虚部", "辐角", "模长", "寄存", "共轭复数", "导函数", "累加求和", "极限", "求值",
                    "函数零点", "定积分", "e底指数", "最大公约", "最小公倍", "排列", "组合", "伽玛函数",
                    "四舍五入", "向下取整", "向上取整", "取正负号", "取余", "质数", "判断质数", "输出精度", "输出进制"},
            {"上次运算", "寄存器", "圆周率", "自然底数", "法拉第", "普朗克", "约化普朗克",
                    "欧拉", "黄金分割", "光速", "阿伏伽德罗", "理想气体", "卡钦", "玻尔兹曼",
                    "万有引力", "磁通量子", "真", "假", "电子质量", "质子质量", "中子质量"}};

    private static final Pattern FUNCTIONS_KEYWORDS = Pattern.compile(
            "\\b(" + "sqrt|cbrt|root|rand|randInt|lg|ln|log|abs|min|max|fact|" +
                    "sin|cos|tan|asin|acos|atan|re|im|arg|norm|reg|conj|diff|" +
                    "sum|lim|eval|fzero|integ|exp|gcd|lcm|perm|comb|round|floor|" +
                    "ceil|sign|gamma|remn|prime|isPrime|prec|base|Γ" + ")\\b");

    private static final Pattern CONSTANS_KEYWORDS2 = Pattern.compile(
            "\\b(" + "ans|reg|true|false|me|mn|mp" + ")\\b");

    private static final Pattern CONSTANS_KEYWORDS1 = Pattern.compile("[∞i°%πeFhћγφcNRkGΦ]");

    private static final String[] FUNCTION_LIST = {"科学计算", "大数计算", "进制转换", "大写数字"};

    private static final String[] NUMERIC = {
            "7", "8", "9",
            "4", "5", "6",
            "1", "2", "3",
            "·", "0", "=",
            "A", "B", "C",
            "D", "E", "F",
            "⑵", "⑶", "⑷",
            "⑸", "⑹", "⑺",
            "⑻", "⑼", "⑽",
            "⑾", "⑿", "⒀",
            "⒁", "⒂", "⒃"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator_main);
        initToolBar();
        initEditText();
        initTextView();
        initDrawer();
        initPages();
        initTabs();
        initDelete();
        initNumeric();
        initOperator();
        initFunction();
    }

    private void initDelete() {
        delete = findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable editable = inText.getText();
                int index = inText.getSelectionStart();
                int index2 = inText.getSelectionEnd();
                if (index == index2) {
                    if (index == 0) return;
                    editable.delete(index - 1, index);
                } else {
                    editable.delete(index, index2);
                }
            }
        });
        delete.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ExpressionHandler.stop();
                inText.setText(null);
                return true;
            }
        });
    }

    private void initTextView() {
        stateText = findViewById(R.id.text_state);
        stateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExpressionHandler.stop();
                stateText.setText(null);
            }
        });
        outText = findViewById(R.id.text_out);
        outText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(rootValue);
                Snackbar.make(v, "已复制运算结果", Snackbar.LENGTH_SHORT).show();
            }
        });
        outText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ResultsActivity.actionStart(v.getContext(), rootValue);
                return true;
            }
        });
    }

    private void initDrawer() {
        drawer = findViewById(R.id.drawer_main);
        close = findViewById(R.id.close);
        clear = findViewById(R.id.clear);

        findViewById(R.id.drawer_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExpressionHandler.stop();
                inText.setText(null);
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalculatorActivity.this.onBackPressed();
            }
        });

    }

    private void initTabs() {
        TabLayout tabs = findViewById(R.id.tabs_main);
        tabs.setupWithViewPager(drawerPager);
        tabs.getTabAt(0).setText("函数（长按提示）");
        tabs.getTabAt(1).setText("常数");
    }

    private void initPages() {
        drawerPageList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            GridView gridView = new GridView(this);
            drawerPageList.add(gridView);
        }

        drawerPager = findViewById(R.id.viewPager_drawer);
        MainPagerAdapter drawerPagerAdapter = new MainPagerAdapter(drawerPageList);
        drawerPager.setAdapter(drawerPagerAdapter);
    }


    private void initNumeric() {
        GridView numericBar = findViewById(R.id.bar_numeric);
        numericBar.setNumColumns(XX[1]);
        numericBar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = position == 9 ? "." : NUMERIC[position];
                if (str.equals("=")) {
                    if (calcThread != null) {
                        Snackbar.make(view, "请等待当前运算完成", Snackbar.LENGTH_SHORT)
                                .setAction("停止运算", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ExpressionHandler.stop();
                                    }
                                }).show();
                        return;
                    }
                    outText.setTextColor(0xffbdbdbd);
                    stateText.setText("运算中...");
                    calcThread = new Calc(inText.getText().toString());
                    calcThread.start();
                    return;
                }
                modifyInText(str);
            }
        });
        GridViewAdapter numericAdapter = new GridViewAdapter(numericBar, Arrays.asList(NUMERIC),
                null, R.layout.button_numeric, YY[1]);
        numericBar.setAdapter(numericAdapter);
    }

    private void initOperator() {
        GridView operatorBar = findViewById(R.id.bar_operator);
        operatorBar.setNumColumns(XX[2]);
        operatorBar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = position == 0 ? "/" : OPERATOR[position];
                modifyInText(position == 1 ? "•" : str);
            }
        });
        operatorBar.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                modifyInText(OPERATOR_VICE[position]);
                return true;
            }
        });
        GridViewAdapter operatorAdapter = new GridViewAdapter(operatorBar, Arrays.asList(OPERATOR),
                Arrays.asList(OPERATOR_VICE), R.layout.button_operator, YY[2]);
        operatorBar.setAdapter(operatorAdapter);
    }

    private void initFunction() {
        int i = 0;
        for (View view : drawerPageList) {
            GridView operatorProBar = (GridView) view;
            operatorProBar.setNumColumns(XX[3]);

            if (i == 0) {
                operatorProBar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (drawer.isDrawerOpen(GravityCompat.START)) {
                            drawerPager.setCurrentItem(0);
                            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, GravityCompat.START);
                            drawer.closeDrawer(GravityCompat.START);
                        } else
                            drawer.openDrawer(GravityCompat.START);

                        modifyInText((BUTTON[0][position].equals("gamma") ? "Γ" : BUTTON[0][position]) + "()");
                    }
                });

                operatorProBar.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        String text = BUTTON[0][position];
                        AlertDialog.Builder dialog = new AlertDialog.Builder(CalculatorActivity.this);
                        dialog.setTitle(text);
                        dialog.setMessage(HelpUtil.getFunctionHelp(text) + "\n\n使用格式：\n函数名(参数,参数,参数....)\n\n如：\n求导：diff(2x^2+2x,5)");
                        dialog.setPositiveButton("确定", null);
                        dialog.show();
                        return true;
                    }
                });
            } else {
                operatorProBar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        modifyInText(BUTTON[1][position]);
                    }
                });
            }
            int id = i == 0 ? R.layout.button_function : R.layout.button_constant;
            GridViewAdapter operatorProAdapter = new GridViewAdapter(operatorProBar,
                    Arrays.asList(BUTTON[i++]), Arrays.asList(BUTTON_VICE[i - 1]), id, YY[3]);

            operatorProBar.setAdapter(operatorProAdapter);
        }
    }

    private void modifyInText(String str) {
        int index = inText.getSelectionStart();
        int index2 = inText.getSelectionEnd();
        if (index == index2) {
            inText.getText().insert(index, str);
        } else {
            inText.getText().replace(index, index2, str);
        }
    }

    class FastCalc extends Thread implements Runnable {
        private String exp;

        public FastCalc(String exp) {
            this.exp = exp;
        }

        @Override
        public void run() {
            final long t = System.currentTimeMillis();
            final String[] value = ExpressionHandler.calculation(exp);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    outText.setTextColor(0xffbdbdbd);
                    stateText.setText("运算结束，耗时 " + (System.currentTimeMillis() - t) + " 毫秒");
                    if (value[0].getBytes().length > 1000) {
                        outText.setText("数值太大，请长按此处显示结果");
                    } else
                        outText.setText(value[0]);
                    rootValue = value[0];
                    calcThread = null;
                }
            });
        }
    }

    class Calc extends Thread implements Runnable {
        private String exp;

        public Calc(String exp) {
            this.exp = exp;
        }

        @Override
        public void run() {
            final long t = System.currentTimeMillis();
            final String[] value = ExpressionHandler.calculation(exp);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    stateText.setText("运算结束，耗时 " + (System.currentTimeMillis() - t) + " 毫秒");
                    if (value[1].equals("true")) {
                        outText.setTextColor(0xffff4081);
                        outText.setText(value[0]);
                    } else {
                        Constants.setAns(value[0]);
                        if (value[0].getBytes().length > 1000) {
                            outText.setText("数值太大，请长按此处显示结果");
                            ResultsActivity.actionStart(context, value[0]);
                        } else
                            outText.setText(value[0]);
                    }
                    rootValue = value[0];
                    calcThread = null;
                }
            });
        }

    }

    private boolean modified = true;
    private int selection = 0;
    private Thread calcThread;
    private String rootValue;

    private void initEditText() {
        inText = (EditText) findViewById(R.id.editText);
        AutofitHelper.create(inText).setMinTextSize(28).setMaxLines(1);
        inText.requestFocus();
        inText.requestFocusFromTouch();
        inText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    if (calcThread == null)
                        stateText.setText(null);
                    outText.setTextColor(0xffbdbdbd);
                    outText.setText(null);
                    rootValue = null;
                    return;
                }

                if (calcThread == null) {
                    stateText.setText("运算中...");
                    calcThread = new FastCalc(s.toString());
                    calcThread.start();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!modified) return;

                selection = inText.getSelectionStart();
                s.clearSpans();

                for (Matcher m = Pattern.compile("x").matcher(s); m.find(); )
                    s.setSpan(new ForegroundColorSpan(0xfff48fb1), m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                for (Matcher m = CONSTANS_KEYWORDS1.matcher(s); m.find(); )
                    s.setSpan(new ForegroundColorSpan(0xfffff59d), m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                for (Matcher m = CONSTANS_KEYWORDS2.matcher(s); m.find(); )
                    s.setSpan(new ForegroundColorSpan(0xfffff59d), m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                for (Matcher m = FUNCTIONS_KEYWORDS.matcher(s); m.find(); )
                    s.setSpan(new ForegroundColorSpan(0xffa5d6a7), m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                for (Matcher m = Pattern.compile("[()\\-*+.,/!^=√•]").matcher(s); m.find(); )
                    s.setSpan(new ForegroundColorSpan(0xff81d4fa), m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                modified = false;
                inText.setText(s);
                modified = true;

                if (selection >= 2 && s.toString().substring(selection - 2, selection).equals("()"))
                    selection--;
                inText.setSelection(selection);
            }
        });
    }

    private void initToolBar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("科学计算");
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalculatorActivity.this.onBackPressed();
            }
        });
    }

    private void setGodMode(boolean isGodMode) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        ActionBar actionBar = getSupportActionBar();
        godMenuItem.setChecked(isGodMode);
        if (isGodMode) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            drawer.setVisibility(View.GONE);
            Class<EditText> cls = EditText.class;
            Method method;
            try {
                method = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(inText, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            imm.showSoftInput(inText, InputMethodManager.SHOW_FORCED);
        } else {
            actionBar.setDisplayHomeAsUpEnabled(true);
            drawer.setVisibility(View.VISIBLE);
            Class<EditText> cls = EditText.class;
            Method method;
            try {
                method = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(inText, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            imm.hideSoftInputFromWindow(inText.getWindowToken(), 0);
        }
    }

    private MenuItem godMenuItem;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean isGodMode = preferences.getBoolean("godMode", false);
        godMenuItem = menu.add("自由输入").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                boolean isGodMode = !item.isChecked();
                preferences.edit().putBoolean("godMode", isGodMode).apply();
                setGodMode(isGodMode);
                return true;
            }
        }).setCheckable(true).setChecked(isGodMode);
        setGodMode(isGodMode);

        menu.add("帮助").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(CalculatorActivity.this);
                dialog.setTitle("帮助")
                        .setMessage(R.string.app_help)
                        .setPositiveButton("确定", null)
                        .show();
                return true;
            }
        });
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }
        super.onBackPressed();
    }
/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawerPager.setCurrentItem(0);
                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, GravityCompat.START);
                    drawer.closeDrawer(GravityCompat.START);
                } else
                    drawer.openDrawer(GravityCompat.START);
                break;
        }
        return true;
    }
    */
}
