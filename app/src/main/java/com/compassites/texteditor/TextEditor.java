package com.compassites.texteditor;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.compassites.texteditor.CustomAdapter.FILE;
import static com.compassites.texteditor.CustomAdapter.IMAGE;
import static com.compassites.texteditor.CustomAdapter.TEXT;
import static com.compassites.texteditor.CustomAdapter.VIDEO;

/**
 * Created by harshitha.
 */

public class TextEditor extends LinearLayout {
    private Context mContext;
    private SampleModel mSampleModel;
    private int position;

    @Bind(R.id.imageview)
    ImageView image;

    @Bind(R.id.videoview)
    VideoView video;

    @Bind(R.id.editor)
    EditText editor;

    @Bind(R.id.cross)
    TextView cross;

    private CustomAdapter.EditorCallbacks mEditorCallbacks;


    public TextEditor(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.text_editor_view, this);
        ButterKnife.bind(this, view);

    }


    public TextEditor(Context context) {
        super(context);
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.text_editor_view, this);
        ButterKnife.bind(this, view);


    }

    public TextEditor(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.text_editor_view, this);
        ButterKnife.bind(this, view);

    }


    public void setData(SampleModel sampleModel, int pos) {
        mSampleModel = sampleModel;
        position = pos;
        init();
    }

    private void init() {
        cross.setVisibility(View.GONE);
        editor.setText(mSampleModel.textContent);
        editor.setVisibility(View.VISIBLE);
        editor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                String result;


//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//                    result = Html.toHtml(editor.getText(), Html.FROM_HTML_MODE_LEGACY);
//                } else {
//                    result = Html.toHtml(editor.getText());
//                }
                mSampleModel.textContent = editor.getText().toString();
                mEditorCallbacks.dataModified(mSampleModel, position);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editor.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus) {
                    if (TextUtils.isEmpty(editor.getText().toString())) {

                    }
                }
            }
        });
        attachViews();

    }

    public void setCloseListener(CustomAdapter.EditorCallbacks editorCallbacks) {
        mEditorCallbacks = editorCallbacks;
    }

    @OnClick(R.id.cross)
    public void onCrossClick(View v) {
        mEditorCallbacks.closeListener(position, getType());

    }

    private int getType() {
        if (mSampleModel.type == VIDEO) {
            return VIDEO;
        } else if (mSampleModel.type == IMAGE) {
            return IMAGE;
        } else {
            return TEXT;
        }
    }


    private void attachViews() {
        switch (mSampleModel.type) {
            case IMAGE:
                if (mSampleModel.imagePath == null) {
                    cross.setVisibility(View.GONE);
                    image.setVisibility(View.GONE);
                } else {
                    cross.setVisibility(View.VISIBLE);
                    image.setVisibility(View.VISIBLE);
                    video.setVisibility(View.GONE);
                    cross.setTag(IMAGE);
                    mSampleModel.videoPath = null;
                }
                break;
            case VIDEO:
                if (mSampleModel.videoPath == null) {
                    cross.setVisibility(View.GONE);
                    video.setVisibility(View.GONE);
                } else {
                    cross.setVisibility(View.VISIBLE);
                    image.setVisibility(View.GONE);
                    video.setVisibility(View.VISIBLE);
                    cross.setTag(VIDEO);

                    mSampleModel.videoPath = "Video url:  www.video.com";
                    mSampleModel.imagePath = null;
                }
                break;
            case FILE:
                image.setVisibility(View.GONE);
                video.setVisibility(View.GONE);
                break;
            default:
                image.setVisibility(View.GONE);
                video.setVisibility(View.GONE);
                break;
        }
        mEditorCallbacks.dataModified(mSampleModel, position);

    }
}
