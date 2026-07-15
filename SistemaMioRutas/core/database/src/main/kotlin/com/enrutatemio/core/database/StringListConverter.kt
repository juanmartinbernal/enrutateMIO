package com.enrutatemio.core.database

import androidx.room.TypeConverter

/** Convierte listas de strings (paradas, códigos de ruta) a/desde texto separado por `|`. */
class StringListConverter {

    @TypeConverter
    fun fromList(value: List<String>?): String = value?.joinToString(separator = SEPARATOR) ?: ""

    @TypeConverter
    fun toList(value: String?): List<String> =
        value?.split(SEPARATOR)?.filter { it.isNotBlank() } ?: emptyList()

    private companion object {
        const val SEPARATOR = "|"
    }
}
