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
//        createBitmap();
////        getBitmapWidthHeight();
//
//        bitmap2 = Bitmap.createBitmap(
//                30,
//                30,
//                Bitmap.Config.RGB_565
//        );
//
//        drawCanvas();
//
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setImageBitmap(drawTextToBitmap(this,imageView,"Text"));
    }

//    public void createBitmap(){
//        bitmap1 = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
//    }
//
//
//    public void getBitmapWidthHeight(){
//
//        width = bitmap1.getWidth() + BitmapSize * 2;
//        height = bitmap1.getHeight() + BitmapSize * 2;
//
//    }
//    public void drawCanvas(){
//
//        canvas = new Canvas(bitmap2 );
//
//        canvas.drawColor(Color.CYAN);
//
//        canvas.drawBitmap(
//                bitmap2,
//                5,
//                5,
//                null
//        );
//
//    }

    public Bitmap drawTextToBitmap(Context gContext,
                                   View view,
                                   String gText) {
        Resources resources = gContext.getResources();
        float scale = resources.getDisplayMetrics().density;
        Bitmap bitmap =
                ((BitmapDrawable)imageView.getDrawable()).getBitmap();

        android.graphics.Bitmap.Config bitmapConfig =
                bitmap.getConfig();
        // set default bitmap config if none
        if(bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        bitmap = bitmap.copy(bitmapConfig, true);

        Canvas canvas = new Canvas(bitmap);
        // new antialised Paint
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // text color - #3D3D3D
        paint.setColor(Color.rgb(61, 61, 61));
        // text size in pixels
        paint.setTextSize((int) (48 * scale));
        // text shadow
        paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);

        // draw text to the Canvas center
        Rect bounds = new Rect();
        paint.getTextBounds(gText, 0, gText.length(), bounds);
        int x = (bitmap.getWidth() - bounds.width())/2;
        int y = (bitmap.getHeight() + bounds.height())/2;

        canvas.drawText(gText, view.getWidth(), view.getHeight(), paint);

        return bitmap;
    }
}
