package cn.teachcourse;

import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

public class MediaPlayerActivity extends AppCompatActivity implements View.OnClickListener,MediaPlayer.OnCompletionListener{
    private MediaPlayer mediaPlayer;
    private SurfaceView surfaceView;
    private Button pause;
    private Button play;
    private Button stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);
        mediaPlayer = new MediaPlayer();
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView1);

        pause = (Button) findViewById(R.id.button1);
        play = (Button) findViewById(R.id.button2);
        stop = (Button) findViewById(R.id.button3);

        play.setOnClickListener(this);
        stop.setOnClickListener(this);
        pause.setOnClickListener(this);
        mediaPlayer.setOnCompletionListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button2:

                playVideo();
                break;

            case R.id.button3:

                stopVideo();
                break;

            case R.id.button1:

                pauseVideo();
                break;
        }
    }


    private void pauseVideo() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            pause.setText("继续");
        } else {
            mediaPlayer.start();
            pause.setText("暂停");
        }
    }

    private void stopVideo() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            surfaceView.setBackgroundResource(R.drawable.bg_finish);
            pause.setEnabled(false);
        }
    }

    private void playVideo() {
        mediaPlayer.reset();
        try {
            //设置音频流类型
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            // 设置播放的视频源
            AssetFileDescriptor fd = getAssets().openFd("mv_song.mp4");
            mediaPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
            mediaPlayer.setDisplay(surfaceView.getHolder());// 设置将视频画面输出到SurfaceView
            mediaPlayer.prepare();// 预加载需要播放的视频
            mediaPlayer.start();
            // 改变SurfaceView背景的图片
//                    surfaceView.setBackgroundResource(R.drawable.bg_playing);
            surfaceView.setBackgroundColor(0);
            pause.setText("暂停");
            pause.setEnabled(true);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer.release();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        surfaceView.setBackgroundResource(R.drawable.bg_finish);
        Toast.makeText(MediaPlayerActivity.this, "视频播放完毕！", Toast.LENGTH_SHORT)
                .show();
    }
}
