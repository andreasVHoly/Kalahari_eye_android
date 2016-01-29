package avh.com.kalaharieye;

import android.app.Application;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;


import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    //UI ELEMENTS
    private ToggleButton btnShootMode;
    private ToggleButton btnLiveMode;
    private Button btnNextShot;
    private ImageButton btnRefreshVideo;
    private TextView debugText;
    private ImageView mainImage;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    //FOR THE IMAGE PANEL
    private ArrayList<ImageView> imagePanel;
    private int noImages;
    private LinearLayout imageContainer;


    //ENUMS FOR APP STATE
    public enum AppState {
        SHOOTING_MODE, LIVE_MODE, NO_VIDEO_MODE
    }

    public AppState currentMode;


    //FOR THE IP CAM CONNECTION
    private CameraHandler cam;



    //VERY IMPORTANT, LOADS THE LIBRARY SO WE CAN USE IT
    static {
        System.loadLibrary("opencv_java3");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assignUIVariables();
        setUpImagePanel();
        cam = new CameraHandler(100, 150, "8080", "admin", "1234");
        defaultState();
        //cam.connectToCamera(); //TODO reenable later

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * Call on every application resume
     **/
    /*@Override
    protected void onResume()
    {
        Log.i(TAG, "Called onResume");
        super.onResume();

        Log.i(TAG, "Trying to load OpenCV library");
        if (!OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, this, mOpenCVCallBack)) {
            Log.e(TAG, "Cannot connect to OpenCV Manager");
        }
    }

    private BaseLoaderCallback mOpenCVCallBack = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i("OpenCV", "OpenCV loaded successfully");
                    // Create and set View

                    setContentView(R.layout.activity_main);
                    cam.connectToCamera();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };*/
    private void defaultState() {
        currentMode = AppState.NO_VIDEO_MODE;
        btnLiveMode.setChecked(false);
        btnShootMode.setChecked(false);
        btnRefreshVideo.setEnabled(true);
    }

    //finds the UI variables that we need to find as the app starts
    private void assignUIVariables() {
        btnLiveMode = (ToggleButton) findViewById(R.id.button_live_mode);
        btnShootMode = (ToggleButton) findViewById(R.id.button_shooting_mode);
        btnNextShot = (Button) findViewById(R.id.button_next_shot);
        debugText = (TextView) findViewById(R.id.textView);
        btnRefreshVideo = (ImageButton) findViewById(R.id.button_refresh);
        mainImage = (ImageView) findViewById(R.id.main_image);

    }


    //SETS UP ALL THE VARIABLES FOR THE IMAGE PANEL
    private void setUpImagePanel(){
        imagePanel = new ArrayList<>();
        noImages = 0;
        imageContainer = (LinearLayout) findViewById(R.id.image_container);
        //TODO need to still convert the image panbel to be scrollable, do this by adding in  a scrollview in xml
    }

    //ADDS A NEW IMAGE TO THE PANEL
    private void addImageView(ImageView image){
        imagePanel.add(image);
        noImages++;
        imageContainer.addView(image);
    }


    //HANDLES SHOOTING MODE BUTTON RESPONSE
    public void onShootingButtonPress(View v) {
        debugText.setText("shoot register");
        /*if (btnShootMode.isChecked()) {
            btnLiveMode.setChecked(false);
            currentMode = AppState.SHOOTING_MODE;
        } else {
            btnLiveMode.setChecked(true);
            currentMode = AppState.LIVE_MODE;
        }*/
        enableShootingButton();
    }

    //HANDLES LIVE BUTTON RESPONSE
    public void onLiveButtonPress(View v) {
        debugText.setText("live register");
        /*if (btnLiveMode.isChecked()) {
            btnShootMode.setChecked(false);
            currentMode = AppState.LIVE_MODE;
        } else {
            btnShootMode.setChecked(true);
            currentMode = AppState.SHOOTING_MODE;
        }*/
        enableLiveButton();
    }

    public void enableLiveButton(){
        btnShootMode.setChecked(false);
        btnLiveMode.setChecked(true);
        currentMode = AppState.LIVE_MODE;
    }

    public void enableShootingButton(){
        btnLiveMode.setChecked(false);
        btnShootMode.setChecked(true);
        currentMode = AppState.SHOOTING_MODE;
    }



    //HANDLES NEXT SHOT BUTTON RESPONSE
    public void onNextShotButtonPress(View v) {
        debugText.setText("next shot register");
        showNextShot();
    }

    private void showNextShot(){

    }

    //HANDLES REFRESH BUTTON RESPONSE
    public void onRefreshButtonPress(View v) {
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
            VideoCapture vcap = new VideoCapture();
            vcap.open(cam.getConnectionAddress());
            Mat firstImage = new Mat();
            vcap.read(firstImage);
            convertMat(firstImage);

        }*/
        stopRefreshAnimation();


    }


    //this converts the mat we just received from the video capture and converts
    //it to a bitmap and assigns it to the main image
    private void convertMat(Mat mat) {
        Bitmap bm = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat, bm);
        mainImage.setImageBitmap(bm);

    }


    private void startRefreshAnimation() {
        RotateAnimation r = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        r.setRepeatCount(Animation.INFINITE);
        r.setDuration(1000);
        btnRefreshVideo.startAnimation(r);
    }

    private void stopRefreshAnimation() {
        btnRefreshVideo.clearAnimation();
    }

    //SAVE ALL IMAGES IN THE SESSION
    private void saveSession(){
        //TODO cycle through the images and save them
    }

    //SAVES THE IMAGE ONTO THE PHONE
    private void saveImageToPhone(){
        //TODO save image to disk
    }


    //START A NEW SESSION, DISCARDING ALL PREVIOUS IMAGES
    private void newSession(){
        for (int i = 1; i <= noImages; i++){
            imageContainer.removeViewAt(i);
        }
        imagePanel.clear();
        noImages = 0;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.context_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch(item.getItemId()){
            case R.id.menu_about:
                //load the about menu
                Log.i("Context Menu", "About us reg");
                return true;
            case R.id.menu_exit_app:
                System.exit(0);
                return true;
            case R.id.menu_live_mode:
                enableLiveButton();
                return true;
            case R.id.menu_next_shot:
                showNextShot();
                return true;
            case R.id.menu_shooting_mode:
                enableShootingButton();
                return true;
            case R.id.menu_save_session:
                saveSession();
                return true;
            case R.id.menu_new_session:
                newSession();
                return true;
            default:
                return false;
        }

    }



    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://avh.com.kalaharieye/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://avh.com.kalaharieye/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }


}
