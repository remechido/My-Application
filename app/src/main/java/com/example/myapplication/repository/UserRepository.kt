package com.example.myapplication.repository

import com.example.myapplication.database.AppDatabase
import com.example.myapplication.database.User
import kotlinx.coroutines.flow.Flow

/**
 * Repositório para gerir operações de utilizadores
 * Camada entre a UI e a base de dados
 */
class UserRepository(private val database: AppDatabase) {
    
    private val userDao = database.userDao()
    
    /**
     * Fazer login do utilizador
     */
    suspend fun loginUser(email: String, password: String): User? {
        return userDao.loginUser(email, password)
    }
    
    /**
     * Registar novo utilizador
     */
    suspend fun registerUser(email: String, password: String, isAdmin: Boolean = false): Boolean {
        return try {
            // Verificar se email já existe
            if (userDao.emailExists(email) > 0) {
                false // Email já existe
            } else {
                val user = User(email = email, password = password, isAdmin = isAdmin)
                userDao.insertUser(user)
                true // Sucesso
            }
        } catch (e: Exception) {
            false // Erro
        }
    }
    
    /**
     * Obter todos os utilizadores
     */
    fun getAllUsers(): Flow<List<User>> {
        return userDao.getAllUsers()
    }
    
    /**
     * Obter utilizador por ID
     */
    suspend fun getUserById(id: Int): User? {
        return userDao.getUserById(id)
    }
    
    /**
     * Atualizar senha do utilizador
     */
    suspend fun updatePassword(userId: Int, newPassword: String): Boolean {
        return try {
            userDao.updatePassword(userId, newPassword)
            true
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Eliminar utilizador
     */
    suspend fun deleteUser(user: User): Boolean {
        return try {
            userDao.deleteUser(user)
            true
        } catch (e: Exception) {
            false
        }
    }
}