package com.example.ilya.postman

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.ilya.postman.data.User
import com.example.ilya.postman.fragments.ListTabFragment
import com.example.ilya.postman.listsData.ItemAdapter
import com.example.ilya.postman.listsData.ItemData
import org.json.JSONArray
import org.json.JSONObject

class ChatUsersAddActivity : CustomAppCompactActivity(), View.OnClickListener {
    private lateinit var tabs: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var searchField: EditText
    private lateinit var foundedUsers: ItemAdapter
    private lateinit var addedUsers: ItemAdapter

    private lateinit var receiver: ChatUsersAddReceiver

    private var query = String()
    private var oldQueryLength = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_users_add)

        foundedUsers = ItemAdapter(this, ArrayList(), R.layout.list_item, R.drawable.ic_person_add_black)
        addedUsers = ItemAdapter(this, ArrayList(), R.layout.list_item, R.drawable.ic_delete_black)

        searchField = findViewById(R.id.chat_users_add_search_field)
        tabs = findViewById(R.id.chat_users_add_tabs)
        tabs.addTab(tabs.newTab().setText(getText(R.string.founded)))
        tabs.addTab(tabs.newTab().setText(getText(R.string.added)))

        viewPager = findViewById(R.id.chat_users_add_view_pager)
        viewPager.adapter = TabAdapter(2, supportFragmentManager)
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager.currentItem = (tab!!.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }
        })

        findViewById<Button>(R.id.chat_users_add_button).setOnClickListener(this)

        receiver = ChatUsersAddReceiver()
        registerReceiver(receiver, IntentFilter(RECEIVER_ACTION))
    }

    override fun onClientServiceConnected() {
        requestUsersInfo(intent.getIntegerArrayListExtra("usersId"))

        searchField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                oldQueryLength = p0!!.length
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0!!.isEmpty()){
                    foundedUsers.clear()
                    return
                }

                query = p0.toString()
                val request = JSONObject()
                request.put("id", 4)
                request.put("query", query)

                clientService!!.sendMessage(request.toString())
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun requestUsersInfo(usersId: ArrayList<Int>) {
        val request = JSONObject()
        request.put("id", 5)
        request.put("usersId", JSONArray(usersId))

        clientService!!.sendMessage(request.toString())
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    override fun onClick(p0: View?) {
        val userIds = ArrayList<Int>()
        for (i in 0 until addedUsers.count) userIds.add(addedUsers.getItem(i).id.toInt())

        setResult(Activity.RESULT_OK, Intent().putIntegerArrayListExtra("usersId", userIds))
        finish()
    }

    private fun setFoundedUsers(users: ArrayList<ItemData>){
        foundedUsers.clear()

        val iterator = users.iterator()
        for (user in iterator)
            if (user.id.toInt() == User.getId(this) || addedUsers.contains(user))
                iterator.remove()

        foundedUsers.addAll(users)
    }

    private inner class ChatUsersAddReceiver: BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            when (p1!!.getIntExtra("messageId", -1)) {
                4 -> {
                    setFoundedUsers(jsonArrayToArrayList(JSONArray(p1.getStringExtra("data"))))
                }

                5 -> {
                    addedUsers.clear()
                    addedUsers.addAll(jsonArrayToArrayList(JSONArray(p1.getStringExtra("data"))))
                }
            }
        }

        private fun jsonArrayToArrayList(array: JSONArray): ArrayList<ItemData>{
            val users = ArrayList<ItemData>()
            for (i in 0 until array.length()) {
                val user = array.getJSONObject(i)
                users.add(ItemData(
                        user["id"].toString().toLong(),
                        "${user["name"]} ${user["name2"]}",
                        user["login"].toString()
                ))
            }

            return users
        }
    }

    private inner class TabAdapter(val tabCount: Int, fm: FragmentManager): FragmentStatePagerAdapter(fm){
        override fun getItem(position: Int): Fragment? {
            val fragment = ListTabFragment()
            var emptyText = String()
            when(position){
                0 -> {
                    fragment.listAdapter = foundedUsers
                    emptyText = getString(R.string.founded_users)
                    fragment.onItemClick = fun(position: Int){
                        val item = foundedUsers.getItem(position)
                        foundedUsers.remove(item)
                        addedUsers.add(item)
                    }
                }

                1 -> {
                    fragment.listAdapter = addedUsers
                    emptyText = getString(R.string.added_users)
                    fragment.onItemClick = fun(position: Int) {
                        addedUsers.remove(addedUsers.getItem(position))

                    }
                }

                else -> return null
            }

            val data = Bundle()
            data.putString("emptyText", emptyText)
            fragment.arguments = data

            return fragment
        }

        override fun getCount(): Int {
            return tabCount
        }
    }

    companion object {
        const val RECEIVER_ACTION = "com.example.ilya.postman.ChatUsersAddReceiver"
    }
}
