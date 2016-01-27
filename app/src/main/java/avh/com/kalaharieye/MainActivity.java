package avh.com.kalaharieye;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;


public class MainActivity extends AppCompatActivity {

    //UI ELEMENTS
    private ToggleButton btnShootMode;
    private ToggleButton btnLiveMode;
    private Button btnNextShot;
    private ImageButton btnRefreshVideo;
    private TextView debugText;

    //ENUMS FOR APP STATE
    public enum AppState{SHOOTING_MODE,LIVE_MODE,NO_VIDEO_MODE}
    public AppState currentMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(avh.com.kalaharieye.R.layout.activity_main);
        assignUIVariables();
        defaultState();
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


        RotateAnimation r = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        r.setDuration((long)4000);
        r.setRepeatCount(0);
        btnRefreshVideo.startAnimation(r);
    }


}
