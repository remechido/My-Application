package com.example.myapplication.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * DAO para operações com likes
 */
@Dao
interface LikeDao {
    
    /**
     * Dar like num post
     */
    @Insert
    suspend fun insertLike(like: Like)
    
    /**
     * Remover like de um post
     */
    @Query("DELETE FROM likes WHERE userId = :userId AND postId = :postId")
    suspend fun removeLike(userId: Int, postId: Int)
    
    /**
     * Verificar se utilizador já deu like no post
     */
    @Query("SELECT COUNT(*) FROM likes WHERE userId = :userId AND postId = :postId")
    suspend fun hasUserLikedPost(userId: Int, postId: Int): Int
    
    /**
     * Contar likes de um post
     */
    @Query("SELECT COUNT(*) FROM likes WHERE postId = :postId")
    suspend fun getLikesCount(postId: Int): Int
    
    /**
     * Obter utilizadores que deram like num post
     */
    @Query("""
        SELECT u.* FROM users u 
        INNER JOIN likes l ON u.id = l.userId 
        WHERE l.postId = :postId
    """)
    fun getUsersWhoLikedPost(postId: Int): Flow<List<User>>
}