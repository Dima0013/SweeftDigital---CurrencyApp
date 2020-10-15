package com.altun.currencyapp_sweeftdigital

import android.app.Dialog
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import kotlinx.android.synthetic.main.dialog_layout.*

object Tools {
    lateinit var dialog: Dialog
    var rate: Float = 0f
    fun showCurrencyCalculateDialog(context: Context, name: String, rate: Float) {
        dialog = Dialog(context)
        this.rate = rate
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_layout)

        val params: ViewGroup.LayoutParams = dialog.window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog.window!!.attributes = params as WindowManager.LayoutParams
        dialog.changeTextView.text = name

        dialog.gelEditText.addTextChangedListener(gelTextWatcher)

        dialog.okButton.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private val gelTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s!!.isNotEmpty()) {
                val text = rate * s.toString().toFloat()
                dialog.changeEditText.setText(text.toString())
            } else
                dialog.changeEditText.setText("")
        }

        override fun afterTextChanged(s: Editable?) {}
    }
}