package com.example.myapplication.repository

import com.example.myapplication.database.AppDatabase
import com.example.myapplication.database.Alert
import com.example.myapplication.database.AlertWithSender
import kotlinx.coroutines.flow.Flow

/**
 * Repositório para gerir alertas
 */
class AlertRepository(private val database: AppDatabase) {
    
    private val alertDao = database.alertDao()
    
    /**
     * Criar novo alerta
     */
    suspend fun createAlert(senderId: Int, title: String, message: String, targetGroup: String? = null): Boolean {
        return try {
            val alert = Alert(
                senderId = senderId,
                title = title,
                message = message,
                targetGroup = targetGroup
            )
            alertDao.insertAlert(alert)
            true
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Obter todos os alertas
     */
    fun getAllAlertsWithSender(): Flow<List<AlertWithSender>> {
        return alertDao.getAllAlertsWithSender()
    }
    
    /**
     * Obter alertas por grupo
     */
    fun getAlertsByGroup(group: String): Flow<List<AlertWithSender>> {
        return alertDao.getAlertsByGroup(group)
    }
    
    /**
     * Marcar alerta como lido
     */
    suspend fun markAlertAsRead(alertId: Int): Boolean {
        return try {
            alertDao.markAlertAsRead(alertId)
            true
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Contar alertas não lidos
     */
    suspend fun getUnreadAlertsCount(): Int {
        return alertDao.getUnreadAlertsCount()
    }
}