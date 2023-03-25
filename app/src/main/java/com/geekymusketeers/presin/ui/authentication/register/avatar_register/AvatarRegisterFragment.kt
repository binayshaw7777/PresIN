package com.geekymusketeers.presin.ui.authentication.register.avatar_register


import android.Manifest
import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.geekymusketeers.presin.R
import com.geekymusketeers.presin.analytics.AnalyticsData
import com.geekymusketeers.presin.base.BaseFragment
import com.geekymusketeers.presin.base.ViewModelFactory
import com.geekymusketeers.presin.databinding.FragmentAvatarRegisterBinding
import com.geekymusketeers.presin.utils.Logger
import com.github.dhaval2404.imagepicker.ImagePicker
import com.permissionx.guolindev.PermissionX


class AvatarRegisterFragment : BaseFragment() {

    private val args: AvatarRegisterFragmentArgs by navArgs()
    private var _binding: FragmentAvatarRegisterBinding? = null
    private val binding get() = _binding!!
    private var avatarPictureURI: Uri? = null
    private val avatarRegisterViewModel: AvatarRegisterViewModel by viewModels{
        ViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAvatarRegisterBinding.inflate(layoutInflater, container, false)

        initObservers()
        clickHandlers()
        return binding.root
    }

    private fun clickHandlers() {
        binding.apply {
            skipOrNextButton.setOnClickListener {
                Logger.debugLog(skipOrNextButton.getHeader())
                if (skipOrNextButton.getHeader() == getString(R.string.skip)) {
                    val action = AvatarRegisterFragmentDirections.actionAvatarRegisterFragmentToFaceScanRegisterFragment(args.UserObject)
                    findNavController().navigate(action)
                }else {
                    avatarRegisterViewModel.avatarRegister(args.UserObject)

                }
            }
            addAvatar.setOnClickListener {
                uploadImage()
            }
        }
    }


    private fun initObservers() {
        avatarRegisterViewModel.run {
            observerException(this)
            enableAvatarRegisterButtonLiveData.observe(viewLifecycleOwner) {
                binding.skipOrNextButton.setHeader(getString(R.string.next))
            }
            userLiveData.observe(viewLifecycleOwner) {
                val actions = AvatarRegisterFragmentDirections.actionAvatarRegisterFragmentToFaceScanRegisterFragment(it)
                findNavController().navigate(actions)
            }
            isValidAvatar.observe(viewLifecycleOwner) {
                showErrorDialog(getString(R.string.error), getString(R.string.avatar_error))
            }
        }
    }


    private fun uploadImage() {
        PermissionX.init(this)
            .permissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
            .onExplainRequestReason { scope, deniedList ->
                scope.showRequestReasonDialog(deniedList, "Core fundamental are based on these permissions", "OK", "Cancel")
            }
            .onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(deniedList, "You need to allow necessary permissions in Settings manually", "OK", "Cancel")
            }
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    Toast.makeText(requireContext(), "All permissions are granted", Toast.LENGTH_LONG).show()
                    ImagePicker.with(requireActivity())
                        .cropSquare()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(512, 512)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .createIntent {
                            startForProfileImageResult.launch(it)
                        }
                } else {
                    Toast.makeText(requireContext(), "These permissions are denied: $deniedList", Toast.LENGTH_LONG).show()
                }
            }
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    //Image Uri will not be null for RESULT_OK
                    val fileUri = data?.data!!
                    val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, fileUri)
                    avatarRegisterViewModel.setAvatarUri(fileUri)
                    //You can get File object from intent
//                    val filePath = fileUri.path
//                    val file = File(filePath)
                    binding.avatar.setImageURI(fileUri)
                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun getScreenName() = AnalyticsData.ScreenName.AVATAR_REGISTER_FRAGMENT

}