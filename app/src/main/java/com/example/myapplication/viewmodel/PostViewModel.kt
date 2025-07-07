package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.database.AppDatabase
import com.example.myapplication.database.PostWithAuthor
import com.example.myapplication.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para gerir posts e likes
 */
class PostViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository: PostRepository
    
    // Lista de posts
    private val _posts = MutableStateFlow<List<PostWithAuthor>>(emptyList())
    val posts: StateFlow<List<PostWithAuthor>> = _posts
    
    // Estado de loading
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    // Mensagens
    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message
    
    // Posts com likes do utilizador atual
    private val _userLikes = MutableStateFlow<Set<Int>>(emptySet())
    val userLikes: StateFlow<Set<Int>> = _userLikes
    
    init {
        val database = AppDatabase.getDatabase(application)
        repository = PostRepository(database)
        loadPosts()
    }
    
    /**
     * Carregar posts
     */
    private fun loadPosts() {
        viewModelScope.launch {
            repository.getAllPostsWithAuthor().collect { postList ->
                _posts.value = postList
            }
        }
    }
    
    /**
     * Criar novo post
     */
    fun createPost(authorId: Int, imageUrl: String, caption: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _message.value = null
            
            try {
                val success = repository.createPost(authorId, imageUrl, caption)
                if (success) {
                    _message.value = "Post criado com sucesso!"
                } else {
                    _message.value = "Erro ao criar post"
                }
            } catch (e: Exception) {
                _message.value = "Erro ao criar post: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    /**
     * Dar/remover like
     */
    fun toggleLike(userId: Int, postId: Int) {
        viewModelScope.launch {
            try {
                val success = repository.toggleLike(userId, postId)
                if (success) {
                    // Atualizar estado dos likes do utilizador
                    loadUserLikes(userId)
                }
            } catch (e: Exception) {
                _message.value = "Erro ao dar like: ${e.message}"
            }
        }
    }
    
    /**
     * Carregar likes do utilizador
     */
    fun loadUserLikes(userId: Int) {
        viewModelScope.launch {
            try {
                val likedPosts = mutableSetOf<Int>()
                _posts.value.forEach { post ->
                    if (repository.hasUserLikedPost(userId, post.id)) {
                        likedPosts.add(post.id)
                    }
                }
                _userLikes.value = likedPosts
            } catch (e: Exception) {
                // Ignorar erro silenciosamente
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