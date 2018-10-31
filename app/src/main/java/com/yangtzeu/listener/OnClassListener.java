package com.yangtzeu.listener;

import com.yangtzeu.entity.Course;

import java.util.List;

public interface OnClassListener {
    void onClass(List<Course> course);
}
