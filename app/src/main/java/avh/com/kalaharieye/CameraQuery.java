package avh.com.kalaharieye;

import android.graphics.drawable.Drawable;
import android.os.*;

import java.io.InputStream;
import java.net.URL;


public class CameraQuery implements Runnable{

    private Thread currentThread;



    public CameraQuery(){
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    @Override
    public void run(){
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);

        currentThread = Thread.currentThread();
    }



    public Drawable getJPEGImage(String url){
        //default for foscam camera
        if (url.contentEquals("")){
            url = "http://192.168.10.112:88/cgi-bin/CGIProxy.fcgi?cmd=snapPicture2&usr=admin1&pwd=foscam1";
        }

        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable img = Drawable.createFromStream(is, "src name");
            return img;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }




}
