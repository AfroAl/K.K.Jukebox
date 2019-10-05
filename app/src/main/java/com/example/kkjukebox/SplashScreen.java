package com.example.kkjukebox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //Get saved preferences
        SharedPreferences sp = getSharedPreferences("com.example.kkjukebox_preferences", 0);
        String weather_prefs = sp.getString("weather_prefs", "sun");
        String game_prefs = sp.getString("game_prefs", "ac");
        String kk_prefs = sp.getString("kk_prefs", "never");

        int weather = -1;
        assert weather_prefs != null;
        switch (weather_prefs) {
            case "sun":
                weather = 0;
                break;
            case "rain":
                weather = 1;
                break;
            case "hail":
                weather = 2;
                break;
        }

        int kk = -1;
        assert kk_prefs != null;
        switch (kk_prefs) {
            case "always":
                kk = 0;
                break;
            case "saturaday":
                kk = 1;
                break;
            case "never":
                kk = 2;
                break;
        }

        assert game_prefs != null;
        switch (game_prefs) {
            case "ac": {
                Intent i = new Intent(this, ACActivity.class);
                i.putExtra("weather", weather);
                i.putExtra("kk", kk);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
                break;
            }
            case "accf": {
                Intent i = new Intent(this, ACCFActivity.class);
                i.putExtra("weather", weather);
                i.putExtra("kk", kk);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
                break;
            }
            case "acnl": {
                Intent i = new Intent(this, ACNLActivity.class);
                i.putExtra("weather", weather);
                i.putExtra("kk", kk);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
                break;
            }
        }
    }
}
