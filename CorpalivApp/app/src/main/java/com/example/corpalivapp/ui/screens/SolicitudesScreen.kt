package com.example.corpalivapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.corpalivapp.data.local.CorpalivDatabase
import com.example.corpalivapp.data.model.Colaboracion
import com.example.corpalivapp.ui.components.CampoFormulario
import com.example.corpalivapp.ui.components.FondoCorpaliv
import com.example.corpalivapp.ui.components.TarjetaSolicitud
import com.example.corpalivapp.viewmodel.CorpalivViewModelFactory
import com.example.corpalivapp.viewmodel.SolicitudesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SolicitudesScreen(
    onVolver: () -> Unit,
    onEditar: (Colaboracion) -> Unit,
    database: CorpalivDatabase
) {
    val factory = remember(database) {
        CorpalivViewModelFactory(database)
    }

    val solicitudesViewModel: SolicitudesViewModel =
        viewModel(factory = factory)

    val estado by solicitudesViewModel.estado
        .collectAsStateWithLifecycle()

    val estadoSnackbar = remember { SnackbarHostState() }

    // Solicitud que la persona quiere borrar; null = no hay diálogo abierto.
    var porEliminar by remember { mutableStateOf<Colaboracion?>(null) }

    // Al entrar: si ya se había buscado un correo, se repite la búsqueda con datos frescos.
    LaunchedEffect(Unit) {
        solicitudesViewModel.recargar()
    }

    // Muestra los avisos del ViewModel (por ejemplo "Solicitud eliminada.").
    LaunchedEffect(estado.mensaje) {
        val mensaje = estado.mensaje
        if (mensaje != null) {
            estadoSnackbar.showSnackbar(mensaje)
            solicitudesViewModel.mensajeMostrado()
        }
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text("Mis solicitudes") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver al inicio"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        snackbarHost = { SnackbarHost(hostState = estadoSnackbar) }
    ) { paddingValues ->

        // Fondo de CORPALIV que cambia según el tema claro u oscuro.
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
                        text = "Escribe el correo con que enviaste tus solicitudes " +
                                "para ver en qué estado están.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                item {
                    CampoFormulario(
                        valor = estado.correo,
                        alCambiar = solicitudesViewModel::cambiarCorreo,
                        etiqueta = "Correo electrónico",
                        icono = Icons.Filled.Email,
                        error = estado.errorCorreo,
                        tipoTeclado = KeyboardType.Email
                    )
                }

                item {
                    Button(
                        onClick = { solicitudesViewModel.buscar() },
                        enabled = !estado.cargando,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(imageVector = Icons.Filled.Search, contentDescription = null)
                        Text(text = "Buscar", modifier = Modifier.padding(start = 8.dp))
                    }
                }

                // Se muestra una sola de estas opciones según el estado.
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
                            TextButton(onClick = { solicitudesViewModel.recargar() }) {
                                Text("Reintentar")
                            }
                        }
                    }
                } else if (estado.correoBuscado.isNotBlank() && estado.solicitudes.isEmpty()) {
                    item {
                        Text(
                            text = "No hay solicitudes con el correo ${estado.correoBuscado}.",
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                } else {
                    items(
                        items = estado.solicitudes,
                        key = { colaboracion -> colaboracion.id }
                    ) { colaboracion ->
                        TarjetaSolicitud(
                            colaboracion = colaboracion,
                            alEditar = { onEditar(colaboracion) },
                            alEliminar = { porEliminar = colaboracion },
                            modifier = Modifier.animateItem()
                        )
                    }
                }
            }
        }
    }

    // Se copia a un val porque Kotlin no deja revisar null directo en un "by remember".
    val seleccionada = porEliminar
    if (seleccionada != null) {
        DialogoEliminar(
            tipo = seleccionada.tipoColaboracion,
            alConfirmar = {
                solicitudesViewModel.eliminar(seleccionada)
                porEliminar = null
            },
            alCancelar = { porEliminar = null }
        )
    }
}

@Composable
private fun DialogoEliminar(
    tipo: String,
    alConfirmar: () -> Unit,
    alCancelar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = alCancelar,
        title = { Text("¿Eliminar esta solicitud?") },
        text = { Text("Se borrará tu solicitud de $tipo en CORPALIV. No se puede deshacer.") },
        confirmButton = {
            TextButton(onClick = alConfirmar) {
                Text(
                    text = "Eliminar",
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        dismissButton = {
            TextButton(onClick = alCancelar) {
                Text("Cancelar")
            }
        }
    )
}
