package com.mariofc.sportsnewsapp

import android.content.Context
import com.google.gson.Gson
import com.mariofc.sportsnewsapp.data.Archive
import com.mariofc.sportsnewsapp.data.Athlete
import com.mariofc.sportsnewsapp.data.Event
import com.mariofc.sportsnewsapp.data.New
import com.mariofc.sportsnewsapp.data.Profile
import com.mariofc.sportsnewsapp.data.SportNew
import java.lang.IllegalArgumentException

class ContextExtension {
    companion object {
        fun <T> Context.getData(classType: Class<T>, defaultJsonValue:String = "[]"): T {
            val json = this.getSharedPreferences(getDataKey(classType), Context.MODE_PRIVATE).getString(
                getDataKey(classType), defaultJsonValue
            )
            return Gson().fromJson(json, classType)
        }

        fun <T> Context.saveData(data: T, classType: Class<T>){
            val json = Gson().toJson(data)

            val pref = this.getSharedPreferences(getDataKey(classType), Context.MODE_PRIVATE)
            val editor = pref.edit()

            editor.putString(getDataKey(classType), json)
            editor.apply()
        }

        private fun <T> getDataKey(classType: Class<T>): String = when (classType){
            Array<SportNew>::class.java -> "sport_news"
            Array<New>::class.java -> "news"
            Array<Athlete>::class.java -> "athletes"
            Array<Event>::class.java -> "events"
            Array<Archive>::class.java -> "archives"
            Profile::class.java -> "profile"
            Array<Profile>::class.java -> "profiles"
            else -> throw IllegalArgumentException("Invalid type specified.")
        }
    }
}