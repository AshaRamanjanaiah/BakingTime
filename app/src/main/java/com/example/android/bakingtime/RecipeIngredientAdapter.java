package com.example.android.bakingtime;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingtime.model.Ingredient;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeIngredientAdapter extends RecyclerView.Adapter<RecipeIngredientAdapter.RecipeIngredientViewHolder> {

    private List<Ingredient> ingredients = new ArrayList<>();

    public RecipeIngredientAdapter(List<Ingredient> ingredients){
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public RecipeIngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.recipe_ingredients_item, parent, false);

        RecipeIngredientViewHolder recipeIngredientViewHolder = new RecipeIngredientViewHolder(view);

        return recipeIngredientViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeIngredientViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public class RecipeIngredientViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_quantity)
        TextView mQuantityTextview;

        @BindView(R.id.tv_measure)
        TextView mMeasureTextview;

        @BindView(R.id.tv_ingredient)
        TextView mIngredientTextview;

        public RecipeIngredientViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(int lastIndex){
            mQuantityTextview.setText(ingredients.get(lastIndex).getQuantity().toString());
            mMeasureTextview.setText(ingredients.get(lastIndex).getMeasure());
            mIngredientTextview.setText(" "+ingredients.get(lastIndex).getIngredient());
        }
    }
}
