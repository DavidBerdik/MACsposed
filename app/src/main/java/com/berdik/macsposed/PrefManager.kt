package com.berdik.macsposed

import android.content.Context
import android.content.SharedPreferences

class PrefManager {
    companion object {
        private var prefs: SharedPreferences? = null
        private var hookActive: Boolean? = null

        fun loadPrefs(context: Context) {
            if (prefs == null) {
                prefs = context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_WORLD_READABLE)
            }
        }

        fun isHookOn(): Boolean {
            if (hookActive == null) {
                hookActive = prefs!!.getBoolean("hookActive", false)
            }
            return hookActive as Boolean
        }

        fun toggleHookState() {
            hookActive = !isHookOn()
            val prefEdit = prefs!!.edit()
            prefEdit.putBoolean("hookActive", hookActive!!)
            prefEdit.apply()
        }
    }
}