package com.yangtzeu.database;

import android.app.Application;

import com.blankj.utilcode.util.Utils;

import java.util.HashMap;
public class DatabaseUtils {
    private static HashMap<String,MyOpenHelper> dataMaps = new HashMap<>();

    public static MyOpenHelper getHelper(String name) {
        Application context = Utils.getApp();
        if (dataMaps.keySet().contains(name)) {
            if (dataMaps.get(name) == null) {
                MyOpenHelper helper = new MyOpenHelper(context, name);
                dataMaps.put(name, helper);
                return helper;
            } else {
                return dataMaps.get(name);
            }
        } else {
            MyOpenHelper helper = new MyOpenHelper(context, name);
            dataMaps.put(name, helper);
            return helper;
        }
    }
}
