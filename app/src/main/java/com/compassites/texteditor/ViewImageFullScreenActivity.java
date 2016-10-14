package com.compassites.texteditor;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by harshitha
 */
public class ViewImageFullScreenActivity extends Activity {


    @Bind(R.id.image)
    ImageView image;

    private String path;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_image_full_screen);
        ButterKnife.bind(this);
        path = getIntent().getStringExtra("list1");
        image.setImageBitmap(CommonUtils.getBitmap(path,this));
    }


}
