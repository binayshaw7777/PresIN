package com.geekymusketeers.presin.ui.authentication.register.face_scan_register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.geekymusketeers.presin.R
import com.geekymusketeers.presin.analytics.AnalyticsData
import com.geekymusketeers.presin.base.BaseFragment
import com.geekymusketeers.presin.base.ViewModelFactory
import com.geekymusketeers.presin.databinding.FragmentFaceScanRegisterBinding
import com.geekymusketeers.presin.utils.showToast


class FaceScanRegisterFragment : BaseFragment() {

    private var _binding: FragmentFaceScanRegisterBinding? = null
    private val binding get() = _binding!!
    private val faceScanViewModel: FaceScanViewModel by viewModels {
        ViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFaceScanRegisterBinding.inflate(layoutInflater, container, false)

        initObservers()
        initViews()
        return binding.root
    }

    private fun initObservers() {
        faceScanViewModel.run {
            observerException(this)
            enableFaceScanButtonLiveData.observe(viewLifecycleOwner) {
                binding.addFaceButton.isEnabled = it
                binding.addFaceButton.setButtonEnabled(it)
            }
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

    private fun initViews() {
        binding.run {
            addFace.apply {

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun getScreenName() = AnalyticsData.ScreenName.FACE_SCAN_REGISTER_FRAGMENT
}