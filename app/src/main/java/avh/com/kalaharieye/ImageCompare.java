package avh.com.kalaharieye;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.style.LineHeightSpan;
import android.util.Log;

public class ImageCompare {


    private int[][] previousImage, currentImage;
    private int height,width, threshold;

    public ImageCompare( int threshold){

        this.threshold = threshold;

    }

    public void setWidthAndHeight(int width, int height){
        this.width = width;
        this.height = height;
        previousImage = new int[height][width];
        currentImage = new int[height][width];
    }

    public void setPreviousImage(Drawable dr){
        Bitmap bm = ((BitmapDrawable)dr).getBitmap();
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                previousImage[x][y] = bm.getPixel(y,x);
            }
        }
    }


    public Bitmap drawableToBitmap(Drawable dr){



        Bitmap bm = ((BitmapDrawable)dr).getBitmap();
        //to make a mutable bitmap
        Bitmap edit = bm.copy(bm.getConfig(),true);




        int height = bm.getHeight();
        int width = bm.getWidth();

        //Log.w("Height::", Integer.toString(bm.getHeight()));
        //Log.w("Width::", Integer.toString(bm.getWidth()));



        for (int x = 0; x < height; x++){
            for (int y = 0; y < width; y++){
                //Log.w("x,y::", x + " " + y);
                //getpixel(width,height)
                currentImage[x][y] = convertToGrayScale(bm.getPixel(y,x));

                if (Math.abs(currentImage[x][y] - previousImage[x][y]) > threshold ){
                    edit.setPixel(y,x,Color.RED);
                }
            }
        }

        previousImage = currentImage.clone();
        return edit;
    }





    private int convertToGrayScale(int pixel){
        int col = Color.red(pixel);
        col += Color.blue(pixel);
        col += Color.green(pixel);

        col = col/3;
        return col;

    }



}
