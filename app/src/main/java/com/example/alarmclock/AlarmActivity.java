package com.example.alarmclock;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AlarmActivity extends AppCompatActivity {
    Ringtone    ringtone; //AGIGA
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_alarm);

        Uri notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ringtone = RingtoneManager.getRingtone(this, notificationUri);
        if(ringtone== null){
            notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            ringtone = RingtoneManager.getRingtone(this, notificationUri);
        }
        if(ringtone != null){
            ringtone.play();
        }
    }

    private void StopAlarmClock(View v){
        ringtone.stop();
    }


    @Override
    protected void onDestroy() {

        if(ringtone != null && ringtone.isPlaying()){
            ringtone.stop();
        }
        super.onDestroy();

    }
}
