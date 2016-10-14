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
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RichTextEditorDemoActivity extends ImageCaptureActivity {

    private static final int FILE_SELECT_CODE = 0;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int PERMISSION_REQUEST_CODE1 = 2;

    private List<SampleModel> sampleModels = new ArrayList<>();
    private CustomAdapter customAdapter;
    private CustomDialogClass mCustomDialogClass;

    @Bind(R.id.list)
    RecyclerView list;

    @Bind(R.id.attach)
    ImageView attach;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        addInitialEditTextView();
        requestPermission();
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
        list.setLayoutManager(mLayoutManager);
        list.setItemAnimator(new DefaultItemAnimator());
        list.setAdapter(customAdapter);

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

            if (TextUtils.isEmpty(sampleModels.get(position).textContent)) {
                sampleModels.remove(position);
            } else {
                SampleModel sampleModel = sampleModels.get(position);
                sampleModel.imagePath = null;
                sampleModels.set(position, sampleModel);
            }
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
    public void getSelectedPhotoFromGalleryPath(ArrayList<SelectionImageModel> uriList) {
        SelectionImageModel selectionModel = uriList.get(0);
        if(selectionModel.getRequestCode()==REQUEST_IMAGE_FROM_GALLERY) {
            SampleModel sampleModel1 = new SampleModel();
            sampleModel1.type = CustomAdapter.IMAGE;
            sampleModel1.imagePath = selectionModel.getFilePath();
            sampleModel1.bitmap = selectionModel.getBitmap();
            sampleModels.add(sampleModel1);
            customAdapter.updateData(sampleModels);
            customAdapter.notifyItemInserted(sampleModels.size() - 1);
        }else {
            SampleModel sampleModel1 = new SampleModel();
            sampleModel1.type = CustomAdapter.IMAGE;
            sampleModel1.videoPath = selectionModel.getFilePath();
            sampleModels.add(sampleModel1);
            customAdapter.updateData(sampleModels);
            customAdapter.notifyItemInserted(sampleModels.size() - 1);
        }
    }

    @Override
    public void getSelectedPhotoPath(SelectionImageModel path) {

    }


    @OnClick(R.id.attach)
    public void onAttachClick() {
        if (mCustomDialogClass == null) {
            mCustomDialogClass = new CustomDialogClass(RichTextEditorDemoActivity.this);
            mCustomDialogClass.setListener(mClickListener);
        }
        mCustomDialogClass.show();
    }


    private CustomDialogClass.ClickListener mClickListener = new CustomDialogClass.ClickListener() {
        @Override
        public void onImageClick() {
            uploadImageOrVideo(REQUEST_IMAGE_FROM_GALLERY);
        }

        @Override
        public void onFileClick() {
            showFileChooser();
        }

        @Override
        public void onVideoClick() {
//            SampleModel sampleModel1 = new SampleModel();
//            sampleModel1.type = CustomAdapter.VIDEO;
//            sampleModel1.videoPath = "video";
//            sampleModels.add(sampleModel1);
//            customAdapter.updateData(sampleModels);
//            customAdapter.notifyItemInserted(sampleModels.size() - 1);
            uploadImageOrVideo(REQUEST_VIDEO_FROM_GALLERY);
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
        Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()
        );
        intent.setDataAndType(uri, "text/*");
        startActivityForResult(Intent.createChooser(intent, "Open folder"), FILE_SELECT_CODE);
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            Toast.makeText(this, "Camera permission allows us to access location data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    PERMISSION_REQUEST_CODE);
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == FILE_SELECT_CODE) {
            Log.d("LOGTAG", "success");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        Toast.makeText(this, "Storage permission allows us to access location data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
                    } else {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE1);
                    }
                    Toast.makeText(this, "Camera permission granted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Camera permission Denied", Toast.LENGTH_LONG).show();

                }
                break;
            case PERMISSION_REQUEST_CODE1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Storage permission granted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Storage permission Denied", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }

}
