package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.myapplication.data.HorarioViewModel
import com.example.myapplication.model.Aula

@Composable
fun AdminScreen(navController: NavHostController, horarioViewModel: HorarioViewModel = viewModel()) {
    var isAuthenticated by remember { mutableStateOf(false) }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    if (isAuthenticated) {
        AdminPanel(navController, horarioViewModel)
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Login de Administrador",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFF4CAF50),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Utilizador") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Senha") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (username == "admin" && password == "1234") {
                        isAuthenticated = true
                        errorMessage = ""
                    } else {
                        errorMessage = "Credenciais inválidas"
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("Entrar", color = Color.White, style = MaterialTheme.typography.titleMedium)
            }

            if (errorMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(errorMessage, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
fun AdminPanel(navController: NavHostController, horarioViewModel: HorarioViewModel) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabTitles = listOf("Criar Utilizador", "Lista de Utilizador", "Adicionar Post", "Adicionar Horário")
    val userList = remember { mutableStateListOf<String>() }

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
                    navController.navigate("login") {
                        popUpTo("admin") { inclusive = true }
                    }
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

        when (selectedTab) {
            0 -> CreateUserTab(userList)
            1 -> UserListTab(userList)
            2 -> AddPostTab()
            3 -> AddHorarioTab(horarioViewModel)
        }
    }
}

@Composable
fun CreateUserTab(userList: MutableList<String>) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

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
                    userList.add(email)
                    message = "Utilizador criado com sucesso!"
                    email = ""
                    password = ""
                } else {
                    message = "Preencha todos os campos."
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
        ) {
            Text("Criar Utilizador", color = Color.White)
        }

        if (message.isNotEmpty()) {
            Text(
                message,
                color = if (message.contains("sucesso")) Color(0xFF388E3C) else MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun UserListTab(userList: List<String>) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(userList.size) { index ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Text(
                    text = userList[index],
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
fun AddPostTab() {
    var imageUrl by remember { mutableStateOf("") }
    var postText by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = imageUrl,
            onValueChange = { imageUrl = it },
            label = { Text("URL da Imagem") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = postText,
            onValueChange = { postText = it },
            label = { Text("Texto do Post") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 4
        )

        Button(
            onClick = {
                if (imageUrl.isNotBlank() && postText.isNotBlank()) {
                    message = "Post adicionado!"
                    imageUrl = ""
                    postText = ""
                } else {
                    message = "Preencha todos os campos."
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
        ) {
            Text("Adicionar Post", color = Color.White)
        }

        if (message.isNotEmpty()) {
            Text(
                message,
                color = if (message.contains("adicionado")) Color(0xFF388E3C) else MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun AddHorarioTab(viewModel: HorarioViewModel) {
    var horario by remember { mutableStateOf("") }
    var seg by remember { mutableStateOf("") }
    var ter by remember { mutableStateOf("") }
    var qua by remember { mutableStateOf("") }
    var qui by remember { mutableStateOf("") }
    var sex by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(value = horario, onValueChange = { horario = it }, label = { Text("Horário") })
        OutlinedTextField(value = seg, onValueChange = { seg = it }, label = { Text("Segunda") })
        OutlinedTextField(value = ter, onValueChange = { ter = it }, label = { Text("Terça") })
        OutlinedTextField(value = qua, onValueChange = { qua = it }, label = { Text("Quarta") })
        OutlinedTextField(value = qui, onValueChange = { qui = it }, label = { Text("Quinta") })
        OutlinedTextField(value = sex, onValueChange = { sex = it }, label = { Text("Sexta") })

        Button(
            onClick = {
                if (horario.isNotBlank()) {
                    viewModel.adicionarAula(Aula(horario, seg, ter, qua, qui, sex))
                    message = "Horário adicionado com sucesso!"
                    horario = ""; seg = ""; ter = ""; qua = ""; qui = ""; sex = ""
                } else {
                    message = "Preencha o campo de horário."
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
        ) {
            Text("Adicionar Horário", color = Color.White)
        }

        if (message.isNotEmpty()) {
            Text(
                message,
                color = if (message.contains("sucesso")) Color(0xFF388E3C) else MaterialTheme.colorScheme.error
            )
        }
    }
}
