package com.example.kkjukebox;

import android.content.Context;
import android.content.Intent;
import java.util.Calendar;
import java.util.TimerTask;

public class TimeCheck_acnl extends TimerTask {

    private Context c;
    private int button;
    private int oldTime = -1;

    TimeCheck_acnl(Context ct, int n) {
        c = ct; //Get context of current activity
        button = n;
    }

    public void run() { //Called by timer every 0.5s
        int newTime = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        if(oldTime == newTime) {
            //Do Nothing
        }
        else
        {
            //Stop all current services
            c.stopService(new Intent(c, SunnyService_acnl.class));
            c.stopService(new Intent(c, RainyService_acnl.class));
            c.stopService(new Intent(c, SnowyService_acnl.class));

            if(button == 0) { //Start sunny music service
                c.stopService(new Intent(c, SunnyService_acnl.class));
                c.startForegroundService(new Intent(c, SunnyService_acnl.class));

            }
            else if(button == 1) { //Start rainy music service
                c.stopService(new Intent(c, RainyService_acnl.class));
                c.startForegroundService(new Intent(c, RainyService_acnl.class));
            }
            else if(button == 2) { //Start snowy music service
                c.stopService(new Intent(c, SnowyService_acnl.class));
                c.startForegroundService(new Intent(c, SnowyService_acnl.class));
            }

            oldTime = newTime;
        }
    }
}
