package com.ysapps.tools.searchpckages;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class BannerAd {
    static MyXListener ml;

   /* *//**
     *
     * @param ctx The activity where banner should be display
     * @param textUrl URL where the text file hosted.
     * @param bannerUrl URL where the banner resource hosted.
     *//*
    public BannerAd(Activity ctx, String textUrl, String bannerUrl){
        this.ctx = ctx;
        new LoadBunner().execute(textUrl, bannerUrl);
    }*/

    /**
     *
     * @param ctx The activity where banner should be display
     * @param fileUrl URL where the text file hosted.
     */
    public static void openBanner(Context ctx, String fileUrl){
        ml = (MyXListener) ctx;
        new LoadBunner().execute(ctx, fileUrl);
    }

    private static class LoadBunner extends AsyncTask<Object, Void, RelativeLayout> {
        Activity ctx;

        @Override
        protected RelativeLayout doInBackground(Object... params) {
            this.ctx = (Activity) params[0];
            Bitmap bitmap;
            ImageView banner = new ImageView(ctx);
            String bannerUrl , marketUrl;
            ArrayList<String> lines = new ArrayList<>();
            String packageName;
            PackageManager pm = ctx.getPackageManager();
            try {
                java.net.URL url = new java.net.URL((String) params[1]);
                BufferedReader fileBuffer = new BufferedReader(new InputStreamReader(url.openStream()));
                String line = fileBuffer.readLine();
                if (line.equals("0"))
                    return null; //We don't perform the procedure
                while ((line = fileBuffer.readLine()) != null){
                    lines.add(line);
                }
                fileBuffer.close();
                packageName = lines.get(0);
                int orientation =  ctx.getResources().getConfiguration().orientation;
                if (pm.getLaunchIntentForPackage(packageName) == null){ //The app isn't installed in device
                    if (orientation == Configuration.ORIENTATION_PORTRAIT)
                        bannerUrl = lines.get(2);
                    else
                        bannerUrl = lines.get(3);
                    marketUrl = lines.get(1);
                } else { //The app installed in device
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE)
                        bannerUrl = lines.get(5);
                    else
                        bannerUrl = lines.get(6);
                    marketUrl = lines.get(4);
                }
                /*if (orientation == Configuration.ORIENTATION_PORTRAIT)
                    ctx.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                else
                    ctx.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);*/
                InputStream bitmapBuffer = new java.net.URL(bannerUrl).openStream();
                bitmap = BitmapFactory.decodeStream(bitmapBuffer);
                bitmapBuffer.close();
                if (bitmap == null)
                    return null;
                RelativeLayout container;

                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                ImageView x = new ImageView(ctx);
                x.setImageDrawable(ctx.getResources().getDrawable(R.drawable.the_x));
                RelativeLayout.LayoutParams xParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                xParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT | RelativeLayout.ALIGN_PARENT_TOP);
                x.setLayoutParams(xParams);
                banner.setImageBitmap(bitmap);
                banner.setScaleType(ImageView.ScaleType.FIT_XY);
                banner.setLayoutParams(layoutParams);
                banner.setTag(marketUrl);
                container = new RelativeLayout(ctx);
                container.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                container.addView(banner);
                container.addView(x);
                container.bringToFront();
                View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bannerClicked(v);
                    }
                };
                banner.setOnClickListener(listener);
                x.setOnClickListener(listener);
                return container;


            } catch (IOException  | IndexOutOfBoundsException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(RelativeLayout container) {
            if (container != null)
                ((ViewGroup)(ctx.getWindow().getDecorView().findViewById(android.R.id.content))).addView(container);

        }
    }

    public static  void bannerClicked(final View v){
        final RelativeLayout container = (RelativeLayout) v.getParent();
        Object tag = v.getTag();
        final Activity ctx = (Activity) v.getContext();
        if (tag != null) {
            String url = (String) v.getTag();
            Uri uri = Uri.parse(url);
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            try {
                ctx.startActivity(goToMarket);
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        container.removeAllViews();
                        ((ViewGroup)(ctx.getWindow().getDecorView().findViewById(android.R.id.content))).removeView(container);
                    }
                }, 1000);
            } catch (ActivityNotFoundException e) {
                container.removeAllViews();
                ((ViewGroup)(ctx.getWindow().getDecorView().findViewById(android.R.id.content))).removeView(container);
                e.printStackTrace();
            }
        } else {
            ml.onXClicked();
            container.removeAllViews();
            ((ViewGroup)(ctx.getWindow().getDecorView().findViewById(android.R.id.content))).removeView(container);

        }
    }

    public interface MyXListener {
        void onXClicked();
    }
}