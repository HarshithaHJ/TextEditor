package com.compassites.texteditor;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.view.Display;

/**
 * Created by harshitha.
 */

public class CommonUtils {
    public static int getDeviceWidth(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    public static int getDeviceHeight(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }

    //TODO to provide better image quality
    public static Bitmap getBitmap(String filePath, Activity context) {
        Bitmap bitmap = null;
        try {

            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bitmap = BitmapFactory.decodeFile(filePath, bmOptions);
            float screenHeight = CommonUtils.getDeviceHeight(context);
            float screenWidth = CommonUtils.getDeviceWidth(context);
            if (screenWidth == 0 || screenHeight == 0) {
                return bitmap;
            }
            float aspectRatio = screenWidth / screenHeight;
            int modifiedScreenHeight = 1000;
            int modifiedScreenWidth = (int) (modifiedScreenHeight * aspectRatio);
            bitmap = Bitmap.createScaledBitmap(bitmap, modifiedScreenWidth, modifiedScreenHeight, true);

        } catch (Exception e) {

        }
        return bitmap;

    }

    public static Bitmap getBitmapWithSpecification(String filePath, Activity context) {
        Bitmap bitmap = null;
        try {

            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bitmap = BitmapFactory.decodeFile(filePath, bmOptions);
            float screenHeight = 50;
            float screenWidth = getDeviceWidth(context);
            if (screenWidth == 0 || screenHeight == 0) {
                return bitmap;
            }
            float aspectRatio = screenWidth / screenHeight;
            int modifiedScreenHeight = 50;
            int modifiedScreenWidth = (int) (modifiedScreenHeight * aspectRatio);
            bitmap = Bitmap.createScaledBitmap(bitmap, modifiedScreenWidth, modifiedScreenWidth, true);

        } catch (Exception e) {

        }
        return bitmap;

    }
}
