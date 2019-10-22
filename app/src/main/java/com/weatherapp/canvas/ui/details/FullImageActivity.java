package com.weatherapp.canvas.ui.details;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.weatherapp.canvas.R;
import com.weatherapp.canvas.util.FileHelper;

import java.io.File;

public class FullImageActivity extends AppCompatActivity {
    private static final String TAG = "FullImageActivity";
    private ImageView imageView;
    private Uri pictureFile;
    private Bitmap bitmap1,bitmap2;
    int BitmapSize = 30;
    int width, height;
    Canvas canvas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.image);

        Uri pictureFile = (Uri) getIntent().getExtras().get("image");
        imageView.setImageURI(pictureFile);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.share){
            share(pictureFile);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void share(Uri uri){
//        Drawable mDrawable = mImageView.getDrawable();
//        Bitmap mBitmap = ((BitmapDrawable) mDrawable).getBitmap();
//
//        String path = MediaStore.Images.Media.insertImage(getContentResolver(), mBitmap, "Image Description", null);
//        Uri uri = Uri.parse(path);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(intent, "Share Image"));
    }
}
