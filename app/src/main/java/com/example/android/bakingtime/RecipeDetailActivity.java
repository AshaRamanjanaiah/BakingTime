package com.example.android.bakingtime;

import android.content.Intent;
import android.os.Parcelable;
import android.provider.SyncStateContract;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bakingtime.model.Ingredient;
import com.example.android.bakingtime.model.Recipes;
import com.example.android.bakingtime.model.Step;
import com.example.android.bakingtime.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailActivity extends AppCompatActivity implements RecipeDetailFragment.OnStepsClickListener {

    @BindView(R.id.detail_act_toolbar)
    Toolbar mDetailActivityToolbar;

    @BindView(R.id.twopane_linear_layout)
    @Nullable LinearLayout mLinearLayout;

    Recipes recipe = null;

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if(intent.hasExtra(Constants.RECIPE)){
            recipe = intent.getParcelableExtra(Constants.RECIPE);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();

        if(mLinearLayout != null){
            mTwoPane = true;
            if(savedInstanceState == null) {

                Bundle bundleStep = new Bundle();
                bundleStep.putParcelableArrayList(Constants.INGREDIENTS_FOR_COOKING, (ArrayList<? extends Parcelable>) recipe.getIngredients());

                RecipeStepDetailFragment recipeStepDetailFragment = new RecipeStepDetailFragment();
                recipeStepDetailFragment.setArguments(bundleStep);

                fragmentManager.beginTransaction()
                        .add(R.id.recipe_step_container, recipeStepDetailFragment)
                        .commit();
            }
        }else {
            mTwoPane = false;
        }

        if(savedInstanceState == null) {

            Bundle bundle = new Bundle();
            bundle.putParcelable(Constants.RECIPE, recipe);

            RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
            recipeDetailFragment.setArguments(bundle);

            fragmentManager.beginTransaction()
                    .add(R.id.recipe_detail_container, recipeDetailFragment)
                    .commit();
        }else{
            recipe = savedInstanceState.getParcelable(Constants.RECIPE);
        }

        setToolbar();
    }

    public void setToolbar(){
        setSupportActionBar(mDetailActivityToolbar);
        RecipeDetailActivity.this.setTitle(recipe.getName());

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onStepsSelected(Step step, String dataType, int position) {

        Bundle bundle = new Bundle();
        bundle.putString(Constants.DATA_TYPE, dataType);
        bundle.putString(Constants.RECIPE, recipe.getName());
        bundle.putParcelable(Constants.STEP_FOR_COOKING, step);

        if(mTwoPane){

            FragmentManager fragmentManager = getSupportFragmentManager();

            RecipeStepDetailFragment recipeStepDetailFragment = new RecipeStepDetailFragment();
            recipeStepDetailFragment.setArguments(bundle);

            fragmentManager.beginTransaction()
                    .replace(R.id.recipe_step_container, recipeStepDetailFragment)
                    .commit();

        }else {

            Intent intent = new Intent(this, RecipeStepDetailActivity.class);
            intent.putExtra(Constants.STEP_FOR_COOKING, step);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    public void onIngredientsSelected(List<Ingredient> ingredient, String dataType) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DATA_TYPE, dataType);
        bundle.putString(Constants.RECIPE, recipe.getName());
        bundle.putParcelableArrayList(Constants.INGREDIENTS_FOR_COOKING, (ArrayList<? extends Parcelable>) recipe.getIngredients());

        if(mTwoPane){
            FragmentManager fragmentManager = getSupportFragmentManager();

            RecipeStepDetailFragment recipeStepDetailFragment = new RecipeStepDetailFragment();
            recipeStepDetailFragment.setArguments(bundle);

            fragmentManager.beginTransaction()
                    .replace(R.id.recipe_step_container, recipeStepDetailFragment)
                    .commit();

        } else {
            Intent intent = new Intent(this, RecipeStepDetailActivity.class);
            intent.putParcelableArrayListExtra(Constants.INGREDIENTS_FOR_COOKING, (ArrayList<? extends Parcelable>) recipe.getIngredients());
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(Constants.RECIPE, recipe);
        super.onSaveInstanceState(outState);
    }
}
