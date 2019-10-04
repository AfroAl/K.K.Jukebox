package com.example.kkjukebox;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import java.util.Calendar;
import java.util.Random;

public class SnowyService_acnl extends Service {

    private MediaPlayer mp;

    private float volIn = 0f;
    private float volOut = 1f;
    private float speed = 0.05f;

    //Arrays for songs
    private int[] kksongs = new int[70];
    private int[] snowySongs = {R.raw.h0_acnl, R.raw.h1_acnl, R.raw.h2_acnl, R.raw.h3_acnl, R.raw.h4_acnl, R.raw.h5_acnl, R.raw.h6_acnl, R.raw.h7_acnl, R.raw.h8_acnl, R.raw.h9_acnl, R.raw.h10_acnl, R.raw.h11_acnl, R.raw.h12_acnl, R.raw.h13_acnl, R.raw.h14_acnl, R.raw.h15_acnl, R.raw.h16_acnl, R.raw.h17_acnl, R.raw.h18_acnl, R.raw.h19_acnl, R.raw.h20_acnl, R.raw.h21_acnl, R.raw.h22_acnl, R.raw.h23_acnl};

    //Found out current song
    public void fadeOut(float deltaT) {
        while(volOut > 0f) {
            mp.setVolume(volOut, volOut);
            volOut -= speed* deltaT;
        }

        mp.setVolume(volOut, volOut);
        volOut = 1f;
    }

    //Fade in current song
    public void fadeIn(float deltaT) {
        while(volIn < 1f) {
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

    //Create Channel for API >=26
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel";
            String description = "To display Notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("Service", name, importance);
            channel.setDescription(description);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{ 0 });
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override //Called before onStartCommand
    public void onCreate() {
        setUpKK(); //Initialize K.K. songs

        createNotificationChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Service")
                .setSmallIcon(R.drawable.ic_leaf)
                .setContentTitle("Chill out to them funky fresh jams!")
                .setContentText("K.K. style!")
                .setColor(ContextCompat.getColor(this, R.color.leafGreen));

        Notification notification = builder.build();
        startForeground(3, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        int song = findSong(hour, day); //Find the current song to play
        mp = MediaPlayer.create(this, song);
        mp.setLooping(true);
        mp.start();
        fadeIn(0.05f);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        fadeOut(0.05f);
        mp.stop();
        mp.release();
    }

    //Find current song given hour & day
    public int findSong(int h, int d) {
        int n;
        Random rand = new Random();

        if(d == 7 && h >= 20) {
            n = kksongs[rand.nextInt(70)];
            return n;
        }

        n = snowySongs[h];
        return n;
    }

    //Initialize K.K. songs from res/raw
    public void setUpKK() {
        for(int i=1; i<=70; i++) {
            kksongs[i-1] = this.getResources().getIdentifier("kk".concat(Integer.toString(i)).concat("_acnl"), "raw", this.getPackageName());
        }
    }
}