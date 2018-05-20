package com.example.ilya.postman.net

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Binder
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.example.ilya.postman.*
import com.example.ilya.postman.data.User
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ConnectException
import java.net.Socket
import java.net.SocketException
import java.util.*
import java.util.concurrent.LinkedBlockingDeque
import kotlin.concurrent.schedule

class ClientService: Service(){
    private var socket:Socket? = null
    private val sendQueue = LinkedBlockingDeque<String>()
    private val binder = CustomBinder()

    override fun onCreate() {
        super.onCreate()
        ConnectTask().execute()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder {
        return binder
    }

    override fun onDestroy() {
        super.onDestroy()
        sendMessage(JSONObject().put("id", 1).toString())
        isRun = false
    }

    fun restart(){
        stopSelf()
        val intent = Intent(this, this::class.java)
        Timer().schedule(60000){ if (!isRun) startService(intent) }
    }

    fun sendMessage(message: String) {
        sendQueue.add(message)
    }

    inner class CustomBinder: Binder(){
        fun getService(): ClientService {
            return this@ClientService
        }
    }

    private inner class ConnectTask: AsyncTask<Any, Any, Any>(){
        override fun doInBackground(vararg p0: Any?) {
            try {
                socket = Socket(address, port)
                isRun = true
                ReceptionTread().start()
                SendThread().start()
            }catch (ex: Exception){
                restart()
            }
        }
    }

    private inner class ReceptionTread: Thread(){
        override fun run() {
            super.run()
            if (socket == null) return

            val reader = BufferedReader(InputStreamReader(socket!!.getInputStream()))
            while (isRun){
                try {
                    val request = reader.readLine()
                    if (request == null){
                        restart()
                        break
                    }

                    val jsonObject = JSONObject(request)

                    when (jsonObject["id"]){
                        0 -> {
                            sendBroadcast(
                                    Intent(LoginActivity.RECEIVER_ACTION)
                                            .putExtra("messageId", 0)
                                            .putExtra("emailIsValid", jsonObject["emailIsValid"] as Boolean)
                                            .putExtra("passIsValid", jsonObject["passIsValid"] as Boolean)
                                            .putExtra("login", jsonObject["login"] as String)
                                            .putExtra("name", jsonObject["name"] as String)
                                            .putExtra("name2", jsonObject["name2"] as String)
                                            .putExtra("userId", jsonObject["userId"] as Int)
                            )
                        }

                        1 -> {
                            sendBroadcast(
                                    Intent(LoginActivity.RECEIVER_ACTION)
                                            .putExtra("messageId", 1)
                                            .putExtra("emailIsValid", jsonObject["emailIsValid"] as Boolean)
                                            .putExtra("loginIsValid", jsonObject["loginIsValid"] as Boolean)
                            )
                        }

                        2 -> {
                            var success = false
                            if (jsonObject["emailIsValid"] as Boolean && jsonObject["passIsValid"] as Boolean)
                                success = true

                            sendBroadcast(
                                    Intent(MainActivity.RECEIVER_ACTION)
                                            .putExtra("messageId", 0)
                                            .putExtra("success", success)
                            )
                        }

                        4 -> {
                            sendBroadcast(
                                    Intent(ChatUsersAddActivity.RECEIVER_ACTION)
                                            .putExtra("messageId", 4)
                                            .putExtra("data", jsonObject["data"] as String)
                            )
                        }

                        5 -> {
                            sendBroadcast(
                                    Intent(ChatUsersAddActivity.RECEIVER_ACTION)
                                            .putExtra("messageId", 5)
                                            .putExtra("data", jsonObject["data"] as String)
                            )
                        }

                        6 -> {
                            sendBroadcast(
                                    Intent(MainActivity.RECEIVER_ACTION)
                                            .putExtra("messageId", 6)
                                            .putExtra("success", jsonObject["success"] as Boolean)
                            )
                        }

                        7 -> {
                            sendBroadcast(
                                    Intent(MainActivity.RECEIVER_ACTION)
                                            .putExtra("messageId", 7)
                                            .putExtra("chats", (jsonObject["chats"] as JSONArray).toString())
                            )
                        }

                        8 -> {
                            val chatId = jsonObject["chatId"].toString().toInt()
                            val text = jsonObject["text"].toString()
                            val sendTime = jsonObject["sendTime"].toString().toLong()
                            if (ChatActivity.currentChatId == chatId)
                                sendBroadcast(
                                        Intent(ChatActivity.RECEIVER_ACTION)
                                                .putExtra("messageId", 8)
                                                .putExtra("msgId", jsonObject["msgId"].toString().toInt())
                                                .putExtra("text", text)
                                                .putExtra("senderId", jsonObject["senderId"].toString().toInt())
                                                .putExtra("chatId", chatId)
                                                .putExtra("sendTime", sendTime)
                                )
                            else {
                                val intent = Intent(this@ClientService, ChatActivity::class.java)
                                        .putExtra("chatId", chatId.toLong())

                                val pIntent = PendingIntent.getActivity(
                                        this@ClientService,
                                        0,
                                        intent,
                                        PendingIntent.FLAG_UPDATE_CURRENT)

                                val notification = NotificationCompat.Builder(this@ClientService, "")
                                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                                        .setContentTitle("title")
                                        .setContentText(text)
                                        .setContentIntent(pIntent)
                                        .setAutoCancel(true)
                                        .setWhen(sendTime)
                                        .build()

                                (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                                        .notify(chatId, notification)
                            }
                        }

                        9 -> {
                            sendBroadcast(
                                    Intent(ChatActivity.RECEIVER_ACTION)
                                            .putExtra("messageId", 9)
                                            .putExtra("messages", (jsonObject["messages"] as JSONArray).toString())
                            )
                        }
                    }

                }catch (ex: SocketException){
                    restart()
                }
            }
        }
    }

    private inner class SendThread: Thread(){
        private val writer = PrintWriter(socket!!.getOutputStream(), true)

        override fun run() {
            super.run()
            while (isRun) {
                try {
                    writer.println(sendQueue.take())
                }catch (ex: InterruptedException){
                    ex.printStackTrace()
                }
            }
        }
    }

    companion object {
        var isRun = false
        var address = "192.168.0.88"
//        var address = "192.168.43.80"
        var port = 4000
    }
}
