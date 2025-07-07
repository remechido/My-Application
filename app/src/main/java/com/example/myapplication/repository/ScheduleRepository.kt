package com.example.myapplication.repository

import com.example.myapplication.database.AppDatabase
import com.example.myapplication.database.Schedule
import kotlinx.coroutines.flow.Flow

/**
 * Repositório para gerir horários
 */
class ScheduleRepository(private val database: AppDatabase) {
    
    private val scheduleDao = database.scheduleDao()
    
    /**
     * Criar novo horário
     */
    suspend fun createSchedule(schedule: Schedule): Boolean {
        return try {
            scheduleDao.insertSchedule(schedule)
            true
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Obter todos os horários
     */
    fun getAllSchedules(): Flow<List<Schedule>> {
        return scheduleDao.getAllSchedules()
    }
    
    /**
     * Atualizar horário
     */
    suspend fun updateSchedule(schedule: Schedule): Boolean {
        return try {
            scheduleDao.updateSchedule(schedule)
            true
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Eliminar horário
     */
    suspend fun deleteSchedule(schedule: Schedule): Boolean {
        return try {
            scheduleDao.deleteSchedule(schedule)
            true
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Limpar todos os horários
     */
    suspend fun clearAllSchedules(): Boolean {
        return try {
            scheduleDao.clearAllSchedules()
            true
        } catch (e: Exception) {
            false
        }
    }
}