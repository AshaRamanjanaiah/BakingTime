package com.example.android.bakingtime.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PrefUtils {

    public static void addToDefaultSharedPreference(Context context, String recipeName){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.RECIPE_NAME, recipeName);
        editor.apply();
    }

    public static String readfromDefaultSharedPreference(Context context){
        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(context);
        String recipeName = mSettings.getString(Constants.RECIPE_NAME, Constants.RECIPE_NAME_MISSING);
        return recipeName;
    }
}
