package com.pose.dictionary

import android.app.Application
import androidx.sqlite.db.SimpleSQLiteQuery

class JEXRepository(application: Application) {

    private var database: JEXDatabase = JEXDatabase.getDataBase(application.applicationContext)!!
    private var jexDao: JEXDao = database.jexDao()

    fun getEqualEntries(searchword: String): List<Int> {
        return jexDao.getEqualEntries(searchword)
    }

    fun getStartWithEntries(searchword: String): List<Int> {
        return jexDao.getStartWithEntries(searchword)
    }

    fun getIncludesEntries(searchword: String): List<Int> {
        return jexDao.getIncludesEntries(searchword)
    }

    fun getEndWithEntries(searchword: String): List<Int> {
        return jexDao.getEndWithEntries(searchword)
    }


    fun getResultWords(key: Int) : List<JEXEntity> {
        return jexDao.getResultWords(key)
    }

    fun getAnnotations(key: Int) : List<AnnotationEntity> {
        return jexDao.getAnnotations(key)
    }

    fun analyze() {
        jexDao.analyze(SimpleSQLiteQuery("ANALYZE"))
    }

    fun vacuum() {
        jexDao.vacuum(SimpleSQLiteQuery("VACUUM"))
    }

    fun setCaseSensitive() {
        jexDao.setCaseSensitive(SimpleSQLiteQuery("PRAGMA case_sensitive_like=ON"))
    }
}

