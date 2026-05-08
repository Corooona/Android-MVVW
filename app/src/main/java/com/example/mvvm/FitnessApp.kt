package com.example.mvvm

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.example.mvvm.viewmodel.MainViewModel
import kotlin.system.exitProcess

class FitnessApp : Application(), ViewModelStoreOwner {

    private val appViewModelStore: ViewModelStore by lazy { ViewModelStore() }

    override val viewModelStore: ViewModelStore
        get() = appViewModelStore

    val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreate() {
        super.onCreate()
        
        // --- CONFIGURACIÓN DE DEBUG GLOBAL ---
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Log.e("CRASH_DEBUG", "--------------------------------------------------")
            Log.e("CRASH_DEBUG", "LA APP SE HA CRASHEADO EN EL HILO: ${thread.name}")
            Log.e("CRASH_DEBUG", "MOTIVO DEL ERROR: ${throwable.message}")
            Log.e("CRASH_DEBUG", "CAUSA: ${throwable.cause}")
            Log.e("CRASH_DEBUG", "STACKTRACE COMPLETO:", throwable)
            Log.e("CRASH_DEBUG", "--------------------------------------------------")
            
            // Esto permite que el sistema termine de cerrar la app después de loguear
            exitProcess(1)
        }
        
        Log.d("APP_DEBUG", "FitnessApp inicializada correctamente")
    }
}
