package com.fernandopretell.pruebanisum.source.local

import androidx.room.TypeConverter
import com.fernandopretell.pruebanisum.model.SongModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Collections.emptyList




class ResultConverter {

    var gson = Gson()

    @TypeConverter
    fun stringToObjectList(data: String?): List<SongModel> {
        if (data == null) return emptyList()

        val listType = object : TypeToken<List<SongModel>>() {}.type

        return gson.fromJson<List<SongModel>>(data, listType)
    }

    @TypeConverter
    fun objectListToString(list: List<SongModel>): String? {
        return gson.toJson(list)
    }
}