package com.example.android.bakingtime;

import android.content.Context;
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

import com.example.android.bakingtime.model.Ingredient;
import com.example.android.bakingtime.model.Recipes;
import com.example.android.bakingtime.model.Step;
import com.example.android.bakingtime.utils.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RecipeDetailFragment extends Fragment {

    private Recipes recipe;

    private Unbinder unbinder;

    @BindView(R.id.tv_ingredients)
    TextView mRecipeIngredients;

    @BindView(R.id.rv_recipe_details)
    RecyclerView mRecipeDetails;

    OnStepsClickListener mCallback;

    public interface OnStepsClickListener{
        void onStepsSelected(Step step, String dataType, int position);
        void onIngredientsSelected(List<Ingredient> ingredient, String dataType);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnStepsClickListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException((context.toString()) + "must implement OnStepsClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(savedInstanceState != null && savedInstanceState.containsKey(Constants.RECIPE)){
            recipe = savedInstanceState.getParcelable(Constants.RECIPE);
        }else {
            if(getArguments().containsKey(Constants.RECIPE)) {
                recipe = getArguments().getParcelable(Constants.RECIPE);
            }
        }

        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        unbinder = ButterKnife.bind(this, rootView);


        mRecipeIngredients.setText(getResources().getString(R.string.ingredients));
        mRecipeIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onIngredientsSelected(recipe.getIngredients(), Constants.INGREDIENTS_FOR_COOKING);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecipeDetails.setLayoutManager(layoutManager);

        mRecipeDetails.setHasFixedSize(true);

        if(recipe != null) {

            RecipeDetailAdapter recipeDetailAdapter = new RecipeDetailAdapter(recipe.getSteps(), (RecipeDetailActivity)getActivity());

            mRecipeDetails.setAdapter(recipeDetailAdapter);
            mRecipeDetails.setNestedScrollingEnabled(false);
        }

        return rootView;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(Constants.RECIPE, recipe);
        super.onSaveInstanceState(outState);
    }
}
