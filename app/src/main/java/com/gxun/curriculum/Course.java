package com.gxun.curriculum;

public class Course {

    int id;
    String courseId; // 课程编号
    String courseName; // 课程名
    String classroom; // 教室
    String teacher; // 任课老师
    int day; // 星期几
    int startSection; // 开始节次
    int endSection; // 结束节次
    int weekType; // 单双周
    int startWeek; // 开始周次
    int endWeek; // 结束周次

    public Course() {
    }

    public Course(int id, String courseId, String courseName, String classroom, String teacher, int day, int startSection, int endSection, int weekType, int startWeek, int endWeek) {
        this.id = id;
        this.courseId = courseId;
        this.courseName = courseName;
        this.classroom = classroom;
        this.teacher = teacher;
        this.day = day;
        this.startSection = startSection;
        this.endSection = endSection;
        this.weekType = weekType;
        this.startWeek = startWeek;
        this.endWeek = endWeek;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getStartSection() {
        return startSection;
    }

    public void setStartSection(int startSection) {
        this.startSection = startSection;
    }

    public int getEndSection() {
        return endSection;
    }

    public void setEndSection(int endSection) {
        this.endSection = endSection;
    }

    public int getWeekType() {
        return weekType;
    }

    public void setWeekType(int weekType) {
        this.weekType = weekType;
    }

    public int getStartWeek() {
        return startWeek;
    }

    public void setStartWeek(int startWeek) {
        this.startWeek = startWeek;
    }

    public int getEndWeek() {
        return endWeek;
    }

    public void setEndWeek(int endWeek) {
        this.endWeek = endWeek;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", courseId='" + courseId + '\'' +
                ", courseName='" + courseName + '\'' +
                ", classroom='" + classroom + '\'' +
                ", teacher='" + teacher + '\'' +
                ", day=" + day +
                ", startSection=" + startSection +
                ", endSection=" + endSection +
                ", weekType='" + weekType + '\'' +
                ", startWeek=" + startWeek +
                ", endWeek=" + endWeek +
                '}';
    }
    public String showCourseInfo(){
        String type = "全部";
        String [] weekday = {"一","二","三","四","五","六","日"};
        if (weekType == 0){
            type = "全部";
        }else if (weekType == 1){
            type = "单周";
        } else {
            type = "双周";
        }
        return "课程编号：" + courseId + '\n' +
               "课程名称：" + courseName + '\n' +
                "教        室：" + classroom + '\n' +
                "老        师：" + teacher + '\n' +
                "星        期：周" + weekday[day-1] +  '\n' +
                "起止节次：" + startSection + "~" + endSection + '\n' +
                "单  双  周：" + type + '\n' +
                "起止周数：" + startWeek + "~" + endWeek;
    }
}
