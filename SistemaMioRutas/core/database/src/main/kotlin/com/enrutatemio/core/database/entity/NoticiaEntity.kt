package com.enrutatemio.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Noticia cacheada localmente (migrada de la tabla legacy `noticias`). */
@Entity(tableName = "noticias")
data class NoticiaEntity(
    @PrimaryKey val id: String,
    val texto: String,
    val publicadaEnMillis: Long,
    val fuente: String,
    val leida: Boolean,
    val esUrgente: Boolean,
    val autorNombre: String?,
    val autorAvatarUrl: String?,
)
