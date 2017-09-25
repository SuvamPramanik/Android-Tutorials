package com.android.pendingintent;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Suvam on 9/25/2017.
 */

public class SecondActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity_layout);
        Button b1 = (Button) findViewById(R.id.buttonStart);
        b1.setOnClickListener(this);
    }

    public void startAlert(View view) {
        EditText text = (EditText) findViewById(R.id.editText1);
        int time = Integer.parseInt(text.getText().toString());
        Intent intent = new Intent(this, MyReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //calls the alarm
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        //trigger after 1 second
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                + (time * 1000), pendingIntent);
    }

    @Override
    public void onClick(View view) {
        startAlert(view);
    }
}
