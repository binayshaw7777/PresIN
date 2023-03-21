package com.geekymusketeers.presin.ui.custom_views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.geekymusketeers.presin.R
import com.geekymusketeers.presin.databinding.LayoutPrimaryButtonBinding


class CustomButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private lateinit var binding: LayoutPrimaryButtonBinding
    private var isEnabled: Boolean = false

    init {
        initView(context, attrs)
    }

    @SuppressLint("CustomViewStyleable")
    private fun initView(context: Context, attrs: AttributeSet?) {

        binding = LayoutPrimaryButtonBinding.inflate(LayoutInflater.from(context), this, true)
        context.obtainStyledAttributes(attrs, R.styleable.ButtonCustomLayout).apply {
            try {
                val header = getString(R.styleable.ButtonCustomLayout_header)
                val endDrawableIcon = getDrawable(R.styleable.ButtonCustomLayout_endIcon)
                val inputEnabled = getBoolean(R.styleable.ButtonCustomLayout_inputEnabled, false)
                setHeader(header)
                setEndDrawableIcon(endDrawableIcon)
                setButtonEnabled(inputEnabled)
            } finally {
                recycle()
            }
        }
    }

    private fun setEndDrawableIcon(drawable: Drawable?) {
        drawable?.let {
            binding.buttonIcon.setImageDrawable(it)
        }
    }

    fun setButtonEnabled(inputEnabled: Boolean) {
        binding.run {
            root.isEnabled = inputEnabled
            isEnabled = inputEnabled
            buttonConstraintLayout.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    if (inputEnabled) R.color.button_enable_color else R.color.button_disable_color
                )
            )
        }
    }

    private fun setHeader(header: String?) {
        binding.buttonText.text = header
    }

}