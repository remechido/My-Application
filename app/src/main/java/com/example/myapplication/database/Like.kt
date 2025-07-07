package com.example.myapplication.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

/**
 * Entidade Like para likes nos posts
 */
@Entity(
    tableName = "likes",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Post::class,
            parentColumns = ["id"],
            childColumns = ["postId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Like(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int, // ID do utilizador que deu like
    val postId: Int, // ID do post
    val timestamp: Long = System.currentTimeMillis()
)