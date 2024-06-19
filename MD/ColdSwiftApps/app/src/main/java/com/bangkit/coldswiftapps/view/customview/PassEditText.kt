package com.bangkit.coldswiftapps.view.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.bangkit.coldswiftapps.R

class PassEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty() && !isValidPassword(s)) error = context.getString(R.string.error_password_length)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun isValidPassword(password: CharSequence?): Boolean {
        return when {
            password.isNullOrEmpty() -> false
            password.length >= 8 -> true
            else -> false
        }
    }
}