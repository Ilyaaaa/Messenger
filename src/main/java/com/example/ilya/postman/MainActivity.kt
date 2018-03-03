package com.example.ilya.postman

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.example.ilya.postman.data.User
import com.example.ilya.postman.fragments.MainFragment
import com.makeramen.roundedimageview.RoundedImageView
import org.json.JSONObject

class MainActivity : CustomAppCompactActivity(), View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    private lateinit var navigationDrawerLayout: DrawerLayout
    private lateinit var navigationDrawerToggle: ActionBarDrawerToggle
    private lateinit var toolbar: Toolbar
    private lateinit var navView: NavigationView
    private lateinit var navImageView: RoundedImageView
    private lateinit var navNameField: TextView
    private lateinit var navEmailField: TextView
    private lateinit var navStatusField: TextView

    private val mainFragment = MainFragment()

    private lateinit var mainReceiver:MainReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        toolbar = findViewById(R.id.main_toolbar)
        setSupportActionBar(toolbar)

        navView = findViewById(R.id.nav_view)
        val navHeader = navView.getHeaderView(0)
        navImageView = navHeader.findViewById(R.id.nav_avatar)
        navNameField = navHeader.findViewById(R.id.nav_name_field)
        navEmailField = navHeader.findViewById(R.id.nav_email_field)
        navStatusField = navHeader.findViewById(R.id.nav_status_field)

        navigationDrawerLayout = findViewById(R.id.main_drawer)
        navigationDrawerToggle = ActionBarDrawerToggle(this, navigationDrawerLayout, R.string.drawer_open, R.string.drawer_close)
        navigationDrawerLayout.addDrawerListener(navigationDrawerToggle)
        navigationDrawerToggle.syncState()
        findViewById<NavigationView>(R.id.nav_view).setNavigationItemSelectedListener(this)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)

        supportFragmentManager.beginTransaction()
                .add(R.id.container_layout, mainFragment)
                .commit()

        mainReceiver = MainReceiver()
        registerReceiver(mainReceiver, IntentFilter(RECEIVER_ACTION))
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (navigationDrawerToggle.onOptionsItemSelected(item)) return true
        return super.onOptionsItemSelected(item)
    }

    override fun onClientServiceConnected() {
        if (User.getId(applicationContext) == null)
            startLoginActivity()
        else authCheck()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK){
            when (requestCode){
                LOGIN_START_FOR_RESULT_ID -> {
                    initContent()
                }
            }
        }
    }

    private fun initContent(){
        //TODO: Init all content
    }

    override fun onClick(p0: View?) {
        if (p0 == null) return
        if(clientService == null) return

        when (p0.id) {

        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        when (item.itemId) {
            R.id.test_item -> {
                navNameField.text = User.getName(applicationContext)
                navEmailField.text = User.getEmail(applicationContext)
            }
        }

        fragmentTransaction.commit()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mainReceiver)
    }

    private fun startLoginActivity(){
        startActivityForResult(Intent(this, LoginActivity::class.java), LOGIN_START_FOR_RESULT_ID)
    }

    private fun authCheck() {
        clientService!!.sendMessage(
                JSONObject()
                        .put("id", 3)
                        .put("email", User.getEmail(applicationContext))
                        .put("pass", User.getPass(applicationContext)).toString()
        )
    }

    private inner class MainReceiver: BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {
            when (p1!!.getIntExtra("messageId", -1)){
                0 -> {
                    if (!p1.getBooleanExtra("success", false)) startLoginActivity()
                    else initContent()
                }
            }
        }
    }

    companion object {
        private const val LOGIN_START_FOR_RESULT_ID = 1
        const val RECEIVER_ACTION = "com.example.ilya.postman.MainReceiver"
    }
}
