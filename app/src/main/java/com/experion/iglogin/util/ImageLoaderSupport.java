package com.experion.iglogin.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import androidx.annotation.Nullable;

public class ImageLoaderSupport {


    public void loadImage(Context context, String imageUrl,
                          ImageView imageView, int placeHolder, final ProgressBar progressBar) {
        if (imageUrl == null | TextUtils.isEmpty(imageUrl)) {
            try {
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.transforms(new CenterCrop(), new CircleCrop());
                requestOptions.placeholder(placeHolder);
                requestOptions.error(placeHolder);
                Glide.with(context)
                        .load(placeHolder)
                        .apply(requestOptions)
                        .thumbnail(0.1f)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                // call to the image load error event
                                progressBar.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource
                                    dataSource, boolean isFirstResource) {
                                // call to image ready event
                                progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        }).into(imageView);
            } catch (Exception e) {
                Log.e("ImageLoaderSupport", "loadImage:" + e.getMessage());
            }
        } else {
            try {

                RequestOptions requestOptions = new RequestOptions();
                requestOptions.transforms(new CenterCrop(), new CircleCrop());
                requestOptions.placeholder(placeHolder);
                requestOptions.error(placeHolder);

                Glide.with(context)
                        .load(imageUrl)
                        .apply(requestOptions)
                        .thumbnail(0.1f)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                // call to the image load error event
                                progressBar.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource
                                    dataSource, boolean isFirstResource) {
                                // call to image ready event
                                progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        }).into(imageView);
            } catch (Exception e) {
                Log.e("ImageLoaderSupport", "loadImage:" + e.getMessage());
            }
        }
    }
}
