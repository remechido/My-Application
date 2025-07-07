package com.example.myapplication.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * DAO para operações com alertas
 */
@Dao
interface AlertDao {
    
    /**
     * Inserir novo alerta
     */
    @Insert
    suspend fun insertAlert(alert: Alert)
    
    /**
     * Obter todos os alertas ordenados por data
     */
    @Query("""
        SELECT a.*, u.email as senderEmail 
        FROM alerts a 
        INNER JOIN users u ON a.senderId = u.id 
        ORDER BY a.timestamp DESC
    """)
    fun getAllAlertsWithSender(): Flow<List<AlertWithSender>>
    
    /**
     * Obter alertas por grupo
     */
    @Query("""
        SELECT a.*, u.email as senderEmail 
        FROM alerts a 
        INNER JOIN users u ON a.senderId = u.id 
        WHERE a.targetGroup = :group OR a.targetGroup IS NULL
        ORDER BY a.timestamp DESC
    """)
    fun getAlertsByGroup(group: String): Flow<List<AlertWithSender>>
    
    /**
     * Marcar alerta como lido
     */
    @Query("UPDATE alerts SET isRead = 1 WHERE id = :alertId")
    suspend fun markAlertAsRead(alertId: Int)
    
    /**
     * Contar alertas não lidos
     */
    @Query("SELECT COUNT(*) FROM alerts WHERE isRead = 0")
    suspend fun getUnreadAlertsCount(): Int
}

/**
 * Classe para alertas com informação do remetente
 */
data class AlertWithSender(
    val id: Int,
    val senderId: Int,
    val title: String,
    val message: String,
    val targetGroup: String?,
    val timestamp: Long,
    val isRead: Boolean,
    val senderEmail: String
)