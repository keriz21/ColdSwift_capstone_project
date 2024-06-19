package com.bangkit.coldswiftapps.view.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.bangkit.coldswiftapps.R

class NIKEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty() && !isValidNIK(s)) error = context.getString(R.string.nik_error)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun isValidNIK(nik: CharSequence): Boolean {
        return when {
            nik.isNullOrEmpty() -> false
            nik.length >= 16 -> true
            else -> false
        }
    }
}