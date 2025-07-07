package com.example.myapplication.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

/**
 * Entidade Message para a base de dados
 * Representa uma mensagem entre utilizadores
 */
@Entity(
    tableName = "messages",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["senderId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["receiverId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Message(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val senderId: Int, // ID do utilizador que enviou
    val receiverId: Int, // ID do utilizador que recebeu
    val content: String, // Conteúdo da mensagem
    val timestamp: Long = System.currentTimeMillis(), // Hora de envio
    val isRead: Boolean = false // Se a mensagem foi lida
)