package com.example.mvvm.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mvvm.FitnessApp
import com.example.mvvm.model.SesionHistorial
import com.example.mvvm.viewmodel.MainViewModel

class HistorialActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = (application as FitnessApp).mainViewModel
        
        setContent {
            MaterialTheme {
                HistorialRPGScreen(viewModel) {
                    finish()
                }
            }
        }
    }
}

@Composable
fun HistorialRPGScreen(viewModel: MainViewModel, onBack: () -> Unit) {
    val sesiones by viewModel.historial.observeAsState(emptyList())
    val nivel = (sesiones.sumOf { it.xpGanada.toDouble() } / 100).toInt() + 1
    val xpTotal = sesiones.sumOf { it.xpGanada }
    
    var isMale by remember { mutableStateOf(true) }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F9FA))) {
        Box(modifier = Modifier.fillMaxWidth()) {
            TextButton(onClick = onBack, modifier = Modifier.padding(4.dp)) {
                Text("< Volver")
            }
        }

        // --- SECCIÓN DEL AVATAR (ESTILO CLARO Y EVOLUCIÓN FÍSICA) ---
        AvatarEvolutionSection(nivel, xpTotal, isMale) {
            isMale = !isMale
        }

        Text(
            "MI PROGRESO",
            modifier = Modifier.padding(start = 20.dp, top = 24.dp, bottom = 12.dp),
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF2D3436),
            fontSize = 18.sp
        )

        var sesionSeleccionada by remember { mutableStateOf<SesionHistorial?>(null) }

        LazyColumn(
            modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(sesiones.reversed()) { sesion ->
                SessionCard(sesion, onClick = { sesionSeleccionada = sesion })
            }
        }

        sesionSeleccionada?.let { sesion ->
            SessionDetailDialog(sesion = sesion, onDismiss = { sesionSeleccionada = null })
        }
        
    }
}

@Composable
fun AvatarEvolutionSection(nivel: Int, xp: Int, isMale: Boolean, onToggleGender: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f, targetValue = 1.02f,
        animationSpec = infiniteRepeatable(tween(2000), RepeatMode.Reverse)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(380.dp)
            .background(Brush.verticalGradient(listOf(Color(0xFFE3F2FD), Color.White)))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("NIVEL", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("$nivel", color = Color(0xFF0984E3), fontWeight = FontWeight.Black, fontSize = 28.sp)
                }
                
                OutlinedButton(
                    onClick = onToggleGender,
                    shape = RoundedCornerShape(50)
                ) {
                    Text(if (isMale) "HOMBRE" else "MUJER", fontSize = 12.sp, color = Color(0xFF0984E3))
                }
            }

            Box(
                modifier = Modifier
                    .size(220.dp)
                    .scale(scale),
                contentAlignment = Alignment.Center
            ) {
                EvolutionBodyCanvas(nivel, isMale, modifier = Modifier.fillMaxSize())
            }

            val estadoFisico = when {
                nivel >= 15 -> "FISICOCULTURISTA"
                nivel >= 10 -> "ATLETA FUERTE"
                nivel >= 5  -> "EN FORMA"
                else        -> "PRINCIPIANTE"
            }
            
            Surface(
                color = Color(0xFF0984E3).copy(alpha = 0.1f),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = estadoFisico,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                    color = Color(0xFF0984E3),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            val progreso = (xp % 100) / 100f
            Column(Modifier.width(260.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                LinearProgressIndicator(
                    progress = progreso,
                    modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                    color = Color(0xFF0984E3),
                    trackColor = Color(0xFFDFE6E9)
                )
                Text("${xp % 100} / 100 XP", color = Color.Gray, fontSize = 11.sp, modifier = Modifier.padding(top = 4.dp))
            }
        }
    }
}

@Composable
fun EvolutionBodyCanvas(nivel: Int, isMale: Boolean, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val grid = 20
        val px = size.width / grid
        
        fun drawPixel(x: Int, y: Int, color: Color) {
            drawRect(
                color = color,
                topLeft = Offset(x * px, y * px),
                size = Size(px + 0.5f, px + 0.5f)
            )
        }

        val skin = Color(0xFFFFDBAC)
        val clothes = Color(0xFF2D3436)
        val hair = if (isMale) Color(0xFF4E342E) else Color(0xFFF9A825)

        // Cabeza
        for (x in 8..11) for (y in 2..6) drawPixel(x, y, skin)
        for (x in 7..12) drawPixel(x, 2, hair)
        if (!isMale) for (y in 3..8) { drawPixel(7, y, hair); drawPixel(12, y, hair) }
        drawPixel(8, 4, Color.Black); drawPixel(11, 4, Color.Black)

        // Cuerpo: Evolución de volumen
        val anchoTorso = when {
            nivel >= 15 -> 4
            nivel >= 10 -> 3
            nivel >= 5  -> 2
            else        -> 1
        }

        val startX = 10 - anchoTorso
        val endX = 9 + anchoTorso
        for (x in startX..endX) {
            for (y in 7..12) drawPixel(x, y, clothes)
        }

        // Brazos: Evolución de volumen
        if (nivel < 5) {
            for (y in 7..10) { drawPixel(startX - 1, y, skin); drawPixel(endX + 1, y, skin) }
        } else if (nivel < 15) {
            for (y in 7..11) {
                drawPixel(startX - 1, y, skin); drawPixel(startX - 2, y, skin)
                drawPixel(endX + 1, y, skin); drawPixel(endX + 2, y, skin)
            }
        } else {
            for (y in 7..12) {
                drawPixel(startX - 1, y, skin); drawPixel(startX - 2, y, skin); drawPixel(startX - 3, y, skin)
                drawPixel(endX + 1, y, skin); drawPixel(endX + 2, y, skin); drawPixel(endX + 3, y, skin)
            }
        }

        // Piernas
        for (y in 13..16) {
            drawPixel(9, y, Color(0xFF636E72))
            drawPixel(10, y, Color(0xFF636E72))
            if (nivel >= 10) {
                drawPixel(8, y, Color(0xFF636E72)); drawPixel(11, y, Color(0xFF636E72))
            }
        }
    }
}

@Composable
fun SessionDetailDialog(sesion: SesionHistorial, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Cerrar") }
        },
        title = {
            Text(sesion.fecha, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        },
        text = {
            Column {
                Text(
                    "${sesion.volumenTotal.toInt()} kg totales · +${sesion.xpGanada} XP",
                    color = Color(0xFF00B894),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                sesion.ejercicios.forEach { set ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(set.ejercicio, modifier = Modifier.weight(1f))
                        Text("${set.peso}kg × ${set.reps}", color = Color.Gray)
                    }
                    Divider()
                }
            }
        }
    )
}

@Composable
fun SessionCard(sesion: SesionHistorial, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(48.dp),
                color = Color(0xFF00B894).copy(alpha = 0.1f),
                shape = CircleShape
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("🔥", fontSize = 20.sp)
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(sesion.fecha, fontWeight = FontWeight.Bold, color = Color(0xFF2D3436), fontSize = 16.sp)
                Text("${sesion.ejercicios.size} ejercicios", color = Color.Gray, fontSize = 13.sp)
            }
            
            Column(horizontalAlignment = Alignment.End) {
                Text("+${sesion.xpGanada} XP", color = Color(0xFF00B894), fontWeight = FontWeight.ExtraBold)
                Text("${sesion.volumenTotal.toInt()} kg", fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}
