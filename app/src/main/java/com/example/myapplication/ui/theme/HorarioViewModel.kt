package com.example.myapplication.data

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.myapplication.model.Aula

class HorarioViewModel : ViewModel() {

    // Lista observável de aulas
    private val _horarios = mutableStateListOf(
        Aula("08:00", "SI", "Design", "Matemática", "TW", "SI"),
        Aula("10:00", "TW", "Matemática", "SI", "Design", "Matemática"),
        Aula("14:00", "Design", "SI", "TW", "Matemática", "Design")
    )

    val horarios: List<Aula> get() = _horarios

    fun adicionarAula(aula: Aula) {
        _horarios.add(aula)
    }
}
