package com.android.androidwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Created by Suvam on 9/25/2017.
 */

public class MyWidgetProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {

        // initializing widget layout
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.widget_layout);

        // register for button event
        remoteViews.setOnClickPendingIntent(R.id.sync_button,
                createPendingIntent(context));

        // updating view with initial data
        remoteViews.setImageViewBitmap(R.id.imageView, BitmapFactory.decodeResource(context.getResources(), R.drawable.image1));

        // request for widget update
        pushWidgetUpdate(context, remoteViews);
    }

    public static PendingIntent createPendingIntent(Context context) {
        ++MyWidgetReceiver.count;

        // initiate widget update request
        Intent intent = new Intent();
        intent.setAction(WidgetConsts.WIDGET_UPDATE_ACTION);
        return PendingIntent.getBroadcast(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static void pushWidgetUpdate(Context context, RemoteViews remoteViews) {
        ComponentName myWidget = new ComponentName(context,
                MyWidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(myWidget, remoteViews);
    }
}

