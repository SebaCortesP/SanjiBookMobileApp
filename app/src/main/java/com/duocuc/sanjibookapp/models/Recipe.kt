package com.duocuc.sanjibookapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Entity(
    tableName = "recipes",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
@TypeConverters(Converters::class)
data class Recipe(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val ingredients: List<String>,
    val preparation: String,
    val category: String,
    @ColumnInfo(name = "user_id") val userId: Int)


class Converters {
    @TypeConverter
    fun fromList(value: List<String>): String {
        return value.joinToString(separator = ",")
    }

    @TypeConverter
    fun toList(value: String): List<String> {
        return if (value.isEmpty()) emptyList() else value.split(",")
    }
}