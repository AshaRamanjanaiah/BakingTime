package com.example.android.bakingtime;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingtime.model.Recipes;
import com.example.android.bakingtime.utils.JSONUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainRecipeFragment extends Fragment {

    private static int RECIPE_LIST_ITEMS = 4;
    private RecipeAdapter recipeAdapter;

    @BindView(R.id.rv_recipes)
    RecyclerView mRecipeList;

    private Unbinder unbinder;

    public MainRecipeFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_cards, container, false);

        unbinder = ButterKnife.bind(this, rootView);

        ArrayList<Recipes> recipes = JSONUtils.getRecipes(getContext());
        RECIPE_LIST_ITEMS = recipes.size();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecipeList.setLayoutManager(layoutManager);

        mRecipeList.setHasFixedSize(true);

        recipeAdapter = new RecipeAdapter(recipes);

        mRecipeList.setAdapter(recipeAdapter);

        return rootView;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
