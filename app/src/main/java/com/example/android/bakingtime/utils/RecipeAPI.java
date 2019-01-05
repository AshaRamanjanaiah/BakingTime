package com.example.android.bakingtime.utils;

import com.example.android.bakingtime.model.Recipes;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipeAPI {
    @GET("/topher/2017/May/59121517_baking/baking.json")
    Call<Recipes[]> getRecipies();
}
