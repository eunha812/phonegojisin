package com.ssafy.phonesin.ui.module.camera

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.ssafy.phonesin.R
import com.ssafy.phonesin.databinding.FragmentCameraBinding
import com.ssafy.phonesin.databinding.FragmentFrameBinding
import com.ssafy.phonesin.ui.util.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class FrameFragment : BaseFragment<FragmentFrameBinding>(
    R.layout.fragment_frame
) {

    private val viewModel by activityViewModels<CameraViewModel>()
    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFrameBinding {
        return FragmentFrameBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@FrameFragment.viewModel
        }
    }

    override fun init() {
        val imagePath = arguments?.getString("imagePath")
        val cameraFacing = arguments?.getString("cameraFacing")
        val imageView = bindingNonNull.imageView
        val saveButton = bindingNonNull.textViewTitle

        val originalBitmap = BitmapFactory.decodeFile(imagePath)
        Log.d("FrameFragment", "Original Bitmap: $originalBitmap")

        val rotationDegrees = if (cameraFacing == "FRONT") 270f else 90f
        val matrix = Matrix().apply { postRotate(rotationDegrees) }

        val rotatedBitmap = Bitmap.createBitmap(
            originalBitmap,
            0,
            0,
            originalBitmap.width,
            originalBitmap.height,
            matrix,
            true
        )

        imageView.setImageBitmap(rotatedBitmap)

        saveButton.setOnClickListener {
            val viewFrame = bindingNonNull.rootView
            val frameBitmap = layoutToBitmap(viewFrame)

            frameBitmap?.let {
                val timeStamp =
                    SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                val storageDir =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                val imageFile = File(storageDir, "IMG_$timeStamp.jpg")

                try {
//                    val fos = FileOutputStream(imageFile)
//                    frameBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
//                    fos.flush()
//                    fos.close()
//
//                    requireContext().sendBroadcast(
//                        Intent(
//                            Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
//                            imageFile.toUri()
//                        )
//                    )
                    Log.d("tag", "사진 저장 ${imageFile.toUri()}")
                } catch (e: Exception) {
                    Log.d("tag", "사진 저장 실패 ${imageFile.toUri()}")
                } finally {
                    frameBitmap.recycle()
                    viewModel.uploadImage(imageFile)
                }
            }
        }
    }

    private fun layoutToBitmap(layout: View): Bitmap? {
        layout.isDrawingCacheEnabled = true
        layout.buildDrawingCache()
        val drawingCache = layout.drawingCache
        if (drawingCache != null) {
            val bitmap = Bitmap.createBitmap(drawingCache)
            layout.isDrawingCacheEnabled = false
            return bitmap
        }
        layout.isDrawingCacheEnabled = false
        return null
    }
}
