package com.example.lost_animals.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.lost_animals.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;



public class AddPostActivity extends Activity {
    public static String START_BY_CAMERA = "START_BY_CAMERA";
    public static String START_BY_GALLERY = "START_BY_GALLERY";
    private ImageView post_image;
    private EditText etDescription;
    private boolean fromCamera = true;
    private Button choose_location_button;
    private Uri selectedImage;
    private String picturePath;
    private View.OnClickListener on_choose_location_button_click_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(AddPostActivity.this,ChooseLocationActivity.class);
            intent.putExtra(ChooseLocationActivity.DESCRIPTION_KEY, etDescription.getText().toString());
            BitmapDrawable drawable = (BitmapDrawable) post_image.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            intent.putExtra(ChooseLocationActivity.CAMERA_KEY,fromCamera);
            if (fromCamera)
                intent.putExtra(ChooseLocationActivity.IMAGE_KEY, bitmap);
            else{
                intent.putExtra(ChooseLocationActivity.IMAGE_KEY, picturePath);
            }
            startActivityForResult(intent, 1);

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {return;}
        String name = data.getStringExtra(ChooseLocationActivity.RESULT_CODE);
        if (name.equals("exit")){
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        post_image = findViewById(R.id.post_image);
        choose_location_button = findViewById(R.id.bt_choose_location);
        choose_location_button.setOnClickListener(on_choose_location_button_click_listener);
        etDescription = findViewById(R.id.et_description);


        Bundle extras = getIntent().getExtras();
        String key = extras.getString("KEY");
        if(key.equals(START_BY_CAMERA)){
            Bitmap thumbnail = (Bitmap) extras.get("Bitmap");
            post_image.setImageBitmap(thumbnail);
        }else if (key.equals(START_BY_GALLERY)){
            selectedImage = (Uri) extras.get("Bitmap_uri");
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
            assert c != null;
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePath[0]);
            picturePath = c.getString(columnIndex);
            c.close();

            Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
            post_image.setImageBitmap(thumbnail);
            fromCamera = false;
        }

    }

}
