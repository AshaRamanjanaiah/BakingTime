package com.example.android.bakingtime;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.example.android.bakingtime.model.Recipes;
import com.example.android.bakingtime.utils.Constants;

import java.util.ArrayList;

public class MyWidgetRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        return new MyWidgetRemoteviewFactory(this.getApplicationContext(), intent);
    }
}
