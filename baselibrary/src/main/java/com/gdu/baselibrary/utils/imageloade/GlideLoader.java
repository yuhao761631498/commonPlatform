package com.gdu.baselibrary.utils.imageloade;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

public class GlideLoader implements ILoaderStrategy {
    private RequestBuilder requestBuilder = null;

    @Override
    public void loaderImage(LoaderOptions options) {
        if (options.getContext() != null) {
            final RequestManager requestManager = Glide.with(options.getContext());
            if (options.getUrl() != null) {
                requestBuilder = requestManager.load(options.getUrl());
            } else if (options.getFile() != null) {
                requestBuilder = requestManager.load(options.getFile());
            } else if (options.getDrawableResId() != 0) {
                requestBuilder = requestManager.load(options.getDrawableResId());
            } else if (options.getUri() != null) {
                requestBuilder = requestManager.load(options.getUri());
            } else if (options.getUrl() == null) {
                requestBuilder = requestManager.load("");
            }
        } else {
            throw new NullPointerException("Context must not be null");
        }

        if (requestBuilder == null) {
            throw new NullPointerException("requestBuilder must not be null");
        }

        RequestOptions requestOptions = new RequestOptions();
        if (options.getTargetWidth() > 0 && options.getTargetHeight() > 0) {
            requestOptions = requestOptions.override(options.getTargetWidth(), options.getTargetHeight());
        }
        if (options.isCenterInside()) {
            requestOptions = requestOptions.centerInside();
        } else if (options.isCenterCrop()) {
            requestOptions = requestOptions.centerCrop();
        }
        if (options.isCircleImage()) {
            requestOptions = requestOptions.transform(new CircleCrop());
        }
        if (options.isRoundedImage()) {
            requestOptions = requestOptions.transform(new GlideRoundTransform(options.getBitmapAngle()));
        }
        if (options.getErrorResId() != 0) {
            requestOptions = requestOptions.error(options.getErrorResId());
        }
        if (options.getPlaceholderResId() != 0) {
            requestOptions = requestOptions.placeholder(options.getPlaceholderResId());
        }

        if (options.getTargetView() instanceof ImageView) {
            requestBuilder.apply(requestOptions).into(((ImageView) options.getTargetView()));
        }
    }

    @Override
    public void clearMemoryCache() {
    }

    @Override
    public void clearDiskCache() {
    }
}
