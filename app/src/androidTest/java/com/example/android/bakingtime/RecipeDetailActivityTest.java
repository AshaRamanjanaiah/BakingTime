package com.example.android.bakingtime;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.bakingtime.model.Ingredient;
import com.example.android.bakingtime.model.Recipes;
import com.example.android.bakingtime.model.Step;
import com.example.android.bakingtime.utils.Constants;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class RecipeDetailActivityTest  {

    @Rule
    public ActivityTestRule<RecipeDetailActivity> mActivityRule =
            new ActivityTestRule<RecipeDetailActivity>(RecipeDetailActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    ArrayList<Recipes> recipes = new ArrayList<>();
                    ArrayList<Ingredient> ingredients = new ArrayList<>();
                    ingredients.add(new Ingredient((double) 2, "CUP", "Graham Cracker crumbs"));

                    ArrayList<Step> steps = new ArrayList<>();
                    steps.add(new Step(0, "Recipe Introduction", "Recipe Introduction"
                            , "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4", ""));
                    recipes.add(new Recipes(1, "Nutella Pie", ingredients, steps, 8, ""));
                    Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

                    Intent result = new Intent(targetContext, RecipeDetailActivity.class);
                    result.putExtra(Constants.RECIPE, recipes.get(0));
                    return result;
                }
            };

    @Test
    public void clickListItemOpensNewActivity() {

        onView(withId(R.id.tv_ingredients))
                .perform(click());

        onView(withId(R.id.tv_measure)).check(matches(withText("CUP")));
    }

}
