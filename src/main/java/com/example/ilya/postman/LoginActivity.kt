package com.example.ilya.postman

import android.app.Activity
import android.content.*
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import com.example.ilya.postman.data.User
import org.json.JSONObject

class LoginActivity : CustomAppCompactActivity(), View.OnClickListener, TextView.OnEditorActionListener{
    private lateinit var goButton: Button
    private lateinit var toggleButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var emailLayout: TextInputLayout
    private lateinit var loginLayout: TextInputLayout
    private lateinit var nameLayout: TextInputLayout
    private lateinit var name2Layout: TextInputLayout
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var password2Layout: TextInputLayout
    private lateinit var emailTextView: AutoCompleteTextView
    private lateinit var loginTextView: EditText
    private lateinit var nameTextView: EditText
    private lateinit var name2TextView: EditText
    private lateinit var passwordTextView: EditText
    private lateinit var password2TextView: EditText

    private lateinit var receiver: LogInReceiver

    private var email: String? = null
    private var pass: String? = null

    private var register = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        goButton = findViewById(R.id.go_button)
        toggleButton = findViewById(R.id.toggle_button)
        progressBar = findViewById(R.id.login_progress)
        emailLayout = findViewById(R.id.email_layout)
        loginLayout = findViewById(R.id.login_layout)
        nameLayout = findViewById(R.id.name_layout)
        name2Layout = findViewById(R.id.name2_layout)
        passwordLayout = findViewById(R.id.password_layout)
        password2Layout = findViewById(R.id.repeat_password_layout)
        emailTextView = findViewById(R.id.email_field)
        loginTextView = findViewById(R.id.login_field)
        nameTextView = findViewById(R.id.name_field)
        name2TextView = findViewById(R.id.name2_field)
        passwordTextView = findViewById(R.id.password_field)
        password2TextView = findViewById(R.id.repeat_password_field)

        goButton.setOnClickListener(this)
        toggleButton.setOnClickListener(this)
        passwordTextView.setOnEditorActionListener(this)
        password2TextView.setOnEditorActionListener(this)

        receiver = LogInReceiver()
        registerReceiver(receiver, IntentFilter(RECEIVER_ACTION))
    }

    override fun onDestroy() {
        super.onDestroy()

        unregisterReceiver(receiver)
    }

    override fun onClick(p0: View?) {
        if (p0 == null) return
        if(getClientService() == null) return

        when (p0.id){
            goButton.id -> {
                showProgressBar()
                progressBar.progress++

                if (validate()) {
                    email = emailTextView.text.toString()
                    pass = passwordTextView.text.toString()

                    if (register)
                        register(
                                loginTextView.text.toString(),
                                nameTextView.text.toString(),
                                name2TextView.text.toString(),
                                email!!,
                                pass!!)
                    else authorize(email!!, pass!!)
                } else hideProgressBar()
            }

            toggleButton.id -> {
                if (!register){
                    loginLayout.visibility = View.VISIBLE
                    nameLayout.visibility = View.VISIBLE
                    name2Layout.visibility = View.VISIBLE
                    password2Layout.visibility = View.VISIBLE
                    toggleButton.setText(R.string.sign_in)
                    passwordTextView.imeOptions = EditorInfo.IME_ACTION_NEXT

                    register = true
                }else{
                    loginLayout.visibility = View.GONE
                    loginTextView.text = null
                    nameLayout.visibility = View.GONE
                    nameTextView.text = null
                    name2Layout.visibility = View.GONE
                    name2TextView.text = null
                    password2Layout.visibility = View.GONE
                    password2TextView.text = null
                    toggleButton.setText(R.string.register)
                    passwordTextView.imeOptions = EditorInfo.IME_ACTION_GO

                    register = false
                }

                emailLayout.error = null
                loginLayout.error = null
                nameLayout.error = null
                name2Layout.error = null
                passwordLayout.error = null
                password2Layout.error = null
                emailTextView.requestFocus()
            }
        }
    }

    override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
        if (p0 == null) return false

        when (p0.id){
            passwordTextView.id -> {
                if (p1 == EditorInfo.IME_ACTION_GO) goButton.performClick()
                else if (p1 == EditorInfo.IME_ACTION_NEXT) password2TextView.requestFocus()
            }
            password2TextView.id -> {
                if (p1 == EditorInfo.IME_ACTION_GO) goButton.performClick()
            }
        }

        return true
    }


    override fun onClientServiceConnected() {

    }

    private fun authorize(email: String, pass: String) {
        getClientService()!!.sendMessage(
                JSONObject()
                        .put("id", 0)
                        .put("email", email)
                        .put("pass", pass).toString()
        )
    }

    private fun register(login: String, name: String, name2: String, email: String, pass: String) {
        getClientService()!!.sendMessage(
                JSONObject()
                        .put("id", 2)
                        .put("login", login)
                        .put("name", name)
                        .put("name2", name2)
                        .put("email", email)
                        .put("pass", pass).toString()
        )
    }

    private fun showProgressBar(){
        progressBar.visibility = View.VISIBLE
        goButton.isEnabled = false
    }
    private fun hideProgressBar(){
        progressBar.visibility = View.INVISIBLE
        progressBar.progress = 0
        goButton.isEnabled = true
    }

    private fun validate(): Boolean{
        val email = emailTextView.text.toString()
        val login = loginTextView.text.toString()
        val name = nameTextView.text.toString()
        val name2 = name2TextView.text.toString()
        val pass = passwordTextView.text.toString()
        val pass2 = password2TextView.text.toString()

        if (email.isEmpty()){
            emailLayout.error = getString(R.string.error_field_required)
            return false
        }
        progressBar.progress++

        if (pass.isEmpty()){
            passwordLayout.error = getString(R.string.error_field_required)
            return false
        }
        progressBar.progress++

        if (email.substringBefore('@').isEmpty() || email.substringAfter('@').length <= 4) {
            emailLayout.error = getString(R.string.error_invalid_email)
            return false
        }
        progressBar.progress++


        if (pass.length < 8) {
            passwordLayout.error = getString(R.string.error_invalid_password)
            return false
        }

        if (register){
            if (login.isEmpty()){
                loginLayout.error = getString(R.string.error_field_required)
                return false
            }

            if (name.isEmpty()){
                nameLayout.error = getString(R.string.error_field_required)
                return false
            }

            if (name2.isEmpty()){
                name2Layout.error = getString(R.string.error_field_required)
                return false
            }

            if (pass2 != pass){
                password2Layout.error = getString(R.string.error_pass_not_match)
                return false
            }
        }

        progressBar.progress++

        return true
    }

    private inner class LogInReceiver: BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {
            if (p1 == null) return

            val context = applicationContext
            progressBar.progress += 4

            when (p1.getIntExtra("messageId", -1)){
                0 -> {
                    val emailIsValid = p1.getBooleanExtra("emailIsValid", false)
                    val passIsValid = p1.getBooleanExtra("passIsValid", false)

                    if (!emailIsValid) emailLayout.error = getString(R.string.error_user_not_exists)
                    else if (!passIsValid) passwordLayout.error = getString(R.string.error_incorrect_password)
                    else {
                        User.setId(context, p1.getIntExtra("userId", -1))
                        User.setLogin(context, p1.getStringExtra("login"))
                        User.setName(context, p1.getStringExtra("name"))
                        User.setName2(context, p1.getStringExtra("name2"))
                        User.setEmail(context, email!!)
                        User.setPass(context, pass!!)

                        setResult(Activity.RESULT_OK, Intent())
                        finish()
                    }
                }

                1 -> {
                    val emailIsValid = p1.getBooleanExtra("emailIsValid", false)
                    val loginIsValid = p1.getBooleanExtra("loginIsValid", false)
                    if (!emailIsValid) emailLayout.error = getString(R.string.error_user_exists)
                    else if (!loginIsValid) loginLayout.error = getString(R.string.error_login_busy)
                    else authorize(email!!, pass!!)
                }
            }

            progressBar.progress ++
            hideProgressBar()
        }
    }

    companion object {
        const val RECEIVER_ACTION = "com.example.ilya.postman.LoginReceiver"
    }
}
