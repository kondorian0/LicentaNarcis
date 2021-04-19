package com.narcis.neamtiu.licentanarcis.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

public class DownloadImageTask  extends AsyncTask<String, Void, Bitmap> {
    String urldisplay = null;
    Bitmap bmp = null;
    ImageView imgViewURL;

    public DownloadImageTask(ImageView bmImage) {
        this.imgViewURL = bmImage;;
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        urldisplay = urls[0];
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            bmp = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
            bmp = null;
        }
        return bmp;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        if (result != null)
            imgViewURL.setImageBitmap(result);
    }
}
