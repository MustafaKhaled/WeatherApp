package com.weatherapp.canvas.util;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileHelper {
    private static final int sEOF = -1;
    private static final int sDEFAULT_BUFFER_SIZE = 1024 * 4;

    public static File createImageFile(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);;
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return image;
    }

    public static File prepareFile(@NonNull Context context, @Nullable Uri uri) throws IOException {
        if (uri == null) return null;
        final ContentResolver contentResolver = context.getContentResolver();
        InputStream inputStream = contentResolver.openInputStream(uri);
        File file = createImageFile(context);
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

    private static void copy(InputStream input, OutputStream output) throws IOException {
        int n;
        byte[] buffer = new byte[sDEFAULT_BUFFER_SIZE];
        while (sEOF != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
    }
}
