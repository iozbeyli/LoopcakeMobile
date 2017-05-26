package com.loopcake.loopcakemobile.AsyncCommunication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.loopcake.loopcakemobile.Session;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by MEHMET on 26.05.2017.
 */

public class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {

    private final WeakReference<ImageView> imageViewReference;
    //private final MemoryCache memoryCache;
    //private final BrandItem brandCatogiriesItem;
    private Context context;
    private String url;

    public ImageDownloaderTask(ImageView imageView, String url, Context context) {
        imageViewReference = new WeakReference<ImageView>(imageView);
        //memoryCache = new MemoryCache();
        //brandCatogiriesItem = new BrandItem();
        this.url = url;
        this.context = context;
    }

    @Override
    protected Bitmap doInBackground(String... params) {

        return downloadBitmap(url);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }

        if (imageViewReference != null) {
            ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                if (bitmap != null) {
                    //memoryCache.put("1", bitmap);
                    //brandCatogiriesItem.setUrl(url);
                    //brandCatogiriesItem.setThumb(bitmap);
                    // BrandCatogiriesItem.saveLocalBrandOrCatogiries(context, brandCatogiriesItem);
                    imageView.setImageBitmap(bitmap);
                } else {
                    /*Drawable placeholder = imageView.getContext().getResources().getDrawable(R.drawable.placeholder);
                    imageView.setImageDrawable(placeholder);*/
                }
            }

        }
    }

    private Bitmap downloadBitmap(String url) {
        HttpURLConnection urlConnection = null;
        try {
            URL uri = new URL(url);
            urlConnection = (HttpURLConnection) uri.openConnection();

            int statusCode = urlConnection.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {
                return null;
            }

            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream != null) {

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
        } catch (Exception e) {
            Log.d("URLCONNECTIONERROR", e.toString());
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            Log.w("ImageDownloader", "Error downloading image from " + url);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();

            }
        }
        return null;
    }
}