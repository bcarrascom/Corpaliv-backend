package com.example.corpalivapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.corpalivapp.validacion.ColaboracionValidador
import com.example.corpalivapp.viewmodel.ColaboracionViewModel
import com.example.corpalivapp.viewmodel.FormularioColaboracionEstado

// Campos del formulario de colaboración + mensaje de error + botón.
// Lo usan "Colabora con nosotros" (crear) y "Editar solicitud" (editar).
@Composable
fun FormularioColaboracion(
    formulario: FormularioColaboracionEstado,
    viewModel: ColaboracionViewModel,
    textoBoton: String,
    alEnviar: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        CampoFormulario(
            valor = formulario.nombre,
            alCambiar = viewModel::cambiarNombre,
            etiqueta = "Nombre completo",
            icono = Icons.Filled.Person,
            error = formulario.errorNombre
        )

        CampoFormulario(
            valor = formulario.organizacion,
            alCambiar = viewModel::cambiarOrganizacion,
            etiqueta = "Organización",
            icono = Icons.Filled.Business,
            error = formulario.errorOrganizacion
        )

        CampoFormulario(
            valor = formulario.correo,
            alCambiar = viewModel::cambiarCorreo,
            etiqueta = "Correo electrónico",
            icono = Icons.Filled.Email,
            error = formulario.errorCorreo,
            tipoTeclado = KeyboardType.Email
        )

        SelectorTipo(
            seleccionado = formulario.tipo,
            opciones = ColaboracionValidador.TIPOS_COLABORACION,
            alElegir = viewModel::cambiarTipo,
            error = formulario.errorTipo
        )

        CampoFormulario(
            valor = formulario.cantidadPersonas,
            alCambiar = viewModel::cambiarCantidad,
            etiqueta = "Cantidad de personas",
            icono = Icons.Filled.Groups,
            error = formulario.errorCantidad,
            tipoTeclado = KeyboardType.Number
        )

        CampoFecha(
            fecha = formulario.fecha,
            alElegir = viewModel::elegirFechaDelCalendario,
            error = formulario.errorFecha
        )

        CasillaAceptacion(
            marcada = formulario.aceptaContacto,
            alCambiar = viewModel::cambiarAceptaContacto,
            texto = "Acepto que CORPALIV me contacte en relación con mi solicitud.",
            error = formulario.errorAceptaContacto
        )

        // Error del servidor o de conexión al enviar.
        val errorEnvio = formulario.errorEnvio
        if (errorEnvio != null) {
            Text(
                text = errorEnvio,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        var texto = textoBoton
        if (formulario.enviando) {
            texto = "Enviando..."
        }

        // Se desactiva mientras espera al servidor, para no enviar dos veces.
        Button(
            onClick = alEnviar,
            enabled = !formulario.enviando,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(texto)
        }
    }
}
