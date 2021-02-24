package com.pose.dictionary.data.preferences

import android.app.Application
import android.content.Context
import com.pose.dictionary.JEXViewModel

class PreferenceRepository(application: Application) {
    companion object {
        private const val PREFERENCE_FILE_KEY = "jexPreferenceFileKey "
        private const val JAPANESE_PREF_KEY = "japanese"
        private const val ANNOT_PREF_KEY = "annot"
        private const val ENGLISH_PREF_KEY = "english"
        private const val GERMAN_PREF_KEY = "german"
        private const val SEARCH_METHOD_KEY = "searchMethod"
    }

    private val sharedPref =
        application.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE)

    fun saveJapanesePref(japanese: Boolean) {
        sharedPref.edit().putBoolean(JAPANESE_PREF_KEY, japanese).apply()
    }

    fun loadJapanesePref(): Boolean {
        return sharedPref.getBoolean(JAPANESE_PREF_KEY, true)
    }

    fun saveAnnotPref(annot: Boolean) {
        sharedPref.edit().putBoolean(ANNOT_PREF_KEY, annot).apply()
    }

    fun loadAnnotPref(): Boolean {
        return sharedPref.getBoolean(ANNOT_PREF_KEY, true)
    }

    fun saveEnglishPref(english: Boolean) {
        sharedPref.edit().putBoolean(ENGLISH_PREF_KEY, english).apply()
    }

    fun loadEnglishPref(): Boolean {
        return sharedPref.getBoolean(ENGLISH_PREF_KEY, true)
    }

    fun saveGermanPref(german: Boolean) {
        sharedPref.edit().putBoolean(GERMAN_PREF_KEY, german).apply()
    }

    fun loadGermanPref(): Boolean {
        return sharedPref.getBoolean(GERMAN_PREF_KEY, true)
    }

    fun saveSearchMethod(method: JEXViewModel.SearchMethod) {
        sharedPref.edit().putString(SEARCH_METHOD_KEY, method.value).apply()
    }

    fun loadSearchMethod(): JEXViewModel.SearchMethod {
        val value = sharedPref.getString(SEARCH_METHOD_KEY, JEXViewModel.SearchMethod.Match.value)
        JEXViewModel.SearchMethod.values().forEach {
            if (value == it.value) {
                return it
            }
        }
        return JEXViewModel.SearchMethod.Match
    }
}