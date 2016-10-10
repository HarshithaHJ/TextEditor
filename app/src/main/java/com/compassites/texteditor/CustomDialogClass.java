package com.compassites.texteditor;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CustomDialogClass extends Dialog {

    public Activity activity;
    @Bind(R.id.video)
    TextView video;
    @Bind(R.id.image)
    TextView image;
    @Bind(R.id.audio)
    TextView audio;

    public CustomDialogClass(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        setContentView(R.layout.custom_dialog);
        ButterKnife.bind(this);


    }

    private ClickListener mClickListener;

    public void setListener(ClickListener clickListener) {
        mClickListener = clickListener;
    }


    @OnClick(R.id.video)
    public void onVideoClick(View view) {
        mClickListener.onVideoClick();
        dismiss();
    }


    @OnClick(R.id.image)
    public void onImageClick(View view) {
        mClickListener.onImageClick();
        dismiss();
    }

    @OnClick(R.id.audio)
    public void onFileClick(View view) {
        mClickListener.onFileClick();
        dismiss();
    }

    public interface ClickListener {
        void onImageClick();

        void onFileClick();

        void onVideoClick();
    }
}