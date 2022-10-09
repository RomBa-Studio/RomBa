package com.example.alarmclock;

import android.app.AlarmManager;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AlarmActivity extends AppCompatActivity {
    private AlarmManager.AlarmClockInfo alarmClockInfo;
    private Ringtone    ringtone;
    private long currentTime;

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

    public void StopAlarmClock(View v) {
        stopRington();
        
    }

    public void HoldAlarmClock(View v) {
        Intent intent = getIntent();
        alarmClockInfo = (AlarmManager.AlarmClockInfo) intent.getParcelableExtra("1");
        currentTime = alarmClockInfo.getTriggerTime();
        Toast.makeText(this, Long.toString(currentTime), Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onDestroy() {
        stopRington();
        super.onDestroy();
    }

    private void stopRington() {
        if(ringtone != null && ringtone.isPlaying())
            ringtone.stop();
    }
}
