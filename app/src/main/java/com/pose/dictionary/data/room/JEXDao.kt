package com.pose.dictionary

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
interface JEXDao {

    @Query("SELECT DISTINCT key FROM JEX WHERE searchword = :searchword and lan != 'rus' order by pri desc LIMIT 300")
    fun getEqualEntries(searchword: String): List<Int>

    @Query("SELECT DISTINCT key FROM JEX WHERE searchword like :searchword || '%' and lan != 'rus' order by pri desc LIMIT 100")
    fun getStartWithEntries(searchword: String): List<Int>

    @Query("SELECT DISTINCT key FROM JEX WHERE searchword like '%' || :searchword and lan != 'rus' order by pri desc LIMIT 100")
    fun getEndWithEntries(searchword: String): List<Int>

    @Query("SELECT DISTINCT key FROM JEX WHERE searchword like '%' || :searchword || '%' and lan != 'rus' order by pri desc LIMIT 100")
    fun getIncludesEntries(searchword: String): List<Int>

    @Query("SELECT * FROM JEX WHERE key = :key and " +
            "(lan = 'K' or lan = 'R' or lan = 'eng' or lan= 'ger') " +
            "order by lan ='K' desc, lan = 'R' desc, lan='eng' desc, lan ='ger' desc, pri desc")
    fun getResultWords(key: Int): List<JEXEntity>

    @Query("SELECT * FROM ATTRIBUTES WHERE key =:key")
    fun getAnnotations(key: Int): List<AnnotationEntity>

    @RawQuery
    fun analyze(supportSQLiteQuery: SupportSQLiteQuery?): Int

    @RawQuery
    fun vacuum(supportSQLiteQuery: SupportSQLiteQuery?): Int

    @RawQuery
    fun setCaseSensitive(supportSQLiteQuery: SupportSQLiteQuery?): Int
}