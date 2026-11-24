
package com.example.corpalivapp.ui.screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.corpalivapp.acciones.AccionesContacto
import com.example.corpalivapp.data.local.CorpalivDatabase
import com.example.corpalivapp.notificaciones.NotificacionColaboracion
import com.example.corpalivapp.ui.components.FondoCorpaliv
import com.example.corpalivapp.ui.components.FormularioColaboracion
import com.example.corpalivapp.ui.components.TarjetaContactoDirecto
import com.example.corpalivapp.viewmodel.ColaboracionViewModel
import com.example.corpalivapp.viewmodel.CorpalivViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactoScreen(
    onVolver: () -> Unit,
    database: CorpalivDatabase
) {
    val factory = remember(database) {
        CorpalivViewModelFactory(database)
    }

    val colaboracionViewModel: ColaboracionViewModel =
        viewModel(factory = factory)

    val formulario by colaboracionViewModel.formulario
        .collectAsStateWithLifecycle()

    val estadoSnackbar = remember {
        SnackbarHostState()
    }

    val context = LocalContext.current
    val alcance = rememberCoroutineScope()

    // Solicita permiso para mostrar notificaciones cuando corresponda.
    val lanzadorPermiso = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) {
            NotificacionColaboracion.mostrarSolicitudRecibida(
                context,
                formulario.tipoGuardado,
                formulario.fechaGuardada
            )
        } else {
            alcance.launch {
                estadoSnackbar.showSnackbar(
                    "No recibirás la notificación. Puedes activarla en Ajustes."
                )
            }
        }
    }

    // Reacciona al guardado exitoso del formulario.
    LaunchedEffect(formulario.guardadoConExito) {
        if (formulario.guardadoConExito) {
            if (NotificacionColaboracion.tienePermiso(context)) {
                NotificacionColaboracion.mostrarSolicitudRecibida(
                    context,
                    formulario.tipoGuardado,
                    formulario.fechaGuardada
                )
            } else {
                lanzadorPermiso.launch(
                    Manifest.permission.POST_NOTIFICATIONS
                )
            }

            estadoSnackbar.showSnackbar(
                "¡Gracias! Tu solicitud fue enviada a CORPALIV."
            )

            colaboracionViewModel.ocultarMensajeExito()
        }
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = {
                    Text("Colabora con nosotros")
                },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver al inicio"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(
                        alpha = 0.92f
                    ),
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor =
                        MaterialTheme.colorScheme.onSurface
                )
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = estadoSnackbar)
        }
    ) { paddingValues ->

        // Fondo floral que cambia según el tema del sistema.
        FondoCorpaliv {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TarjetaContactoDirecto(
                    alLlamar = {
                        val abrio = AccionesContacto.llamar(context)

                        if (!abrio) {
                            alcance.launch {
                                estadoSnackbar.showSnackbar(
                                    "No encontramos una app para llamar."
                                )
                            }
                        }
                    },
                    alEscribirCorreo = {
                        val abrio =
                            AccionesContacto.escribirCorreo(context)

                        if (!abrio) {
                            alcance.launch {
                                estadoSnackbar.showSnackbar(
                                    "No encontramos una app de correo."
                                )
                            }
                        }
                    },
                    alAbrirMapa = {
                        val abrio = AccionesContacto.abrirMapa(context)

                        if (!abrio) {
                            alcance.launch {
                                estadoSnackbar.showSnackbar(
                                    "No encontramos una app de mapas."
                                )
                            }
                        }
                    }
                )

                Text(
                    text = "Completa el formulario para expresar tu interés " +
                            "en colaborar con CORPALIV.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )

                FormularioColaboracion(
                    formulario = formulario,
                    viewModel = colaboracionViewModel,
                    textoBoton = "Enviar solicitud",
                    alEnviar = { colaboracionViewModel.enviarFormulario() }
                )
            }
        }
    }
}
