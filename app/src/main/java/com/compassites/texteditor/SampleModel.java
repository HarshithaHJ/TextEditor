package com.compassites.texteditor;

import android.graphics.Bitmap;

/**
 * Created by harshitha on 26/9/16.
 */

public class SampleModel {
    public int type;
    public String textContent;
    public String imagePath;
    public String videoPath;
    public Bitmap bitmap;
    public Boolean isBold=false;
    public Boolean isUnderLine=false;
    public Boolean isItalic=false;
    public int textSize=TextEditor.MIN_TEXT_SIZE;

}
