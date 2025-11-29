package com.example.assigmentone;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.Type;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Storage {
    private static final String PREFS = "task_prefs";
    private static final String KEY = "tasks";

    public static void saveTasks(Context context, ArrayList<Task> tasks) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Gson gson = new Gson();
        String json = gson.toJson(tasks);

        editor.putString(KEY, json);
        editor.apply();
    }

    public static ArrayList<Task> loadTasks(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);

        String json = prefs.getString(KEY, null);

        if (json == null) {
            return new ArrayList<>();}

        Type type = new TypeToken<ArrayList<Task>>() {}.getType();
        return new Gson().fromJson(json, type);
    }
}
