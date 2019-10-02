package com.example.kkjukebox;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class playBR extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.sendBroadcast(new Intent("PLAY_MUSIC"));
    }
}
