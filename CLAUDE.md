# Android Fitness App — CLAUDE.md

## Descripción general

Aplicación Android de registro de entrenamientos (fitness tracker) construida como práctica del patrón de arquitectura **MVVM**. Permite al usuario registrar series de ejercicios, finalizar sesiones y ver el historial de entrenamientos con XP y volumen acumulado.

## Stack tecnológico

- **Lenguaje**: Kotlin
- **Plataforma**: Android (minSdk implícito por AGP 9.0.1)
- **Arquitectura**: MVVM (Model–View–ViewModel)
- **UI**: XML layouts con Views clásicas (sin Jetpack Compose)
- **Lifecycle**: `androidx.lifecycle` — LiveData + ViewModel
- **Build**: Gradle con Kotlin DSL (`build.gradle.kts`), Version Catalog (`libs.versions.toml`)
- **Dependencias principales**:
  - `androidx.core:core-ktx` 1.17.0
  - `androidx.appcompat:appcompat` 1.7.1
  - `com.google.android.material:material` 1.13.0
  - `androidx.constraintlayout:constraintlayout` 2.2.1
  - `androidx.activity:activity` 1.12.4

## Arquitectura y estructura

```
app/src/main/java/com/example/mvvm/
├── FitnessApp.kt                    # Application class — expone ViewModel compartido
├── ui/
│   ├── LoginActivity.kt             # Pantalla de entrada (launcher)
│   ├── DashboardActivity.kt         # Hub principal post-login
│   ├── MainActivity.kt              # Registro de series en una sesión
│   └── HistorialActivity.kt         # Lista de sesiones pasadas
├── viewmodel/
│   └── MainViewModel.kt             # Único ViewModel — LiveData para estado/historial
└── model/
    └── DataRepository.kt            # Repositorio en memoria + data classes
```

### Flujo de navegación

```
LoginActivity → DashboardActivity → MainActivity (nueva sesión)
                                 → HistorialActivity (ver progreso)
```

El `USER_ID` se pasa como `Intent.putExtra` entre pantallas.

### ViewModel compartido

`FitnessApp` implementa `ViewModelStoreOwner` para que el `MainViewModel` sea accesible desde cualquier Activity y **persista datos entre pantallas** (el historial y los sets de la sesión actual viven aquí).

## Modelo de datos

```kotlin
SetLog(ejercicio: String, peso: Float, reps: Int)
SessionSummary(volumenTotal: Float, xpGanada: Int, huboPR: Boolean, subioDeNivel: Boolean)
SesionHistorial(fecha: String, ejercicios: List<SetLog>, volumenTotal: Float, xpGanada: Int)
```

- El repositorio es **solo en memoria** — no hay persistencia en base de datos ni SharedPreferences.
- `huboPR` y `subioDeNivel` se generan con `Random` (simulados).
- XP = `volumenTotal / 10`

## Convenciones del proyecto

- Prefijos de ID en XML: `et` (EditText), `btn` (Button), `tv` (TextView), `lv` (ListView)
- Español para nombres de variables internas, mensajes UI, y comentarios
- Una sola Activity por pantalla, sin Fragments
- Sin inyección de dependencias (DI manual vía `FitnessApp`)

## Estado actual del desarrollo

El proyecto cubre las tres prácticas implementadas hasta ahora:
1. Implementación MVVM base
2. Conexión Activity ↔ ViewModel con LiveData
3. Navegación entre pantallas con paso de datos e historial

**No hay**: base de datos local (Room), autenticación real, backend, ni tests significativos más allá de los stubs generados por Android Studio.

## Git
- No incluir "Co-authored-by" en los mensajes de commit
- Los mensajes de commit deben ser en español
- No hacer commits automáticos sin mi confirmación