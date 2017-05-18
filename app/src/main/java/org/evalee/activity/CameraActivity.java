package org.evalee.activity;

import org.evalee.camera.CameraInterface;
import org.evalee.camera.CameraInterface.CamOpenOverCallback;
import org.evalee.camera.preview.CameraSurfaceView;
import org.evalee.permission.PermissionActivity;
import org.evalee.util.DisplayUtil;

import android.Manifest;
import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.example.evalee.playcamerav10.R;

public class CameraActivity extends PermissionActivity implements CamOpenOverCallback {
	private static final String TAG = "CameraActivity_lsn";
	CameraSurfaceView surfaceView = null;
	ImageButton shutterBtn;
	float previewRate = -1f;
	private static final int CAMERA_REQUEST_CODE = 0X001;
	private static final int STORAGE_REQUEST_CODE = 0X002;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_camera);
		requestPermission(new String[]{Manifest.permission.CAMERA},CAMERA_REQUEST_CODE);
	}

	@Override
	public void permissionSuccess(int requestCode) {
		super.permissionSuccess(requestCode);

		switch (requestCode){
			case CAMERA_REQUEST_CODE:
				Thread openThread = new Thread(){
					@Override
					public void run() {
						// TODO Auto-generated method stub
						CameraInterface.getInstance().doOpenCamera(CameraActivity.this);
					}
				};
				openThread.start();
				initUI();
				break;

			case STORAGE_REQUEST_CODE:
				CameraInterface.getInstance().doTakePicture();
				break;
		}

		shutterBtn.setOnClickListener(new BtnListeners());
	}

	private void initUI(){
		surfaceView = (CameraSurfaceView)findViewById(R.id.camera_surfaceview);
		shutterBtn = (ImageButton)findViewById(R.id.btn_shutter);
		LayoutParams params = surfaceView.getLayoutParams();
		Point p = DisplayUtil.getScreenMetrics(this);
		params.width = p.x;
		params.height = p.y;

		previewRate = DisplayUtil.getScreenRate(this);
		surfaceView.setLayoutParams(params);

		LayoutParams p2 = shutterBtn.getLayoutParams();
		p2.width = DisplayUtil.dip2px(this, 80);
		p2.height = DisplayUtil.dip2px(this, 80);;
		shutterBtn.setLayoutParams(p2);
	}

	@Override
	public void cameraHasOpened() {
		// TODO Auto-generated method stub
		SurfaceHolder holder = surfaceView.getSurfaceHolder();
		CameraInterface.getInstance().doStartPreview(holder, previewRate);
	}
	private class BtnListeners implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.btn_shutter:
				requestPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_REQUEST_CODE);
				break;
			default:break;
			}
		}

	}



}
