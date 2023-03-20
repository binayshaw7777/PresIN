package com.geekymusketeers.presin.ui.custom_views

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import com.geekymusketeers.presin.R
import com.geekymusketeers.presin.databinding.LayoutCustomEditTextBinding
import com.geekymusketeers.presin.utils.setNonDuplicateClickListener


class CustomEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private lateinit var binding: LayoutCustomEditTextBinding
    private var isRequired: Boolean = true

    init {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        binding = LayoutCustomEditTextBinding.inflate(LayoutInflater.from(context), this, true)
        context.obtainStyledAttributes(attrs, R.styleable.EditTextCustomLayout).apply {
            try {
                val header = getString(R.styleable.EditTextCustomLayout_header)
                val endDrawableIcon = getDrawable(R.styleable.EditTextCustomLayout_endIcon)
                val inputEnabled = getBoolean(R.styleable.EditTextCustomLayout_inputEnabled, true)
                val hint = getString(R.styleable.EditTextCustomLayout_hint)
                val input = getString(R.styleable.EditTextCustomLayout_input)
                val minHeight = getInt(R.styleable.EditTextCustomLayout_minimumHeight, 0)
                val isMultipleLine = getBoolean(R.styleable.EditTextCustomLayout_multiLine, false)
                setHeader(header)
                setEndDrawableIcon(endDrawableIcon)
                seditTextBoxEnabled(inputEnabled)
                setHint(hint)
                seditTextBox(input)
                if (isMultipleLine) {
                    switchToMultiLined()
                }
                if (minHeight != 0) {
                    setMinimHeight(minHeight)
                }

            } finally {
                recycle()
            }
        }
    }

    fun setHeader(header: String?) {
        binding.editTextHeader.text = header
    }

    fun setEndDrawableIcon(drawable: Drawable?) {
        drawable?.let {
            binding.editTextEndIcon.setImageDrawable(it)
        }
    }

    fun switchToMultiLined() {
        binding.run {
            editTextBox.apply {
                isSingleLine = false
                inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
                maxLines = 3
                gravity = Gravity.START or Gravity.TOP
            }
        }
    }

    fun setMinimHeight(height: Int) {
        binding.editTextBox.minimumHeight = height
    }

    fun seditTextBoxEnabled(isEnabled: Boolean) {
        binding.run {
            editTextBox.isEnabled = isEnabled
            editTextBox.setTextColor(
                ContextCompat.getColor(
                    context,
                    if (isEnabled) R.color.edit_text_box_border else R.color.text_color
                )
            )
//            clInput.setBackgroundResource(if (isEnabled) R.drawable.rect_white_fill_grey_outline_5dp else R.drawable.rect_grey_fill_grey_outline_5dp)
        }
    }

    fun setHint(hint: String?) {
        binding.editTextBox.apply {
            this.hint = hint
            setHintTextColor(ContextCompat.getColor(context, R.color.hint_color))
        }
    }

    fun seditTextBox(text: String?) {
        binding.editTextBox.setText(text)
    }

//    fun setRequiredInput(isRequired: Boolean) {
//        this.isRequired = isRequired
//        binding.tvRequiredAsterisk.isVisible = isRequired
//    }

    fun setImeOptionType(imeOption: Int) {
        binding.editTextBox.imeOptions = imeOption
    }

    fun seditTextBoxType(inputType: Int) {
        binding.editTextBox.inputType = inputType
    }

    fun setMultiLined(maxLength: Int) {
        binding.editTextBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s != null && maxLength > 0 && s.length >= maxLength) {
                    val cursorPos = binding.editTextBox.selectionEnd
                    val nextLineIndex = s.toString().indexOf('\n', cursorPos)
                    if (nextLineIndex < 0 || nextLineIndex > cursorPos + 1) {
                        s.append('\n')
                    }
                }
            }
        })
    }

    fun setMaxLines(maxLine: Int) {
        binding.editTextBox.maxLines = maxLine
    }

    fun setMaxLength(maxLength: Int) {
        binding.editTextBox.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
    }

    fun setLayoutListener(isFocusable: Boolean, listener: () -> Unit) {
        binding.run {
            editTextBox.isFocusable = isFocusable
            editTextBox.setNonDuplicateClickListener {
                listener.invoke()
            }
            root.setNonDuplicateClickListener {
                listener.invoke()
            }
            editTextBox.doAfterTextChanged {
                val input = it.toString()
//                setRequiredInput(input.isEmpty())
            }
        }
    }

    fun setUserInputListener(listener: ((input: String) -> Unit)? = null) {
        binding.editTextBox.doAfterTextChanged {
            val input = it.toString().trim()
            listener?.invoke(input)
        }
    }

}