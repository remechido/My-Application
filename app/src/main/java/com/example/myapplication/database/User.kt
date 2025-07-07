package com.example.myapplication.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidade User para a base de dados
 * Representa um utilizador no sistema
 */
@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val email: String,
    val password: String,
    val isAdmin: Boolean = false, // Campo para identificar administradores
    val createdAt: Long = System.currentTimeMillis() // Data de criação
)