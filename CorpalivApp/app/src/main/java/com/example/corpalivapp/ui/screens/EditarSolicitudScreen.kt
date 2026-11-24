package com.example.corpalivapp.ui.screens

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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.corpalivapp.data.local.CorpalivDatabase
import com.example.corpalivapp.data.model.Colaboracion
import com.example.corpalivapp.ui.components.FondoCorpaliv
import com.example.corpalivapp.ui.components.FormularioColaboracion
import com.example.corpalivapp.viewmodel.ColaboracionViewModel
import com.example.corpalivapp.viewmodel.CorpalivViewModelFactory

// Edita una solicitud existente (PUT). Usa el mismo formulario que "Colabora con nosotros".
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarSolicitudScreen(
    colaboracion: Colaboracion,
    onVolver: () -> Unit,
    database: CorpalivDatabase
) {
    val factory = remember(database) {
        CorpalivViewModelFactory(database)
    }

    // key = "edicion": otra instancia distinta a la del formulario de Contacto,
    // así lo que se edita aquí no aparece escrito en "Colabora con nosotros".
    val edicionViewModel: ColaboracionViewModel =
        viewModel(key = "edicion", factory = factory)

    val formulario by edicionViewModel.formulario
        .collectAsStateWithLifecycle()

    // Al abrir la pantalla, llena el formulario con los datos de la solicitud elegida.
    LaunchedEffect(colaboracion.id) {
        edicionViewModel.cargarParaEditar(colaboracion)
    }

    // Cuando el servidor confirma los cambios, se vuelve a "Mis solicitudes" (que recarga la lista).
    LaunchedEffect(formulario.cambiosGuardados) {
        if (formulario.cambiosGuardados) {
            edicionViewModel.terminarEdicion()
            onVolver()
        }
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text("Editar solicitud") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver a mis solicitudes"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        FondoCorpaliv {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Corrige los datos de tu solicitud. El estado lo actualiza " +
                            "el equipo de CORPALIV.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )

                FormularioColaboracion(
                    formulario = formulario,
                    viewModel = edicionViewModel,
                    textoBoton = "Guardar cambios",
                    alEnviar = { edicionViewModel.guardarCambios() }
                )
            }
        }
    }
}
