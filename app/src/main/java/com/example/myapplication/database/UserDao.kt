package com.example.myapplication.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * DAO (Data Access Object) para operações com utilizadores
 */
@Dao
interface UserDao {
    
    /**
     * Inserir novo utilizador
     */
    @Insert
    suspend fun insertUser(user: User)
    
    /**
     * Fazer login do utilizador
     */
    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    suspend fun loginUser(email: String, password: String): User?
    
    /**
     * Verificar se email já existe
     */
    @Query("SELECT COUNT(*) FROM users WHERE email = :email")
    suspend fun emailExists(email: String): Int
    
    /**
     * Obter todos os utilizadores
     */
    @Query("SELECT * FROM users ORDER BY createdAt DESC")
    fun getAllUsers(): Flow<List<User>>
    
    /**
     * Obter utilizador por ID
     */
    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: Int): User?
    
    /**
     * Atualizar senha do utilizador
     */
    @Query("UPDATE users SET password = :newPassword WHERE id = :userId")
    suspend fun updatePassword(userId: Int, newPassword: String)
    
    /**
     * Eliminar utilizador
     */
    @Delete
    suspend fun deleteUser(user: User)
    
    /**
     * Obter utilizadores por tipo (professores)
     */
    @Query("SELECT * FROM users WHERE isAdmin = 1")
    fun getTeachers(): Flow<List<User>>
}