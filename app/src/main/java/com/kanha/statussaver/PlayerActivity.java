package com.kanha.statussaver;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.StyledPlayerView;

public class PlayerActivity extends AppCompatActivity {

    private static final String TAG = "PlayerActivity";

    StyledPlayerView playerView;
    ExoPlayer exoPlayer;

    Uri uri;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        uri = Uri.parse(getIntent().getStringExtra("path"));

        playerView = findViewById(R.id.playerView);

        exoPlayer = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(exoPlayer);

        MediaItem mediaItem = MediaItem.fromUri(uri);
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.prepare();

        // exoplayer listener

        exoPlayer.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                Player.Listener.super.onPlaybackStateChanged(playbackState);
                switch (playbackState) {
                    case Player.STATE_BUFFERING:
                        Log.d(TAG, "onPlaybackStateChanged: buffering");
                        break;
                    case Player.STATE_ENDED:
                        Log.d(TAG, "onPlaybackStateChanged: ended");
                        finish();
                        break;
                    case Player.STATE_IDLE:
                        Log.d(TAG, "onPlaybackStateChanged: idle");
                        break;
                    case Player.STATE_READY:
                        Log.d(TAG, "onPlaybackStateChanged: ready");
                        exoPlayer.play();
                        playerView.setKeepScreenOn(true);
                        playerView.hideController();
                        break;
                }
            }
        });
    }

    // stop exoplayer

    @Override
    protected void onStop() {
        super.onStop();
        exoPlayer.stop();
    }

    // release exoplayer

    @Override
    protected void onDestroy() {
        super.onDestroy();
        exoPlayer.release();
    }

    // pause exoplayer

    @Override
    protected void onPause() {
        super.onPause();
        exoPlayer.pause();
    }

    // resume exoplayer

    @Override
    protected void onResume() {
        super.onResume();
        exoPlayer.play();
    }

    // restart exoplayer

    @Override
    protected void onRestart() {
        super.onRestart();
        exoPlayer.play();
    }

    // start exoplayer

    @Override
    protected void onStart() {
        super.onStart();
        exoPlayer.play();
    }
}