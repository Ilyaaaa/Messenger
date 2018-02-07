package com.example.ilya.postman

import android.content.*
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.support.design.widget.TextInputLayout
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import com.example.ilya.postman.net.ClientService
import org.json.JSONObject

class LoginActivity : AppCompatActivity(), View.OnClickListener, TextView.OnEditorActionListener{
    private lateinit var goButton: Button
    private lateinit var toggleButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var emailLayout: TextInputLayout
    private lateinit var nameLayout: TextInputLayout
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var password2Layout: TextInputLayout
    private lateinit var emailTextView: AutoCompleteTextView
    private lateinit var nameTextView: EditText
    private lateinit var passwordTextView: EditText
    private lateinit var password2TextView: EditText

    private lateinit var clientServiceIntent:Intent
    private var clientService: ClientService? = null
    private lateinit var clientServiceConnection: ServiceConnection
    private lateinit var logInReceiver: LogInReceiver

    private var register = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        goButton = findViewById(R.id.go_button)
        toggleButton = findViewById(R.id.toggle_button)
        progressBar = findViewById(R.id.login_progress)
        emailLayout = findViewById(R.id.email_layout)
        nameLayout = findViewById(R.id.name_layout)
        passwordLayout = findViewById(R.id.password_layout)
        password2Layout = findViewById(R.id.repeat_password_layout)
        emailTextView = findViewById(R.id.email)
        nameTextView = findViewById(R.id.name)
        passwordTextView = findViewById(R.id.password)
        password2TextView = findViewById(R.id.repeat_password)

        goButton.setOnClickListener(this)
        toggleButton.setOnClickListener(this)
        passwordTextView.setOnEditorActionListener(this)
        password2TextView.setOnEditorActionListener(this)

        logInReceiver = LogInReceiver()
        registerReceiver(logInReceiver, IntentFilter(receiverAction))

        clientServiceIntent = Intent(this, ClientService::class.java)
        clientServiceConnection = object : ServiceConnection {
            override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
                clientService = (p1 as ClientService.CustomBinder).getService()
            }

            override fun onServiceDisconnected(p0: ComponentName?) {
                clientService = null
            }
        }

        if (!ClientService.isRun) startService(clientServiceIntent)
    }

    override fun onStart() {
        super.onStart()
        bindService(clientServiceIntent, clientServiceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onClick(p0: View?) {
        if (p0 == null) return
        if(clientService == null) return

        when (p0.id){
            goButton.id -> {
                showProgressBar()
                progressBar.progress++

                if (validate()) {
                    val request = JSONObject()
                            .put("email", emailTextView.text)
                            .put("pass", passwordTextView.text)

                    if (register){
                        request.put("id", 2)
                        request.put("name", nameTextView.text)
                    }else request.put("id", 0)

                    clientService!!.sendMessage(request.toString())
                } else hideProgressBar()
            }

            toggleButton.id -> {
                if (!register){
                    nameLayout.visibility = View.VISIBLE
                    password2Layout.visibility = View.VISIBLE
                    toggleButton.setText(R.string.sign_in)
                    passwordTextView.imeOptions = EditorInfo.IME_ACTION_NEXT

                    register = true
                }else{
                    nameLayout.visibility = View.GONE
                    nameTextView.text = null
                    password2Layout.visibility = View.GONE
                    password2TextView.text = null
                    toggleButton.setText(R.string.register)
                    passwordTextView.imeOptions = EditorInfo.IME_ACTION_GO

                    register = false
                }

                emailLayout.error = null
                nameLayout.error = null
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

    override fun onDestroy() {
        super.onDestroy()
        unbindService(clientServiceConnection)
        unregisterReceiver(logInReceiver)
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
        val name = nameTextView.text.toString()
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
            if (name.isEmpty()){
                nameLayout.error = getString(R.string.error_field_required)
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
            progressBar.progress += 4

            when (p1!!.getIntExtra("messageId", -1)){
                0 -> {
                    val emailIsValid = p1.getBooleanExtra("emailIsValid", false)
                    val passIsValid = p1.getBooleanExtra("passIsValid", false)

                    Log.d(getString(R.string.net_log_tag), "$emailIsValid $passIsValid")
                }

                1 -> {
                    val success = p1.getBooleanExtra("success", false)
                    val userId = p1.getIntExtra("userId", -1)

                    Log.d(getString(R.string.net_log_tag), "$success $userId")
                }
            }

            progressBar.progress ++
            hideProgressBar()
        }
    }

    companion object {
        const val receiverAction = "com.example.ilya.postman.LoginReceiver"
    }
}
