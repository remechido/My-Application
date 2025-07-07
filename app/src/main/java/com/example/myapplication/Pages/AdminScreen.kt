package com.example.myapplication.Pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.myapplication.database.User
import com.example.myapplication.viewmodel.AdminViewModel

@Composable
fun AdminScreen(
    navController: NavHostController,
    onLogout: () -> Unit,
    adminViewModel: AdminViewModel = viewModel()
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabTitles = listOf("Criar Utilizador", "Gerir Utilizadores")
    
    // Observar estados do ViewModel
    val users by adminViewModel.users.collectAsState()
    val isLoading by adminViewModel.isLoading.collectAsState()
    val message by adminViewModel.message.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Painel do Admin",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFF4CAF50)
            )
            Button(
                onClick = {
                    onLogout()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Sair", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color(0xFFE8F5E9),
            contentColor = Color(0xFF4CAF50),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(4.dp, RoundedCornerShape(12.dp))
                .background(Color(0xFFE8F5E9), RoundedCornerShape(12.dp))
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Mostrar mensagem se existir
        message?.let { msg ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (msg.contains("sucesso")) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                )
            ) {
                Text(
                    text = msg,
                    modifier = Modifier.padding(16.dp),
                    color = if (msg.contains("sucesso")) Color(0xFF2E7D32) else Color(0xFFD32F2F)
                )
            }
        }

        when (selectedTab) {
            0 -> CreateUserTab(adminViewModel, isLoading)
            1 -> ManageUsersTab(users, adminViewModel, isLoading)
        }
    }
}

@Composable
fun CreateUserTab(adminViewModel: AdminViewModel, isLoading: Boolean) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isAdmin by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Senha") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )

        Button(
            onClick = {
                if (email.isNotBlank() && password.isNotBlank()) {
                    adminViewModel.createUser(email, password, isAdmin)
                    email = ""
                    password = ""
                    isAdmin = false
                } else {
                    // Mensagem será gerida pelo ViewModel
                }
            },
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Text("Criar Utilizador", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        
        // Checkbox para definir se é administrador
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = isAdmin,
                onCheckedChange = { isAdmin = it }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("É Administrador")
        }
    }
}

@Composable
fun ManageUsersTab(users: List<User>, adminViewModel: AdminViewModel, isLoading: Boolean) {
    var selectedUser by remember { mutableStateOf<User?>(null) }
    var showPasswordDialog by remember { mutableStateOf(false) }
    var newPassword by remember { mutableStateOf("") }
    
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(users) { user ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = user.email,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (user.isAdmin) "Administrador" else "Utilizador",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (user.isAdmin) Color(0xFF2E7D32) else Color(0xFF666666)
                        )
                    }
                    
                    Row {
                        // Botão para alterar senha
                        IconButton(
                            onClick = {
                                selectedUser = user
                                showPasswordDialog = true
                            }
                        ) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Alterar Senha",
                                tint = Color(0xFF4CAF50)
                            )
                        }
                        
                        // Botão para eliminar (não permitir eliminar admin)
                        if (!user.isAdmin) {
                            IconButton(
                                onClick = {
                                    adminViewModel.deleteUser(user)
                                }
                            ) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Eliminar",
                                    tint = Color(0xFFD32F2F)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    
    // Dialog para alterar senha
    if (showPasswordDialog && selectedUser != null) {
        AlertDialog(
            onDismissRequest = { 
                showPasswordDialog = false
                newPassword = ""
            },
            title = { Text("Alterar Senha") },
            text = {
                Column {
                    Text("Alterar senha para: ${selectedUser!!.email}")
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = { Text("Nova Senha") },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (newPassword.isNotBlank()) {
                            adminViewModel.changeUserPassword(selectedUser!!.id, newPassword)
                            showPasswordDialog = false
                            newPassword = ""
                        }
                    }
                ) {
                    Text("Alterar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { 
                        showPasswordDialog = false
                        newPassword = ""
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}
