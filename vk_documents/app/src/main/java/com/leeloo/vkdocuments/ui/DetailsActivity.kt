package com.leeloo.vkdocuments.ui

import android.app.Activity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.leeloo.vkdocuments.R
import kotlinx.android.synthetic.main.activity_details.*


class DetailsActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        arrow_back.setOnClickListener {
            finish()
        }
        if (intent.extras?.getBoolean("isGif") == true)
            Glide.with(this)
                .asGif()
                .load(intent.extras?.getString("url"))
                .into(image_details)
        else
            Glide.with(this)
                .load(intent.extras?.getString("url"))
                .into(image_details)
    }
}