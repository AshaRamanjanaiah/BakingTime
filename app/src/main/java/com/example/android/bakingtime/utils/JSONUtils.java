package com.example.android.bakingtime.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.example.android.bakingtime.model.Recipes;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

public class JSONUtils {

    private static final String TAG = JSONUtils.class.getSimpleName();

    public static ArrayList<Recipes> getRecipes(Context context){

        AssetManager assetManager = context.getAssets();
        ArrayList<Recipes> recipesArrayList = new ArrayList<>();

        try {
            InputStream in = assetManager.open("baking.json");
            Gson gsonRead = new Gson();
            BufferedReader buffer=new BufferedReader(new InputStreamReader(in));

            Recipes[] recipes = gsonRead.fromJson(buffer, Recipes[].class);

            Collections.addAll(recipesArrayList, recipes);

            Log.d(TAG, "Read Json from the file");
        } catch (IOException e) {
            Log.d(TAG, "Error reading JSON file");
        }

        return recipesArrayList;
    }
}
