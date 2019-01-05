package com.example.android.bakingtime;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingtime.model.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailAdapter extends RecyclerView.Adapter<RecipeDetailAdapter.RecipieDetailViewholder> {

    private List<Step> steps = new ArrayList<>();

    public RecipeDetailAdapter(List<Step> steps){
        this.steps = steps;
    }


    @NonNull
    @Override
    public RecipieDetailViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.recipe_detail_item, parent, false);

        RecipieDetailViewholder recipieDetailViewholder = new RecipieDetailViewholder(view);

        return recipieDetailViewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipieDetailViewholder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    class RecipieDetailViewholder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_steps)
        TextView mRecipeStepsTextview;

        public RecipieDetailViewholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(int lastIndex){
            mRecipeStepsTextview.setText(steps.get(lastIndex).getShortDescription());
        }


    }
}
