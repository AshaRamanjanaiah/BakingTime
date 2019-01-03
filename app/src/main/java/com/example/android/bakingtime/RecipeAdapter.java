package com.example.android.bakingtime;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingtime.model.Ingredient;
import com.example.android.bakingtime.model.Recipes;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private ArrayList<Recipes> mRecipeItems;

    public RecipeAdapter(ArrayList<Recipes> recipes){
        mRecipeItems = recipes;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.recipe_list_item, parent, false);

        RecipeViewHolder recipeViewHolder = new RecipeViewHolder(view);

        return recipeViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mRecipeItems.size();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_item_recipe)
        TextView mRecipeTextView;


        public RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(int listIndex){
            mRecipeTextView.setText(mRecipeItems.get(listIndex).getName());

            /*List<Ingredient> ingredients = mRecipeItems[listIndex].getIngredients();

            String ingredients_list = "";

            for (Ingredient listItem : ingredients) {
                System.out.println(listItem);
                ingredients_list = ingredients_list + "," + listItem.getIngredient();
            }


            mIngredientsListTextview.setText(ingredients_list);*/
        }
    }

}
