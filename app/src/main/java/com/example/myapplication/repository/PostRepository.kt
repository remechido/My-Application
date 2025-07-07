package com.example.myapplication.repository

import com.example.myapplication.database.AppDatabase
import com.example.myapplication.database.Post
import com.example.myapplication.database.PostWithAuthor
import com.example.myapplication.database.Like
import kotlinx.coroutines.flow.Flow

/**
 * Repositório para gerir posts e likes
 */
class PostRepository(private val database: AppDatabase) {
    
    private val postDao = database.postDao()
    private val likeDao = database.likeDao()
    
    /**
     * Criar novo post
     */
    suspend fun createPost(authorId: Int, imageUrl: String, caption: String): Boolean {
        return try {
            val post = Post(
                authorId = authorId,
                imageUrl = imageUrl,
                caption = caption
            )
            postDao.insertPost(post)
            true
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Obter todos os posts com informação do autor
     */
    fun getAllPostsWithAuthor(): Flow<List<PostWithAuthor>> {
        return postDao.getAllPostsWithAuthor()
    }
    
    /**
     * Dar/remover like num post
     */
    suspend fun toggleLike(userId: Int, postId: Int): Boolean {
        return try {
            val hasLiked = likeDao.hasUserLikedPost(userId, postId) > 0
            
            if (hasLiked) {
                // Remover like
                likeDao.removeLike(userId, postId)
            } else {
                // Dar like
                val like = Like(userId = userId, postId = postId)
                likeDao.insertLike(like)
            }
            
            // Atualizar contador de likes no post
            val newCount = likeDao.getLikesCount(postId)
            postDao.updateLikesCount(postId, newCount)
            
            true
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Verificar se utilizador deu like no post
     */
    suspend fun hasUserLikedPost(userId: Int, postId: Int): Boolean {
        return likeDao.hasUserLikedPost(userId, postId) > 0
    }
    
    /**
     * Obter número de likes de um post
     */
    suspend fun getLikesCount(postId: Int): Int {
        return likeDao.getLikesCount(postId)
    }
}