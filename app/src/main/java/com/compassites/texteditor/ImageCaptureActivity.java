package com.compassites.texteditor;

import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by harshitha
 */
public abstract class ImageCaptureActivity extends AppCompatActivity {
    public static final int REQUEST_TAKE_PHOTO = 1;
    public static final int REQUEST_FROM_GALLERY = 2;
    public String mCurrentPhotoPath;

    public void uploadImageOrVideo(int flag) {
        switch (flag) {
            case REQUEST_TAKE_PHOTO:
                launchCamera();
                break;
            case REQUEST_FROM_GALLERY:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),
                        REQUEST_FROM_GALLERY);
                break;
        }
    }


    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        Log.e("timestamp", timeStamp);
        String imageFileName = "JPEG_" + timeStamp + "_";
        Log.e("image", imageFileName);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (!storageDir.exists()) {
            storageDir.mkdirs();
            storageDir.createNewFile();
        }
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Save a file: path for use with ACTION_VIEW intents
        if (image != null)
            mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void launchCamera() {


        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //startActivityForResult(Utility.pictureActionIntent, Utility.CAMERA_PICTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            //Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            if (requestCode == REQUEST_TAKE_PHOTO) {
                // Save Image To Gallery
                if (mCurrentPhotoPath != null) {
                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    File f = new File(mCurrentPhotoPath);
                    Uri contentUri = Uri.fromFile(f);
                    Log.d("File name", "File Path" + f.getAbsolutePath());

                    mediaScanIntent.setData(contentUri);
                    this.sendBroadcast(mediaScanIntent);
                    beginCrop(contentUri);
                } else {
                    Toast.makeText(this, "Problem with this image", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else if (requestCode == REQUEST_FROM_GALLERY) {

                ArrayList<SelectionImageModel> galleryImageUrls = new ArrayList<>();
                ClipData clipData = data.getClipData();
                if (data.getData() == null && clipData != null) {
                    Log.d("From Gallery", "Data Part" + clipData.getItemCount());

                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        Uri uri = clipData.getItemAt(i).getUri();
                        Log.d("Normal Data", "Uri Info :" + uri.toString());
                        //File myFile = new File(uri.toString());
                        SelectionImageModel galleryImageUrl = AddPhotosinfo(uri);
                        if (galleryImageUrl != null) {
                            galleryImageUrls.add(galleryImageUrl);
                        }
                        //Log.d("Real Path", "RealPath :"+myFile.getAbsolutePath());

                    }

                } else {
                    SelectionImageModel galleryImageUrl = AddPhotosinfo(data.getData());
                    if (galleryImageUrl == null) {
                        return;
                    }
                    galleryImageUrls.add(galleryImageUrl);
                }
                if (galleryImageUrls.size() >= 1) {
                    getSelectedPhotoFromGalleryPath(galleryImageUrls);
                }

            } else if (requestCode == Crop.REQUEST_CROP) {
                handleCrop();
            }


        }

    }

    public Bitmap getBitmap(File currentbitmap) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inSampleSize = 1;
        Bitmap bitmap = BitmapFactory.decodeFile(currentbitmap.getAbsolutePath(), bmOptions);
        bitmap = Bitmap.createScaledBitmap(bitmap, 350, 400, true);

        //imageView.setImageBitmap(bitmap);
        return bitmap;

    }

    private void handleCrop() {
        if (mCurrentPhotoPath != null) {
            File f = new File(mCurrentPhotoPath);
            SelectionImageModel localImageUrl = new SelectionImageModel();
            localImageUrl.setBitmap(getBitmap(f));
            localImageUrl.setFilePath(mCurrentPhotoPath);
            getSelectedPhotoPath(localImageUrl);
        }
    }

    private void beginCrop(Uri source) {
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Crop.of(source, Uri.fromFile(photoFile)).asSquare().start(this);
    }

    public SelectionImageModel AddPhotosinfo(Uri mUriInfo) {
        SelectionImageModel galleryImageUrl = new SelectionImageModel();

        try {


            String[] filePathColumn = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA};
            Cursor cursor = null;

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                String wholeID = DocumentsContract.getDocumentId(mUriInfo);

                // Split at colon, use second item in the array
                String idInfo = wholeID.split(":")[1];
                // where id is equal to
                String sel = MediaStore.Images.Media._ID + "=?";
                cursor = getContentResolver().query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        filePathColumn, sel, new String[]{idInfo}, null);


            } else {
                cursor = getContentResolver().query(
                        mUriInfo, filePathColumn, null, null, null);
            }

            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[1]);
            String filePath = cursor.getString(columnIndex);
            long id = cursor.getLong(cursor.getColumnIndex(filePathColumn[0]));
            Log.d("The ID :", "ID DATA:" + id);
            Bitmap currentThumb = MediaStore.Images.Thumbnails.getThumbnail(
                    getApplicationContext().getContentResolver(), id,
                    MediaStore.Images.Thumbnails.MICRO_KIND, null);
            if (currentThumb == null) {
                return null;
            }
            galleryImageUrl.setBitmap(currentThumb);
            galleryImageUrl.setFilePath(filePath);
            cursor.close();
        } catch (Exception ex) {
            Log.d("Exception Occured", "Error Ocuured");
            return null;
        }
        return galleryImageUrl;

    }


    public abstract void getSelectedPhotoFromGalleryPath(ArrayList<SelectionImageModel> uris);

    public abstract void getSelectedPhotoPath(SelectionImageModel path);

}
