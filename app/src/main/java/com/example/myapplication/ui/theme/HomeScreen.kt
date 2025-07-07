package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Dados do post
data class Post(val username: String, val content: String)

// Barra superior estilo Instagram
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstagramTopBar() {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "BejaConnect",
                style = MaterialTheme.typography.headlineSmall,
                color = Color(0xFF1B5E20)
            )
        },
        navigationIcon = {
            IconButton(onClick = { /* ação da câmera */ }) {
                Icon(
                    imageVector = Icons.Filled.PhotoCamera,
                    contentDescription = "Abrir câmera",
                    tint = Color(0xFF1B5E20)
                )
            }
        },
        actions = {
            IconButton(onClick = { /* ação de mensagens */ }) {
                Icon(
                    imageVector = Icons.Filled.Send,
                    contentDescription = "Mensagens",
                    tint = Color(0xFF1B5E20)
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color(0xFFE8F5E9)
        )
    )
}

// Item do post (post no feed)
@Composable
fun AcademicPostItem(post: Post) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
        ) {
            Text(
                text = post.username,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B5E20)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = post.content,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 16.sp,
                color = Color(0xFF388E3C)
            )
        }
    }
}

// Tela principal
@Composable
fun HomeScreen() {
    val postList = listOf(
        Post("prof.joao", "Discussão sobre a teoria da relatividade 📚"),
        Post("dr.ana", "Novo artigo publicado sobre IA 🤖"),
        Post("maria.research", "Resultados interessantes do nosso estudo 🌱")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE8F5E9))
    ) {
        InstagramTopBar()

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(postList) { post ->
                AcademicPostItem(post)
            }
        }
    }
}
