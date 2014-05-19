package edu.chalmers.glaucoma.pupil;

import java.util.List;

import org.opencv.android.JavaCameraView;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.widget.Toast;

public class PupilCameraView extends JavaCameraView {

public PupilCameraView(Context context, AttributeSet attrs) {
    super(context, attrs);
} 

public void setResolution(Camera.Size resolution) {
    disconnectCamera();
    connectCamera((int)resolution.width, (int)resolution.height);       
}

public void setFocusMode (Context item, int type){

    Camera.Parameters params = mCamera.getParameters(); 

    List<String> FocusModes = params.getSupportedFocusModes();

    switch (type){
    case 0:
        if (FocusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO))
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);

    case 1:
        if (FocusModes.contains(Camera.Parameters.FOCUS_MODE_MACRO))
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_MACRO);
        else
            Toast.makeText(item, "Macro Mode not supported", Toast.LENGTH_SHORT).show();
        break;      
    }

    mCamera.setParameters(params);
}   

public void setFlashMode (Context item, int type){

    Camera.Parameters params = mCamera.getParameters();
    List<String> FlashModes = params.getSupportedFlashModes();

    switch (type){
    case 0:
        if (FlashModes.contains(Camera.Parameters.FLASH_MODE_OFF))
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        else
            Toast.makeText(item, "Off Mode not supported", Toast.LENGTH_SHORT).show();          
        break;
    case 1:
        if (FlashModes.contains(Camera.Parameters.FLASH_MODE_TORCH))
            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        else
            Toast.makeText(item, "Torch Mode not supported", Toast.LENGTH_SHORT).show();        
        break;
    }

    mCamera.setParameters(params);
}   

public Camera.Size getResolution() {

    Camera.Parameters params = mCamera.getParameters(); 

    Camera.Size s = params.getPreviewSize();
    return s;
}
}