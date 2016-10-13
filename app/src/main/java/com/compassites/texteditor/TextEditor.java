package com.compassites.texteditor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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

public class TextEditor extends LinearLayout implements
        View.OnLongClickListener, View.OnClickListener, ActionMode.Callback,
        View.OnFocusChangeListener, TextWatcher {

    private Context mContext;
    private SampleModel mSampleModel;
    private int position;
    private PopupWindow mPopupWindow;
    private CustomAdapter.EditorCallbacks mEditorCallbacks;

    public static final int MAX_TEXT_SIZE = 75;
    public static final int MIN_TEXT_SIZE = 15;

    @Bind(R.id.imageview)
    ImageView image;

    @Bind(R.id.videoview)
    VideoView video;

    @Bind(R.id.editor)
    EditText editor;

    @Bind(R.id.cross)
    TextView cross;


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


    public void setData(SampleModel sampleModel, int pos, CustomAdapter.EditorCallbacks editorCallbacks) {
        mSampleModel = sampleModel;
        position = pos;
        mEditorCallbacks = editorCallbacks;
        init();
    }


    private void init() {
        editor.setOnFocusChangeListener(this);
        editor.setCustomSelectionActionModeCallback(this);
        editor.setOnLongClickListener(this);
        cross.setVisibility(View.GONE);
        editor.setText(mSampleModel.textContent);
        editor.setVisibility(View.VISIBLE);
        setStyleForText();
        editor.addTextChangedListener(this);
        attachViews();

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


    /**
     * attach views to existing layout based on the user selection
     */
    private void attachViews() {
        switch (mSampleModel.type) {
            case IMAGE:
                if (mSampleModel.imagePath == null) {
                    ViewManager.hideTheViews(cross, image, image);
                    mSampleModel.type = CustomAdapter.TEXT;
                } else {
                    ViewManager.showTheViews(cross, image);
                    ViewManager.hideTheViews(video);
                    cross.setTag(IMAGE);
                    image.setImageBitmap(mSampleModel.bitmap);
                    mSampleModel.videoPath = null;
                }
                break;
            case VIDEO:
                if (mSampleModel.videoPath == null) {
                    ViewManager.hideTheViews(cross, video, image);
                    mSampleModel.type = CustomAdapter.TEXT;
                } else {
                    ViewManager.showTheViews(cross, video);
                    ViewManager.hideTheViews(image);
                    cross.setTag(VIDEO);
                    mSampleModel.videoPath = "Video url:  www.video.com";
                    mSampleModel.imagePath = null;
                }
                break;
            case FILE:
                ViewManager.hideTheViews(cross, image);
                break;
            default:
                ViewManager.hideTheViews(cross, video, image);
                break;
        }
        mEditorCallbacks.dataModified(mSampleModel, position);

    }

    /**
     * on text focus change remove text selector color
     *
     * @param v
     * @param hasFocus
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            editor.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));

        }
    }

    /**
     * on view long click show selector color
     */
    @Override
    public boolean onLongClick(View v) {
        editor.setBackgroundColor(ContextCompat.getColor(mContext, R.color.text_selector));
        showStylePopUp();
        return true;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mSampleModel.textContent = editor.getText().toString();
        mEditorCallbacks.dataModified(mSampleModel, position);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

    }

    /**
     * Show style option pop up layout on top of text editor
     */
    private void showStylePopUp() {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_tip_text,
                null);
        ((TextView) view.findViewById(R.id.bold)).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.italic)).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.underline)).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.increase_text_size)).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.decrease_text_size)).setOnClickListener(this);

        mPopupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        int[] position = new int[2];
        editor.getLocationOnScreen(position);
        mPopupWindow.showAtLocation(editor, Gravity.NO_GRAVITY, position[0] + editor.getWidth(), position[1] - editor.getHeight());
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                editor.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
            }
        });
    }

    @OnClick(R.id.imageview)
    public void onImageClick(View view) {
        Intent intent = new Intent(mContext, ViewImageFullScreenActivity.class);
        intent.putExtra("list", mSampleModel.bitmap);
        mContext.startActivity(intent);
    }

    /**
     * style option's click's handled here
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bold:
                mSampleModel.isBold = !mSampleModel.isBold;
                break;
            case R.id.italic:
                mSampleModel.isItalic = !mSampleModel.isItalic;
                break;
            case R.id.underline:
                mSampleModel.isUnderLine = !mSampleModel.isUnderLine;
                break;
            case R.id.increase_text_size:
                if (mSampleModel.textSize < MAX_TEXT_SIZE)
                    mSampleModel.textSize = mSampleModel.textSize + 2;
                break;
            case R.id.decrease_text_size:
                if (mSampleModel.textSize > MIN_TEXT_SIZE)
                    mSampleModel.textSize = mSampleModel.textSize - 2;
                break;
        }
        setStyleForText();
    }

    /**
     * Based on style request from the user style attribute's will be assigned here
     */
    private void setStyleForText() {
        int value = Typeface.NORMAL;
        if (mSampleModel.isBold) {
            value = Typeface.BOLD;
        }
        if (mSampleModel.isItalic) {
            value = value | Typeface.ITALIC;
        }
        if (mSampleModel.isUnderLine) {
            editor.setPaintFlags(editor.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        } else {
            editor.setPaintFlags(0);
        }
        editor.setTypeface(null, value);
        editor.setTextSize(mSampleModel.textSize);
    }

}
