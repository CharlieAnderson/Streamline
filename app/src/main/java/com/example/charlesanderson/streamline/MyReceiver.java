package com.example.charlesanderson.streamline;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by charlesanderson on 4/16/17.
 */

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent ) {
        Toast.makeText(context, "Resetting all timers now", Toast.LENGTH_LONG).show();
        context.sendBroadcast(new Intent("RESET_TIMERS"));

    }
}