package edu.chalmers.glaucoma;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.MediaRecorder;
import android.media.MediaRecorder.AudioEncoder;
import android.media.MediaRecorder.VideoEncoder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;


public class PupilActivity extends Activity implements SurfaceHolder.Callback {
	
    private SurfaceView prSurfaceView;
    private SurfaceHolder prSurfaceHolder;
    private Button prStartBtn;
    private boolean prRecordInProcess;
    private Camera prCamera;
	private final String cVideoFilePath = Environment.getExternalStorageDirectory().getPath() + "/glaucoma_video/";
	private Parameters parameters;
	private static final String    	TAG = "HelloCV::Activity";
	private MediaRecorder prMediaRecorder;
	private File prRecordedFile;
	
	/** FLASHLIGHT FIELDS **/
	private boolean isLightOn = false;
	private int nr_test=3;
	private int flashTimeMs = 50;
	private int waitTimeMs =2000;
	private Button button;
	
	private Context prContext;
	
    @SuppressWarnings("deprecation")
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        prContext = this.getApplicationContext();
        setContentView(R.layout.activity_pupil);
        Utils.createDirIfNotExist(cVideoFilePath);
        
        prSurfaceView = (SurfaceView) findViewById(R.id.surface_camera);
        prSurfaceHolder = prSurfaceView.getHolder();
        prSurfaceHolder.addCallback(this);
        prSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		prMediaRecorder = new MediaRecorder();
		prRecordInProcess = false;
		
		/** RECORD BUTTON **/
		prStartBtn = (Button) findViewById(R.id.main_btn1);
        prStartBtn.setOnClickListener(new View.OnClickListener() {
			//@Override
			public void onClick(View v) {
				record();
			}
		});
        
		/** FLASHLIGHT INITIALIZE **/
		button = (Button) findViewById(R.id.buttonFlashlight);
		
		Context context = this;
		PackageManager pm = context.getPackageManager();

		// if device support camera?
		if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			Log.e("err", "Device has no camera!");
			return;
		}
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				flash();
			}
		});
    }

    private void initialize(){
		Log.i(TAG, "************************** INITIALIZE *************************");
		try {
			prCamera.unlock();
			if (prCamera == null) {
				Toast.makeText(this.getApplicationContext(), "Camera is not available!", Toast.LENGTH_SHORT).show();
				finish();
			}
		    prMediaRecorder.setCamera(prCamera);
		    prMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		    prMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
		    prMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
		    prMediaRecorder.setAudioEncoder(AudioEncoder.AAC);
		    prMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
		    prMediaRecorder.setVideoSize(1280, 720);
	
		    String lVideoFileFullPath = cVideoFilePath + String.valueOf(System.currentTimeMillis()) + ".mp4";
			prRecordedFile = new File(lVideoFileFullPath);
		    
			prMediaRecorder.setOutputFile(prRecordedFile.getPath());
		    //prMediaRecorder.setVideoSize(720, 480);
		    prMediaRecorder.setVideoFrameRate(20);
		    prMediaRecorder.setPreviewDisplay(prSurfaceHolder.getSurface());
		    
		    
		    prMediaRecorder.prepare();
	    } catch (IllegalStateException e) {
	        e.printStackTrace();
	        finish();
	    } catch (IOException e) {
	        e.printStackTrace();
	        finish();
	    }
	}
	
	public void record() {
	    if (prRecordInProcess) {
	    	Log.i(TAG, "************************** END RECORDING *************************");
	    	prMediaRecorder.stop();
			prMediaRecorder.reset();
			try {
				prCamera.reconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
			prStartBtn.setText("Start");
			prRecordInProcess = false;
			prCamera.startPreview();
	        prRecordInProcess = false;
	    } else {
	    	Log.i(TAG, "************************** START RECORDING *************************");
	    	prCamera.stopPreview();
	        
	        initialize();
	        prMediaRecorder.start();
	        prStartBtn.setText("Stop");
	        prRecordInProcess = true;
	        
	        //Start flashing
	        for(int i=0;i<nr_test;i++){
	        	try {
				    Thread.sleep(waitTimeMs);			//wait before starting the flash
				} catch(InterruptedException ex) {
				    Thread.currentThread().interrupt();
				}
	        	flash();								//turn flash on
	        	try {
				    Thread.sleep(flashTimeMs);
				} catch(InterruptedException ex) {
				    Thread.currentThread().interrupt();
				}
	        	flash();								//turn flash off
	        }
	        try {
			    Thread.sleep(waitTimeMs);				//wait before ending the recording
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
	        record();									//call to end the recording
	    }
	}
	
	public void flash() {
		Log.i(TAG, "************************** FLASH *************************");
	    if(!prRecordInProcess) {
	        prCamera.lock();
	    }

	    parameters.setFlashMode(parameters.getFlashMode().equals(Parameters.FLASH_MODE_TORCH) ? Parameters.FLASH_MODE_OFF : Parameters.FLASH_MODE_TORCH);
	    prCamera.setParameters(parameters);

	    if(!prRecordInProcess) {
	       prCamera.unlock();
	    }
	}
    
	//@Override
	public void surfaceCreated(SurfaceHolder arg0) {
	    try {
	        prCamera = Camera.open();
	        prCamera.setDisplayOrientation(90);
	        parameters = prCamera.getParameters();
	        parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
	        prCamera.setParameters(parameters);
	        prCamera.setPreviewDisplay(arg0);
	        prCamera.startPreview();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }       
	}
    
	//@Override
	public void surfaceChanged(SurfaceHolder _holder, int _format, int _width, int _height) {
		Camera.Parameters lParam = prCamera.getParameters();
		prCamera.setParameters(lParam);
		try {
			prCamera.setPreviewDisplay(_holder);
			prCamera.startPreview();
		} catch (IOException _le) {
			_le.printStackTrace();
		}
	}

	//@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		if (prRecordInProcess) {
			record();
			//stopRecording();
		} else {
			prCamera.stopPreview();
		}
		prMediaRecorder.release();
		prMediaRecorder = null;
		prCamera.release();
		prCamera = null;
	}
	
	
	private static final int REQUEST_DECODING_OPTIONS = 0;
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	super.onActivityResult(requestCode, resultCode, intent);
    	switch (requestCode) {
    	case REQUEST_DECODING_OPTIONS:
    		if (resultCode == RESULT_OK) {
    			//updateEncodingOptions();
    		}
    		break;
    	}
	}
	
	@Override
	protected void onStop() {
		super.onStop();

		if (prCamera != null) {
			prCamera.release();
		}
	}
	
}