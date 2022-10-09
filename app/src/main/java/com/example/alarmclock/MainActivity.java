package com.example.alarmclock;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    AlarmManager.AlarmClockInfo alarmClockInfo;
    private Button setAlarmBtn;
    Switch OnOffAlarmClock;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault() );
        setAlarmBtn = findViewById(R.id.SetupClockButton);
        OnOffAlarmClock = findViewById(R.id.OnOffAlarmClock);

        Calendar calendar = Calendar.getInstance();
        setAlarmBtn.setOnClickListener(v ->{
            MaterialTimePicker materialTimePicker = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(calendar.get(Calendar.HOUR_OF_DAY))
                    .setMinute(calendar.get(Calendar.MINUTE)+1)
                    .setTitleText("Выберите время для будильника")
                    .build();

            materialTimePicker.addOnPositiveButtonClickListener(view ->{
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND,0);
                calendar.set(Calendar.MINUTE, materialTimePicker.getMinute());
                calendar.set(Calendar.HOUR_OF_DAY, materialTimePicker.getHour());
                if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {   //Исправяет ошибку, при которой нельзя было ставить время меньще текущего.
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                }

                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                alarmClockInfo = new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(), getAlarmInfoPendingIntent());

                alarmManager.setAlarmClock(alarmClockInfo, getAlarmActionPendingIntent());

                //alarmManager.cancel(getCancelPendingIntent());
                Toast.makeText(this,"Будильник установлен на: " + sdf.format(calendar.getTime()), Toast.LENGTH_SHORT).show();
                OnOffAlarmClock.toggle();
                FillViewAlarms(calendar);
            });
            materialTimePicker.show(getSupportFragmentManager(), "tag_picker");
        });

        // Если не работает будильник в android 10, нужно запросить разрешение на показ окон поверх других приложений
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));

                startActivity(intent);
            }
        }

    }

    private void FillViewAlarms(Calendar calendar) {
        textView = findViewById(R.id.textView1);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM  HH:mm", Locale.getDefault() );
        textView.setText("Будильник установлен на: "+ sdf.format(calendar.getTime()));

    }

    private PendingIntent getAlarmInfoPendingIntent() {
        Intent alarmInfoIntent = new Intent(this, MainActivity.class);
        alarmInfoIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        return PendingIntent.getActivity(this,0, alarmInfoIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent getAlarmActionPendingIntent(){
        Intent intent = new Intent(this, AlarmActivity.class);
        intent.putExtra("1", alarmClockInfo);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        return PendingIntent.getActivity(this,1,intent,PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent getCancelPendingIntent(){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        return PendingIntent.getBroadcast(getApplicationContext(), 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public AlarmManager.AlarmClockInfo GetAlarmClockInfo() {
        return  alarmClockInfo;
    }
}