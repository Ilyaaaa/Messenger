package com.example.ilya.postman

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import com.example.ilya.postman.data.User
import com.example.ilya.postman.listsData.MessageItemAdapter
import com.example.ilya.postman.listsData.MessageItemData
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class ChatActivity : CustomAppCompactActivity(), View.OnClickListener {
    private lateinit var listView: ListView
    private lateinit var msgTextView: TextView
    private lateinit var sendButton: Button

    private lateinit var adapter: MessageItemAdapter
    private lateinit var receiver: ChatReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        currentChatId = intent.getLongExtra("chatId", -1).toInt()

        listView = findViewById(R.id.message_list)
        msgTextView = findViewById(R.id.msg_text_field)
        sendButton = findViewById(R.id.msg_send_button)
        sendButton.setOnClickListener(this)

        adapter = MessageItemAdapter(this, ArrayList(), R.layout.messages_list_item)
        listView.adapter = adapter

        receiver = ChatReceiver()
        registerReceiver(receiver, IntentFilter(RECEIVER_ACTION))
    }

    override fun onClientServiceConnected() {
        val request = JSONObject()
        request.put("id", 9)
        request.put("chatId", currentChatId)
        getClientService()!!.sendMessage(request.toString())

        msgTextView.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                sendButton.isEnabled = !p0!!.isEmpty()
            }

            override fun afterTextChanged(p0: Editable?) { }
        })
    }

    override fun onDestroy() {
        super.onDestroy()

        unregisterReceiver(receiver)
        currentChatId = null
    }

    override fun onClick(p0: View?) {
        if (p0 == null) return

        when (p0.id) {
            R.id.msg_send_button -> {
                val request = JSONObject()
                request.put("id", 8)
                request.put("text", msgTextView.text.toString())
                request.put("senderId", User.getId(this))
                request.put("chatId", currentChatId)
                request.put("sendTime", Date().time)

                getClientService()!!.sendMessage(request.toString())

                sendButton.text = null
            }
        }
    }

    private inner class ChatReceiver: BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            if (p1 == null) return

            when (p1.getIntExtra("messageId", -1)) {
                8 -> {
                    val message = MessageItemData(
                            p1.getIntExtra("msgId", -1).toLong(),
                            p1.getStringExtra("text"),
                            p1.getIntExtra("senderId", -1),
                            p1.getIntExtra("chatId", -1),
                            Date(p1.getLongExtra("sendTime", -1))
                    )

                    adapter.add(message)
                }

                9 -> {
                    val messages = JSONArray(p1.getStringExtra("messages"))

                    val msgItems = ArrayList<MessageItemData>()
                    for (i in 0 until messages.length()) {
                        val message = MessageItemData(
                                (messages[i] as JSONObject)["id"].toString().toLong(),
                                (messages[i] as JSONObject)["text"].toString(),
                                (messages[i] as JSONObject)["senderId"].toString().toInt(),
                                (messages[i] as JSONObject)["chatId"].toString().toInt(),
                                Date((messages[i] as JSONObject)["sendTime"].toString().toLong())
                        )

                        msgItems.add(message)
                    }

                    adapter.addAll(msgItems)
                }
            }
        }
    }

    companion object {
        const val RECEIVER_ACTION = "com.example.ilya.postman.ChatReceiver"
        var currentChatId: Int? = null
    }
}
