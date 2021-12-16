package com.example.map_app.database

import androidx.room.TypeConverter
import com.example.map_app.database.tables.Source

class Converters {

    @TypeConverter
    fun fromSource(source: Source): String {
        return source.name
    }

    @TypeConverter
    fun toSource(name: String): Source {
        return Source(name, name)
    }
}