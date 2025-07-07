package com.example.myapplication.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * DAO para operações com horários
 */
@Dao
interface ScheduleDao {
    
    /**
     * Inserir novo horário
     */
    @Insert
    suspend fun insertSchedule(schedule: Schedule)
    
    /**
     * Obter todos os horários
     */
    @Query("SELECT * FROM schedules ORDER BY timeSlot ASC")
    fun getAllSchedules(): Flow<List<Schedule>>
    
    /**
     * Atualizar horário
     */
    @Update
    suspend fun updateSchedule(schedule: Schedule)
    
    /**
     * Eliminar horário
     */
    @Delete
    suspend fun deleteSchedule(schedule: Schedule)
    
    /**
     * Limpar todos os horários
     */
    @Query("DELETE FROM schedules")
    suspend fun clearAllSchedules()
}