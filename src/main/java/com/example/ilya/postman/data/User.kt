package com.example.ilya.postman.data

import android.content.Context

object User {
    private const val PREF_NAME = "UserPrefs"

    fun setId(context: Context, id: Int){
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("id", id)
        editor.apply()
    }

    fun getId(context: Context): Int?{
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val id = sharedPreferences.getInt("id", -1)
        return if (id != -1) id
        else null
    }

    fun setLogin(context: Context, login: String){
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("login", login)
        editor.apply()
    }

    fun getLogin(context: Context): String?{
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString("login", null)
    }

    fun setName(context: Context, name: String){
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("name", name)
        editor.apply()
    }

    fun getName(context: Context): String?{
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString("name", null)
    }

    fun setName2(context: Context, name2: String){
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("name2", name2)
        editor.apply()
    }

    fun getName2(context: Context): String?{
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString("name2", null)
    }

    fun setEmail(context: Context, email: String){
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("email", email)
        editor.apply()
    }

    fun getEmail(context: Context): String?{
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString("email", null)
    }

    fun setPass(context: Context, pass: String){
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("pass", pass)
        editor.apply()
    }

    fun getPass(context: Context): String?{
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString("pass", null)
    }
}