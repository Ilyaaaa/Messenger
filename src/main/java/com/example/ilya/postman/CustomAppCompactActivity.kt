package com.example.ilya.postman

import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.support.v7.app.AppCompatActivity
import com.example.ilya.postman.net.ClientService

abstract class CustomAppCompactActivity: AppCompatActivity() {
    private lateinit var clientServiceIntent: Intent
    private var clientService: ClientService? = null
    private lateinit var clientServiceConnection: ServiceConnection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        clientServiceIntent = Intent(this, ClientService::class.java)
        clientServiceConnection = object : ServiceConnection {
            override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
                clientService = (p1 as ClientService.CustomBinder).getService()
                onClientServiceConnected()
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

    override fun onDestroy() {
        super.onDestroy()

        unbindService(clientServiceConnection)
    }

    fun getClientService(): ClientService? {
        return clientService
    }

    abstract fun onClientServiceConnected()
}