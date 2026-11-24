package com.example.corpalivapp

import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts

import com.example.corpalivapp.ui.screens.HomeScreen
import com.example.corpalivapp.ui.theme.MiAppTheme
import com.example.corpalivapp.notificaciones.NotificacionColaboracion
import com.example.corpalivapp.permisos.PermisoRedLocal

class MainActivity :
    ComponentActivity() {

    // Muestra el diálogo del sistema para el permiso de red local.
    // No hace falta reaccionar a la respuesta: si se niega, las pantallas muestran el error de conexión.
    private val pedirPermisoRedLocal =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { }

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(
            savedInstanceState
        )
        enableEdgeToEdge()
        NotificacionColaboracion.crearCanal(this)   // registra el canal al arrancar la app
        if (PermisoRedLocal.falta(this)) {
            pedirPermisoRedLocal.launch(PermisoRedLocal.PERMISO)
        }
        setContent {
            MiAppTheme {
                App()
            }
        }
    }
}