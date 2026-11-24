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
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.corpalivapp.data.local.CorpalivDatabase
import com.example.corpalivapp.ui.components.FondoCorpaliv
import com.example.corpalivapp.ui.components.TarjetaProducto
import com.example.corpalivapp.viewmodel.CatalogoEstado
import com.example.corpalivapp.viewmodel.CorpalivViewModelFactory
import com.example.corpalivapp.viewmodel.ProductoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoScreen(
    onVolver: () -> Unit,
    database: CorpalivDatabase
) {
    val factory = remember(database) {
        CorpalivViewModelFactory(database)
    }

    val productoViewModel: ProductoViewModel =
        viewModel(factory = factory)

    val estado by productoViewModel.estado
        .collectAsStateWithLifecycle()

    // Se ejecuta una vez cada vez que se entra al catálogo: trae los datos actuales del servidor.
    LaunchedEffect(Unit) {
        productoViewModel.cargarProductos()
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text("Productos y servicios") },
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
        }
    ) { paddingValues ->

        // Fondo de CORPALIV que cambia según el tema claro u oscuro.
        FondoCorpaliv {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Se muestra una sola de las tres vistas según el estado.
                if (estado.cargando) {
                    CatalogoCargando()
                } else if (estado.error != null) {
                    CatalogoError(
                        mensaje = estado.error ?: "",
                        onReintentar = { productoViewModel.cargarProductos() }
                    )
                } else {
                    CatalogoLista(estado = estado)
                }
            }
        }
    }
}

// Rueda de carga centrada mientras llega la respuesta de ms-productos.
@Composable
private fun CatalogoCargando() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Text(
            text = "Cargando catálogo...",
            modifier = Modifier.padding(top = 16.dp),
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

// Tarjeta con el mensaje de error y el botón para volver a intentar.
@Composable
private fun CatalogoError(
    mensaje: String,
    onReintentar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.CloudOff,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
                Text(
                    text = mensaje,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                )
                Button(onClick = onReintentar) {
                    Icon(
                        imageVector = Icons.Filled.Refresh,
                        contentDescription = null
                    )
                    Text(
                        text = "Reintentar",
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}

// Lista de productos que llegó desde el microservicio.
@Composable
private fun CatalogoLista(estado: CatalogoEstado) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Cada compra apoya la inclusión laboral de las personas " +
                        "que participan en Alba Lab. Toca un producto para ver el detalle.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        if (estado.productos.isEmpty()) {
            item {
                Text(
                    text = "Todavía no hay productos disponibles.",
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        items(
            items = estado.productos,
            key = { producto -> producto.id }
        ) { producto ->
            TarjetaProducto(
                producto = producto,
                modifier = Modifier.animateItem()
            )
        }
    }
}
