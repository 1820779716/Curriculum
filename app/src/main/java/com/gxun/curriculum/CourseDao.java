package com.gxun.curriculum;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CourseDao extends SQLiteOpenHelper {

    private static final String db_name = "curriculum.db"; // 数据库文件名
    private static final String table_name = "t_course"; // 表名
    private static int NUMBER = 1; // 当前数据库版本，用于升级

    public CourseDao(@Nullable Context context) {
        // 上下文，数据库文件名，null，版本号
        super(context, db_name, null, NUMBER);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // 创建表
        String sql = "CREATE TABLE t_course (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "courseId VARCHAR(50) NOT NULL , ---课程编号\n" +
                "courseName VARCHAR(50) NOT NULL , ---课程名\n" +
                "classroom VARCHAR(50) NOT NULL , ---教室\n" +
                "teacher VARCHAR(16) NOT NULL , ---任课老师\n" +
                "day INTEGER NOT NULL, ---星期几\n" +
                "startSection INTEGER NOT NULL, ---开始节次\n" +
                "endSection INTEGER NOT NULL, ---结束节次\n" +
                "weekType INTEGER NOT NULL, ---单双周\n" +
                "startWeek INTEGER NOT NULL ,-----开始周次\n" +
                "endWeek INTEGER NOT NULL -----结束周次\n" +
                ");";
        sqLiteDatabase.execSQL(sql);

        // 初始化
        sql = "INSERT INTO t_course\n" +
                "VALUES(NULL,'0001','计算机组成原理','校友楼401','周卫',1,3,4,0,1,17),\n" +
                "(NULL,'0001','计算机组成原理','逸夫楼605','周卫',1,7,8,0,1,17),\n" +
                "(NULL,'0001','计算机组成原理','逸夫楼605','周卫',3,5,6,0,1,17),\n" +
                "(NULL,'0001','计算机组成原理','逸夫楼605','周卫',3,7,8,0,1,17),\n" +
                "(NULL,'0002','移动软件开发','逸夫楼604','周卫',1,5,6,0,1,17),\n" +
                "(NULL,'0002','移动软件开发','逸夫楼604','周卫',3,1,2,0,1,17),\n" +
                "(NULL,'0003','专业英语','学友楼104','刘美玲',2,1,2,0,1,15),\n" +
                "(NULL,'0003','专业英语','学友楼104','刘美玲',3,3,4,0,1,15),\n" +
                "(NULL,'0004','程序设计进阶（C#）','逸夫楼604','谢宁新',2,3,4,0,1,16),\n" +
                "(NULL,'0004','程序设计进阶（C#）','逸夫楼604','谢宁新',5,5,6,0,1,16),\n" +
                "(NULL,'0005','UNIX/LINUX','文综楼406','靳庆庚',2,9,10,0,1,12),\n" +
                "(NULL,'0005','UNIX/LINUX','文综楼406','靳庆庚',4,9,10,0,1,12),\n" +
                "(NULL,'0006','软件设计模式','逸夫楼604','张纲强',4,1,4,0,1,16),\n" +
                "(NULL,'0007','计算机操作系统','学友楼504','文勇',5,1,2,0,1,17),\n" +
                "(NULL,'0007','计算机操作系统','逸夫楼604','文勇',5,3,4,0,1,17),\n" +
                "(NULL,'0008','大学生就业指导','学友楼402','潘彦燕',1,9,10,0,13,17),\n" +
                "(NULL,'0008','大学生就业指导','学友楼402','潘彦燕',1,9,10,0,13,17);\n";
        sqLiteDatabase.execSQL(sql);
    }

    public long insertCourse(ContentValues cv) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.insert(table_name, null, cv);
    }

    public int deleteCourse(int id) { // 删除课程信息
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        /**
         * 第一个参数：表名
         * 第二个参数：删除的where后的语句
         * 第三个参数：? 所代表的值
         * 返回删除的数量
         */
        int count = sqLiteDatabase.delete(table_name, "id=?", new String[]{String.valueOf(id)});
        sqLiteDatabase.close();
        return count;
    }

    public List<Course> selectCourse(String endWeek){
        List<Course> courseList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(table_name, null, "endWeek>=?", new String[]{endWeek}, null, null, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Course course = new Course();
                course.setId(cursor.getInt(0));
                course.setCourseId(cursor.getString(1));
                course.setCourseName(cursor.getString(2));
                course.setClassroom(cursor.getString(3));
                course.setTeacher(cursor.getString(4));
                course.setDay(cursor.getInt(5));
                course.setStartSection(cursor.getInt(6));
                course.setEndSection(cursor.getInt(7));
                course.setWeekType(cursor.getInt(8));
                course.setStartWeek(cursor.getInt(9));
                course.setEndWeek(cursor.getInt(10));
                courseList.add(course);
            }
        }
        sqLiteDatabase.close();
        return courseList;
    }

    public Course selectCourseById(int id){
        Course course = new Course();
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(table_name, null, "id=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                course.setId(cursor.getInt(0));
                course.setCourseId(cursor.getString(1));
                course.setCourseName(cursor.getString(2));
                course.setClassroom(cursor.getString(3));
                course.setTeacher(cursor.getString(4));
                course.setDay(cursor.getInt(5));
                course.setStartSection(cursor.getInt(6));
                course.setEndSection(cursor.getInt(7));
                course.setWeekType(cursor.getInt(8));
                course.setStartWeek(cursor.getInt(9));
                course.setEndWeek(cursor.getInt(10));

            }
        }
        sqLiteDatabase.close();
        return course;
    }

    public List<Course> listAllCourse() { // 查询所有课程信息
        List<Course> courseList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(table_name, null, null, null, null, null, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Course course = new Course();
                course.setId(cursor.getInt(0));
                course.setCourseId(cursor.getString(1));
                course.setCourseName(cursor.getString(2));
                course.setClassroom(cursor.getString(3));
                course.setTeacher(cursor.getString(4));
                course.setDay(cursor.getInt(5));
                course.setStartSection(cursor.getInt(6));
                course.setEndSection(cursor.getInt(7));
                course.setWeekType(cursor.getInt(8));
                course.setStartWeek(cursor.getInt(9));
                course.setEndWeek(cursor.getInt(10));
                courseList.add(course);
            }
        }
        sqLiteDatabase.close();
        return courseList;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
