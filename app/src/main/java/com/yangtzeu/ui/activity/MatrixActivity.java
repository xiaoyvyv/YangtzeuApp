package com.yangtzeu.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.yangtzeu.R;
import com.yangtzeu.entity.MatrixBean;
import com.yangtzeu.ui.activity.base.BaseActivity;
import com.yangtzeu.utils.GoogleUtils;
import com.yangtzeu.utils.MyUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;

public class MatrixActivity extends BaseActivity {
    private static List<MatrixBean> matrixs = new ArrayList<>();
    private int ADD_MATRIX = 120;
    private int ACCURACY = 4;
    private int DO_ACTION = 0;
    private double MatrixDouble = 0;
    private MatrixBean D0_MatrixBean;
    private MatrixBean D1_MatrixBean;

    private Toolbar toolbar;
    private TextView which_matrix;
    private TextView which_matrix2;
    private TextView which_action;
    private TextView result;
    private LinearLayout googleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matrix);

        init();
        MyUtils.setToolbarBackToHome(this, toolbar);
    }

    @Override
    public void findViews() {
        toolbar = findViewById(R.id.toolbar);
        which_action = findViewById(R.id.which_action);
        which_matrix = findViewById(R.id.which_matrix1);
        which_matrix2 = findViewById(R.id.which_matrix2);
        result = findViewById(R.id.result);
        googleView = findViewById(R.id.googleView);

    }

    @Override
    public void setEvents() {

        AdView adView1 = GoogleUtils.newBannerView(this, AdSize.LARGE_BANNER);
        adView1.loadAd(GoogleUtils.getRequest());
        googleView.addView(adView1);
        AdView adView2 = GoogleUtils.newBannerView(this, AdSize.LARGE_BANNER);
        adView2.loadAd(GoogleUtils.getRequest());
        googleView.addView(adView2);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("矩阵列表").setIcon(R.drawable.ic_matrix)
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        showMatrixList(null);
                        return false;
                    }
                }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        menu.add("精确度").setIcon(R.drawable.ic_about)
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        final EditText edit = new EditText(MatrixActivity.this);
                        int dp20 = ConvertUtils.dp2px(25);
                        edit.setPadding(dp20, dp20 / 2, dp20, 0);
                        edit.setTextSize(15);
                        edit.setText(String.valueOf(ACCURACY));
                        edit.setInputType(InputType.TYPE_CLASS_NUMBER);
                        edit.setHint("请输入精确度的值");
                        edit.setBackgroundColor(getResources().getColor(R.color.translate));
                        AlertDialog dialog = new AlertDialog.Builder(MatrixActivity.this)
                                .setTitle("设置精确度")
                                .setView(edit)
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        MyUtils.canCloseDialog(dialog, true);
                                    }
                                })
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (edit.getText().toString().isEmpty()) {
                                            MyUtils.canCloseDialog(dialog, false);
                                            ToastUtils.showShort("请输入数的值");
                                        } else {
                                            try {
                                                int acc = Integer.valueOf(edit.getText().toString());
                                                if (acc>= 10) {
                                                    MyUtils.canCloseDialog(dialog, false);
                                                    ToastUtils.showShort("设置的精确度必须小于10");
                                                    return;
                                                }
                                                MyUtils.canCloseDialog(dialog, true);
                                                setAccuracy(acc);
                                                ToastUtils.showShort("您设置的精确度为" + ACCURACY);
                                            } catch (NumberFormatException e) {
                                                ToastUtils.showShort("你的输入有误");
                                                MyUtils.canCloseDialog(dialog, false);
                                            }
                                        }
                                    }
                                }).create();
                        dialog.show();
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setCancelable(false);
                        return false;
                    }
                }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }


    @SuppressLint("InflateParams,SetTextI18n")
    private void showMatrixList(final OnChooseListener listener) {

        View view = getLayoutInflater().inflate(R.layout.activity_matrix_dialog1, null);
        final LinearLayout container = view.findViewById(R.id.slow_container);
        if (matrixs.size() != 0) {
            container.removeAllViews();
        }

        final AlertDialog dialog = new AlertDialog.Builder(MatrixActivity.this)
                .setTitle("矩阵列表")
                .setMessage("长按删除")
                .setView(view)
                .setPositiveButton("知道了", null)
                .setNegativeButton("创建新矩阵", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showCreateMatrixDialog();
                    }
                })
                .create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);

        for (int i = 0; i < matrixs.size(); i++) {
            View dialog_item = getLayoutInflater().inflate(R.layout.activity_matrix_dialog1_item, null);
            container.addView(dialog_item);

            LinearLayout click = dialog_item.findViewById(R.id.click);
            TextView name = dialog_item.findViewById(R.id.name);
            TextView size = dialog_item.findViewById(R.id.size);
            name.setText("矩阵：" + matrixs.get(i).getName());
            size.setText("宽：" + matrixs.get(i).getWidth() + "  ×  高：" + matrixs.get(i).getHeight());

            final int finalI = i;
            click.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (matrixs.get(finalI) != null) {
                        ToastUtils.showShort("成功删除矩阵-" + matrixs.get(finalI).getName());
                        container.removeViewAt(finalI);
                        matrixs.remove(finalI);
                        dialog.dismiss();
                    }
                    return true;
                }
            });

            click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.OnChoose(dialog, matrixs.get(finalI));
                    }
                }
            });
        }
    }


    public interface OnChooseListener {
        void OnChoose(AlertDialog dialog, MatrixBean matrixBean);
    }

    @SuppressLint("InflateParams")
    private void showCreateMatrixDialog() {
        View dialog2 = getLayoutInflater().inflate(R.layout.activity_matrix_dialog2, null);
        final EditText name = dialog2.findViewById(R.id.name);
        final EditText width = dialog2.findViewById(R.id.width);
        final EditText height = dialog2.findViewById(R.id.height);
        AlertDialog dialog = new AlertDialog.Builder(MatrixActivity.this)
                .setTitle("创建新矩阵")
                .setView(dialog2)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MyUtils.canCloseDialog(dialog, true);
                    }
                })
                .setPositiveButton("创建", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String mName = name.getText().toString();
                        String mWidth = width.getText().toString();
                        String mHeight = height.getText().toString();
                        if (mName.isEmpty() || mWidth.isEmpty() || mHeight.isEmpty()) {
                            ToastUtils.showShort("请输入完整参数");
                            MyUtils.canCloseDialog(dialog, false);
                        } else {

                            int i_width = Integer.parseInt(mWidth);
                            int i_height = Integer.parseInt(mHeight);
                            LogUtils.e(i_width, i_height);

                            Intent intent = new Intent(MatrixActivity.this, MatrixAddActivity.class);
                            intent.putExtra("name", mName);
                            intent.putExtra("width", i_width);
                            intent.putExtra("height", i_height);
                            startActivityForResult(intent, ADD_MATRIX);
                            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                            MyUtils.canCloseDialog(dialog, true);
                        }
                    }
                })
                .create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == ADD_MATRIX) {
            MatrixBean matrixBean = (MatrixBean) data.getSerializableExtra("matrix");
            if (matrixBean != null) {
                matrixs.add(matrixBean);
            }
        }
    }

    public void chooseAction(View view) {
        //创建弹出式菜单对象（最低版本11）
        PopupMenu popup = new PopupMenu(MatrixActivity.this, which_action);//第二个参数是绑定的那个view
        //获取菜单填充器
        MenuInflater inflater = popup.getMenuInflater();
        //填充菜单
        inflater.inflate(R.menu.matrix_activity_menu, popup.getMenu());
        //绑定菜单项的点击事件
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ((LinearLayout) which_matrix2.getParent()).setVisibility(View.GONE);
                switch (item.getItemId()) {
                    case R.id.value:
                        which_action.setText("求矩阵的行列式值");
                        DO_ACTION = 0;
                        break;
                    case R.id.ni:
                        which_action.setText("求矩阵的逆矩阵");
                        DO_ACTION = 1;
                        break;
                    case R.id.zhuanzhi:
                        which_action.setText("求矩阵的转置矩阵");
                        DO_ACTION = 2;
                        break;
                    case R.id.zhi:
                        which_action.setText("求矩阵的秩");
                        DO_ACTION = 3;
                        break;
                    case R.id.norm:
                        which_action.setText("求矩阵的范式");
                        DO_ACTION = 4;
                        break;
                    case R.id.cond:
                        which_action.setText("求矩阵的条件数");
                        DO_ACTION = 5;
                        break;
                    case R.id.eig:
                        which_action.setText("求矩阵的特征值");
                        DO_ACTION = 6;
                        break;
                    case R.id.plus:
                        ((LinearLayout) which_matrix2.getParent()).setVisibility(View.VISIBLE);
                        which_action.setText("求两矩阵的加法");
                        DO_ACTION = 7;
                        break;
                    case R.id.minus:
                        ((LinearLayout) which_matrix2.getParent()).setVisibility(View.VISIBLE);
                        which_action.setText("求两矩阵的减法");
                        DO_ACTION = 8;
                        break;
                    case R.id.times:
                        ((LinearLayout) which_matrix2.getParent()).setVisibility(View.VISIBLE);
                        which_action.setText("求两矩阵的乘法");
                        DO_ACTION = 9;
                        break;
                    case R.id.d_times:
                        which_action.setText("求数和矩阵的乘法");
                        DO_ACTION = 10;
                        final EditText edit = new EditText(MatrixActivity.this);
                        int dp20 = ConvertUtils.dp2px(25);
                        edit.setPadding(dp20, dp20 / 2, dp20, 0);
                        edit.setTextSize(15);
                        edit.setText(String.valueOf(MatrixDouble));
                        edit.setInputType(InputType.TYPE_CLASS_NUMBER);
                        edit.setHint("请输入数的值");
                        edit.setBackgroundColor(getResources().getColor(R.color.translate));
                        AlertDialog dialog = new AlertDialog.Builder(MatrixActivity.this)
                                .setTitle("请输入矩阵所乘数的值")
                                .setView(edit)
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        MyUtils.canCloseDialog(dialog, true);
                                    }
                                })
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (edit.getText().toString().isEmpty()) {
                                            MyUtils.canCloseDialog(dialog, false);
                                            ToastUtils.showShort("请输入数的值");
                                        } else {
                                            try {
                                                MyUtils.canCloseDialog(dialog, true);
                                                MatrixDouble = Double.parseDouble(edit.getText().toString());
                                                ToastUtils.showShort("您输入数的值为" + MatrixDouble);
                                            } catch (NumberFormatException e) {
                                                ToastUtils.showShort("你的输入有误");
                                                MyUtils.canCloseDialog(dialog, false);
                                            }
                                        }
                                    }
                                }).create();
                        dialog.show();
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setCancelable(false);
                        break;
                    case R.id.left_div:
                        ((LinearLayout) which_matrix2.getParent()).setVisibility(View.VISIBLE);
                        which_action.setText("求矩阵的左除");
                        DO_ACTION = 11;
                        break;
                    case R.id.right_div:
                        ((LinearLayout) which_matrix2.getParent()).setVisibility(View.VISIBLE);
                        which_action.setText("求矩阵的右除");
                        DO_ACTION = 12;
                        break;
                }
                return false;
            }
        });
        popup.show(); //这一行代码不要忘记了

    }

    public void chooseMatrix1(View view) {
        showMatrixList(new OnChooseListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void OnChoose(AlertDialog dialog, MatrixBean matrixBean) {
                dialog.dismiss();
                D0_MatrixBean = matrixBean;
                String string = ArrayToString(matrixBean.getMatrix());
                which_matrix.setText("当前选择矩阵：" + matrixBean.getName() + "\n" + string);
            }
        });
    }

    public void chooseMatrix2(View view) {
        showMatrixList(new OnChooseListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void OnChoose(AlertDialog dialog, MatrixBean matrixBean) {
                dialog.dismiss();
                D1_MatrixBean = matrixBean;
                String string = ArrayToString(matrixBean.getMatrix());
                which_matrix2.setText("当前选择矩阵：" + matrixBean.getName() + "\n" + string);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void do_result(View view) {
        double[][] empty = {{0, 0}, {0, 0}};
        if (D0_MatrixBean == null) {
            ToastUtils.showShort("请先选择矩阵");
            return;
        }
        Matrix A, B;
        A = new Matrix(D0_MatrixBean.getMatrix());
        if (D1_MatrixBean != null) {
            B = new Matrix(D1_MatrixBean.getMatrix());
        } else {
            B = new Matrix(empty);
        }
        int rowNum_A = A.getRowDimension();  // 矩阵行数
        int colNum_A = A.getColumnDimension(); // 矩阵列数
        int rowNum_B = B.getRowDimension();  // 矩阵行数
        int colNum_B = B.getColumnDimension(); // 矩阵列数

        LogUtils.e(rowNum_A, colNum_A, rowNum_B, colNum_B);
        switch (DO_ACTION) {
            case 0:
                if (rowNum_A != colNum_A) {
                    ToastUtils.showShort("矩阵宽高必须相等");
                    break;
                }
                // 行列式
                double det = 0;
                try {
                    det = A.det();
                } catch (Exception e) {
                    String message = e.getMessage();
                    LogUtils.e(message, e.toString(), e.getLocalizedMessage());
                    if (message != null) {
                        AlertDialog dialog = new AlertDialog.Builder(MatrixActivity.this)
                                .setTitle("警告")
                                .setMessage("严重错误：\n" + message)
                                .setPositiveButton("知道了", null)
                                .create();
                        dialog.show();
                    } else ToastUtils.showLong(e.toString());
                }
                double detNum = MyUtils.getScale(det, ACCURACY);
                result.setText("计算结果：" + detNum);
                break;
            case 1:
                try {
                    //求矩阵的逆
                    Matrix inverse = A.inverse();
                    String string = ArrayToString(inverse.getArray());
                    result.setText("计算结果：矩阵的逆\n" + string);
                    if (rowNum_A != colNum_A) {
                        AlertDialog dialog = new AlertDialog.Builder(MatrixActivity.this)
                                .setTitle("提示")
                                .setMessage("线性代数书上定义：\n对于n阶矩阵A，如果有一个n阶矩阵B，使AB=BA=E，则说矩阵A是可逆的。\n\n这个概念下必须是方阵，我们开始学的就是只有方阵。\n\n这里程序考虑广义逆，则可以是m*n的。\n\n进行求逆就是求它的伪逆。")
                                .setPositiveButton("知道了", null)
                                .create();
                        dialog.show();
                    }
                } catch (Exception e) {
                    String message = e.getMessage();
                    if (message != null) {
                        AlertDialog dialog = new AlertDialog.Builder(MatrixActivity.this)
                                .setTitle("警告")
                                .setMessage("严重错误：\n" + message + "\n\n温馨提示：求矩阵的逆，行列式的值不能为0，且为方阵")
                                .setPositiveButton("知道了", null)
                                .create();
                        dialog.show();
                    } else ToastUtils.showLong(e.toString());
                }

                break;
            case 2:
                try {
                    //求矩阵的转置
                    Matrix transpose = A.transpose();
                    String string = ArrayToString(transpose.getArray());
                    result.setText("计算结果：矩阵转置\n" + string);
                } catch (Exception e) {
                    String message = e.getMessage();
                    if (message != null) {
                        AlertDialog dialog = new AlertDialog.Builder(MatrixActivity.this)
                                .setTitle("警告")
                                .setMessage("严重错误：\n" + message)
                                .setPositiveButton("知道了", null)
                                .create();
                        dialog.show();
                    } else ToastUtils.showLong(e.toString());
                }
                break;
            case 3:
                int rank = A.rank();
                result.setText("计算结果：矩阵的秩\n" + rank);
                break;
            case 4:
                double norm1 = A.norm1();
                result.setText("计算结果：矩阵的范式\n" + MyUtils.getScale(norm1, ACCURACY));
                break;
            case 5:
                double cond = A.cond();
                result.setText("计算结果：矩阵的条件数\n" + MyUtils.getScale(cond, ACCURACY));
                break;
            case 6:
                try {
                    //求矩阵的特征值
                    EigenvalueDecomposition eig = A.eig();
                    //特征向量矩阵
                    Matrix v = eig.getV();
                    //块对角特征值矩阵
                    Matrix d = eig.getD();

                    //特征值的虚部
                    double[] imag = eig.getImagEigenvalues();
                    //特征值的实部
                    double[] real = eig.getRealEigenvalues();

                    result.setText("计算结果：矩阵特征值\n" +
                            "\n特征值的虚部：" + Arrays.toString(imag) +
                            "\n特征值的实部：" + Arrays.toString(real) +
                            "\n块对角特征值矩阵：" + ArrayToString(d.getArray()) +
                            "\n特征向量矩阵：" + ArrayToString(v.getArray()));
                } catch (Exception e) {
                    String message = e.getMessage();
                    if (message != null) {
                        AlertDialog dialog = new AlertDialog.Builder(MatrixActivity.this)
                                .setTitle("警告")
                                .setMessage("严重错误：\n" + message)
                                .setPositiveButton("知道了", null)
                                .create();
                        dialog.show();
                    } else ToastUtils.showLong(e.toString());
                }
                break;
            case 7:
                //矩阵加法
                if (D1_MatrixBean == null) {
                    ToastUtils.showShort("请选择第二个矩阵");
                    break;
                }
                if (colNum_A != colNum_B || rowNum_A != rowNum_B) {
                    ToastUtils.showShort("矩阵" + D0_MatrixBean.getName() + "和矩阵" + D1_MatrixBean.getName() + "宽高必须一致");
                    break;
                }

                Matrix Plus = new Matrix(empty);
                try {
                    Plus = A.plus(B);
                } catch (Exception e) {
                    String message = e.getMessage();
                    if (message != null) {
                        AlertDialog dialog = new AlertDialog.Builder(MatrixActivity.this)
                                .setTitle("警告")
                                .setMessage("严重错误：\n" + message)
                                .setPositiveButton("知道了", null)
                                .create();
                        dialog.show();
                    } else ToastUtils.showLong(e.toString());
                }
                String plus_Result = ArrayToString(Plus.getArray());
                result.setText("计算结果：矩阵" + D0_MatrixBean.getName() + "  +  矩阵" + D1_MatrixBean.getName() + "\n" + plus_Result);
                break;
            case 8:
                //矩阵减法
                if (D1_MatrixBean == null) {
                    ToastUtils.showShort("请选择第二个矩阵");
                    break;
                }

                if (colNum_A != colNum_B || rowNum_A != rowNum_B) {
                    ToastUtils.showShort("矩阵" + D0_MatrixBean.getName() + "和矩阵" + D1_MatrixBean.getName() + "宽高必须一致");
                    break;
                }

                Matrix Minus = new Matrix(empty);
                try {
                    Minus = A.minus(B);
                } catch (Exception e) {
                    String message = e.getMessage();
                    if (message != null) {
                        AlertDialog dialog = new AlertDialog.Builder(MatrixActivity.this)
                                .setTitle("警告")
                                .setMessage("严重错误：\n" + message)
                                .setPositiveButton("知道了", null)
                                .create();
                        dialog.show();
                    } else ToastUtils.showLong(e.toString());
                }
                String minus_Result = ArrayToString(Minus.getArray());
                result.setText("计算结果：矩阵" + D0_MatrixBean.getName() + "  -  矩阵" + D1_MatrixBean.getName() + "\n" + minus_Result);
                break;
            case 9:
                //矩阵乘法
                if (D1_MatrixBean == null) {
                    ToastUtils.showShort("请选择第二个矩阵");
                    break;
                }
                if (colNum_A != rowNum_B) {
                    ToastUtils.showShort("矩阵" + D0_MatrixBean.getName() + "的宽度必须等于矩阵" + D1_MatrixBean.getName() + "的高度");
                    break;
                }

                Matrix Times = new Matrix(empty);
                try {
                    Times = A.times(B);
                } catch (Exception e) {
                    String message = e.getMessage();
                    if (message != null) {
                        AlertDialog dialog = new AlertDialog.Builder(MatrixActivity.this)
                                .setTitle("警告")
                                .setMessage("严重错误：\n" + message)
                                .setPositiveButton("知道了", null)
                                .create();
                        dialog.show();
                    } else ToastUtils.showLong(e.toString());
                }
                String times_Result = ArrayToString(Times.getArray());
                result.setText("计算结果：矩阵" + D0_MatrixBean.getName() + "  ×  矩阵" + D1_MatrixBean.getName() + "\n" + times_Result);
                break;
            case 10:
                //矩阵放大缩小
                Matrix DoubleTimes = new Matrix(empty);
                try {
                    DoubleTimes = A.times(MatrixDouble);
                } catch (Exception e) {
                    String message = e.getMessage();
                    if (message != null) {
                        AlertDialog dialog = new AlertDialog.Builder(MatrixActivity.this)
                                .setTitle("警告")
                                .setMessage("严重错误：\n" + message)
                                .setPositiveButton("知道了", null)
                                .create();
                        dialog.show();
                    } else ToastUtils.showLong(e.toString());
                }
                String d_times_Result = ArrayToString(DoubleTimes.getArray());
                result.setText("计算结果：矩阵" + D0_MatrixBean.getName() + "  ×  " + MatrixDouble + "\n" + d_times_Result);
                break;
            case 11:
                //矩阵左除
                if (D1_MatrixBean == null) {
                    ToastUtils.showShort("请选择第二个矩阵");
                    break;
                }
                if (colNum_A != colNum_B || rowNum_A != rowNum_B) {
                    ToastUtils.showShort("矩阵" + D0_MatrixBean.getName() + "和矩阵" + D1_MatrixBean.getName() + "宽高必须一致");
                    break;
                }

                Matrix left = new Matrix(empty);
                try {
                    left = A.arrayLeftDivide(B);
                } catch (Exception e) {
                    String message = e.getMessage();
                    if (message != null) {
                        AlertDialog dialog = new AlertDialog.Builder(MatrixActivity.this)
                                .setTitle("警告")
                                .setMessage("严重错误：\n" + message)
                                .setPositiveButton("知道了", null)
                                .create();
                        dialog.show();
                    } else ToastUtils.showLong(e.toString());
                }
                String left_Result = ArrayToString(left.getArray());
                result.setText("计算结果：矩阵" + D0_MatrixBean.getName() + "  左除  矩阵" + D1_MatrixBean.getName() + "\n" + left_Result);
                break;
            case 12:
                //矩阵右除
                if (D1_MatrixBean == null) {
                    ToastUtils.showShort("请选择第二个矩阵");
                    break;
                }

                if (colNum_A != colNum_B || rowNum_A != rowNum_B) {
                    ToastUtils.showShort("矩阵" + D0_MatrixBean.getName() + "和矩阵" + D1_MatrixBean.getName() + "宽高必须一致");
                    break;
                }
                Matrix right = new Matrix(empty);
                try {
                    right = A.arrayLeftDivide(B);
                } catch (Exception e) {
                    String message = e.getMessage();
                    if (message != null) {
                        AlertDialog dialog = new AlertDialog.Builder(MatrixActivity.this)
                                .setTitle("警告")
                                .setMessage("严重错误：\n" + message)
                                .setPositiveButton("知道了", null)
                                .create();
                        dialog.show();
                    } else ToastUtils.showLong(e.toString());
                }
                String right_Result = ArrayToString(right.getArray());
                result.setText("计算结果：矩阵" + D0_MatrixBean.getName() + "  右除  矩阵" + D1_MatrixBean.getName() + "\n" + right_Result);
                break;
        }


    }

    private String ArrayToString(double[][] doubles) {
        if (doubles == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        for (double[] aDouble : doubles) {
            for (double anADouble : aDouble) {
                builder.append(MyUtils.getScale(anADouble, ACCURACY));
                builder.append("\t\t");
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    public void setAccuracy(int i) {
        ACCURACY = i;
    }
}
