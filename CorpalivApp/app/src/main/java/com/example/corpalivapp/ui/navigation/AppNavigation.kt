package com.example.corpalivapp.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.corpalivapp.data.local.CorpalivDatabase
import com.example.corpalivapp.data.model.Colaboracion
import com.example.corpalivapp.ui.screens.CatalogoScreen
import com.example.corpalivapp.ui.screens.ContactoScreen
import com.example.corpalivapp.ui.screens.EditarSolicitudScreen
import com.example.corpalivapp.ui.screens.HomeScreen
import com.example.corpalivapp.ui.screens.PanelVinculacionScreen
import com.example.corpalivapp.ui.screens.ProgramasScreen
import com.example.corpalivapp.ui.screens.SolicitudesScreen

@Composable
fun AppNavigation(database: CorpalivDatabase) {
    var pantallaActual by rememberSaveable {
        mutableStateOf("inicio")
    }

    // Solicitud elegida en "Mis solicitudes" para editar (null = ninguna).
    var solicitudEnEdicion by remember {
        mutableStateOf<Colaboracion?>(null)
    }

    // Botón atrás del sistema: desde "editar" vuelve a "Mis solicitudes"; desde las demás, al inicio.
    BackHandler(enabled = pantallaActual != "inicio") {
        if (pantallaActual == "editar") {
            pantallaActual = "solicitudes"
        } else {
            pantallaActual = "inicio"
        }
    }

    // Cada vez que cambia pantallaActual, la pantalla vieja sale y la nueva entra deslizándose.
    AnimatedContent(
        targetState = pantallaActual,
        transitionSpec = {
            val vuelveAtras = targetState == "inicio" || initialState == "editar"
            crearTransicion(vuelveAtras)
        },
        label = "navegacion"
    ) { pantalla ->
        when (pantalla) {
            "programas" -> ProgramasScreen(
                onVolver = { pantallaActual = "inicio" },
                database = database
            )

            "catalogo" -> CatalogoScreen(
                onVolver = { pantallaActual = "inicio" },
                database = database
            )

            "contacto" -> ContactoScreen(
                onVolver = { pantallaActual = "inicio" },
                database = database
            )

            "solicitudes" -> SolicitudesScreen(
                onVolver = { pantallaActual = "inicio" },
                onEditar = { colaboracion ->
                    solicitudEnEdicion = colaboracion
                    pantallaActual = "editar"
                },
                database = database
            )

            "editar" -> PantallaEditar(
                colaboracion = solicitudEnEdicion,
                onVolver = { pantallaActual = "solicitudes" },
                database = database
            )

            "panel" -> PanelVinculacionScreen(
                onVolver = { pantallaActual = "inicio" },
                database = database
            )

            else -> HomeScreen(
                onProgramasClick = { pantallaActual = "programas" },
                onCatalogoClick = { pantallaActual = "catalogo" },
                onContactoClick = { pantallaActual = "contacto" },
                onSolicitudesClick = { pantallaActual = "solicitudes" },
                onPanelClick = { pantallaActual = "panel" }
            )
        }
    }
}

// Si la app se recreó (por ejemplo al girar el teléfono) y se perdió la solicitud elegida,
// se vuelve a "Mis solicitudes" en vez de mostrar un formulario vacío.
@Composable
private fun PantallaEditar(
    colaboracion: Colaboracion?,
    onVolver: () -> Unit,
    database: CorpalivDatabase
) {
    if (colaboracion == null) {
        LaunchedEffect(Unit) {
            onVolver()
        }
        return
    }
    EditarSolicitudScreen(
        colaboracion = colaboracion,
        onVolver = onVolver,
        database = database
    )
}

// Al avanzar, la nueva pantalla llega desde la derecha.
// Al volver (al inicio o desde "editar"), llega desde la izquierda.
private fun crearTransicion(vuelveAtras: Boolean): ContentTransform {
    if (vuelveAtras) {
        val entrada = slideInHorizontally { ancho -> -ancho } + fadeIn()
        val salida = slideOutHorizontally { ancho -> ancho } + fadeOut()
        return entrada togetherWith salida
    }
    val entrada = slideInHorizontally { ancho -> ancho } + fadeIn()
    val salida = slideOutHorizontally { ancho -> -ancho } + fadeOut()
    return entrada togetherWith salida
}
