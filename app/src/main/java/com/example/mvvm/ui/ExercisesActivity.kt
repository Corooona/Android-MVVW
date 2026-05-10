package com.example.mvvm.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mvvm.FitnessApp
import com.example.mvvm.model.Exercise
import com.example.mvvm.viewmodel.ExercisesState
import com.example.mvvm.viewmodel.MainViewModel

class ExercisesActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Obtiene el ViewModel compartido desde FitnessApp, igual que las demás Activities
        val viewModel = (application as FitnessApp).mainViewModel

        // Dispara la llamada a la API al entrar a la pantalla
        viewModel.fetchExercises()

        setContent {
            val state by viewModel.exercisesState.observeAsState(ExercisesState.Loading)

            ExercisesScreen(
                state = state,
                onRetry = { viewModel.fetchExercises() },
                onBack = { finish() }
            )
        }
    }
}

@Composable
fun ExercisesScreen(
    state: ExercisesState,
    onRetry: () -> Unit,
    onBack: () -> Unit
) {
    MaterialTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.fillMaxWidth()) {
                TextButton(onClick = onBack, modifier = Modifier.padding(4.dp)) {
                    Text("< Volver")
                }
            }
            when (state) {
                is ExercisesState.Loading -> LoadingView()
                is ExercisesState.Success -> ExerciseList(exercises = state.exercises)
                is ExercisesState.Error   -> ErrorView(message = state.message, onRetry = onRetry)
            }
        }
    }
}

// --- Estados individuales ---

@Composable
private fun LoadingView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ExerciseList(exercises: List<Exercise>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(exercises) { exercise ->
            ExerciseCard(exercise = exercise)
        }
    }
}

@Composable
private fun ExerciseCard(exercise: Exercise) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // El nombre viene de translations[0]. Si la lista llega vacía, usamos el ID como fallback.
            val nombre = exercise.translations.firstOrNull()?.name?.takeIf { it.isNotBlank() }
                ?: "Ejercicio #${exercise.id}"
            Text(
                text = nombre,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            // category ahora es un objeto con .name en lugar de un Int
            Text(
                text = "Categoría: ${exercise.category.name ?: "Sin categoría"}",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                text = if (exercise.muscles.isEmpty()) "Músculos: —"
                       else "Músculos: ${exercise.muscles.joinToString(", ") { it.name ?: "" }}",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Composable
private fun ErrorView(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Error: $message",
            color = MaterialTheme.colorScheme.error,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text("Reintentar")
        }
    }
}
