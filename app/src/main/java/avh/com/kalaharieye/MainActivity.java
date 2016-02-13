package avh.com.kalaharieye;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    //UI ELEMENTS
    private ToggleButton btnShootMode;
    private ToggleButton btnLiveMode;
    private Button btnNextShot;
    private ImageButton btnRefreshVideo;
    private TextView debugText;
    private ImageView mainImage;


    private CameraQuery camQuery;
    private ImageCompare imgCompare;


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
    /*static {
        System.loadLibrary("opencv_java3");
    }*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assignUIVariables();
        setUpImagePanel();
        camQuery = new CameraQuery();
        imgCompare = new ImageCompare(120);
        //cam = new CameraHandler(65, 150, "88", "admin1", "foscam1", true);
        defaultState();
        //cam.connectToCamera(); //TODO reenable later

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();






    }


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
        //TODO need to still convert the image pannel to be scrollable, do this by adding in  a scrollview in xml
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
        setMainImage();
    }



    //HANDLES NEXT SHOT BUTTON RESPONSE
    public void onNextShotButtonPress(View v) {
        debugText.setText("next shot register");
        if (currentMode == AppState.SHOOTING_MODE ){
            showNextShot();
        }


    }

    private void showNextShot(){
        Drawable image = camQuery.getJPEGImage("");
        imgCompare.setWidthAndHeight(1920,1080);
        Bitmap newImage = imgCompare.drawableToBitmap(image);
        if (newImage == null){
            return;
        }
        btnRefreshVideo.setVisibility(View.INVISIBLE);

        mainImage.setImageBitmap(newImage);
        //Log.w("Width", Integer.toString(mainImage.getWidth()));
        //Log.w("Height", Integer.toString(mainImage.getHeight()));
    }


    private void setMainImage(){
        Drawable image = camQuery.getJPEGImage("");
        btnRefreshVideo.setVisibility(View.INVISIBLE);
        mainImage.setImageDrawable(image);
        imgCompare.setPreviousImage(image);
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
        if (cam.connectToCamera()){
            currentMode = AppState.LIVE_MODE;
            //now we need to handle the input from the camera
            //we need to set up an update method
            //also we need to make refresh button dissapear
            VideoCapture vcap = new VideoCapture();
            vcap.open(cam.getConnectionAddress());
            Mat firstImage = new Mat();
            vcap.read(firstImage);
            convertMat(firstImage);

        }

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
        for (int i = 0; i < noImages; i++){
            saveImageToPhone(imagePanel.get(i), i);
        }
    }

    //SAVES THE IMAGE ONTO THE PHONE, get the image and its position in the queue
    private void saveImageToPhone(ImageView img, int i){
        //first we set up the image to be in a state to be saved adn make a bitmap of it
        img.buildDrawingCache();
        Bitmap bm = img.getDrawingCache();

        //now we save the image to the device
        File storageLocation = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String filename = generateFileName(i);
        File file = new File(storageLocation, filename);

        //we write the file
        try{
            FileOutputStream fos = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();

            saveFile(getApplicationContext(),Uri.fromFile(file));
        }
        catch (IOException e){
            e.printStackTrace();
        }




    }

    //GENERATES A FILE NAME WITH CURRENT DATE AND TIME WITH INDENTIFIER TO MAKE UNIQUE NAMES FOR THE SAVED IMAGES
    private String generateFileName(int i){
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int day = c.get(Calendar.DATE);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);

        return Integer.toString(day) + Integer.toString(month) + Integer.toString(year)+ Integer.toString(hour) + Integer.toString(minute ) + "_" + Integer.toString(i) + ".jpg";
    }


    private void saveFile(Context context, Uri imgURI){
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(imgURI);
        context.sendBroadcast(scanIntent);
    }


    //START A NEW SESSION, DISCARDING ALL PREVIOUS IMAGES
    private void newSession(){
        //need to delete item at 1 each time as items get shifted up once one above is deleted and 0 is the text

        for (int i = 0; i < noImages; i++){
            imageContainer.removeViewAt(1);
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
                loadAboutUsPage();
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
            case R.id.menu_settings:
                loadSettingsPage();
                return true;
            default:
                return false;
        }

    }


    private void loadSettingsPage(){
        //start new intent
        Intent nextScreen = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(nextScreen);
    }

    private void loadAboutUsPage(){
        //Starting a new Intent
        Intent nextScreen = new Intent(getApplicationContext(), AboutUsActivity.class);
        startActivity(nextScreen);
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




    //TESTING******************

    private void testImageSave(){
        /*String uri = "@mipmap/shot2";
        int imgres = getResources().getIdentifier(uri,null,getPackageName());
        ImageView img1 = new ImageView(getApplicationContext());
        Drawable res = getResources().getDrawable(imgres);
        img1.setImageDrawable(res);

        String uri2 = "@mipmap/shot_1";
        int imgres2 = getResources().getIdentifier(uri2,null,getPackageName());
        ImageView img2 = new ImageView(getApplicationContext());
        Drawable res2 = getResources().getDrawable(imgres2);
        img2.setImageDrawable(res2);

        addImageView(img1);
        addImageView(img2);*/

    }


}
