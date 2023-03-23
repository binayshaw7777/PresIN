package com.geekymusketeers.presin.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.geekymusketeers.presin.R
import com.geekymusketeers.presin.network.ApiError
import com.geekymusketeers.presin.utils.Logger
import java.io.IOException

/**
 * Base Fragment for all the Fragments present in the Project. Provides some common functionality for all the Fragments.
 */
abstract class BaseFragment : Fragment() {

    private lateinit var progressDialog: Dialog

//    override fun onPause() {
//        hideProgress()
//        super.onPause()
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle the back navigation here
                Logger.debugLog("Back is pressed")
                NavHostFragment.findNavController(this@BaseFragment).navigateUp()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }


    /**
     * Generic function to get parent activity of the fragment. Since the function is inlined, no reflection is needed and normal operators like !is and as are now available for you to use
     */
    inline fun <reified T : AppCompatActivity> getParentActivity(): T? {
        var parentActivity: T? = null
        activity?.let {
            parentActivity = it as T
        }
        return parentActivity
    }

//    protected open fun observeProgress(viewModel: BaseViewModel, isDismissible: Boolean = true) {
//        viewModel.progressLiveData.observe(this) { progress ->
//            if (progress) {
//                showProgress(isDismissible)
//                Log.d("saurabh", "show progress $javaClass")
//            } else {
//                hideProgress()
//            }
//        }
//    }

    protected open fun obServeErrorAndException(apiError: ApiError, viewModel: BaseViewModel) {
        (activity as BaseActivity).showErrorDialog(null, apiError.message)
        observerException(viewModel)
    }

    protected open fun observeErrorAndException(viewModel: BaseViewModel) {
        viewModel.errorLiveData.observe(this) {
            showErrorDialog(null, it.message)
        }
        observerException(viewModel)
    }

    protected open fun observerException(viewModel: BaseViewModel) {
        viewModel.exceptionLiveData.observe(this) { exception ->
            if (exception is IOException) {
                showNoInternetDialog()
            } else {
                showErrorDialog()
            }
        }
    }

    private fun showNoInternetDialog() {
        showErrorDialog(getString(R.string.no_internet), getString(R.string.no_internet_message))
    }

    protected open fun showErrorDialog(
        header: String? = null,
        message: String? = null,
    ) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(if (header.isNullOrEmpty()) getString(R.string.error) else header)
        builder.setMessage(if (message.isNullOrEmpty()) getString(R.string.some_error_occoured) else message)
        builder.setPositiveButton(R.string.ok) { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        builder.setOnDismissListener {
        }
        builder.setCancelable(false)
        val dialog = builder.create()
        dialog.show()
    }

    /* Kotlin requires explicit modifiers for overridable members and overrides. Add open if you need function/member to be overridable by default they are final.
        public, protected, internal and private are visibility modifiers, by default public
     */
//    protected fun showProgress(isDismissible: Boolean) {
//        if (this::progressDialogue.isInitialized.not()) {
//            progressDialogue =
//                RelativeLayoutProgressDialog.onCreateDialogModel(requireActivity()).apply {
//                    setCancelable(isDismissible)
//                }
//        }
//        progressDialogue.show()
//    }
//
//    protected fun hideProgress() {
//        if (this::progressDialogue.isInitialized && progressDialogue.isShowing) {
//            progressDialogue.dismiss()
//            Log.d("saurabh", "hide progress $javaClass")
//        }
//    }

    fun showKeyboard(editText: EditText) {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED)
    }

    fun hideKeyboard() {
        val view: View? = requireActivity().currentFocus
        view?.let {
            val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    abstract fun getScreenName(): String
}