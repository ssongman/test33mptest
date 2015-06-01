package sample.ssongman.test33mptest;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by Administrator on 2015-03-22.
 */

@SuppressWarnings("ResourceType")
public class MPTest extends Activity {
    MediaPlayer mPlayer;
    String mSdPath;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mptest);

        mPlayer = MediaPlayer.create(this, R.raw.dingdong);
        mSdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    public void mOnClick(View v) {
        MediaPlayer player;
        switch (v.getId()) {
            // 리소스 재생
            case R.id.btn1:
                player = MediaPlayer.create(this, R.raw.dingdong);
                player.start();
                break;
            // 파일 재생
            case R.id.btn2:
                player = new MediaPlayer();
                try {
                    player.setDataSource(mSdPath + "//음악/010-로이킴-Love Love Love.mp3");
                    player.prepare();
                    player.start();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "error : " + e.getMessage(), 0).show();
                }
                break;
            // 스트림 재생
            case R.id.btn3:
                player = new MediaPlayer();
                try {
                    Uri uri = Uri.parse("http://www.soen.kr/data/saemaul1.mp3");
                    player.setDataSource(this, uri);
                    player.prepare();
                    player.start();
                } catch (Exception e) {
                    Toast.makeText(this, "error : " + e.getMessage(), 0).show();
                }
                break;
            // 미리 준비된 객체로 재생
            case R.id.btn4:
                mPlayer.seekTo(0);
                mPlayer.start();
                break;
            // 준비하지 않은 상태로 재생
            case R.id.btn5:
                player = new MediaPlayer();
                try {
                    player.setDataSource(mSdPath + "/음악/010-로이킴-Love Love Love.mp3");
                    player.start();
                } catch (Exception e) {
                    Toast.makeText(this, "error : " + e.getMessage(), 0).show();
                }
                break;
            // 다른 파일 열기
            case R.id.btn6:
                player = MediaPlayer.create(this, R.raw.dingdong);
                //player.reset();
                try {
                    player.setDataSource(mSdPath + "/음악/010-로이킴-Love Love Love.mp3");
                    player.prepare();
                    player.start();
                } catch (IllegalArgumentException e) {
                    Toast.makeText(this, "IllegalArgumentException", 0).show();
                } catch (IllegalStateException e) {
                    Toast.makeText(this, "IllegalStateException", 0).show();
                } catch (IOException e) {
                    Toast.makeText(this, "IOException", 0).show();
                }
                break;
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }
}
