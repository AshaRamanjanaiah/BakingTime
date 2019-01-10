package com.example.android.bakingtime;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingtime.model.Recipes;
import com.example.android.bakingtime.utils.Constants;
import com.example.android.bakingtime.utils.RecipeAPI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainRecipeFragment extends Fragment implements RecipeAdapter.ListItemClickListener {

    public static final String TAG = MainRecipeFragment.class.getSimpleName();

    private RecipeAdapter recipeAdapter;

    ArrayList<Recipes> recipes = new ArrayList<>();

    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";

    private RecipeAPI recipeAPI;

    @BindView(R.id.rv_recipes)
    RecyclerView mRecipeList;

    private Unbinder unbinder;

    public MainRecipeFragment(){

    }

    public ArrayList<Recipes> getRecipes() {
        return recipes;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_cards, container, false);

        unbinder = ButterKnife.bind(this, rootView);

        if(savedInstanceState != null && savedInstanceState.containsKey(Constants.RECIPIES_LIST)){
            recipes = savedInstanceState.getParcelableArrayList(Constants.RECIPIES_LIST);
        }else {
            createRecipesAPI();
        }

        if(!isTablet(getContext())) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            mRecipeList.setLayoutManager(layoutManager);
        }else{
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2 );
            mRecipeList.setLayoutManager(gridLayoutManager);
        }

        mRecipeList.setHasFixedSize(true);

        recipeAdapter = new RecipeAdapter(recipes, this);

        mRecipeList.setAdapter(recipeAdapter);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList(Constants.RECIPIES_LIST, recipes);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    @Override
    public void onListItemClick(Recipes recipe) {
        Intent intent = new Intent(getActivity(), RecipeDetailActivity.class);
        intent.putExtra(Constants.RECIPE, recipe);
        startActivity(intent);
    }

    public void createRecipesAPI(){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        recipeAPI = retrofit.create(RecipeAPI.class);

        Call<Recipes[]> call = recipeAPI.getRecipies();
        call.enqueue(new Callback<Recipes[]>() {
            @Override
            public void onResponse(Call<Recipes[]> call, Response<Recipes[]> response) {
                if(response.isSuccessful()){

                    Recipes[] recipeList = response.body();

                    Collections.addAll(recipes, recipeList);
                    mRecipeList.setAdapter(new RecipeAdapter(recipes, MainRecipeFragment.this));

                    RecipeWidgetProvider.sendRefreshBroadcast(getActivity(), recipes);

                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getActivity());
                    int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(getActivity(), RecipeWidgetProvider.class));
                    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_listView);

                    Log.d(TAG, "Successfully got Recipes data");
                }else{
                    Log.d(TAG, "Failed to get response");
                    Log.d(TAG, String.valueOf(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<Recipes[]> call, Throwable t) {
                Log.d(TAG, "Failed to get response");
            }
        });
    }
}
