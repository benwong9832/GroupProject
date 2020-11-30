package edu.hkbu.comp4097.groupproject.ui

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, url: String) {
    if (!url.isNullOrBlank()) {
        Picasso.get().load(url).into(view)
    }
}