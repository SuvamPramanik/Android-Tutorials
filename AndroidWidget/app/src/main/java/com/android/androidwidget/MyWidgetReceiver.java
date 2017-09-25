package com.android.androidwidget;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Created by Suvam on 9/25/2017.
 */


public class MyWidgetReceiver extends BroadcastReceiver {

    public static int count = 1;  //initialize it to 1 as the default image is R.drawable.image1
    //array to hold the images
    private int[] images = {R.drawable.image1, R.drawable.image2, R.drawable.image3, R.drawable.image4, R.drawable.image5};

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(WidgetConsts.WIDGET_UPDATE_ACTION)) {
            updateWidgetContent(context, intent);
        }
    }

    private void updateWidgetContent(Context context, Intent intent) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        //updating remote view
        remoteViews.setImageViewBitmap(R.id.imageView, getImageBitmap(context));

        // re-registering for click listener
        remoteViews.setOnClickPendingIntent(R.id.sync_button,
                MyWidgetProvider.createPendingIntent(context));

        MyWidgetProvider.pushWidgetUpdate(context.getApplicationContext(), remoteViews);
    }

    private Bitmap getImageBitmap(Context context) {
        if(count >= images.length)
            count = 0;
       return BitmapFactory.decodeResource(context.getResources(), images[count]);

    }
}