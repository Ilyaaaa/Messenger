package com.example.ilya.postman.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.ilya.postman.ChatUsersAddActivity
import com.example.ilya.postman.CustomAppCompactActivity

import com.example.ilya.postman.R
import com.example.ilya.postman.data.User
import org.json.JSONArray
import org.json.JSONObject

class ChatAddFragment : Fragment(), View.OnClickListener {
    private lateinit var chatNameLayout: TextInputLayout
    private lateinit var descriptionLayout: TextInputLayout
    private lateinit var chatNameField: EditText
    private lateinit var descriptionField: EditText
    private lateinit var inviteButton: Button
    private lateinit var invitedUserTextView: TextView
    private lateinit var createButton: Button

    private val addedUsersId = ArrayList<Int>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chat_add, container, false)

        chatNameLayout = view.findViewById(R.id.chat_name_layout)
        descriptionLayout = view.findViewById(R.id.chat_desc_layout)
        chatNameField = view.findViewById(R.id.chat_name_field)
        descriptionField = view.findViewById(R.id.chat_desc_field)
        inviteButton = view.findViewById(R.id.users_invite_button)
        inviteButton.setOnClickListener(this)
        invitedUserTextView = view.findViewById(R.id.invited_users_count_text)
        createButton = view.findViewById(R.id.create_chat_button)
        createButton.setOnClickListener(this)

        return view
    }

    override fun onClick(p0: View?) {
        if (p0 == null) return

        when (p0.id) {
            R.id.users_invite_button -> {
                startActivityForResult(
                        Intent(activity, ChatUsersAddActivity::class.java)
                                .putIntegerArrayListExtra("usersId", addedUsersId),
                        USERS_ADD_START_FOR_RESULT_ID
                )
            }

            R.id.create_chat_button -> {
                if (!validate()) return

                val request = JSONObject()
                request.put("id", 6)
                request.put("name", chatNameField.text.toString())
                request.put("description", descriptionField.text.toString())
                request.put("ownerId", User.getId(activity))
                request.put("users", JSONArray(addedUsersId))

                (activity as CustomAppCompactActivity).clientService!!.sendMessage(request.toString())
            }
        }
    }

    private fun validate(): Boolean {
        chatNameLayout.error = null
        descriptionLayout.error = null

        return if (chatNameField.text.isEmpty()) {
            chatNameLayout.error = getString(R.string.error_field_required)
            false
        } else true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != RESULT_OK) return

        when (requestCode) {
            USERS_ADD_START_FOR_RESULT_ID -> {
                if (data == null) return

                addedUsersId.clear()
                addedUsersId.addAll(data.getIntegerArrayListExtra("usersId"))
                for (i in addedUsersId) Log.d("Messenger", i.toString())

                invitedUserTextView.text = addedUsersId.size.toString()
            }
        }
    }

    companion object {
        private const val USERS_ADD_START_FOR_RESULT_ID = 1
    }
}
