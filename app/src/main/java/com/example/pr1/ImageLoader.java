package com.example.pr1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

public class ImageLoader {
    public static Bitmap loadProfileIcon(int userId) {
        try {
            String url = "http://10.0.2.2:5184/api/Users/ProfileIcon/" + userId;

            Connection.Response response = Jsoup.connect(url)
                    .ignoreContentType(true)
                    .timeout(15000)
                    .execute();

            if (response.statusCode() == 200) {
                byte[] bytes = response.bodyAsBytes();
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            } else if (response.statusCode() == 202) {
                Log.i("ImageLoader", "Icon is generating for user " + userId);
            } else if (response.statusCode() == 404) {
                Log.i("ImageLoader", "No icon for user " + userId);
            }
        } catch (IOException e) {
            Log.e("ImageLoader", "Error: " + e.getMessage());
        }
        return null;
    }

    public static void loadProfileIconAsync(int userId, ImageView imageView) {
        new AsyncTask<Integer, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Integer... params) {
                return loadProfileIcon(params[0]);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (bitmap != null && imageView != null) {
                    imageView.setImageBitmap(bitmap);
                } else if (imageView != null) {
                    imageView.setImageResource(R.drawable.er);
                }
            }
        }.execute(userId);
    }
}
