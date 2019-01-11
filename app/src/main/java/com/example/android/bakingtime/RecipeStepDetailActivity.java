package com.example.android.bakingtime;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.android.bakingtime.model.Ingredient;
import com.example.android.bakingtime.model.Step;
import com.example.android.bakingtime.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepDetailActivity extends AppCompatActivity {

    @BindView(R.id.detail_step_toolbar)
    Toolbar mDetailStepActivityToolbar;

    private String mRecipeName;

    private Bundle bundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);

        ButterKnife.bind(this);

        Intent intent = getIntent();

        extractDataFromIntent(intent);

        setToolbar();

        if(savedInstanceState == null) {

            RecipeStepDetailFragment recipeStepDetailFragment = new RecipeStepDetailFragment();
            recipeStepDetailFragment.setArguments(bundle);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.recipe_step_container, recipeStepDetailFragment)
                    .commit();
        }
    }

    private void setToolbar(){
        setSupportActionBar(mDetailStepActivityToolbar);
        RecipeStepDetailActivity.this.setTitle(mRecipeName);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    private void extractDataFromIntent(Intent intent){
        if(intent.hasExtra(Constants.STEP_FOR_COOKING)){

          Step step = intent.getParcelableExtra(Constants.STEP_FOR_COOKING);
            if(intent.hasExtra(Constants.RECIPE)){
                mRecipeName = intent.getExtras().getString(Constants.RECIPE);
            }
            bundle.putParcelable(Constants.STEP_FOR_COOKING, step);

        }else if(intent.hasExtra(Constants.INGREDIENTS_FOR_COOKING)){

            List<Ingredient> ingredients = intent.getParcelableArrayListExtra(Constants.INGREDIENTS_FOR_COOKING);
            if(intent.hasExtra(Constants.RECIPE)) {
                mRecipeName = intent.getExtras().getString(Constants.RECIPE);
            }
            bundle.putParcelableArrayList(Constants.INGREDIENTS_FOR_COOKING, (ArrayList<? extends Parcelable>) ingredients);
        }
    }
}
