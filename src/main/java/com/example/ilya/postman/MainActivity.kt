package com.example.ilya.postman

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.example.ilya.postman.data.User
import com.example.ilya.postman.fragments.ChatAddFragment
import com.example.ilya.postman.fragments.MainFragment
import com.example.ilya.postman.listsData.ChatItemAdapter
import com.example.ilya.postman.listsData.ChatItemData
import com.makeramen.roundedimageview.RoundedImageView
import com.minibugdev.drawablebadge.BadgePosition
import com.minibugdev.drawablebadge.DrawableBadge
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : CustomAppCompactActivity(), View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    private lateinit var navigationDrawerLayout: DrawerLayout
    private lateinit var navigationDrawerToggle: ActionBarDrawerToggle
    private lateinit var toolbar: Toolbar
    private lateinit var navView: NavigationView
    private lateinit var navImageView: RoundedImageView
    private lateinit var navLoginField: TextView
    private lateinit var navNameField: TextView
    private lateinit var navStatusField: TextView

    private lateinit var receiver: MainReceiver

    private val mainFragment = MainFragment()
    private lateinit var mainFragmentAdapter: ChatItemAdapter

    private val chatAddFragment = ChatAddFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        toolbar = findViewById(R.id.main_toolbar)
        setSupportActionBar(toolbar)

        navView = findViewById(R.id.nav_view)
        val navHeader = navView.getHeaderView(0)
        navImageView = navHeader.findViewById(R.id.nav_avatar)
        navLoginField = navHeader.findViewById(R.id.nav_login_field)
        navNameField = navHeader.findViewById(R.id.nav_name_field)
        navStatusField = navHeader.findViewById(R.id.nav_status_field)

        navigationDrawerLayout = findViewById(R.id.main_drawer)
        navigationDrawerToggle = ActionBarDrawerToggle(this, navigationDrawerLayout, R.string.drawer_open, R.string.drawer_close)
        navigationDrawerLayout.addDrawerListener(navigationDrawerToggle)
        navigationDrawerToggle.syncState()
        findViewById<NavigationView>(R.id.nav_view).setNavigationItemSelectedListener(this)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)

        mainFragmentAdapter = ChatItemAdapter(this, ArrayList(), R.layout.chats_list_item)
        mainFragment.listAdapter = mainFragmentAdapter

        receiver = MainReceiver()
        registerReceiver(receiver, IntentFilter(RECEIVER_ACTION))
    }

    override fun onDestroy() {
        super.onDestroy()

        unregisterReceiver(receiver)
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
        supportFragmentManager.beginTransaction()
                .add(R.id.container_layout, mainFragment)
                .commit()

        navLoginField.text = User.getLogin(applicationContext)
        navNameField.text = "${User.getName(applicationContext)} ${User.getName2(applicationContext)}"

        val request = JSONObject()
        request.put("id", 7)

        getClientService()!!.sendMessage(request.toString())
    }

    override fun onClick(p0: View?) {
        if (p0 == null) return
        if(getClientService() == null) return

        when (p0.id) {

        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        selectItem(item.itemId)

        return true
    }

    private fun selectItem(id: Int){
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        when (id) {
            R.id.new_chat_item -> {
                fragmentTransaction.replace(R.id.container_layout, chatAddFragment)
            }

            R.id.chats_item -> {
                fragmentTransaction.replace(R.id.container_layout, mainFragment)
            }
        }

        navigationDrawerLayout.closeDrawer(GravityCompat.START)
        fragmentTransaction.commit()
    }


    private fun startLoginActivity(){
        startActivityForResult(Intent(this, LoginActivity::class.java), LOGIN_START_FOR_RESULT_ID)
    }

    private fun authCheck() {
        getClientService()!!.sendMessage(
                JSONObject()
                        .put("id", 3)
                        .put("email", User.getEmail(applicationContext))
                        .put("pass", User.getPass(applicationContext)).toString()
        )
    }

    private inner class MainReceiver: BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            if (p1 == null) return

            when (p1.getIntExtra("messageId", -1)){
                0 -> {
                    if (!p1.getBooleanExtra("success", false)) startLoginActivity()
                    else initContent()
                }

                6 -> {
                    if (p1.getBooleanExtra("success", false))
                        selectItem(R.id.chats_item)
                }

                7 -> {
                    val chats = JSONArray(p1.getStringExtra("chats"))

                    val chatsData = ArrayList<ChatItemData>()
                    for (i in 0 until chats.length()) {
                        val drawable = DrawableBadge.Builder(this@MainActivity)
                                .drawableResId(R.drawable.default_chat_avatar)
                                .badgeColor(R.color.colorUnreadMsgBadge)
                                .badgePosition(BadgePosition.BOTTOM_RIGHT)
                                .showBorder(false)
                                .maximumCounter(99)
                                .build()
                                .get(1)

                        val chat = chats[i] as JSONObject

                        val usersId = JSONArray(chat["users"] as String)
                        val chatUsers = ArrayList<Int>()
                        for (j in 0 until usersId.length())
                            chatUsers.add(usersId[j].toString().toInt())


                        chatsData.add(ChatItemData(
                                chat["id"].toString().toLong(),
                                drawable,
                                chat["name"].toString(),
                                "Last message text...",
                                chat["ownerId"].toString().toInt(),
                                chatUsers
                        ))
                    }

                    mainFragmentAdapter.addAll(chatsData)
                }
            }
        }
    }

    companion object {
        private const val LOGIN_START_FOR_RESULT_ID = 1
        const val RECEIVER_ACTION = "com.example.ilya.postman.MainReceiver"
    }
}
