package com.leeloo.vkshare

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKTokenExpiredHandler
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet_content.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.login_view.*


class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: StateViewModel
    private lateinit var bottomSheetDialog: BottomSheetDialog

    private lateinit var target: Target<Bitmap>

    private val tokenTracker = object : VKTokenExpiredHandler {
        override fun onTokenExpired() {
            viewModel.onLogout()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)
            .get(StateViewModel::class.java)
        if (savedInstanceState == null && viewModel.viewState.value == null) {
            if (VK.isLoggedIn()) {
                viewModel.onLogin()
            } else {
                viewModel.onLogout()
            }
        }
        VK.addTokenExpiredHandler(tokenTracker)

        initViews()
        viewModel.viewState.observe(this, Observer {
            if (it.photoUri != null) {
                Glide.with(bottomSheetDialog.choosed_photo)
                    .asBitmap()
                    .load(it.photoUri)
                    .into(target)
                bottomSheetDialog.show()
            } else {
                Glide.with(bottomSheetDialog.choosed_photo)
                    .clear(bottomSheetDialog.choosed_photo)
                bottomSheetDialog.comment_edit.setText("")
                bottomSheetDialog.comment_edit.clearFocus()
                bottomSheetDialog.dismiss()
            }

            login_view.isVisible = !it.isLoggedIn
            content_main.isVisible = it.isLoggedIn

            if (it.isSended == 1)
                Snackbar.make(
                    main_activity,
                    R.string.post_share_ok,
                    Snackbar.LENGTH_SHORT
                ).show()
            else if (it.isSended == 2)
                Snackbar.make(
                    main_activity,
                    R.string.post_share_error,
                    Snackbar.LENGTH_SHORT
                ).show()
        })
    }

    private fun initViews() {
        login_button.setOnClickListener {
            VK.login(this, listOf(VKScope.WALL, VKScope.PHOTOS))
        }
        choose_photo.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }

        bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_content)
        bottomSheetDialog.dismissWithAnimation = true

        bottomSheetDialog.btn_share.setOnClickListener {
            viewModel.onSendClicked(
                bottomSheetDialog.comment_edit.text?.toString() ?: "",
                this
            )
        }
        bottomSheetDialog.bottom_sheet_exit.setOnClickListener {
            viewModel.onDismiss()
        }
        bottomSheetDialog.setOnDismissListener {
            viewModel.onDismiss()
        }
        bottomSheetDialog.setOnShowListener {
            Handler().postDelayed({
                val d = it as BottomSheetDialog
                d.behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }, 0)
        }
        target = object : CustomViewTarget<ImageView, Bitmap>(bottomSheetDialog.choosed_photo) {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                bottomSheetDialog.choosed_photo.setImageBitmap(resource)
                val heightCalculated =
                    (resource.height.toDouble()
                            / resource.width
                            * main_activity.width).toInt()
                bottomSheetDialog.choosed_photo.layoutParams.height = heightCalculated
            }

            override fun onLoadFailed(errorDrawable: Drawable?) {
            }

            override fun onResourceCleared(placeholder: Drawable?) {
                bottomSheetDialog.choosed_photo.setImageResource(0)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val callback = object : VKAuthCallback {
            override fun onLogin(token: VKAccessToken) {
                viewModel.onLogin()
            }

            override fun onLoginFailed(errorCode: Int) {

            }
        }
        if (!VK.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == 1) {
                if (resultCode == Activity.RESULT_OK)
                    viewModel.onImageChoosed(data?.data)
            }
        }
    }

}
