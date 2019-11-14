package com.weatherapp.canvas.ui.details;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.weatherapp.canvas.R;

public class FullImageActivity extends AppCompatActivity {
    private ImageView imageView;
    private Uri pictureFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.image);

        pictureFile = (Uri) getIntent().getExtras().get("image");
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
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/html");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setDataAndType(uri, "image/*");
        startActivity(Intent.createChooser(shareIntent, "Share"));
    }
}
