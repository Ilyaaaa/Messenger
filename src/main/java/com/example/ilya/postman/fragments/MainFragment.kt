package com.example.ilya.postman.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.ListFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import com.example.ilya.postman.ChatActivity
import com.example.ilya.postman.CustomAppCompactActivity
import com.example.ilya.postman.R
import org.json.JSONObject

class MainFragment : ListFragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_main,  container, false)

        view.findViewById<TextView>(android.R.id.empty).text = activity.getString(R.string.chats)

        return view
    }

    override fun onResume() {
        super.onResume()

        val request = JSONObject()
        request.put("id", 7)

        (activity as CustomAppCompactActivity).getClientService()!!.sendMessage(request.toString())
    }

    override fun onListItemClick(l: ListView?, v: View?, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)
        if (v == null) return

        startActivity(Intent(activity, ChatActivity::class.java).putExtra("chatId", id))
    }
}
