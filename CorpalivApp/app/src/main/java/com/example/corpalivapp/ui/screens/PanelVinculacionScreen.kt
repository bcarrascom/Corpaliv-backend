package com.example.corpalivapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.corpalivapp.data.local.CorpalivDatabase
import com.example.corpalivapp.data.model.Colaboracion
import com.example.corpalivapp.ui.components.FondoCorpaliv
import com.example.corpalivapp.viewmodel.CorpalivViewModelFactory
import com.example.corpalivapp.viewmodel.SolicitudesViewModel

// Los mismos tres estados que acepta ms-colaboraciones.
private val ESTADOS = listOf("Pendiente", "Contactado", "Cerrada")

// Panel del equipo de vinculación: ve todas las solicitudes y cambia su estado (PATCH).
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PanelVinculacionScreen(
    onVolver: () -> Unit,
    database: CorpalivDatabase
) {
    val factory = remember(database) {
        CorpalivViewModelFactory(database)
    }

    // key = "panel": instancia propia, para no mezclar su lista con la de "Mis solicitudes".
    val panelViewModel: SolicitudesViewModel =
        viewModel(key = "panel", factory = factory)

    val estado by panelViewModel.estado
        .collectAsStateWithLifecycle()

    val estadoSnackbar = remember { SnackbarHostState() }

    // Al entrar, trae todas las solicitudes actuales del servidor.
    LaunchedEffect(Unit) {
        panelViewModel.cargarTodas()
    }

    LaunchedEffect(estado.mensaje) {
        val mensaje = estado.mensaje
        if (mensaje != null) {
            estadoSnackbar.showSnackbar(mensaje)
            panelViewModel.mensajeMostrado()
        }
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text("Panel de vinculación") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver al inicio"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { panelViewModel.cargarTodas() }) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = "Actualizar lista"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        snackbarHost = { SnackbarHost(hostState = estadoSnackbar) }
    ) { paddingValues ->
        FondoCorpaliv {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = "Solicitudes recibidas: ${estado.solicitudes.size}. " +
                                "Toca un estado para actualizar el seguimiento.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                if (estado.cargando) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                } else if (estado.error != null) {
                    item {
                        Column {
                            Text(
                                text = estado.error ?: "",
                                color = MaterialTheme.colorScheme.error
                            )
                            TextButton(onClick = { panelViewModel.cargarTodas() }) {
                                Text("Reintentar")
                            }
                        }
                    }
                } else {
                    items(
                        items = estado.solicitudes,
                        key = { colaboracion -> colaboracion.id }
                    ) { colaboracion ->
                        TarjetaPanel(
                            colaboracion = colaboracion,
                            alCambiarEstado = { nuevoEstado ->
                                panelViewModel.cambiarEstado(colaboracion, nuevoEstado)
                            },
                            modifier = Modifier.animateItem()
                        )
                    }
                }
            }
        }
    }
}

// Tarjeta del panel: datos de la solicitud y un chip por cada estado (el actual queda marcado).
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TarjetaPanel(
    colaboracion: Colaboracion,
    alCambiarEstado: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var textoPersonas = "${colaboracion.cantidadPersonas} personas"
    if (colaboracion.cantidadPersonas == 1) {
        textoPersonas = "1 persona"
    }

    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "${colaboracion.tipoColaboracion} · $textoPersonas",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "${colaboracion.nombreContacto} · ${colaboracion.organizacion}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "${colaboracion.correo} · Contactar el ${colaboracion.fechaContacto}",
                style = MaterialTheme.typography.bodySmall
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                for (opcion in ESTADOS) {
                    FilterChip(
                        selected = colaboracion.estado == opcion,
                        onClick = { alCambiarEstado(opcion) },
                        label = { Text(opcion) }
                    )
                }
            }
        }
    }
}
