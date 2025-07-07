package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.database.AppDatabase
import com.example.myapplication.database.AlertWithSender
import com.example.myapplication.repository.AlertRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para gerir alertas
 */
class AlertViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository: AlertRepository
    
    // Lista de alertas
    private val _alerts = MutableStateFlow<List<AlertWithSender>>(emptyList())
    val alerts: StateFlow<List<AlertWithSender>> = _alerts
    
    // Estado de loading
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    // Mensagens
    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message
    
    // Contador de alertas não lidos
    private val _unreadCount = MutableStateFlow(0)
    val unreadCount: StateFlow<Int> = _unreadCount
    
    init {
        val database = AppDatabase.getDatabase(application)
        repository = AlertRepository(database)
        loadAlerts()
        loadUnreadCount()
    }
    
    /**
     * Carregar alertas
     */
    private fun loadAlerts() {
        viewModelScope.launch {
            repository.getAllAlertsWithSender().collect { alertList ->
                _alerts.value = alertList
            }
        }
    }
    
    /**
     * Carregar contador de não lidos
     */
    private fun loadUnreadCount() {
        viewModelScope.launch {
            try {
                val count = repository.getUnreadAlertsCount()
                _unreadCount.value = count
            } catch (e: Exception) {
                // Ignorar erro
            }
        }
    }
    
    /**
     * Criar novo alerta
     */
    fun createAlert(senderId: Int, title: String, message: String, targetGroup: String? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            this@AlertViewModel._message.value = null
            
            try {
                val success = repository.createAlert(senderId, title, message, targetGroup)
                if (success) {
                    this@AlertViewModel._message.value = "Alerta enviado com sucesso!"
                    loadUnreadCount() // Atualizar contador
                } else {
                    this@AlertViewModel._message.value = "Erro ao enviar alerta"
                }
            } catch (e: Exception) {
                this@AlertViewModel._message.value = "Erro ao enviar alerta: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    /**
     * Marcar alerta como lido
     */
    fun markAsRead(alertId: Int) {
        viewModelScope.launch {
            try {
                repository.markAlertAsRead(alertId)
                loadUnreadCount() // Atualizar contador
            } catch (e: Exception) {
                // Ignorar erro
            }
        }
    }
    
    /**
     * Obter alertas por grupo
     */
    fun loadAlertsByGroup(group: String) {
        viewModelScope.launch {
            repository.getAlertsByGroup(group).collect { alertList ->
                _alerts.value = alertList
            }
        }
    }
    
    /**
     * Limpar mensagem
     */
    fun clearMessage() {
        _message.value = null
    }
}