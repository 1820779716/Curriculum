package com.gxun.curriculum;

import android.content.ContentValues;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddCourseActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText courseIdEt, courseNameEt, classroomEt, teacherEt, startSectionEt, endSectionEt, startWeekEt, endWeekEt;
    private Spinner daySp, weekTypeSp;
    private TextView saveTv, cancelTv; // 保存、返回的控件

    private CourseDao courseDao;

    private boolean isAllDataRight = true;

    // 接收各组件的值
    private String courseId, courseName, classroom, teacher;
    private int day, weekType, startSection, endSection, startWeek, endWeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_course);

        initialParameter();

        startSectionEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("")) {
                    startSection = Integer.parseInt(s.toString());
                    checkStartSection();
                } else {
                    startSection = 0;
                }
            }
        });
        endSectionEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("")) {
                    endSection = Integer.parseInt(s.toString());
                    checkEndSection();
                } else {
                    endSection = 0;
                }
            }
        });
        startWeekEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("")) {
                    startWeek = Integer.parseInt(s.toString());
                    checkStartWeek();
                } else {
                    startWeek = 0;
                }
            }
        });
        endWeekEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("")) {
                    endWeek = Integer.parseInt(s.toString());
                    checkEndWeek();
                } else {
                    endWeek = 0;
                }
            }
        });
        daySp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                day = position+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        weekTypeSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                weekType = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        saveTv.setOnClickListener(this);
        cancelTv.setOnClickListener(this);
    }

    // 初始化
    private void initialParameter() {
        courseIdEt = findViewById(R.id.courseId);
        courseNameEt = findViewById(R.id.courseName);
        classroomEt = findViewById(R.id.classroom);
        teacherEt = findViewById(R.id.teacher);
        daySp = findViewById(R.id.day);
        startSectionEt = findViewById(R.id.startSection);
        endSectionEt = findViewById(R.id.endSection);
        weekTypeSp = findViewById(R.id.weekType);
        startWeekEt = findViewById(R.id.startWeek);
        endWeekEt = findViewById(R.id.endWeek);
        saveTv = findViewById(R.id.save);
        cancelTv = findViewById(R.id.back);

        day = weekType = startSection = endSection = startWeek = endWeek = 0;
        courseId = courseName = classroom = teacher = "";

        courseDao = new CourseDao(this);
    }

    // 检验输入的数值
    private void checkStartSection() {
        isAllDataRight = true;
        if (startSection < 1 || startSection > 11) {
            Toast.makeText(this, "输入不在1~11的范围内，请重新输入。", Toast.LENGTH_LONG).show();
            isAllDataRight = false;
        }
    }

    private void checkEndSection() {
        isAllDataRight = true;
        if (endSection < 1 || endSection > 11) {
            Toast.makeText(this, "输入不在1~11的范围内，请重新输入。", Toast.LENGTH_LONG).show();
            isAllDataRight = false;
        } else if (startSection > endSection) {
            Toast.makeText(this, "开始节次大于结束节次，请重新输入。", Toast.LENGTH_LONG).show();
            isAllDataRight = false;
        }
    }

    private void checkStartWeek() {
        isAllDataRight = true;
        if (startWeek < 1 || startWeek > 22) {
            Toast.makeText(this, "输入不在1~22的范围内，请重新输入。", Toast.LENGTH_LONG).show();
            isAllDataRight = false;
        }
    }

    private void checkEndWeek() {
        isAllDataRight = true;
        if (endWeek < 1 || endWeek > 22) {
            Toast.makeText(this, "输入不在1~22的范围内，请重新输入。", Toast.LENGTH_LONG).show();
            isAllDataRight = false;
        }
        if (startWeek > endWeek) {
            Toast.makeText(this, "开始周数大于结束周数，请重新输入。", Toast.LENGTH_LONG).show();
            isAllDataRight = false;
        }
    }


    // 点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save:
                addCourse();
                break;
            case R.id.back:
                finish(); // 结束当前Activity
                break;
        }
    }

    // 添加课程
    public void addCourse() {
        if (!isAllDataRight || startSection>endSection || startWeek>endWeek) {
            Toast.makeText(this, "课程信息有误", Toast.LENGTH_LONG).show();
        } else if (!checkCourseInfo()) {
            Toast.makeText(this, "课程信息不完整", Toast.LENGTH_LONG).show();
        } else {
            /**
             * ContentValues类似于MAP
             * 提供了存取数据对应的put(String key, value)和getAsXxx(String key)方法
             * key为字段名称，value为字段值，Xxx指的是各种常用的数据类型
             */
            ContentValues cv = new ContentValues();
            cv.put("courseId",courseId);
            cv.put("courseName",courseName);
            cv.put("classroom",classroom);
            cv.put("teacher",teacher);
            cv.put("day",day);
            cv.put("weekType",weekType);
            cv.put("startSection",startSection);
            cv.put("endSection",endSection);
            cv.put("startWeek",startWeek);
            cv.put("endWeek",endWeek);
            if (courseDao.insertCourse(cv) >= 0){
                Toast.makeText(AddCourseActivity.this, "添加课程成功！", Toast.LENGTH_LONG).show();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finish();
            }else{
                Toast.makeText(AddCourseActivity.this, "添加课程失败！", Toast.LENGTH_LONG).show();
            }
        }
    }

    // 检验所有值
    private boolean checkCourseInfo() {

        boolean flag = true;

        courseId = courseIdEt.getText().toString();
        courseName = courseNameEt.getText().toString();
        classroom = classroomEt.getText().toString();
        teacher = teacherEt.getText().toString();
        if (courseId.equals("") || courseName.equals("") || classroom.equals("")
                || teacher.equals("") || !isAllDataRight || startSection == 0 || endSection == 0 || startWeek == 0 || endWeek == 0) {
            flag = false;
        }
        return flag;
    }

    // 设置点击返回键事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && (event.getRepeatCount() == 0)) {
            finish(); //按下返回键结束当前Activity
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
