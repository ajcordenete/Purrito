package com.aljon.purrito.ui.feeddetail

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.aljon.purrito.EventObserver
import com.aljon.purrito.R
import com.aljon.purrito.databinding.FeedDetailFragmentBinding
import com.aljon.purrito.ui.MainActivity
import com.aljon.purrito.util.Constants
import com.aljon.purrito.util.MediaHelper
import com.aljon.purrito.util.autoCleared
import com.aljon.purrito.view.showAlertDialog
import com.aljon.purrito.view.showSnackbar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer
import javax.inject.Inject


class FeedDetailFragment: DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<FeedDetailViewModel> { viewModelFactory }

    private var binding by autoCleared<FeedDetailFragmentBinding>()

    private val args by navArgs<FeedDetailFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FeedDetailFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner

        binding.viewModel = viewModel

        //needed to make sure toolbar is showing as it can be collapsed because of scrolling on previous fragment...
        (activity as MainActivity).expandToolbar()

        setHasOptionsMenu(true)
        initImageTransition()

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpMenuActions()
        viewModel.initFeed(args.id, args.url)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.feed_detail_menu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val favoriteMenuItem = menu.findItem(R.id.favorite)

        if(viewModel.isFavorite.value == true) {
            favoriteMenuItem.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_favorite)
        } else {
            favoriteMenuItem.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_favorite_outline)
        }
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.favorite -> viewModel.toggleFavorite()
            R.id.share -> viewModel.shareFeed()
            R.id.download -> viewModel.downloadFeed()
        }
        return false
    }

    private fun setUpMenuActions() {
        viewModel.isFavorite.observe(viewLifecycleOwner, Observer { isFavorite ->
            (activity as MainActivity).invalidateOptionsMenu()
        })

        viewModel.shareEvent.observe(viewLifecycleOwner, EventObserver {
            performShare(it)
        })

        viewModel.downloadEvent.observe(viewLifecycleOwner, EventObserver {
            performDownloadWithPermissionCheck(it)
        })
    }

    private fun performShare(url: String) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, url)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    private fun initImageTransition() {
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(R.transition.image_shared_element_transition)

        postponeEnterTransition()
        binding.imageRequestListener = object: RequestListener<Drawable> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                startPostponedEnterTransition()
                return false
            }

            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                binding.feedImage.setImageDrawable(resource)
                startPostponedEnterTransition()
                return false
            }
        }
    }

    private fun performDownloadWithPermissionCheck(url: String) {
        if (ContextCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request the permission
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                Constants.PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE)

        } else {
            // Permission has already been granted, proceed to download
            performDownload(url)
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode) {
            Constants.PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    viewModel.downloadFeed()

                } else {
                    if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        //show explanation to the user
                    } else {
                        //Don't show again is checked... Guide the user on how to enable the permission again
                        showPermissionNotGrantedInfo()
                    }
                }
                return

            }
        }
    }

    private fun performDownload(url: String) {
        if(MediaHelper.isGif(url)) {
            Glide.with(this).asGif().load(url).into(object : CustomTarget<GifDrawable>() {
                override fun onResourceReady(resource: GifDrawable, transition: Transition<in GifDrawable>?) {
                    saveFile(MediaHelper.getBytesFromGifDrawable(resource), Constants.MIME_TYPE_GIF, MediaHelper.getFileExtension(url))
                }
                override fun onLoadCleared(placeholder: Drawable?) {}
            })

        } else {
            Glide.with(this).asBitmap().load(url).into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    saveFile(MediaHelper.getBytesFromBitmap(resource), Constants.MIME_TYPE_JPG, MediaHelper.getFileExtension(url))
                }
                override fun onLoadCleared(placeholder: Drawable?) {}
            })
        }
    }

    private fun saveFile(byteArray: ByteArray, mime_type: String, file_extension: String) {
        var message = ""
        val success = MediaHelper.saveFile(requireContext(), byteArray, mime_type, file_extension)

        if(success) {
            message = Constants.DOWNLOAD_COMPLETE
        } else {
            message = Constants.DOWNLOAD_FAILED
        }
        view?.showSnackbar(message, Snackbar.LENGTH_SHORT)
    }

    private fun showPermissionNotGrantedInfo() {
        view?.showAlertDialog(R.string.permission_dialog_title, R.string.permission_dialog_message, R.string.settings_label, ::navigateToAppDeviceSettings)
    }

    private fun navigateToAppDeviceSettings() {
        context?.let {
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS;
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.data = Uri.parse("package:" + it.packageName);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            it.startActivity(intent);
        }
    }
}