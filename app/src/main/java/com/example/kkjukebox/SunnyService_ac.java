package com.example.kkjukebox;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
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

public class SunnyService_ac extends Service implements MediaPlayer.OnCompletionListener {

    int kk_pref;
    private MediaPlayer mp;

    private float volIn = 0f;
    private float volOut = 1f;
    private float speed = 0.05f;

    //Arrays for songs
    private int[] kksongs = new int[146];
    private int[] sunnySongs = {R.raw.s0_ac, R.raw.s1_ac, R.raw.s2_ac, R.raw.s3_ac, R.raw.s4_ac, R.raw.s5_ac, R.raw.s6_ac, R.raw.s7_ac, R.raw.s8_ac, R.raw.s9_ac, R.raw.s10_ac, R.raw.s11_ac, R.raw.s12_ac, R.raw.s13_ac, R.raw.s14_ac, R.raw.s15_ac, R.raw.s16_ac, R.raw.s17_ac, R.raw.s18_ac, R.raw.s19_ac, R.raw.s20_ac, R.raw.s21_ac, R.raw.s22_ac, R.raw.s23_ac};

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
        Intent intent = new Intent(this, ACActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        createNotificationChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Service")
                .setSmallIcon(R.drawable.ic_leaf)
                .setContentTitle("Chill out to them funky fresh jams!")
                .setContentText("K.K. style!")
                .setColor(ContextCompat.getColor(this, R.color.leafGreen))
                .setContentIntent(pendingIntent);

        Notification notification = builder.build();
        startForeground(1, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        kk_pref = intent.getIntExtra("kk", 2);
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        int song = findSong(hour, day); //Find the current song to play
        mp = MediaPlayer.create(this, song);
        mp.start();
        mp.setOnCompletionListener(this);
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

        if((kk_pref == 1) && (d == 7 && h >= 20)) {
            if(mp != null && mp.isLooping()) {
                mp.setLooping(false);
            }
            n = kksongs[rand.nextInt(146)];
            return n;
        }

        if(mp != null && !mp.isLooping()) {
            mp.setLooping(true);
        }

        n = sunnySongs[h];
        return n;
    }

    //Initialize K.K. songs from res/raw
    public void setUpKK() {
        for(int i=1; i<=146; i++) {
            kksongs[i-1] = this.getResources().getIdentifier("kk".concat(Integer.toString(i)), "raw", this.getPackageName());
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp1) {
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

        if(kk_pref == 2) {
            if(!mp.isLooping()) {
                mp = MediaPlayer.create(this, findSong(hour, day));
                mp.setLooping(true);
                mp.start();
            }
        }
        else if(kk_pref == 1) {
            mp = MediaPlayer.create(this, findSong(hour,day));
            if(mp.isLooping()) {
                mp.setLooping(false);
            }
            mp.start();
        }
    }
}
