package com.yangtzeu.entity;


public class Course {
    private String mid = "";
    private String name= "暂无";
    private String teacher = "";
    private String teacher_id = "";
    private String room = "";
    private String room_id = "";
    private String week = "";
    private String section = "";
    private String all_week = "";
    public Course() {

    }
    public Course( String mid, String name, String teacher,
                  String teacher_id, String room, String room_id, String week,
                  String section, String all_week) {

        this.mid = mid;
        this.name = name;
        this.teacher = teacher;
        this.teacher_id = teacher_id;
        this.room = room;
        this.room_id = room_id;
        this.week = week;
        this.section = section;
        this.all_week = all_week;
    }
    public String getMid() {
        return this.mid;
    }
    public void setMid(String mid) {
        this.mid = mid;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getTeacher() {
        return this.teacher;
    }
    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }
    public String getTeacher_id() {
        return this.teacher_id;
    }
    public void setTeacher_id(String teacher_id) {
        this.teacher_id = teacher_id;
    }
    public String getRoom() {
        return this.room;
    }
    public void setRoom(String room) {
        this.room = room;
    }
    public String getRoom_id() {
        return this.room_id;
    }
    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }
    public String getWeek() {
        return this.week;
    }
    public void setWeek(String week) {
        this.week = week;
    }
    public String getSection() {
        return this.section;
    }
    public void setSection(String section) {
        this.section = section;
    }
    public String getAll_week() {
        return this.all_week;
    }
    public void setAll_week(String all_week) {
        this.all_week = all_week;
    }


}
