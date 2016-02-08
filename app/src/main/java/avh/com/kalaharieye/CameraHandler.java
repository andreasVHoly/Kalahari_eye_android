package avh.com.kalaharieye;

/**
 * Created by Andreas on 26/1/2016.
 * class that handles the connection to the camera and handles the assosaited data
 */

import org.bytedeco.javacpp.opencv_videoio;
import org.opencv.videoio.VideoCapture;
import android.util.Log;

public class CameraHandler {


    //"http://admin:1234@192.168.0.101:8080/videostream.asf?user=admin&pwd=1234&resolution=64&rate=0";

    private int startIP, endIP;
    private String port, userName, password, connectionAddress;
    private String startString = "http://";
    private String endString = "&resolution=64&rate=0";
    private String midString  = "/videostream.asf?user=";
    private String ipAddress = "@192.168.0.";

    public CameraHandler(int startIP, int endIP, String port, String username, String password, boolean usingFoscam){
        this.port = port;
        this.startIP = startIP;
        this.endIP = endIP;
        this.userName = username;
        this.password = password;

        if (usingFoscam){
            //startString = "rtsp://";
            startString = "http://admin1.foscam1";
            midString = "/videoMain";
        }
    }
    //http://admin1.foscam1@192.168.10.112:88/cgi-bin/CGIProxy.fcgi?cmd=snapPicture2&usr=admin1&pwd=foscam1

    public boolean connectToCamera(){


        String connectionAddressStart = startString + userName + ":" + password + ipAddress;
        //String connectionAddressEnd = ":" + port + midString + userName + "&pwd=" + password + endString;
        String connectionAddressEnd = ":" + port + midString;
        VideoCapture vcap = new VideoCapture();

        boolean successfullConnect = false;

        //String adr = "http://admin1.foscam1@192.168.10.112:88/cgi-bin/CGIProxy.fcgi?cmd=snapPicture2&usr=admin1&pwd=foscam1?dummy=param.mjpg";
        String adr = "http://192.168.10.112:88/cgi-bin/CGIStream.cgi?cmd=GetMJStream&usr=admin1&pwd=foscam1";
        if (vcap.open(adr)){
            Log.i("Camera Handler","Connected to camera ");
            successfullConnect = true;
        }
        else{


            Log.e("Cam handler", "failed");
        }

        /*for (int i = startIP; i < endIP; i++){
            connectionAddress = connectionAddressStart + Integer.toString(i) + connectionAddressEnd;
            if (vcap.open(connectionAddress)){
                Log.i("Camera Handler","Connected to camera at address " + ipAddress + i + " : " + connectionAddress);
                successfullConnect = true;
                break;
            }
            Log.e("Camera Handler","Could not connect to camera at address " + ipAddress + i+ " : " + connectionAddress);
        }*/
        return successfullConnect;

    }




    public void setStartIP(int startIP) {
        this.startIP = startIP;
    }

    public int getEndIP() {
        return endIP;
    }

    public void setEndIP(int endIP) {
        this.endIP = endIP;
    }

    public String getPort() {
        return port;
    }

    public String getConnectionAddress() {
        return connectionAddress;
    }

    public void setConnectionAddress(String connectionAddress) {  this.connectionAddress = connectionAddress;    }

    public int getStartIP() {
        return startIP;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStartString() {
        return startString;
    }

    public void setStartString(String startString) {
        this.startString = startString;
    }

    public String getEndString() {
        return endString;
    }

    public void setEndString(String endString) {
        this.endString = endString;
    }

    public String getMidString() {
        return midString;
    }

    public void setMidString(String midString) {
        this.midString = midString;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }





}
