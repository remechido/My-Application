package com.example.myapplication.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

/**
 * Entidade Alert para alertas de professores
 */
@Entity(
    tableName = "alerts",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["senderId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Alert(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val senderId: Int, // ID do professor que enviou
    val title: String, // Título do alerta
    val message: String, // Mensagem do alerta
    val targetGroup: String? = null, // Grupo alvo (opcional)
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)