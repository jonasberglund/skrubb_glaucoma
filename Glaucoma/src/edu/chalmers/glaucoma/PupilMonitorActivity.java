package edu.chalmers.glaucoma;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Core;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.imgproc.Imgproc;

import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

public class PupilMonitorActivity extends Activity implements CvCameraViewListener2 {
	
	private static final String    	TAG = "HelloCV::Activity";
	private static final Scalar    	EYE_RECT_COLOR = new Scalar(0, 255, 0, 255);
    
    private Mat                    	mRgba;
    private Mat                    	mIntermediateMat;
    private Mat                    	mGrey;

    private CameraBridgeViewBase   	mOpenCvCameraView;
    
	private CascadeClassifier 		mJavaDetectorEye;

    private BaseLoaderCallback  mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");

                    // LOAD NATIVE LIBRARY (NONE AT THE MOMENT)
                    System.loadLibrary("opencv_java248");
                    
    				try {
    					// LOAD EYE CASCADE
    					InputStream is = getResources().openRawResource(
    							R.raw.haarcascade_eye);
    					File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
    					File mCascadeFile = new File(cascadeDir,
    							"haarcascade_eye.xml");
    					FileOutputStream os = new FileOutputStream(mCascadeFile);

    					byte[] buffer = new byte[4096];
    					int bytesRead;
    					while ((bytesRead = is.read(buffer)) != -1) {
    						os.write(buffer, 0, bytesRead);
    					}
    					is.close();
    					os.close();

    					mJavaDetectorEye = new CascadeClassifier(
    							mCascadeFile.getAbsolutePath());
    					if (mJavaDetectorEye.empty()) {
    						Log.e(TAG, "Failed to load cascade classifier");
    						mJavaDetectorEye = null;
    					} else
    						Log.i(TAG, "Loaded cascade classifier from "
    								+ mCascadeFile.getAbsolutePath());

    					cascadeDir.delete();

    				} catch (IOException e) {
    					e.printStackTrace();
    					Log.e(TAG, "Failed to load cascade. Exception thrown: " + e);
    				}
    				
                    mOpenCvCameraView.enableFpsMeter();
                    mOpenCvCameraView.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };
    
    public PupilMonitorActivity(){
    	Log.i(TAG, "Instantiated new " + this.getClass());
    }
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_pupil_test);

        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.activity_surface_view);
        mOpenCvCameraView.setCvCameraViewListener(this);
    }
    
    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mLoaderCallback);
    }
    
    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(height, width, CvType.CV_8UC4);
        mIntermediateMat = new Mat(height, width, CvType.CV_8UC4);
        mGrey = new Mat(height, width, CvType.CV_8UC1);
    }

    public void onCameraViewStopped() {
        mRgba.release();
        mGrey.release();
        mIntermediateMat.release();
    }
    
    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
		
    	mRgba = inputFrame.rgba().t();
		mGrey = inputFrame.gray().t();
		
		findFeatures();

        return mRgba.t();
    }
    
    private void findFeatures() {
    	
    	// FIND EYE
    	MatOfRect eyes = new MatOfRect();
    	mJavaDetectorEye.detectMultiScale(mGrey, eyes, 1.3, 4, 	Objdetect.CASCADE_FIND_BIGGEST_OBJECT | 
    															Objdetect.CASCADE_DO_CANNY_PRUNING |
    															Objdetect.CASCADE_SCALE_IMAGE, new Size(30, 30), new Size());
    	
    	Rect[] eyesArray = eyes.toArray();
    	for (int i = 0; i < eyesArray.length; i++) {
    		
    		// FIND IRIS
    		findIris(mRgba.submat(eyesArray[i]));
    		
    		// DRAW IRIS
    		Core.rectangle(mRgba, eyesArray[i].tl(), eyesArray[i].br(), EYE_RECT_COLOR, 2);
    	}
    }
    
    private void findIris(Mat mROIEye) {
    	
    	// PROCESS
		Mat mROIProc = mROIEye.clone();										// Copy for processing 
		mROIProc.convertTo(mROIProc, -1, 1.5);								// Apply contrast
    	Imgproc.cvtColor(mROIProc, mROIProc, Imgproc.COLOR_RGBA2GRAY);		// Convert to grayscale
    	Imgproc.GaussianBlur(mROIProc, mROIProc, new Size(5, 5), 0, 0);		// Apply blur
    	Imgproc.Canny(mROIProc, mROIProc, 50, 100);							// Apply canny filter
    	
    	// FIND IRIS
    	List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
    	Point pupil = findPupilCenter(mROIEye);
    	Imgproc.findContours(mROIProc, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);
    	
    	for (MatOfPoint c : contours) {
    		double size = Imgproc.contourArea((Mat) c);
    		Rect irisRect = Imgproc.boundingRect(c);
    		int radius = irisRect.width / 2;
    		
    		//If round, sufficient size and contains pupil: iris found
    		if (isRound((double) irisRect.width, (double) irisRect.height) && size >= 100 && irisRect.contains(pupil)) {
    			Log.i(TAG, "************************** FOUND IRIS *************************");
    			
    			findPupil(mROIEye.submat(irisRect));
    			
    			// Draw iris
    			Core.circle(mROIEye, new Point(irisRect.x + radius, irisRect.y + radius), radius, new Scalar(255, 0, 0, 255), 2);
    		}
    		
    	
	
    	}
    	
    	
    }
    
    private void findPupil(Mat mROIIris) {
    	Mat mROIProc = mROIIris.clone();
		
    	// PROCESS
    	Imgproc.cvtColor(mROIProc, mROIProc, Imgproc.COLOR_RGBA2GRAY);					// Convert to grayscale
    	Imgproc.equalizeHist(mROIProc, mROIProc);										// Equalize histogram
    	Imgproc.threshold(mROIProc, mROIProc, 25, 255, Imgproc.THRESH_BINARY);			// Apply threshold filter
    	
    	// FIND PUPIL
    	List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
    	Point pupil = findPupilCenter(mROIIris);
    	Imgproc.findContours(mROIProc, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_NONE);
    	
    	for (MatOfPoint c : contours) {
    		double size = Imgproc.contourArea((Mat) c);
    		Rect pupilRect = Imgproc.boundingRect(c);
    		int radius = pupilRect.width / 2;
    		
    		//If big enough and round --> pupil
    		if (isRound((double) pupilRect.width, (double) pupilRect.height) && size >= 30 && pupilRect.contains(pupil)) {
    			Log.i(TAG, "-------------------- FOUND PUPIL ----------------------");
    			
    			// Draw pupil
    			Core.circle(mROIIris, new Point(pupilRect.x + radius, pupilRect.y + radius), radius, new Scalar(0, 0, 255, 255), 2);
    			Log.i(TAG, "DIAMITER " + 2*radius);
    		}
    	}
    	
    }
    
    private Point findPupilCenter(Mat mROI) {
    	Mat mROIProc = new Mat();
    	
    	Imgproc.cvtColor(mROI, mROIProc, Imgproc.COLOR_RGBA2GRAY);
		
		// Find darkest point
		Core.MinMaxLocResult mmG = Core.minMaxLoc(mROIProc);
		//Log.i(TAG, "################ :" + String.valueOf(mmG.minVal));
		
		// Return eye center
		return mmG.minLoc;
    }
    
    private void drawPupilCenter(Mat mROI, Point center) {
    	Core.circle(mROI, center, 2, new Scalar(255, 255, 255, 255),2);
    }
    
    private boolean isRound(double x, double y) {
    	return Math.abs(1 - (x / y)) <= 0.2;
    }
    
}
