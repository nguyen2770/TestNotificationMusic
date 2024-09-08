package com.example.notificationmusicmanager;


import static com.example.notificationmusicmanager.MyApplication.CHANNEL_ID;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;



import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.IOException;


public class MyService extends Service {

    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        Log.e("nguyen", "My service start");
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Bundle bundle = intent.getExtras();
        Song song = (Song) bundle.get("object_song");
        if (song != null) {
            startMusic(song);
            sendNotification(song);
        }


        return START_NOT_STICKY;
    }

    private void startMusic(Song song) {
        if (mediaPlayer == null) {
            String songUrl = song.getResource(); // Thay đổi với URL của bạn

            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(songUrl);
                mediaPlayer.prepareAsync(); // Use prepareAsync for streaming from internet
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mediaPlayer.start(); // Bắt đầu phát nhạc khi sẵn sàng
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    @SuppressLint("ForegroundServiceType")
    private void sendNotification(Song song) {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_IMMUTABLE);

        // URL của hình ảnh từ đối tượng song
        String imageUrl = song.getImage();  // Giả sử song.getResource() trả về URL của ảnh

        MediaSessionCompat mediaSessionCompat = new MediaSessionCompat(this,"ta");
        // Sử dụng Glide để tải ảnh từ URL
        Glide.with(this)
                .asBitmap()
                .load(imageUrl)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        // Khi ảnh được tải thành công, tạo thông báo
                        Notification notification = new NotificationCompat.Builder(MyService.this, CHANNEL_ID)
                                .setSmallIcon(R.drawable.baseline_music_note_24)
                                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                                .setLargeIcon(resource)  // Đặt ảnh tải về làm large icon
                                // Add media control buttons that invoke intents in your media service
                                .addAction(R.drawable.baseline_skip_previous_24, "Previous", null) // #0
                                .addAction(R.drawable.baseline_pause_circle_outline_24, "Pause", null) // #1
                                .addAction(R.drawable.baseline_skip_next_24, "Next", null) // #2
                                // Apply the media style template.
                                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                                        .setShowActionsInCompactView(1)
                                        .setMediaSession(mediaSessionCompat.getSessionToken()))

                                .setContentTitle(song.getTitle())
                                .setContentText(song.getSingle())

                                .setContentIntent(pendingIntent)
                                .build();

                        // Hiển thị thông báo trong chế độ foreground
                        startForeground(1, notification);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        // Xử lý khi ảnh bị xóa hoặc không tải được
                    }
                });
    }


    @Override
    public void onDestroy() {
        Log.e("Nguyen", "My service stop");
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }
}
