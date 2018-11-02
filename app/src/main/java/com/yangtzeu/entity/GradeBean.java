package com.yangtzeu.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GradeBean implements Serializable {
    private List<GradeBean> gradeBeans= new ArrayList<>();

    private String courseYear;
    private String courseTerm;
    private String courseCode;
    private String courseIndex;
    private String courseName;
    private String courseKind;
    private String courseScore;
    private String courseBuKao;
    private String courseZongPing;
    private String courseZuiZhong;
    private String coursePoint;

    public List<GradeBean> getGradeBeans() {
        return gradeBeans;
    }

    public void setGradeBeans(List<GradeBean> gradeBeans) {
        this.gradeBeans = gradeBeans;
    }

    public String getCourseYear() {
        return courseYear;
    }

    public void setCourseYear(String courseYear) {
        this.courseYear = courseYear;
    }

    public String getCourseTerm() {
        return courseTerm;
    }

    public void setCourseTerm(String courseTerm) {
        this.courseTerm = courseTerm;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseIndex() {
        return courseIndex;
    }

    public void setCourseIndex(String courseIndex) {
        this.courseIndex = courseIndex;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseKind() {
        return courseKind;
    }

    public void setCourseKind(String courseKind) {
        this.courseKind = courseKind;
    }

    public String getCourseScore() {
        return courseScore;
    }

    public void setCourseScore(String courseScore) {
        this.courseScore = courseScore;
    }

    public String getCourseBuKao() {
        return courseBuKao;
    }

    public void setCourseBuKao(String courseBuKao) {
        this.courseBuKao = courseBuKao;
    }

    public String getCourseZongPing() {
        return courseZongPing;
    }

    public void setCourseZongPing(String courseZongPing) {
        this.courseZongPing = courseZongPing;
    }

    public String getCourseZuiZhong() {
        return courseZuiZhong;
    }

    public void setCourseZuiZhong(String courseZuiZhong) {
        this.courseZuiZhong = courseZuiZhong;
    }

    public String getCoursePoint() {
        return coursePoint;
    }

    public void setCoursePoint(String coursePoint) {
        this.coursePoint = coursePoint;
    }



}
