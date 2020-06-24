package com.gxun.curriculum;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView addCourseTv;
    Spinner weekSp;

    private RelativeLayout relativeLayout;

    private CourseDao courseDao = new CourseDao(this);

    private int gridHeight, gridWidth; // 屏幕显示RelativeLayout的宽度、高度
    private int week = 0; // spinner的周数下标
    private long exitTime; // 获取第一次点击返回键的系统时间
    private List<TextView> textViewList = new ArrayList<>(); // 存储所有生成的textView的id，id为t_course表中的id键值
    private static final String TERM_START_TIME = "2020-02-24 00:00:00"; // 开学时间

    private String color[] = { // 颜色代码字符串数组
            "#8AD297", "#5ABF6C", "#F9A883", "#F79060", "#88CFCC", "#63C0BD",
            "#F19C99", "#ED837F", "#F7C56B", "#F5B94E", "#D2A596", "#CA9483",
            "#67BDDE", "#31A6D3", "#9CCF5A", "#8BC73D", "#9AB4CF", "#87A6C6",
            "#9AB4CF", "#87A6C6", "#E593AD", "#6DB69C", "#E593AD", "#DF7999",
            "#E2C38A", "#D6A858", "#B29FD2", "#997FC3", "#E2C490", "#DDB97B",
            "#7B7B7B", "#ADADAD"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 初始化控件
        addCourseTv = findViewById(R.id.add);
        weekSp = findViewById(R.id.weekSp);

        // 设置relativeLayout的宽度高度
        gridWidth = 142;
        gridHeight = 164;
        addCourseTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, AddCourseActivity.class);
                startActivity(intent);
            }
        });
        try {
            setCurrentWeek(); // 设置当前周数
        } catch (ParseException e) {
            e.printStackTrace();
        }
        weekSp.setSelection(week, true); // 设置spinner当前的默认周数
        initialCurriculum();
        weekSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                week = position;
                initialCurriculum(); // 选择完周数后重新初始化课表
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setCurrentWeek() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(TERM_START_TIME);
        long s1 = date.getTime(); // 将时间转为毫bai秒
        long s2 = System.currentTimeMillis(); // 得到当前的毫秒
        long day = (s2 - s1) / 1000 / 60 / 60 / 24 / 7; // 获得现在的周数
        week = (int) day; // 设置周数
    }

    // 初始化课程表
    private void initialCurriculum() {
        // 当list的size大于0，说明有textView，此时清空所有TextView
        if (textViewList.size() > 0) {
            for (TextView tv : textViewList) {
                tv.setVisibility(View.GONE); // 不显示textView
            }
            textViewList.clear(); // 清空List
        }
        // 选择结课日期比当前周数还大的课程
        List<Course> courseList = courseDao.selectCourse(String.valueOf(week));

        for (Course c : courseList) {
            String courseInfo = c.getCourseName() + "@" + c.getClassroom();
            addView(c.getId(), c.getDay(), c.getStartSection(), c.getEndSection(), courseInfo);
        }
    }

    // 获取当前日期
    private int getToday() {
        Calendar cal = Calendar.getInstance();
        int today = cal.get(Calendar.DAY_OF_WEEK) - 1;
        return today;
    }

    // 创建textView
    private TextView createTv(int id, int d, int startSection, int endSection, String courseName) {
        TextView tv = new TextView(this);
        // 指定高度和宽度
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(gridWidth, gridHeight * (endSection - startSection + 1));
        // 指定位置
        tv.setY(gridHeight * (startSection - 1));
        tv.setId(id);
        tv.setLayoutParams(params);
        tv.setGravity(Gravity.CENTER);
        tv.setText(courseName);
        if (d != getToday()) {
            tv.setTextColor(Color.parseColor("#FFFFFF"));
            tv.setBackgroundColor(Color.parseColor(color[new Random().nextInt((color.length - 1))]));
        }
        textViewList.add(tv); // 存储textView
        addListener(tv); // 每创建一个textView就添加一个点击事件跟长按事件
        return tv;
    }

    // 设置监听器
    private void addListener(TextView textView) {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Course course = courseDao.selectCourseById(v.getId());
                showCourseDetail(course);
            }
        });
        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (courseDao.deleteCourse(v.getId()) >= 0) {
                    v.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "删除课程成功！", Toast.LENGTH_LONG).show();
                    return true;
                } else {
                    Toast.makeText(MainActivity.this, "删除课程失败！", Toast.LENGTH_LONG).show();
                    return false;
                }
            }
        });
    }

    // 显示课程详情
    private void showCourseDetail(Course course) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("课程详情");
        builder.setMessage(course.showCourseInfo());
        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

//    // 提示是否删除课程
//    private boolean requestDelCourse() {
//        isDelete = false;
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("提示");
//        builder.setMessage("确定删除该课程？");
//        builder.setPositiveButton("确定",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        isDelete = true;
//                    }
//                });
//        builder.setNegativeButton("取消",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        isDelete = false;
//                    }
//                });
//        AlertDialog dialog = builder.create();
//        dialog.show();
//        return isDelete;
//    }

    // 重载addView();添加视图
    private void addView(int id, int d, int startSection, int endSection, String courseName) {
        TextView tv;
        switch (d) {
            case 1:
                relativeLayout = findViewById(R.id.monday);
                setTodayCouseBackgroundColor(d);
                break;
            case 2:
                relativeLayout = findViewById(R.id.tuesday);
                setTodayCouseBackgroundColor(d);
                break;
            case 3:
                relativeLayout = findViewById(R.id.wednesday);
                setTodayCouseBackgroundColor(d);
                break;
            case 4:
                relativeLayout = findViewById(R.id.thursday);
                setTodayCouseBackgroundColor(d);
                break;
            case 5:
                relativeLayout = findViewById(R.id.friday);
                setTodayCouseBackgroundColor(d);
                break;
            case 6:
                relativeLayout = findViewById(R.id.saturday);
                setTodayCouseBackgroundColor(d);
                break;
            case 7:
                relativeLayout = findViewById(R.id.sunday);
                setTodayCouseBackgroundColor(d);
                break;
        }
        tv = createTv(id, d, startSection, endSection, courseName);
        relativeLayout.addView(tv);
    }

    // 同一设置当日课程背景颜色
    private void setTodayCouseBackgroundColor(int i) {
        if (i == getToday()) {
            relativeLayout.setBackgroundColor(Color.parseColor("#FFD4FFD1"));
        }
    }

    // 监听按键（返回键）
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && (event.getRepeatCount() == 0)) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    // 退出当前Activity
    private void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {  //系统时间减去exitTime
            Toast.makeText(MainActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish(); // 结束Activity
            System.exit(0);
        }
    }

    // 添加完成后回到MainActivity重新初始化课程表
    @Override
    protected void onResume() {
        super.onResume();
        initialCurriculum();
    }
}