package com.example.android.bakingtime;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import com.example.android.bakingtime.model.Ingredient;
import com.example.android.bakingtime.model.Recipes;
import com.example.android.bakingtime.model.Step;
import com.example.android.bakingtime.utils.Constants;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;

public class RecipeDetailActivityIntentTest {

     @Rule
     public IntentsTestRule<RecipeDetailActivity> mIntent =
            new IntentsTestRule<RecipeDetailActivity>(RecipeDetailActivity.class){
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

    @Before
    public void stubAllExternalIntents() {
        // By default Espresso Intents does not stub any Intents. Stubbing needs to be setup before
        // every test run. In this case all external Intents will be blocked.
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }

    @Test
    public void clickListItemOpensNewActivity() {

        onView(withId(R.id.tv_ingredients))
                .perform(click());

        intended(hasComponent(RecipeStepDetailActivity.class.getName()));
    }

}
