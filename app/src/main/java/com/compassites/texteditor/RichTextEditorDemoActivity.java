/***
 * Copyright (activity) 2012 CommonsWare, LLC
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.compassites.texteditor;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.commonsware.cwac.colormixer.ColorMixerActivity;
import com.commonsware.cwac.richedit.ColorPicker;
import com.commonsware.cwac.richedit.ColorPickerOperation;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RichTextEditorDemoActivity extends ImageCaptureActivity
        implements ColorPicker {

    private static final int COLOR_REQUEST = 1337;
    private static final int FILE_SELECT_CODE = 0;

    private ColorPickerOperation colorPickerOp = null;
    private List<SampleModel> sampleModels = new ArrayList<>();
    private CustomAdapter customAdapter;
    private CustomDialogClass mCustomDialogClass;

    @Bind(R.id.list)
    RecyclerView recyclerView;

    @Bind(R.id.attach)
    TextView attach;

    @Bind(R.id.send)
    TextView send;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        addInitialEditTextView();
        setAdapter();

    }

    private void addInitialEditTextView() {
        SampleModel sampleModel = new SampleModel();
        sampleModel.type = CustomAdapter.TEXT;
        sampleModels.add(sampleModel);
    }

    private void setAdapter() {
        customAdapter = new CustomAdapter(sampleModels, mEditorCallbacks);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(customAdapter);
    }

    private CustomAdapter.EditorCallbacks mEditorCallbacks = new CustomAdapter.EditorCallbacks() {
        @Override
        public void closeListener(int position, int type) {
            SampleModel model = sampleModels.get(position);
            if (model.type == CustomAdapter.IMAGE) {
                model.imagePath = null;

            } else if (model.type == CustomAdapter.VIDEO) {
                model.videoPath = null;
            }

//            if (TextUtils.isEmpty(model.textContent)) {
//            } else {
//                if (position > 0) {
//                    sampleModels.get(position - 1).textContent = sampleModels.get(position - 1).textContent + "\n\n" + model.textContent;
//                }
//            }
            sampleModels.remove(position);
            customAdapter.updateData(sampleModels);
            customAdapter.notifyDataSetChanged();
        }

        @Override
        public void dataModified(SampleModel sampleModel, int pos) {
            sampleModels.set(pos, sampleModel);
            customAdapter.updateData(sampleModels);
        }
    };

    @Override
    public boolean pick(ColorPickerOperation op) {
        Intent i = new Intent(this, ColorMixerActivity.class);
        i.putExtra(ColorMixerActivity.TITLE, "Pick a Color");
        if (op.hasColor()) {
            i.putExtra(ColorMixerActivity.COLOR, op.getColor());
        }
        this.colorPickerOp = op;
        startActivityForResult(i, COLOR_REQUEST);

        return (true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent result) {
        int color = 0;

        if (colorPickerOp != null && requestCode == COLOR_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                color = result.getIntExtra(ColorMixerActivity.COLOR, 0);
            }

            if (color == 0) {
                colorPickerOp.onPickerDismissed();
            } else {
                colorPickerOp.onColorPicked(color);
            }

            colorPickerOp = null;
        }
    }

    @Override
    public void getSelectedPhotoFromGalleryPath(ArrayList<SelectionImageModel> uris) {

    }

    @Override
    public void getSelectedPhotoPath(SelectionImageModel path) {
        SampleModel sampleModel1 = new SampleModel();
        sampleModel1.type = CustomAdapter.IMAGE;
        sampleModel1.imagePath = path.getFilePath();
        sampleModel1.bitmap=path.getBitmap();
        sampleModels.add(sampleModel1);
        customAdapter.updateData(sampleModels);
        customAdapter.notifyItemInserted(sampleModels.size() - 1);
    }


    @OnClick(R.id.attach)
    public void onAttachClick() {
        if (mCustomDialogClass == null) {
            mCustomDialogClass = new CustomDialogClass(RichTextEditorDemoActivity.this);
            mCustomDialogClass.setListener(mClickListener);
        }
        mCustomDialogClass.show();
    }
    @OnClick(R.id.send)
    public void onSendClick() {
       printData();
    }
    private CustomDialogClass.ClickListener mClickListener = new CustomDialogClass.ClickListener() {
        @Override
        public void onImageClick() {
            SampleModel sampleModel1 = new SampleModel();
            sampleModel1.type = CustomAdapter.IMAGE;
            sampleModel1.imagePath ="image";
            sampleModels.add(sampleModel1);
            customAdapter.updateData(sampleModels);
            customAdapter.notifyItemInserted(sampleModels.size() - 1);
        }

        @Override
        public void onFileClick() {
            showFileChooser();
        }

        @Override
        public void onVideoClick() {
            SampleModel sampleModel1 = new SampleModel();
            sampleModel1.type = CustomAdapter.VIDEO;
            sampleModel1.videoPath = "video";
            sampleModels.add(sampleModel1);
            customAdapter.updateData(sampleModels);
            customAdapter.notifyItemInserted(sampleModels.size() - 1);
        }
    };

    private void printData() {
        List<SampleModel> sampleModels = customAdapter.getData();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Main content  " + sampleModels.get(0).textContent + "/\n");
        for (int i = 1; i < sampleModels.size(); i++) {
            SampleModel sampleModel = sampleModels.get(i);
            if (sampleModel.imagePath != null) {
                stringBuilder.append(sampleModel.imagePath + "/\n");
            }
            if (sampleModel.videoPath != null) {
                stringBuilder.append(sampleModel.videoPath + "/\n");
            }
            if (!TextUtils.isEmpty(sampleModel.textContent)) {
                stringBuilder.append(sampleModel.textContent + "/\n");
            }
        }
        Log.d("LOGTAG", "Content:   " + stringBuilder.toString());
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }
    private static final int PERMISSION_REQUEST_CODE = 1;
//    private static final int PERMISSION_REQUEST_CODE1 = 2;

    private void requestPermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)){

            Toast.makeText(this,"GPS permission allows us to access location data. Please allow in App Settings for additional functionality.",Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},PERMISSION_REQUEST_CODE);
        }

//        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
//
//            Toast.makeText(this,"GPS permission allows us to access location data. Please allow in App Settings for additional functionality.",Toast.LENGTH_LONG).show();
//
//        } else {
//
//            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE1);
//        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

//                    Snackbar.make(view,"Permission Granted, Now you can access location data.",Snackbar.LENGTH_LONG).show();

                } else {

//                    Snackbar.make(view,"Permission Denied, You cannot access location data.",Snackbar.LENGTH_LONG).show();

                }
                break;
        }
    }
}
