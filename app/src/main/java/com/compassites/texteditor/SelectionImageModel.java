package com.compassites.texteditor;

import android.graphics.Bitmap;

public class SelectionImageModel {
    private String filePath;
    private Bitmap bitmap;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }


    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
