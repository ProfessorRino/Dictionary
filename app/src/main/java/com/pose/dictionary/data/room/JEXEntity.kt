package com.pose.dictionary

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "JEX",
    indices = arrayOf(
        Index(
            value = ["key", "searchword", "resultword", "lan", "pri"],
            name = "KEY_INDEX"
        ),
        Index(
            value = ["searchword", "key", "resultword", "lan", "pri"],
            name = "SEARCH_INDEX"
        )
    )
)

data class JEXEntity
    (
    @ColumnInfo(name = "primaryKey")
    @PrimaryKey
    var primaryKey: Int?,
    @ColumnInfo(name = "key")
    var key: Int?,
    @ColumnInfo(name = "resultword")
    var resultword: String?,
    @ColumnInfo(name = "searchword")
    var searchword: String?,
    @ColumnInfo(name = "lan")
    var lan: String?,
    @ColumnInfo(name = "pri")
    var pri: Int?,
)