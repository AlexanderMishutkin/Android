package com.example.lab11_notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import static android.app.NotificationManager.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = MainActivity.this;
                NotificationChannel newnotchan= null;
                newnotchan= new
                        NotificationChannel("mychannel1","mychannel", IMPORTANCE_HIGH);
                AudioAttributes audioAttributes = new
                        AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build();

                newnotchan.setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+"://"+getPackageName()+"/raw/music.mp3"),
                        audioAttributes);
                NotificationManager notificationManager = (NotificationManager)
                        getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.createNotificationChannel(newnotchan);
                Notification notification = null;
                notification = new
                        Notification.Builder(context,"mychannel1")
                        .setContentTitle("Hi from Alex")
                        .setContentText("Hey, listen!")
                        .setTicker("new notification!")
                        .setChannelId("mychannel1")
                        .setSmallIcon(android.R.drawable.ic_dialog_alert)
                        .setOngoing(true)
                        .build();

                notificationManager.notify(0, notification);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.about) {

            AlertDialog.Builder dialog = new
                    AlertDialog.Builder(MainActivity.this);
            try {
                dialog.setMessage(getTitle().toString() + " версия " +
                        getPackageManager().getPackageInfo(getPackageName(), 0).versionName +
                        "\r\n\nПрограмма с примером создания уведомления\r\n\n" +
                        " Автор - Мишуткин Александр, гр. БПИ-192");
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            dialog.setTitle("О программе");
            dialog.setNeutralButton("OK", new
                    DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            dialog.setIcon(R.mipmap.ic_launcher_round);
            AlertDialog alertDialog = dialog.create();
            alertDialog.show();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
