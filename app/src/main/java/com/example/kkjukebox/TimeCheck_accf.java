package com.example.kkjukebox;

import android.content.Context;
import android.content.Intent;
import java.util.Calendar;
import java.util.TimerTask;

public class TimeCheck_accf extends TimerTask {

    private Context c;
    private int button;
    private int kk_pref;
    private int oldTime = -1;

    TimeCheck_accf(Context ct, int n, int kk) {
        c = ct; //Get context of current activity
        button = n;
        kk_pref = kk;
    }

    public void run() { //Called by timer every 0.5s
        int newTime = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        if(oldTime != newTime)
        {
            //Stop all current services
            c.stopService(new Intent(c, SunnyService_accf.class));
            c.stopService(new Intent(c, RainyService_accf.class));
            c.stopService(new Intent(c, SnowyService_accf.class));
            c.stopService(new Intent(c, kkAlwaysService.class));

            if(kk_pref == 0) {
                c.stopService(new Intent(c, kkAlwaysService.class));
                c.startForegroundService(new Intent(c, kkAlwaysService.class));
            }
            else {
                if(button == 0) { //Start sunny music service
                    c.stopService(new Intent(c, SunnyService_accf.class));
                    Intent i = new Intent(c, SunnyService_accf.class);
                    i.putExtra("kk", kk_pref);
                    c.startForegroundService(i);
                }
                else if(button == 1) { //Start rainy music service
                    c.stopService(new Intent(c, RainyService_accf.class));
                    Intent i = new Intent(c, RainyService_accf.class);
                    i.putExtra("kk", kk_pref);
                    c.startForegroundService(i);
                }
                else if(button == 2) { //Start snowy music service
                    c.stopService(new Intent(c, SnowyService_accf.class));
                    Intent i = new Intent(c, SnowyService_accf.class);
                    i.putExtra("kk", kk_pref);
                    c.startForegroundService(i);
                }
            }

            oldTime = newTime;
        }
    }
}
