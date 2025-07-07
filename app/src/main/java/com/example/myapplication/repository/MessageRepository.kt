package com.example.myapplication.repository

import com.example.myapplication.database.AppDatabase
import com.example.myapplication.database.Message
import kotlinx.coroutines.flow.Flow

/**
 * Repositório para gerir operações de mensagens
 */
class MessageRepository(private val database: AppDatabase) {
    
    private val messageDao = database.messageDao()
    
    /**
     * Enviar mensagem
     */
    suspend fun sendMessage(senderId: Int, receiverId: Int, content: String): Boolean {
        return try {
            val message = Message(
                senderId = senderId,
                receiverId = receiverId,
                content = content
            )
            messageDao.insertMessage(message)
            true
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Obter mensagens entre dois utilizadores
     */
    fun getMessagesBetweenUsers(userId1: Int, userId2: Int): Flow<List<Message>> {
        return messageDao.getMessagesBetweenUsers(userId1, userId2)
    }
    
    /**
     * Obter conversas do utilizador
     */
    fun getUserConversations(userId: Int): Flow<List<Message>> {
        return messageDao.getUserConversations(userId)
    }
    
    /**
     * Marcar mensagem como lida
     */
    suspend fun markAsRead(messageId: Int) {
        messageDao.markAsRead(messageId)
    }
    
    /**
     * Contar mensagens não lidas
     */
    suspend fun getUnreadCount(userId: Int): Int {
        return messageDao.getUnreadCount(userId)
    }
}