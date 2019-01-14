package com.example.android.bakingtime;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import com.example.android.bakingtime.model.Recipes;
import com.example.android.bakingtime.utils.Constants;
import com.example.android.bakingtime.utils.PrefUtils;

import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    public static ArrayList<Recipes> recipes = new ArrayList<>();

    private static final String ACTION_RECIPE_UPDATE = "com.example.android.bakingtime.RECIPE_UPDATE";

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        String recipeName = PrefUtils.readfromDefaultSharedPreference(context);

        CharSequence widgetText = context.getString(R.string.label_natella_pie);

        if(!recipeName.equals("missing")){
            widgetText = recipeName;
        }

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        Intent titleIntent = new Intent(context, MainActivity.class);
        PendingIntent titlePendingIntent = PendingIntent.getActivity(context, 0, titleIntent, 0);
        views.setOnClickPendingIntent(R.id.appwidget_text, titlePendingIntent);

        Intent intentService = new Intent(context, MyWidgetRemoteViewsService.class);
        views.setRemoteAdapter(R.id.widget_listView, intentService);

        Intent clickIntentTemplate = new Intent(context, RecipeDetailActivity.class);
        PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
                .addNextIntentWithParentStack(clickIntentTemplate)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_listView, clickPendingIntentTemplate);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
       // super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        final String action = intent.getAction();
        if (intent.hasExtra(Constants.RECIPIES_LIST) && action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)){//&& action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            recipes = intent.getParcelableArrayListExtra(Constants.RECIPIES_LIST);
            int[] appWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
            // refresh all widgets
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            //int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, RecipeWidgetProvider.class));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_listView);
        }
        super.onReceive(context, intent);
    }

}

