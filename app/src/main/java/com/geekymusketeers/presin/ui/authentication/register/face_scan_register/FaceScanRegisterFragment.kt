package com.geekymusketeers.presin.ui.authentication.register.face_scan_register

import android.graphics.*
import android.media.Image
import android.os.Bundle
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.geekymusketeers.presin.R
import com.geekymusketeers.presin.analytics.AnalyticsData
import com.geekymusketeers.presin.base.BaseFragment
import com.geekymusketeers.presin.base.ViewModelFactory
import com.geekymusketeers.presin.databinding.FragmentFaceScanRegisterBinding
import com.geekymusketeers.presin.utils.invisible
import com.geekymusketeers.presin.utils.show
import com.geekymusketeers.presin.utils.showToast
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.nio.ReadOnlyBufferException
import java.util.concurrent.Executors
import kotlin.experimental.inv


class FaceScanRegisterFragment : BaseFragment() {

    private val args: FaceScanRegisterFragmentArgs by navArgs()
    private var _binding: FragmentFaceScanRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var detector : Any
    private val faceScanViewModel: FaceScanViewModel by viewModels {
        ViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFaceScanRegisterBinding.inflate(layoutInflater, container, false)

        initialization()
        initObservers()
        initViews()
        clickHandlers()
        return binding.root
    }

    private fun initialization() {
        //Initialize Face Detector
        val highAccuracyOpts = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .build()
        detector = FaceDetection.getClient(highAccuracyOpts)
    }

    private fun initViews() {
        cameraBind()
    }

    private fun clickHandlers() {
        binding.addFaceButton.setOnClickListener {
        }
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }


    private fun initObservers() {
        faceScanViewModel.run {
            observerException(this)
            enableFaceScanButtonLiveData.observe(viewLifecycleOwner) {
                binding.addFaceButton.isEnabled = it
                binding.addFaceButton.setButtonEnabled(it)
            }
            progressBarLiveData.observe(viewLifecycleOwner) {
                binding.progressBar.progress = it
            }
            binding.progressBar.progress = 75
            isValidFace.observe(viewLifecycleOwner) {
                val message = getString(R.string.empty_face)
                requireContext().showToast(message)
            }
            errorLiveData.observe(viewLifecycleOwner) {
                //Show an error message
                showErrorDialog(getString(R.string.error), it.message)
            }
        }
    }

    private fun cameraBind() {

        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        val previewView = binding.previewView

        lifecycleScope.launch(Dispatchers.Main) {
            try {
//                bindPreview(cameraProviderFuture)
            } catch (e: Exception) {
                // Handle exception
            }
        }
    }

    private fun bindPreview(cameraProvider: ProcessCameraProvider) {
        val preview = Preview.Builder().build()
        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
            .build()
        preview.setSurfaceProvider(binding.previewView.surfaceProvider)

        val imageAnalysis = ImageAnalysis.Builder()
            .setTargetResolution(Size(640, 480))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        val executor = Executors.newSingleThreadExecutor()

        imageAnalysis.setAnalyzer(executor) { imageProxy ->
            runCatching {
                Thread.sleep(10) // Camera preview refreshed every 10 millis (adjust as required)
//                val mediaImage = imageProxy.image ?: return@runCatching
                //val image =
                    //InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

                // Process acquired image to detect faces
//                detector.process(image)
//                    .addOnSuccessListener { faces ->
//                        if (faces.isNotEmpty()) {
//                            val face = faces[0] // Get first face from detected faces
//                            val frameBmp = toBitmap(mediaImage)
//
//                            val rot = imageProxy.imageInfo.rotationDegrees
//
//                            val frameBmp1 = rotateBitmap(frameBmp, rot, false)
//
//                            val boundingBox = RectF(face.boundingBox)
//
//                            val croppedFace = getCropBitmapByCPU(frameBmp1, boundingBox)
//
//                            val scaled = getResizedBitmap(croppedFace, 112, 112)
//
//                            recognizeImage(scaled) // Send scaled bitmap to create face embeddings.
//                        } else {
//                            binding.addFaceButton.invisible() // If no face is detected, remove the 'Add Face' button from view
//                        }
//                    }
//                    .addOnFailureListener { e -> /* Task failed with an exception */ }
//                    .await()
            }.onFailure { e -> /* Handle exception */ }
                .onSuccess { imageProxy.close() } // Very important to acquire next frame for analysis
        }

        cameraProvider.bindToLifecycle(this, cameraSelector, imageAnalysis, preview)
    }

    private fun rotateBitmap(bitmap: Bitmap, rotationDegrees: Int, flipX: Boolean): Bitmap? {
        val matrix = Matrix()

        // Rotate the image back to straight.
        matrix.postRotate(rotationDegrees.toFloat())

        // Mirror the image along the X or Y axis.
        matrix.postScale(if (flipX) -1.0f else 1.0f, 1.0f)
        val rotatedBitmap =
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

        // Recycle the old bitmap if it has changed.
        if (rotatedBitmap != bitmap) {
            bitmap.recycle()
        }
        return rotatedBitmap
    }

    private fun getCropBitmapByCPU(source: Bitmap?, cropRectF: RectF): Bitmap? {
        val resultBitmap = Bitmap.createBitmap(
            cropRectF.width().toInt(),
            cropRectF.height().toInt(),
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(resultBitmap)

        // draw background
        val paint = Paint(Paint.FILTER_BITMAP_FLAG)
        paint.color = Color.WHITE
        canvas.drawRect(
            RectF(0f, 0f, cropRectF.width(), cropRectF.height()),
            paint
        )
        val matrix = Matrix()
        matrix.postTranslate(-cropRectF.left, -cropRectF.top)
        canvas.drawBitmap(source!!, matrix, paint)
        if (source != null && !source.isRecycled) {
            source.recycle()
        }
        return resultBitmap
    }

    private fun getResizedBitmap(bm: Bitmap, newWidth: Int, newHeight: Int): Bitmap? {
        val width = bm.width
        val height = bm.height
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height
        // CREATE A MATRIX FOR THE MANIPULATION
        val matrix = Matrix()
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight)

        // "RECREATE" THE NEW BITMAP
        val resizedBitmap = Bitmap.createBitmap(
            bm, 0, 0, width, height, matrix, false
        )
        bm.recycle()
        return resizedBitmap
    }

    private fun toBitmap(image: Image): Bitmap? {
        val nv21 = YUV_420_888toNV21(image)
        val yuvImage = YuvImage(nv21, ImageFormat.NV21, image.width, image.height, null)

        val out = ByteArrayOutputStream()
        yuvImage.compressToJpeg(Rect(0, 0, yuvImage.width, yuvImage.height), 75, out)

        val imageBytes = out.toByteArray()
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }
    private fun YUV_420_888toNV21(image: Image): ByteArray {
        val width = image.width
        val height = image.height
        val ySize = width * height
        val uvSize = width * height / 4
        val nv21 = ByteArray(ySize + uvSize * 2)

        val yBuffer = image.planes[0].buffer // Y
        val uBuffer = image.planes[1].buffer // U
        val vBuffer = image.planes[2].buffer // V

        var rowStride = image.planes[0].rowStride
        assert(image.planes[0].pixelStride == 1)

        var pos = 0

        if (rowStride == width) { // likely
            yBuffer.get(nv21, 0, ySize)
            pos += ySize
        } else {
            var yBufferPos = -rowStride.toLong() // not an actual position
            while (pos < ySize) {
                yBufferPos += rowStride.toLong()
                yBuffer.position(yBufferPos.toInt())
                yBuffer.get(nv21, pos, width)
                pos += width
            }
        }

        rowStride = image.planes[2].rowStride
        val pixelStride = image.planes[2].pixelStride

        assert(rowStride == image.planes[1].rowStride)
        assert(pixelStride == image.planes[1].pixelStride)

        if (pixelStride == 2 && rowStride == width && uBuffer.get(0) == vBuffer.get(1)) {
            // maybe V an U planes overlap as per NV21, which means vBuffer[1] is alias of uBuffer[0]
            val savePixel = vBuffer.get(1)
            try {
                vBuffer.put(1, (savePixel.inv()))
                if (uBuffer.get(0) == (savePixel.inv())) {
                    vBuffer.put(1, savePixel)
                    vBuffer.position(0)
                    uBuffer.position(0)
                    vBuffer.get(nv21, ySize, 1)
                    uBuffer.get(nv21, ySize + 1, uBuffer.remaining())

                    return nv21 // shortcut
                }
            } catch (ex: ReadOnlyBufferException) {
                // unfortunately, we cannot check if vBuffer and uBuffer overlap
            }

            // unfortunately, the check failed. We must save U and V pixel by pixel
            vBuffer.put(1, savePixel)
        }

        // other optimizations could check if (pixelStride == 1) or (pixelStride == 2),
        // but performance gain would be less significant

        for (row in 0 until height / 2) {
            for (col in 0 until width / 2) {
                val vuPos = col * pixelStride + row * rowStride
                nv21[pos++] = vBuffer.get(vuPos)
                nv21[pos++] = uBuffer.get(vuPos)
            }
        }

        return nv21
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun getScreenName() = AnalyticsData.ScreenName.FACE_SCAN_REGISTER_FRAGMENT
}