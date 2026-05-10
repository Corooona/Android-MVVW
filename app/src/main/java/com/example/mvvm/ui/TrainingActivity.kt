package com.example.mvvm.ui

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mvvm.FitnessApp
import com.example.mvvm.viewmodel.MainViewModel
import org.json.JSONArray

enum class TrainingStep {
    DETONADOR, RATIONALE, ENTRENAMIENTO
}

class TrainingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = (application as FitnessApp).mainViewModel
        setContent {
            TrainingFlowScreen(viewModel, onBack = { finish() })
        }
    }
}

@Composable
fun TrainingFlowScreen(viewModel: MainViewModel, onBack: () -> Unit) {
    var currentStep by remember { mutableStateOf(TrainingStep.DETONADOR) }
    var isPermissionGranted by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        isPermissionGranted = permissions.values.all { it }
        currentStep = TrainingStep.ENTRENAMIENTO
    }

    val permissionsToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        arrayOf(Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT)
    } else {
        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            when (currentStep) {
                TrainingStep.DETONADOR -> DetonadorScreen(onStart = { currentStep = TrainingStep.RATIONALE }, onBack = onBack)
                TrainingStep.RATIONALE -> RationaleScreen(
                    onAccept = { permissionLauncher.launch(permissionsToRequest) },
                    onDecline = {
                        isPermissionGranted = false
                        currentStep = TrainingStep.ENTRENAMIENTO
                    }
                )
                TrainingStep.ENTRENAMIENTO -> EntrenamientoScreen(
                    granted = isPermissionGranted,
                    viewModel = viewModel,
                    onBack = onBack
                )
            }
        }
    }
}

@Composable
fun DetonadorScreen(onStart: () -> Unit, onBack: () -> Unit) {
    val context = LocalContext.current

    // Leer frases del JSON en assets
    val fraseMotivacional = remember {
        try {
            val jsonString = context.assets.open("frases.json").bufferedReader().use { it.readText() }
            val jsonArray = JSONArray(jsonString)
            val randomIndex = (0 until jsonArray.length()).random()
            jsonArray.getString(randomIndex)
        } catch (e: Exception) {
            "¡A darle con todo hoy!" // Frase de respaldo
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        TextButton(
            onClick = onBack,
            modifier = Modifier.align(Alignment.TopStart).padding(8.dp)
        ) {
            Text("< Volver")
        }

        Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "¿LISTO PARA EL RETO?",
            fontSize = 14.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.primary,
            letterSpacing = 2.sp
        )
        
        Spacer(modifier = Modifier.height(32.dp))

        // Tarjeta con frase motivacional (Sustituye a los círculos de carga)
        Card(
            modifier = Modifier.fillMaxWidth().padding(bottom = 48.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f))
        ) {
            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "“$fraseMotivacional”",
                    fontSize = 18.sp,
                    fontStyle = FontStyle.Italic,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    lineHeight = 24.sp
                )
            }
        }

        Button(
            onClick = onStart,
            modifier = Modifier.fillMaxWidth().height(64.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Iniciar Entrenamiento", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
    }
}

@Composable
fun RationaleScreen(onAccept: () -> Unit, onDecline: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Rationale screen",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Para poder darle mayor información de tu entrenamiento permite el acceso a otros dispositivos con frecuencia cardíaca.",
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text("Allow bluetooth devices", fontWeight = FontWeight.Bold)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                TextButton(onClick = onAccept) { Text("YES", color = Color.Blue) }
                TextButton(onClick = onDecline) { Text("NO", color = Color.Red) }
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = onDecline, modifier = Modifier.fillMaxWidth()) {
                Text("Finalizar Entrenamiento")
            }
        }
    }
}

@Composable
fun EntrenamientoScreen(granted: Boolean, viewModel: MainViewModel, onBack: () -> Unit) {
    var ejercicio by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }
    var reps by remember { mutableStateOf("") }
    
    val mensajeEstado by viewModel.mensajeEstado.observeAsState("")
    val setsCount by viewModel.setsCount.observeAsState(0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            TextButton(onClick = onBack) {
                Text("< Volver")
            }
        }

        Text("Entrenamiento", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        if (granted) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("❤️", fontSize = 32.sp)
                Text(" 155 BPM", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Red)
            }
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("❤️", fontSize = 32.sp, color = Color.Gray)
                Text(" Permite acceso para ver pulso", fontSize = 14.sp, color = Color.Gray)
            }
        }

        Divider(modifier = Modifier.padding(vertical = 16.dp))

        OutlinedTextField(
            value = ejercicio,
            onValueChange = { ejercicio = it },
            label = { Text("Nombre del Ejercicio") },
            modifier = Modifier.fillMaxWidth()
        )
        
        OutlinedTextField(
            value = peso,
            onValueChange = { peso = it },
            label = { Text("Peso (kg)") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )
        
        OutlinedTextField(
            value = reps,
            onValueChange = { reps = it },
            label = { Text("Repeticiones") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )

        Button(
            onClick = {
                viewModel.validarYGuardar(ejercicio, peso, reps)
                peso = ""
                reps = ""
            },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            Text("AGREGAR SERIE")
        }

        Text(
            text = "Series en la sesión: $setsCount",
            modifier = Modifier.padding(top = 8.dp)
        )

        if (mensajeEstado.isNotEmpty()) {
            Text(
                text = mensajeEstado,
                color = if (mensajeEstado.contains("Error")) Color.Red else Color(0xFF009688),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.finalizarEntrenamiento()
                onBack()
            },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE17055))
        ) {
            Text("FINALIZAR ENTRENAMIENTO", fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}
