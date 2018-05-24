package com.example.ilya.postman.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.ilya.postman.CustomAppCompactActivity
import com.example.ilya.postman.R
import com.example.ilya.postman.data.User
import com.example.ilya.postman.fragments.dialogs.PassChangeDialogFragment
import com.example.ilya.postman.fragments.dialogs.TextEditDialogFragment
import org.json.JSONObject

class UserSettingsFragment : Fragment(), View.OnClickListener {
    private lateinit var loginView: TextView
    private lateinit var loginEditButton: Button
    private lateinit var nameView: TextView
    private lateinit var nameEditButton: Button
    private lateinit var surnameView: TextView
    private lateinit var surnameEditButton: Button
    private lateinit var changePassButton: Button

    private val loginEditDialog = TextEditDialogFragment()
    private val nameEditDialog = TextEditDialogFragment()
    private val surnameEditDialog = TextEditDialogFragment()
    private val passChangeDialog = PassChangeDialogFragment()

    private lateinit var newLogin: String
    private lateinit var newName: String
    private lateinit var newSurname: String
    private lateinit var newPass: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_user_settings, container, false)

        loginView = view.findViewById(R.id.settings_login_view)
        loginView.text = User.getLogin(activity)
        loginEditButton = view.findViewById(R.id.settings_login_edit_button)
        loginEditButton.setOnClickListener(this)

        nameView = view.findViewById(R.id.settings_name_view)
        nameView.text = User.getName(activity)
        nameEditButton = view.findViewById(R.id.settings_name_edit_button)
        nameEditButton.setOnClickListener(this)

        surnameView = view.findViewById(R.id.settings_surname_view)
        surnameView.text = User.getName2(activity)
        surnameEditButton = view.findViewById(R.id.settings_surname_edit_button)
        surnameEditButton.setOnClickListener(this)

        changePassButton = view.findViewById(R.id.settings_change_pass_button)
        changePassButton.setOnClickListener(this)

        newLogin = User.getLogin(activity)!!
        val loginData = Bundle()
        loginData.putString("text", newLogin)
        loginData.putString("hint", getString(R.string.login))
        loginEditDialog.arguments = loginData
        loginEditDialog.onOkClick = fun (text: String) {
            newLogin = text

            val request = JSONObject()
            request.put("id", 10)
            request.put("login", text)
            (activity as CustomAppCompactActivity).getClientService()!!.sendMessage(request.toString())
        }

        newName = User.getName(activity)!!
        val nameData = Bundle()
        nameData.putString("text", newName)
        nameData.putString("hint", getString(R.string.name))
        nameEditDialog.arguments = nameData
        nameEditDialog.onOkClick = fun (text: String) {
            newName = text

            val request = JSONObject()
            request.put("id", 11)
            request.put("name", text)
            (activity as CustomAppCompactActivity).getClientService()!!.sendMessage(request.toString())
        }

        newSurname = User.getName2(activity)!!
        val surnameData = Bundle()
        surnameData.putString("text", newSurname)
        surnameData.putString("hint", getString(R.string.surname))
        surnameEditDialog.arguments = surnameData
        surnameEditDialog.onOkClick = fun (text: String) {
            newSurname = text

            val request = JSONObject()
            request.put("id", 12)
            request.put("surname", text)
            (activity as CustomAppCompactActivity).getClientService()!!.sendMessage(request.toString())
        }

        newPass = User.getPass(activity)!!
        passChangeDialog.onOkClick = fun (pass: String) {
            newPass = pass

            val request = JSONObject()
            request.put("id", 13)
            request.put("pass", pass)
            (activity as CustomAppCompactActivity).getClientService()!!.sendMessage(request.toString())
        }

        return view
    }

    override fun onClick(p0: View?) {
        if (p0 == null) return

        when(p0.id) {
            R.id.settings_login_edit_button -> {
                loginEditDialog.show(activity.supportFragmentManager, "login_edit_dialog")
            }

            R.id.settings_name_edit_button -> {
                nameEditDialog.show(activity.supportFragmentManager, "name_edit_dialog")
            }

            R.id.settings_surname_edit_button -> {
                surnameEditDialog.show(activity.supportFragmentManager, "surname_edit_dialog")
            }

            R.id.settings_change_pass_button -> {
                passChangeDialog.show(activity.supportFragmentManager, "pass_change_dialog")
            }
        }
    }

    fun setLoginChangeResult (success: Boolean, error: String) {
        if (success) {
            User.setLogin(activity, newLogin)
            loginEditDialog.dismiss()
            loginView.text = newLogin
        } else {
            if (error.isEmpty()) loginEditDialog.setError(error)
            else loginEditDialog.setError(getString(R.string.error_unknown))
        }
    }

    fun setNameChangeResult (success: Boolean, error: String) {
        if (success) {
            User.setName(activity, newName)
            nameEditDialog.dismiss()
            nameView.text = newName
        } else {
            if (error.isEmpty()) nameEditDialog.setError(error)
            else nameEditDialog.setError(getString(R.string.error_unknown))
        }
    }

    fun setSurnameChangeResult (success: Boolean, error: String) {
        if (success) {
            User.setName2(activity, newSurname)
            surnameEditDialog.dismiss()
            surnameView.text = newSurname
        } else {
            if (error.isEmpty()) surnameEditDialog.setError(error)
            else surnameEditDialog.setError(getString(R.string.error_unknown))
        }
    }

    fun setPassChangeResult (success: Boolean, error: String) {
        if (success) {
            User.setPass(activity, newPass)
            passChangeDialog.dismiss()
        } else {
            if (error.isEmpty()) passChangeDialog.setPassError(error)
            else passChangeDialog.setPassError(getString(R.string.error_unknown))
        }
    }
}
