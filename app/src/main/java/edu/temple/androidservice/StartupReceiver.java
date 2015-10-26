package edu.temple.androidservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartupReceiver extends BroadcastReceiver {
    public StartupReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent startupIntent = new Intent(context, MainActivity.class);
        startupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(startupIntent);
    }
}
