# 🏋️ Fitness App - Android MVVM

¡Bienvenido(a) a la documentación de esta aplicación de Fitness! 
Este documento está diseñado para cualquier persona, sin importar su nivel de conocimiento técnico o si es la primera vez que escucha sobre Kotlin. Aquí te explicaré detalladamente y de forma muy sencilla qué hace esta aplicación y cómo funciona por dentro.

---

## 🎯 ¿De qué trata esta aplicación?

Es una aplicación para Android diseñada para registrar tus entrenamientos en el gimnasio y gamificar (hacer como un juego) tu progreso. La aplicación permite:
- Registrar los ejercicios que realizas, el peso que levantas y cuántas repeticiones haces.
- Calcular automáticamente el **Volumen de Entrenamiento** (Peso x Repeticiones).
- Otorgar **Puntos de Experiencia (XP)** al usuario basados en su esfuerzo.
- Evaluar la posibilidad de haber roto un **Récord Personal (PR)** o si el usuario **sube de nivel**.
- Descargar y mostrar ejercicios reales desde una base de datos en internet (API de Wger).
- Consultar un **Historial** de todas las sesiones pasadas.

---

## 🛠️ ¿Cómo está construida? (Arquitectura)

La aplicación usa una forma de organizarse por dentro llamada **MVVM**, siglas de *Model-View-ViewModel* (Modelo - Vista - Modelo de Vista). Esto es como separar el trabajo de un restaurante en diferentes roles para que no haya caos:

1. **La Vista (View) - El Mesero y el Menú de Comida**
   - **Qué es:** Es todo lo que el usuario ve en la pantalla de su celular (los botones, textos, imágenes y colores). 
   - **Cómo se hace:** En lugar de diseñar cada pantalla usando métodos antiguos y complicados, la app utiliza un sistema moderno llamado **Jetpack Compose** que "dibuja" la pantalla de manera rápida y elegante.
   - **Archivos principales:** `MainActivity.kt`, `DashboardActivity.kt`, `ExercisesActivity.kt`, `HistorialActivity.kt`.

2. **El Modelo (Model) - La Cocina y Almacén**
   - **Qué es:** Es la parte que maneja y almacena toda la información (los datos crudos). Se encarga de hacer los cálculos matemáticas (sumar tu XP), guardar el historial de entrenamientos, o de ir a internet a pedir la información de los ejercicios. 
   - **Archivos principales:** `DataRepository.kt` (El almacén local que guarda temporalmente tus sesiones y tu historial de puntos) y `Exercise.kt` (la estructura que dice cómo se ve la información de un ejercicio, con sus músculos, equipamiento, etc).

3. **El Modelo de Vista (ViewModel) - El Gerente del Restaurante**
   - **Qué es:** Se encarga de conectar la cocina con los meseros. La Vista (pantalla) solo sabe *mostrar* cosas, y el Modelo solo sabe *calcular* cosas. El **ViewModel** toma los datos crudos del Modelo, los prepara de una forma bonita y se los entrega a la Vista para que los ponga en la pantalla. Así, si minimizas la aplicación o volteas tu celular, el ViewModel resguarda la información para que no se pierda nada.
   - **Archivo principal:** `MainViewModel.kt`

---

## 🚀 Flujo de la Aplicación (Pantallas Principales)

* **Pantalla de Inicio de Sesión (Login):** Una pantalla básica donde pones tu usuario y contraseña para ingresar a la aplicación.
* **El Panel Principal (Dashboard):** Aquí es donde registras tu actividad. Escribes qué ejercicio hiciste, el peso y las repeticiones. Cuando presionas "Finalizar Entrenamiento", la aplicación calcula toda tu XP y te dice si has evolucionado de nivel o si tienes un nuevo récord.
* **Historial:** ¿Quieres saber qué hiciste ayer? Esta pantalla se conecta a la información guardada y te muestra un resumen con las fechas de tus sesiones, peso levantado en total y XP ganada.
* **Lista de Ejercicios Reales:** Se conecta a internet para descargar un catálogo de ejercicios disponibles gracias a la parte del sistema encargada de "hablar" con páginas web.

---

## 🧰 Herramientas "Mágicas" que utiliza (Tecnologías)

Esta aplicación no está hecha desde cero instrucción a instrucción, utiliza herramientas preconstruidas (llamadas *Librerías*) para hacer las cosas más rápido y mejor:

* **1. Kotlin:** Este es el lenguaje en el que está escrito todo. Fue creado para ser limpio, claro y evitar muchos problemas que tenían otros lenguajes antiguos. Imagina que en vez de escribir un documento legal de 20 páginas para encender un botón, con Kotlin basta con medio párrafo.
* **2. Corrutinas (Coroutines):** Imagina que le pides a la aplicación que descargue 50 ejercicios de internet. Si la aplicación hiciera eso directamente, la pantalla se congelaría y no podrías mover nada hasta que termine de descargar. Las *Corrutinas* son pequeños "trabajadores invisibles" que hacen las tareas difíciles en el fondo (background) silenciosamente, permitiendo que la pantalla siga reaccionando siempre suave y rápida.
* **3. Retrofit y Gson:** Son los carteros de la aplicación. Se dedican exclusivamente a viajar a internet, tocar la puerta de una página externa (llamada *API de Wger*), agarrar la lista enorme de ejercicios en formato de texto aburrido, volver a la aplicación y convertir esa lista en "tarjetas visuales" entendibles para la aplicación. Todo en microsegundos. *(Archivo: `ApiService.kt`)*
* **4. Coil:** Es el artista de la app. Si Retrofit trae el nombre de un ejercicio y la dirección web de una imagen, Coil se encarga de ir a descargar la foto y pegarla suavemente en la pantalla para que puedas ver qué ejercicio es.

---

## 📝 Resumen final

En resumen, la aplicación está fuertemente dividida por responsabilidades (MVVM). Si mañana quieres cambiar el color de un botón, solo modificas la **Vista**. Si mañana quieres que la fórmula para ganar XP sea distinta, solo modificas el **Modelo**. Esta buena organización permite que la aplicación pueda crecer y evolucionar fácilmente en el futuro sin que se convierta en un enredo o un dolor de cabeza para el programador.
