package com.example.corpalivapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.corpalivapp.viewmodel.ClimaEstado

// Tarjeta con el clima actual en la sede (datos de la API externa Open-Meteo).
@Composable
fun TarjetaClima(
    estado: ClimaEstado,
    alReintentar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.WbSunny,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(36.dp)
            )

            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = "Clima en la sede · Santiago",
                    style = MaterialTheme.typography.labelLarge
                )

                // Se muestra una sola opción según el estado.
                if (estado.cargando) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else if (estado.error != null) {
                    Text(
                        text = estado.error,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                    TextButton(onClick = alReintentar) {
                        Text("Reintentar")
                    }
                } else {
                    Text(
                        text = "${estado.temperatura} · ${estado.descripcion}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = estado.viento,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "Datos: Open-Meteo",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}
