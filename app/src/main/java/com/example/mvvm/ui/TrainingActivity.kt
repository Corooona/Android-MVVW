package com.example.mvvm.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat

enum class TrainingStep {
    DETONADOR, RATIONALE, ENTRENAMIENTO
}

class TrainingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrainingFlowScreen()
        }
    }
}

@Composable
fun TrainingFlowScreen() {
    var currentStep by remember { mutableStateOf(TrainingStep.DETONADOR) }
    var isPermissionGranted by remember { mutableStateOf(false) }
    val context = LocalContext.current

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
                TrainingStep.DETONADOR -> DetonadorScreen(onStart = { currentStep = TrainingStep.RATIONALE })
                TrainingStep.RATIONALE -> RationaleScreen(
                    onAccept = { permissionLauncher.launch(permissionsToRequest) },
                    onDecline = {
                        isPermissionGranted = false
                        currentStep = TrainingStep.ENTRENAMIENTO
                    }
                )
                TrainingStep.ENTRENAMIENTO -> EntrenamientoScreen(
                    granted = isPermissionGranted,
                    onFinish = { /* Volver o finalizar */ }
                )
            }
        }
    }
}

@Composable
fun DetonadorScreen(onStart: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = onStart,
            modifier = Modifier.fillMaxWidth().height(60.dp)
        ) {
            Text("Iniciar Entrenamiento", fontSize = 18.sp)
        }
        Spacer(modifier = Modifier.height(40.dp))
        // Simulando las gráficas del dibujo
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Box(Modifier.size(80.dp).padding(4.dp)) { CircularProgressIndicator(progress = 0.7f) }
            Box(Modifier.size(80.dp).padding(4.dp)) { CircularProgressIndicator(progress = 0.4f) }
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
fun EntrenamientoScreen(granted: Boolean, onFinish: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Entrenamiento", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(20.dp))
        Text("~~~~~~~~~~~~~~~~~~~~~~~~~~~")
        Text("~~~~~~~~~~~~~~~~~~~~~~~~~~~")
        
        Spacer(modifier = Modifier.height(40.dp))
        
        if (granted) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("❤️", fontSize = 40.sp)
                Text(" 155", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.Red)
            }
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("❤️", fontSize = 40.sp, color = Color.Gray)
                Text(" Permite acceso", fontSize = 16.sp, color = Color.Gray)
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = onFinish, modifier = Modifier.fillMaxWidth()) {
            Text("Finalizar entrenamiento")
        }
    }
}
