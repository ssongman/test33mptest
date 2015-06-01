package sample.ssongman.test33mptest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2015-03-23.
 */
public class CameraTest extends Activity {
    MyCameraSurface mSurface;
    Button mShutter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cameratest);

        mSurface = (MyCameraSurface)findViewById(R.id.preview);

        // 오토 포커스 시작
        findViewById(R.id.focus).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                mShutter.setEnabled(false);
                mSurface.mCamera.autoFocus(mAutoFocus);
            }
        });

        // 사진 촬영
        mShutter = (Button)findViewById(R.id.shutter);
        mShutter.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                mSurface.mCamera.takePicture(null, null, mPicture);
            }
        });
}

    // 포커싱 성공하면 촬영 허가
    Camera.AutoFocusCallback mAutoFocus = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            mShutter.setEnabled(success);
        }
    };

    // 사진 저장.
    Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            String sd = Environment.getExternalStorageDirectory().getAbsolutePath();
            String path = sd + "/cameratest.jpg";

            File file = new File(path);
            try {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(data);
                fos.flush();
                fos.close();
            } catch (Exception e) {
                Toast.makeText(CameraTest.this, "파일 저장 중 에러 발생 : " +
                        e.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.parse("file://" + path);
            intent.setData(uri);
            sendBroadcast(intent);

            Toast.makeText(CameraTest.this, "사진 저장 완료 : " + path, Toast.LENGTH_SHORT).show();
            mSurface.mCamera.startPreview();
        }
    };

}



// 미리보기 표면 클래스
class MyCameraSurface extends SurfaceView implements SurfaceHolder.Callback {
    SurfaceHolder mHolder;
    Camera mCamera;

    public MyCameraSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    // 표면 생성시 카메라 오픈하고 미리보기 설정
    public void surfaceCreated(SurfaceHolder holder) {
        mCamera = Camera.open();
        try {
            mCamera.setPreviewDisplay(mHolder);
        } catch (IOException e) {
            mCamera.release();
            mCamera = null;
        }
    }

    // 표면 파괴시 카메라도 파괴한다.
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    // 표면의 크기가 결정될 때 최적의 미리보기 크기를 구해 설정한다.
    public void surfaceChanged(SurfaceHolder holder, int format, int width,	int height) {
        Camera.Parameters params = mCamera.getParameters();
        List<Camera.Size> arSize = params.getSupportedPreviewSizes();
        if (arSize == null) {
            params.setPreviewSize(width, height);
        } else {
            int diff = 10000;
            Camera.Size opti = null;
            for (Camera.Size s : arSize) {
                if (Math.abs(s.height - height) < diff) {
                    diff = Math.abs(s.height - height);
                    opti = s;

                }
            }
            params.setPreviewSize(opti.width, opti.height);
        }
        mCamera.setParameters(params);
        mCamera.startPreview();
    }
}