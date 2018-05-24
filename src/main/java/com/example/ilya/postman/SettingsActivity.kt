package com.example.ilya.postman

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import com.example.ilya.postman.fragments.SettingsFragment
import com.example.ilya.postman.listsData.SettingData
import com.example.ilya.postman.listsData.SettingsAdapter

class SettingsActivity : CustomAppCompactActivity() {
    private val settingsFragment = SettingsFragment()
    private lateinit var settingsFragmentAdapter: SettingsAdapter

    private lateinit var receiver: SettingsReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val items = ArrayList<SettingData>()
        items.add(SettingData(
                SettingsFragment.USER_SETTINGS_ID,
                getString(R.string.user_prof_settings),
                R.drawable.user_settings_icon
        ))

        settingsFragmentAdapter = SettingsAdapter(this, items, R.layout.settings_list_item)
        settingsFragment.listAdapter = settingsFragmentAdapter

        supportFragmentManager.beginTransaction()
                .add(R.id.settings_container, settingsFragment)
                .commit()
    }

    override fun onClientServiceConnected() {
        receiver = SettingsReceiver()
        registerReceiver(receiver, IntentFilter(RECEIVER_ACTION))
    }

    override fun onDestroy() {
        super.onDestroy()

        unregisterReceiver(receiver)
    }

    private inner class SettingsReceiver: BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            if (p1 == null) return

            when (p1.getIntExtra("messageId", -1)) {
                10 -> {
                    settingsFragment.setLoginChangeResult(
                            p1.getBooleanExtra("success", false),
                            p1.getStringExtra("error")
                    )
                }

                11 -> {
                    settingsFragment.setNameChangeResult(
                            p1.getBooleanExtra("success", false),
                            p1.getStringExtra("error")
                    )
                }

                12 -> {
                    settingsFragment.setSurnameChangeResult(
                            p1.getBooleanExtra("success", false),
                            p1.getStringExtra("error")
                    )
                }

                13 -> {
                    settingsFragment.setPassChangeResult(
                            p1.getBooleanExtra("success", false),
                            p1.getStringExtra("error")
                    )
                }

            }
        }
    }

    companion object {
        const val RECEIVER_ACTION = "com.example.ilya.postman.SettingsReceiver"
    }
}
