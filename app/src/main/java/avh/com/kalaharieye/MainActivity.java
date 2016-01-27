package avh.com.kalaharieye;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.opencv.android.Utils;
import org.opencv.core.Mat;


public class MainActivity extends AppCompatActivity {

    //UI ELEMENTS
    private ToggleButton btnShootMode;
    private ToggleButton btnLiveMode;
    private Button btnNextShot;
    private ImageButton btnRefreshVideo;
    private TextView debugText;
    private ImageView mainImage;

    //ENUMS FOR APP STATE
    public enum AppState{SHOOTING_MODE,LIVE_MODE,NO_VIDEO_MODE}
    public AppState currentMode;


    //FOR THE IP CAM CONNECTION
    private CameraHandler cam;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(avh.com.kalaharieye.R.layout.activity_main);
        assignUIVariables();
        cam = new CameraHandler(100,150,"8080","admin","1234");
        defaultState();
        //cam.connectToCamera(); //TODO reenable later
    }


    private void defaultState(){
        currentMode = AppState.NO_VIDEO_MODE;
        btnLiveMode.setChecked(false);
        btnShootMode.setChecked(false);
        btnRefreshVideo.setEnabled(true);
    }

    //finds the UI variables that we need to find as the app starts
    private void assignUIVariables(){
        btnLiveMode = (ToggleButton) findViewById(R.id.button_live_mode);
        btnShootMode = (ToggleButton) findViewById(R.id.button_shooting_mode);
        btnNextShot =  (Button) findViewById(R.id.button_next_shot);
        debugText = (TextView) findViewById(R.id.textView);
        btnRefreshVideo = (ImageButton) findViewById(R.id.button_refresh);
        mainImage = (ImageView) findViewById(R.id.main_image);

    }




    //HANDLES SHOOTING MODE BUTTON RESPONSE
    public void onShootingButtonPress(View v){
        debugText.setText("shoot register");
        if (btnShootMode.isChecked()){
            btnLiveMode.setChecked(false);
            currentMode = AppState.SHOOTING_MODE;
        }
        else{
            btnLiveMode.setChecked(true);
            currentMode = AppState.LIVE_MODE;
        }
    }
    //HANDLES LIVE BUTTON RESPONSE
    public void onLiveButtonPress(View v){
        debugText.setText("live register");
        if (btnLiveMode.isChecked()){
            btnShootMode.setChecked(false);
            currentMode = AppState.LIVE_MODE;
        }
        else{
            btnShootMode.setChecked(true);
            currentMode = AppState.SHOOTING_MODE;
        }
    }

    //HANDLES NEXT SHOT BUTTON RESPONSE
    public void onNextShotButtonPress(View v){
        debugText.setText("next shot register");
    }

    //HANDLES REFRESH BUTTON RESPONSE
    public void onRefreshButtonPress(View v){
        debugText.setText("refresh");
        //try connect to camera
        //need to time this if it fails we need to reset it
        //try to have some progress thing here
        //maybe we can change the image and rotate it

        startRefreshAnimation();
        //TODO renable this later
        /*if (cam.connectToCamera()){
            currentMode = AppState.LIVE_MODE;
            //now we need to handle the input from the camera
            //we need to set up an update method
            //also we need to make refresh button dissapear
        }
        stopRefreshAnimation();*/


    }




    //this converts the mat we just received from the video capture and converts
    //it to a bitmap and assigns it to the main image
    private void convertMat(Mat mat){
        Bitmap bm = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat, bm);
        mainImage.setImageBitmap(bm);

    }


    private void startRefreshAnimation(){
        RotateAnimation r = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        r.setRepeatCount(Animation.INFINITE);
        r.setDuration(1000);
        btnRefreshVideo.startAnimation(r);
    }

    private void stopRefreshAnimation(){
        btnRefreshVideo.clearAnimation();
    }


}
