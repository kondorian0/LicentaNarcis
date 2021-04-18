package com.narcis.neamtiu.licentanarcis.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ViewTarget;
import com.narcis.neamtiu.licentanarcis.firestore.FirestoreManager;
import com.narcis.neamtiu.licentanarcis.models.EventData;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

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
