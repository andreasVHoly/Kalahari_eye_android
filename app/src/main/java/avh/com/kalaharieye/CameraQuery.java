package avh.com.kalaharieye;

import android.graphics.drawable.Drawable;
import android.os.*;
import android.util.Log;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;


public class CameraQuery /*implements Runnable*/{

   /* private Thread currentThread;

*/

    public CameraQuery(){
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }
/*
    @Override
    public void run(){
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);

        currentThread = Thread.currentThread();
    }


*/
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
        catch(MalformedURLException e){
            e.printStackTrace();
            Log.e("MalformedURLException: ", e.toString());
            return null;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        /*try{
            return new GetImageData().execute(new URL(url)).get();
        }
        catch(MalformedURLException e){
            e.printStackTrace();
            Log.e("MalformedURLException: ", e.toString());
            return null;
        }
        catch(InterruptedException e){
            e.printStackTrace();
            Log.e("InterruptedException: ", e.toString());
            return null;
        }
        catch(ExecutionException e){
            e.printStackTrace();
            Log.e("ExecutionException: ", e.toString());
            return null;
        }
        catch(Exception e){
            e.printStackTrace();
            Log.e("Exception: ", e.toString());
            return null;
        }*/


    }




    /*private class GetImageData extends AsyncTask<URL, Integer, Drawable>{


        protected Drawable doInBackground(URL... urls){

            try {
                InputStream is = (InputStream) urls[0].getContent();
                Drawable img = Drawable.createFromStream(is, "src name");
                return img;
            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }




        protected void onPostExecute(Long result){
            Log.i("ImageData", "Image succesfully retrieved");
        }

    }
*/




}
