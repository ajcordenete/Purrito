package com.aljon.purrito.view

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions

@BindingAdapter("imageUrl")
fun bindImageUrl(imageView: ImageView, url: String?) {
    url?.let {
        Glide.with(imageView.context)
            .load(url)
            .into(imageView)
    }
}

@BindingAdapter("imageUrl", "requestListener")
fun bindImageUrlWithTransition(imageView: ImageView, imageUrl: String?, requestListener: RequestListener<Drawable>?) {
    imageUrl?.let {
        Glide.with(imageView.context)
            .load(imageUrl)
            .listener(requestListener)
            .into(imageView)
    }
}