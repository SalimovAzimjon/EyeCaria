package uz.napa.eyecaria.util

import android.content.Context

val PREFERENCE_NAME = "SharedPreferenceExample"
val PREFERENCE_LANGUAGE = "Language"

class MyPreference(context : Context){


    val preference = context.getSharedPreferences(PREFERENCE_NAME,Context.MODE_PRIVATE)

    fun getLang() : String{
        return preference.getString(PREFERENCE_LANGUAGE,"uz")!!
    }

    fun setLang(Language:String,listener:()->Unit){
        val editor = preference.edit()
        editor.putString(PREFERENCE_LANGUAGE,Language)
        editor.apply()
        listener.invoke()
    }

}