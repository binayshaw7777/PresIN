package com.geekymusketeers.presin.ui.authentication.register.face_scan_register

import android.app.Activity
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
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.geekymusketeers.presin.R
import com.geekymusketeers.presin.analytics.AnalyticsData
import com.geekymusketeers.presin.base.BaseFragment
import com.geekymusketeers.presin.base.ViewModelFactory
import com.geekymusketeers.presin.databinding.FragmentFaceScanRegisterBinding
import com.geekymusketeers.presin.utils.*
import com.google.android.gms.tasks.Task
import com.google.common.util.concurrent.ListenableFuture
import com.google.gson.Gson
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import org.tensorflow.lite.Interpreter
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.ReadOnlyBufferException
import java.nio.channels.FileChannel
import java.util.concurrent.Executors
import kotlin.experimental.inv


class FaceScanRegisterFragment : BaseFragment() {

    private val args: FaceScanRegisterFragmentArgs by navArgs()
    private var _binding: FragmentFaceScanRegisterBinding? = null
    private val binding get() = _binding!!
    private var map: MutableMap<String, SimilarityClassifier.Recognition> = mutableMapOf() //To store a embedding with a key -> "added" in this map
    private lateinit var detector: FaceDetector
    var modelFile = "mobile_face_net.tflite" //model name
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var previewView: PreviewView

    private lateinit var cameraSelector: CameraSelector
    private lateinit var cameraProvider: ProcessCameraProvider
    private val inputSize = 112
    private lateinit var intValues: IntArray
    private val isModelQuantized = false
    private val IMAGE_MEAN = 128.0f
    private val IMAGE_STD = 128.0f
    private val OUTPUT_SIZE = 192 //Output size of model
    private var tfLite: Interpreter? = null
    private lateinit var embeddings: Array<FloatArray>

    private val faceScanViewModel: FaceScanViewModel by viewModels {
        ViewModelFactory()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFaceScanRegisterBinding.inflate(layoutInflater, container, false)

        initObservers()
        initViews()
        clickHandlers()

        return binding.root
    }

    private fun initViews() {
        //Load model file
        try {
            tfLite = loadModelFile(requireActivity(), modelFile)?.let { Interpreter(it) }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        //Initialize Face Detector
        val highAccuracyOpts = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .build()
        detector = FaceDetection.getClient(highAccuracyOpts)

        cameraBind()
    }

    private fun clickHandlers() {
        binding.addFaceButton.setOnClickListener {
            addFace()
        }
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.submitButton.setOnClickListener {
            if (map.containsKey("added")) {
                val stringJson = getFromMap(map) //we convert the map into a string by calling a function and passing the map
                Logger.debugLog("String JSON is: $stringJson")
                faceScanViewModel.registerFace(stringJson, args.UserObject, getTimeStamp())
            } else {
                Logger.debugLog("Face not added")
            }

        }
    }


    private fun initObservers() {
        faceScanViewModel.run {
            observerException(this)
            enableFaceAddButtonLiveData.observe(viewLifecycleOwner) {
                binding.addFaceButton.isEnabled = it
                binding.addFaceButton.setButtonEnabled(it)
            }
            enableFaceScanButtonLiveData.observe(viewLifecycleOwner) {
                binding.submitButton.isEnabled = it
                binding.submitButton.setButtonEnabled(it)
            }
            progressBarLiveData.observe(viewLifecycleOwner) {
                binding.progressBar.progress = it
            }
            binding.progressBar.progress = 75
            isValidFace.observe(viewLifecycleOwner) {
                if (it.not()) {
                    val message = getString(R.string.empty_face)
                    requireContext().showToast(message)
                }
            }
            errorLiveData.observe(viewLifecycleOwner) {

                showErrorDialog(getString(R.string.error), it.message)
            }
            userRegisterResponseLiveData.observe(viewLifecycleOwner){
                requireContext().showToast(it.message)
//                findNavController().popBackStack(R.id.loginFragment,false)
                findNavController().popBackStack(R.id.loginFragment, false)
            }
            userLiveData.observe(viewLifecycleOwner) {
                Logger.debugLog("Final user is: $it")
            }
        }
    }

    //Function to add face or store embeddings in a hashmap
    private fun addFace() {
        try {
            if (map.isNotEmpty()) map.clear() //If the hashmap is not empty (the hashmap has previously stored embeddings -> Remove/Clear it)
            val result = SimilarityClassifier.Recognition("0", "", -1f)
            result.extra = embeddings
            map["added"] = result //added the Embedding to hashmap
            Logger.debugLog("Face added successfully: $result")
            faceScanViewModel.addFace(true)
        } catch (e: java.lang.Exception) {
            Logger.debugLog("Error caught at add face: ${e.message}")
        }
    }

    private fun cameraBind() {
        Logger.debugLog("Camera bind started")
        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        previewView = binding.previewView
        cameraProviderFuture.addListener({
            try {
                cameraProvider = cameraProviderFuture.get()
                bindPreview(cameraProvider)
            } catch (e: java.lang.Exception) {
                Logger.debugLog("Error is: ${e.message}")
            }
        }, ContextCompat.getMainExecutor(requireActivity()))
    }

    private fun bindPreview(cameraProvider: ProcessCameraProvider) {
        Logger.debugLog("Bind preview started")
        val preview = Preview.Builder().build()
        cameraSelector = CameraSelector.Builder()
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
                Logger.debugLog("This is inside runCatching")
                val mediaImage = imageProxy.image!!
                val image =
                    InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

                // Process acquired image to detect faces
                val result: Task<List<Face>> = detector.process(image)
                    .addOnSuccessListener { faces ->
                        Logger.debugLog("Faces are: $faces")
                        if (faces.isNotEmpty()) {
                            val face = faces[0] // Get first face from detected faces
                            val frameBmp = toBitmap(mediaImage)

                            val rot = imageProxy.imageInfo.rotationDegrees

                            val frameBmp1 = frameBmp?.let { rotateBitmap(it, rot, false) }

                            val boundingBox = RectF(face.boundingBox)

                            val croppedFace = getCropBitmapByCPU(frameBmp1, boundingBox)

                            //Scale the acquired Face to 112*112 which is required input for model
                            val scaled = croppedFace?.let { getResizedBitmap(it, 112, 112) }

                            if (scaled != null) {
                                recognizeImage(scaled)
                            } // Send scaled bitmap to create face embeddings.
                        } else {
                            faceScanViewModel.setFaceVisible(false)
                        }
                    }
                    .addOnFailureListener { e -> /* Task failed with an exception */
                        Logger.debugLog("onFailureListener: ${e.message}")
                    }.addOnCompleteListener {
                        imageProxy.close()
                    }
            }.onFailure { e -> /* Handle exception */
                Logger.debugLog("onFailure: ${e.message}")
            }
                .onSuccess {
                    Logger.debugLog("on Success")
                } // Very important to acquire next frame for analysis
        }

        cameraProvider.bindToLifecycle(this, cameraSelector, imageAnalysis, preview)
    }

    private fun recognizeImage(bitmap: Bitmap) {
        Logger.debugLog("Recognize image started")

        faceScanViewModel.setFaceVisible(true)

        //Create ByteBuffer to store normalized image
        val imgData = ByteBuffer.allocateDirect(inputSize * inputSize * 3 * 4)
        imgData.order(ByteOrder.nativeOrder())
        intValues = IntArray(inputSize * inputSize)

        //get pixel values from Bitmap to normalize
        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        imgData.rewind()
        for (i in 0 until inputSize) {
            for (j in 0 until inputSize) {
                val pixelValue: Int = intValues.get(i * inputSize + j)
                if (isModelQuantized) {
                    // Quantized model
                    imgData.put((pixelValue shr 16 and 0xFF).toByte())
                    imgData.put((pixelValue shr 8 and 0xFF).toByte())
                    imgData.put((pixelValue and 0xFF).toByte())
                } else { // Float model
                    imgData.putFloat(((pixelValue shr 16 and 0xFF) - IMAGE_MEAN) / IMAGE_STD)
                    imgData.putFloat(((pixelValue shr 8 and 0xFF) - IMAGE_MEAN) / IMAGE_STD)
                    imgData.putFloat(((pixelValue and 0xFF) - IMAGE_MEAN) / IMAGE_STD)
                }
            }
        }
        //imgData is input to our model
        val inputArray = arrayOf<Any>(imgData)
        val outputMap: MutableMap<Int, Any> = HashMap()
        embeddings =
            Array(1) { FloatArray(OUTPUT_SIZE) } //output of model will be stored in this variable
        Logger.debugLog("Embeddings is init: $embeddings")
        outputMap[0] = embeddings
        tfLite?.runForMultipleInputsOutputs(inputArray, outputMap) //Run model
    }

    private fun rotateBitmap(bitmap: Bitmap, rotationDegrees: Int, flipX: Boolean): Bitmap? {
        Logger.debugLog("Rotate bitmap started")

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
        Logger.debugLog("getCropBitmapByCPU started")

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
        if (!source.isRecycled) {
            source.recycle()
        }
        return resultBitmap
    }

    private fun getResizedBitmap(bm: Bitmap, newWidth: Int, newHeight: Int): Bitmap? {
        Logger.debugLog("getResizedBitmap started")

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
        Logger.debugLog("toBitmap started")

        val nv21 = YUV_420_888toNV21(image)
        val yuvImage = YuvImage(nv21, ImageFormat.NV21, image.width, image.height, null)

        val out = ByteArrayOutputStream()
        yuvImage.compressToJpeg(Rect(0, 0, yuvImage.width, yuvImage.height), 75, out)

        val imageBytes = out.toByteArray()
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    private fun YUV_420_888toNV21(image: Image): ByteArray {
        Logger.debugLog("YUV_420_888toNV21 started")

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

    //Convert hashmap to string, basically to pass and store it in firebase
    private fun getFromMap(json: MutableMap<String, SimilarityClassifier.Recognition>): String {
        Logger.debugLog("getFromMap started")

        return Gson().toJson(json)
    }

    //Loads model file
    @Throws(IOException::class)
    private fun loadModelFile(activity: Activity, MODEL_FILE: String): MappedByteBuffer? {
        Logger.debugLog("loadModelFile started")

        val fileDescriptor = activity.assets.openFd(MODEL_FILE)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    override fun getScreenName() = AnalyticsData.ScreenName.FACE_SCAN_REGISTER_FRAGMENT
}