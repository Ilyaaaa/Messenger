package com.example.ilya.postman.net

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ClientReceiver: BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        p0!!.startService(Intent(p0, ClientService::class.java))
    }
}