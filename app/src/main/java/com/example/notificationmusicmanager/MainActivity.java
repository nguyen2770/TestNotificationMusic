package com.example.notificationmusicmanager;

import static com.example.notificationmusicmanager.MyApplication.CHANNEL_ID;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

public class MainActivity extends AppCompatActivity {

    Button button;
    Button btnStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        button = findViewById(R.id.button);
        btnStop = findViewById(R.id.btnStop);

        button.setOnClickListener(view -> {
            mStartService();
        });

        btnStop.setOnClickListener((view -> {
            StopService();
        }));


    }

//    private void sendNotification() {
//
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.mygirl);
//        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setContentTitle("Title push notification")
//                .setContentText("Message push notification")
//                .setSmallIcon(R.drawable.baseline_music_note_24)
//                .setLargeIcon(bitmap)
//                .build();
//
//        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        if(manager!= null){
//            manager.notify(1,notification);
//        }
//    }

    // get image in the internet
    private void sendNotification() {
        // URL của hình ảnh
        String imageUrl = "https://thantrieu.com/resources/arts/1121429554.webp";

        // Sử dụng Glide để tải ảnh từ URL
        Glide.with(this)
                .asBitmap()
                .load(imageUrl)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        // Khi ảnh được tải thành công, tạo thông báo
                        Notification notification = new NotificationCompat.Builder(MainActivity.this, CHANNEL_ID)
                                .setContentTitle("Title push notification")
                                .setContentText("Message push notification")
                                .setSmallIcon(R.drawable.baseline_music_note_24)
                                .setLargeIcon(resource) // Đặt ảnh tải về làm large icon
                                .build();

                        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        if (manager != null) {
                            manager.notify(1, notification);
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        // Xử lý khi ảnh bị xóa hoặc không tải được
                    }
                });
    }

    private void mStartService() {
        Song song = new Song("Chạy Về Khóc Với Anh", "ERIK", "https://thantrieu.com/resources/arts/1121429554.webp", "https://thantrieu.com/resources/music/1121429554.mp3");

        System.out.println(song.getImage());
        Intent myIntent = new Intent(this, MyService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_song", song);
        myIntent.putExtras(bundle);
        startService(myIntent);
    }

    private void StopService() {
        Intent myIntent = new Intent(this, MyService.class);
        stopService(myIntent);
    }
}