package com.woojun.ai.fragment.main

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.VideoResult
import com.woojun.ai.MainActivity
import com.woojun.ai.databinding.FragmentCameraBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class CameraFragment : Fragment() {

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            val mainActivity = activity as MainActivity
            mainActivity.hideBottomNavigation(true)

            camera.setLifecycleOwner(this@CameraFragment)

            camera.addCameraListener(object : CameraListener() {
                override fun onPictureTaken(result: PictureResult) {
                }

                override fun onVideoTaken(result: VideoResult) {
                }

                override fun onVideoRecordingEnd() {
                    super.onVideoRecordingEnd()
                }

                override fun onPictureShutter() {
                }

                override fun onVideoRecordingStart() {
                }
            })

            captureBtn.setOnClickListener {
                camera.takePicture()
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}