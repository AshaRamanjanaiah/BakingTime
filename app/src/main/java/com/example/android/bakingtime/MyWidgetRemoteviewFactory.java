package com.example.android.bakingtime;

import android.content.Context;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingtime.model.Recipes;
import com.example.android.bakingtime.utils.Constants;

import java.util.ArrayList;

public class MyWidgetRemoteviewFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;

    private ArrayList<Recipes> recipes = new ArrayList<>();

    public MyWidgetRemoteviewFactory(Context applicationContext, Intent intent){
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        recipes = RecipeWidgetProvider.recipes;
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if(recipes != null && !recipes.isEmpty()){
            return recipes.get(0).getIngredients().size();
        }
        return 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position == AdapterView.INVALID_POSITION) {
            return null;
        }

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.collection_widget_list_item);
        if(recipes != null && !recipes.isEmpty()) {
            rv.setTextViewText(R.id.tv_widgetRecipeItemName, recipes.get(0).getIngredients().get(position).getIngredient());
        }

        Intent fillInIntent = new Intent();
        fillInIntent.putExtra(Constants.RECIPE, recipes.get(0));
        rv.setOnClickFillInIntent(R.id.widgetItemContainer, fillInIntent);

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
