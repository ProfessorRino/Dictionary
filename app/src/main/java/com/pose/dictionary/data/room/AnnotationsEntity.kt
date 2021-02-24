package com.pose.dictionary

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "ATTRIBUTES",
    indices = arrayOf(
            Index(
                value = ["key", "tagstring"],
                name = "ANNOT_INDEX"
            )
    )
)

data class AnnotationEntity
    (
    @ColumnInfo(name = "primaryKey")
    @PrimaryKey
    var primaryKey: Int?,
    @ColumnInfo(name = "key")
    var key: Int?,
    @ColumnInfo(name = "tagstring")
    var tagstring: String?,
)