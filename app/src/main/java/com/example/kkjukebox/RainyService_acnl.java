package com.example.kkjukebox;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.Calendar;
import java.util.Random;

public class RainyService_acnl extends Service {

    private MediaPlayer mp;
    private float volIn = 0f;
    private float volOut = 1f;
    private float speed = 0.05f;
    private int[] kksongs = new int[70];
    private int[] rainySongs = {R.raw.r0_acnl, R.raw.r1_acnl, R.raw.r2_acnl, R.raw.r3_acnl, R.raw.r4_acnl, R.raw.r5_acnl, R.raw.r6_acnl, R.raw.r7_acnl, R.raw.r8_acnl, R.raw.r9_acnl, R.raw.r10_acnl, R.raw.r11_acnl, R.raw.r12_acnl, R.raw.r13_acnl, R.raw.r14_acnl, R.raw.r15_acnl, R.raw.r16_acnl, R.raw.r17_acnl, R.raw.r18_acnl, R.raw.r19_acnl, R.raw.r20_acnl, R.raw.r21_acnl, R.raw.r22_acnl, R.raw.r23_acnl};

    public void fadeOut(float deltaT)
    {
        while(volOut > 0f)
        {
            mp.setVolume(volOut, volOut);
            volOut -= speed* deltaT;
        }

        mp.setVolume(volOut, volOut);
        volOut = 1f;
    }

    public void fadeIn(float deltaT)
    {
        while(volIn < 1f)
        {
            mp.setVolume(volIn, volIn);
            volIn += speed* deltaT;
        }

        mp.setVolume(volIn, volIn);
        volIn = 0f;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        setUpKK();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        int min = Calendar.getInstance().get((Calendar.MINUTE));
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int date = Calendar.getInstance().get(Calendar.DATE);
        int week = Calendar.getInstance().get(Calendar.WEEK_OF_MONTH);
        int song = findSong(hour, day, min, month, date, week);
        mp = MediaPlayer.create(this, song);
        mp.setLooping(true);
        mp.start();
        fadeIn(0.1f);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fadeOut(0.1f);
        mp.stop();
    }

    public int findSong(int h, int d, int m, int mt, int dt, int w)
    {
        int n;
        Random rand = new Random();

        if(d == 7 && h >= 20) {
            n = kksongs[rand.nextInt(70)];
            return n;
        }

        if(dt == 11 && mt == 1) {
            n = R.raw.festivale_acnl;
            return n;
        }

        if(dt == 31 && mt == 2) {
            n = R.raw.bunny_acnl;
            return n;
        }

        if(d == 1 && mt == 7 && h >= 19) {
            n = R.raw.fireworks_acnl;
            return n;
        }

        if(mt == 9 && dt == 31) {
            n = R.raw.halloween_acnl;
            return n;
        }

        if(w == 4 && mt == 10 && d == 5) {
            n = R.raw.harvest_acnl;
            return n;
        }

        if(mt == 11 && dt == 24) {
            n = R.raw.toy_acnl;
            return n;
        }

        if(mt == 11 && dt == 31 && h == 23 && m < 30) {
            n = R.raw.newyear60_acnl;
            return n;
        }

        if(mt == 11 && dt == 31 && h == 23 && m < 50) {
            n = R.raw.newyear30_acnl;
            return n;
        }

        if(mt == 11 && dt == 31 && h == 23 && m < 55) {
            n = R.raw.newyear10_acnl;
            return n;
        }

        if(mt == 11 && dt == 31 && h == 23) {
            n = R.raw.newyear5_acnl;
            return n;
        }

        if(mt == 0 && dt == 1 && h < 1) {
            n = R.raw.happynewyear_acnl;
            return n;
        }

        if(mt == 0 && dt == 1 && h < 6) {
            n = R.raw.nyd26_acnl;
            return n;
        }

        if(mt == 0 && dt == 1) {
            n = R.raw.nyd_acnl;
            return n;
        }

        n = rainySongs[h];
        return n;
    }

    public void setUpKK() {
        for(int i=1; i<=70; i++) {
            kksongs[i-1] = this.getResources().getIdentifier("kk".concat(Integer.toString(i)), "raw", this.getPackageName());
        }
    }
}
