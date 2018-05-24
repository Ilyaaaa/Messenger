package com.example.ilya.postman.fragments.dialogs

import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText

import com.example.ilya.postman.R

class TextEditDialogFragment : DialogFragment(), View.OnClickListener {
    private lateinit var inputLayout: TextInputLayout
    private lateinit var field: EditText
    private lateinit var okButton: Button
    private lateinit var cancelButton: Button

    lateinit var onOkClick: (text: String) -> Unit

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_text_edit_dialog, container, false)

        inputLayout = view.findViewById(R.id.text_edit_dialog_layout)
        field = view.findViewById(R.id.text_edit_dialog_field)
        field.setText(arguments.getString("text"))
        field.hint = arguments.getString("hint")

        okButton = view.findViewById(R.id.text_edit_dialog_ok_button)
        okButton.setOnClickListener(this)
        cancelButton = view.findViewById(R.id.text_edit_dialog_cancel_button)
        cancelButton.setOnClickListener(this)

        return view
    }

    override fun onClick(p0: View?) {
        if (p0 == null) return

        when (p0.id) {
            R.id.text_edit_dialog_ok_button -> {
                onOkClick(field.text.toString())
            }

            R.id.text_edit_dialog_cancel_button -> {
                dismiss()
            }
        }
    }

    fun setError(error: String) {
        inputLayout.error = error
    }
}
