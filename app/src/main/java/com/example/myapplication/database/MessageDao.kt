package com.example.myapplication.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * DAO (Data Access Object) para operações com mensagens
 */
@Dao
interface MessageDao {
    
    /**
     * Inserir nova mensagem
     */
    @Insert
    suspend fun insertMessage(message: Message)
    
    /**
     * Obter mensagens entre dois utilizadores
     */
    @Query("""
        SELECT * FROM messages 
        WHERE (senderId = :userId1 AND receiverId = :userId2) 
           OR (senderId = :userId2 AND receiverId = :userId1)
        ORDER BY timestamp ASC
    """)
    fun getMessagesBetweenUsers(userId1: Int, userId2: Int): Flow<List<Message>>
    
    /**
     * Obter todas as conversas de um utilizador (últimas mensagens)
     */
    @Query("""
        SELECT m.* FROM messages m
        INNER JOIN (
            SELECT 
                CASE 
                    WHEN senderId = :userId THEN receiverId 
                    ELSE senderId 
                END as otherUserId,
                MAX(timestamp) as lastTimestamp
            FROM messages 
            WHERE senderId = :userId OR receiverId = :userId
            GROUP BY otherUserId
        ) latest ON (
            (m.senderId = :userId AND m.receiverId = latest.otherUserId) OR
            (m.receiverId = :userId AND m.senderId = latest.otherUserId)
        ) AND m.timestamp = latest.lastTimestamp
        ORDER BY m.timestamp DESC
    """)
    fun getUserConversations(userId: Int): Flow<List<Message>>
    
    /**
     * Marcar mensagem como lida
     */
    @Query("UPDATE messages SET isRead = 1 WHERE id = :messageId")
    suspend fun markAsRead(messageId: Int)
    
    /**
     * Contar mensagens não lidas para um utilizador
     */
    @Query("SELECT COUNT(*) FROM messages WHERE receiverId = :userId AND isRead = 0")
    suspend fun getUnreadCount(userId: Int): Int
}