package com.compassites.texteditor;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by harshitha
 */
public class ViewImageFullScreenActivity extends AppCompatActivity {


    @Bind(R.id.image)
    ImageView image;

    private Bitmap bitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_full_screen);
        ButterKnife.bind(this);
        bitmap = getIntent().getParcelableExtra("list");
        image.setImageBitmap(bitmap);
    }
    //TODO to provide better image quality
//    public Bitmap getBitmap(String filePath) {
//        Bitmap bitmap = bitmapObject;
//        try {
//
//            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//            bitmap = BitmapFactory.decodeFile(filePath, bmOptions);
//            float screenHeight = Commonutils.getDeviceHeight(this);
//            float screenWidth = Commonutils.getDeviceWidth(this);
//            if(screenWidth==0 || screenHeight==0){
//                return  bitmap;
//            }
//            float aspectRatio = screenWidth / screenHeight;
//            int modifiedScreenHeight = 1000;
//            int modifiedScreenWidth = (int) (modifiedScreenHeight * aspectRatio);
//            bitmap = Bitmap.createScaledBitmap(bitmap, modifiedScreenWidth, modifiedScreenHeight, true);
//
//        } catch (Exception e) {
//
//        }
//        return bitmap;
//
//    }
}
