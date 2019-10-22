package com.weatherapp.canvas.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileHelper {
    private static final String TAG = "FileHelper";
    private static final int sEOF = -1;
    private static final int sDEFAULT_BUFFER_SIZE = 1024 * 4;
    public static File createImageFile(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp;
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);;
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return image;
    }

    public static Bitmap createBitmapFromFile(String url){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap =  BitmapFactory.decodeFile(url);
        return bitmap;
    }

    public static File createFileFromBitmap(Bitmap bitmap,Context context) throws IOException {
        File f = createImageFile(context);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100 , bos);
        byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(bitmapdata);
        fos.flush();
        fos.close();

        return f;
    }

    public static Bitmap drawTextToBitmap(Context gContext,
                                   Bitmap bitmap,
                                   String gText) {
        Resources resources = gContext.getResources();
        float scale = resources.getDisplayMetrics().density;

        Bitmap.Config bitmapConfig =
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
        paint.setShadowLayer(1F,0F,1F,Color.BLACK);
        paint.setColor(Color.rgb(255, 255, 255));
        paint.setTextSize((int) (50 * scale));
        paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);
        Rect bounds = new Rect();
        paint.getTextBounds(gText, 0, gText.length(), bounds);
        canvas.drawText(gText, 200, 200, paint);

        return bitmap;
    }

    public static File getFileFromUri(@NonNull Context context, @Nullable Uri uri) {
        try {
            return FileHelper.from(context, uri);
        } catch (IOException ignored) {
        }
        return null;
    }

    public static File from(@NonNull Context context, @Nullable Uri uri) throws IOException {
        if (uri == null) return null;
        final ContentResolver contentResolver = context.getContentResolver();
        InputStream inputStream = contentResolver.openInputStream(uri);
        File file = createCacheFile(context, uri);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
        } catch (FileNotFoundException ignored) {
        }
        if (inputStream != null) {
            copy(inputStream, out);
            inputStream.close();
        }

        if (out != null) {
            out.close();
        }
        return file;
    }
    @Nullable
    public static File createCacheFile(@NonNull Context context, @NonNull Uri uri) {
        return createCacheFile(context);
    }

    private static void copy(InputStream input, OutputStream output) throws IOException {
        int n;
        byte[] buffer = new byte[sDEFAULT_BUFFER_SIZE];
        while (sEOF != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
    }

    @Nullable
    public static File createCacheFile(@NonNull Context context) {
        String prefix = "temp";
        final File dir = context.getFilesDir();
        File file = null;
        try {
            file = File.createTempFile(prefix,
                    "." + "jpg",
                    dir);
        } catch (IOException ignored) {
        }
        return file;
    }

}
