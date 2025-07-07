package com.example.myapplication.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * DAO para operações com posts
 */
@Dao
interface PostDao {
    
    /**
     * Inserir novo post
     */
    @Insert
    suspend fun insertPost(post: Post)
    
    /**
     * Obter todos os posts ordenados por data
     */
    @Query("""
        SELECT p.*, u.email as authorEmail 
        FROM posts p 
        INNER JOIN users u ON p.authorId = u.id 
        ORDER BY p.timestamp DESC
    """)
    fun getAllPostsWithAuthor(): Flow<List<PostWithAuthor>>
    
    /**
     * Obter posts de um utilizador específico
     */
    @Query("SELECT * FROM posts WHERE authorId = :userId ORDER BY timestamp DESC")
    fun getPostsByUser(userId: Int): Flow<List<Post>>
    
    /**
     * Eliminar post
     */
    @Delete
    suspend fun deletePost(post: Post)
    
    /**
     * Atualizar contador de likes
     */
    @Query("UPDATE posts SET likesCount = :count WHERE id = :postId")
    suspend fun updateLikesCount(postId: Int, count: Int)
}

/**
 * Classe para posts com informação do autor
 */
data class PostWithAuthor(
    val id: Int,
    val authorId: Int,
    val imageUrl: String,
    val caption: String,
    val timestamp: Long,
    val likesCount: Int,
    val authorEmail: String
)