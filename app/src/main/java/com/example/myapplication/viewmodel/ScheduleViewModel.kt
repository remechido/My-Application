package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.database.AppDatabase
import com.example.myapplication.database.Schedule
import com.example.myapplication.repository.ScheduleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para gerir horários
 */
class ScheduleViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository: ScheduleRepository
    
    // Lista de horários
    private val _schedules = MutableStateFlow<List<Schedule>>(emptyList())
    val schedules: StateFlow<List<Schedule>> = _schedules
    
    // Estado de loading
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    // Mensagens
    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message
    
    init {
        val database = AppDatabase.getDatabase(application)
        repository = ScheduleRepository(database)
        loadSchedules()
    }
    
    /**
     * Carregar horários
     */
    private fun loadSchedules() {
        viewModelScope.launch {
            repository.getAllSchedules().collect { scheduleList ->
                _schedules.value = scheduleList
            }
        }
    }
    
    /**
     * Criar novo horário
     */
    fun createSchedule(timeSlot: String, monday: String, tuesday: String, wednesday: String, thursday: String, friday: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _message.value = null
            
            try {
                val schedule = Schedule(
                    timeSlot = timeSlot,
                    monday = monday,
                    tuesday = tuesday,
                    wednesday = wednesday,
                    thursday = thursday,
                    friday = friday
                )
                val success = repository.createSchedule(schedule)
                if (success) {
                    _message.value = "Horário criado com sucesso!"
                } else {
                    _message.value = "Erro ao criar horário"
                }
            } catch (e: Exception) {
                _message.value = "Erro ao criar horário: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    /**
     * Eliminar horário
     */
    fun deleteSchedule(schedule: Schedule) {
        viewModelScope.launch {
            _isLoading.value = true
            _message.value = null
            
            try {
                val success = repository.deleteSchedule(schedule)
                if (success) {
                    _message.value = "Horário eliminado com sucesso!"
                } else {
                    _message.value = "Erro ao eliminar horário"
                }
            } catch (e: Exception) {
                _message.value = "Erro ao eliminar horário: ${e.message}"
            } finally {
                _isLoading.value = false
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