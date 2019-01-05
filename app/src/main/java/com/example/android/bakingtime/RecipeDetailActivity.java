package com.example.android.bakingtime;

import android.content.Intent;
import android.provider.SyncStateContract;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.android.bakingtime.model.Recipes;
import com.example.android.bakingtime.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailActivity extends AppCompatActivity {

    @BindView(R.id.detail_act_toolbar)
    Toolbar mDetailActivityToolbar;

    Recipes recipe = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if(intent.hasExtra(Constants.RECIPE)){
            recipe = intent.getParcelableExtra(Constants.RECIPE);
        }

        setToolbar();

        if(savedInstanceState == null) {

            Bundle bundle = new Bundle();
            bundle.putParcelable(Constants.RECIPE, recipe);

            RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
            recipeDetailFragment.setArguments(bundle);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.recipe_detail_container, recipeDetailFragment)
                    .commit();
        }
    }

    public void setToolbar(){
        setSupportActionBar(mDetailActivityToolbar);
        RecipeDetailActivity.this.setTitle(recipe.getName());

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }
}
