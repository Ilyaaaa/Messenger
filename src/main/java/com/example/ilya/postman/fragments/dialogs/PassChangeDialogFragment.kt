package com.example.ilya.postman.fragments.dialogs


import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText

import com.example.ilya.postman.R

class PassChangeDialogFragment : DialogFragment(), View.OnClickListener {
    private lateinit var passLayout: TextInputLayout
    private lateinit var passField: EditText
    private lateinit var pass2Layout: TextInputLayout
    private lateinit var pass2Field: EditText
    private lateinit var okButton: Button
    private lateinit var cancelButton: Button

    lateinit var onOkClick: (pass: String) -> Unit

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_pass_change_dialog, container, false)

        passLayout = view.findViewById(R.id.pass_change_dialog_password_layout)
        passField = view.findViewById(R.id.pass_change_dialog_password_field)
        pass2Layout = view.findViewById(R.id.pass_change_dialog_password2_layout)
        pass2Field = view.findViewById(R.id.pass_change_dialog_password2_field)

        okButton = view.findViewById(R.id.pass_change_dialog_ok_button)
        okButton.setOnClickListener(this)
        cancelButton = view.findViewById(R.id.pass_change_dialog_cancel_button)
        cancelButton.setOnClickListener(this)

        return view
    }

    override fun onClick(p0: View?) {
        if (p0 == null) return

        when (p0.id) {
            R.id.pass_change_dialog_ok_button -> {
                onOkClick(passField.text.toString())
            }

            R.id.pass_change_dialog_cancel_button -> {
                dismiss()
            }
        }
    }

    fun setPassError(error: String) {
        passLayout.error = error
        passLayout.requestFocus()
    }
}
