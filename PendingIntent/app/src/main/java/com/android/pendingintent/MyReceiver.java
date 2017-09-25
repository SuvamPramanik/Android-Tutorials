package com.android.pendingintent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Suvam on 9/25/2017.
 */


    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "Alarm triggered!!!", Toast.LENGTH_LONG).show();
        }

    }
