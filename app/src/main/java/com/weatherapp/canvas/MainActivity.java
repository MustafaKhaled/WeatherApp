package com.weatherapp.canvas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;
    private Bitmap bitmap1,bitmap2;
    int BitmapSize = 30;
    int width, height;
    Canvas canvas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.image);
        imageView.setImageResource(R.drawable.icon);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setImageBitmap(drawTextToBitmap(this,imageView,"Text"));
    }


    public Bitmap drawTextToBitmap(Context gContext,
                                   View view,
                                   String gText) {
        Resources resources = gContext.getResources();
        float scale = resources.getDisplayMetrics().density;
        Bitmap bitmap =
                ((BitmapDrawable)imageView.getDrawable()).getBitmap();

        android.graphics.Bitmap.Config bitmapConfig =
                bitmap.getConfig();
        if(bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        bitmap = bitmap.copy(bitmapConfig, true);

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.rgb(61, 61, 61));
        paint.setTextSize((int) (48 * scale));
        paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);
        Rect bounds = new Rect();
        paint.getTextBounds(gText, 0, gText.length(), bounds);
        canvas.drawText(gText, view.getWidth(), view.getHeight(), paint);

        return bitmap;
    }
}
