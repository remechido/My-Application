package com.example.myapplication.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

/**
 * Entidade Post para posts estilo Instagram
 */
@Entity(
    tableName = "posts",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["authorId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Post(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val authorId: Int, // ID do utilizador que criou o post
    val imageUrl: String, // URL da imagem
    val caption: String, // Texto do post
    val timestamp: Long = System.currentTimeMillis(), // Data de criação
    val likesCount: Int = 0 // Número de likes
)