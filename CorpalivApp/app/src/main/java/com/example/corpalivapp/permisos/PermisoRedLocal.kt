package com.example.corpalivapp.permisos

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

// Permiso de red local de Android 17 (API 37).
// Sin él, Android bloquea en silencio las conexiones a direcciones locales como 10.0.2.2
// o 192.168.x.x, que es donde corren los microservicios.
object PermisoRedLocal {

    const val PERMISO = "android.permission.ACCESS_LOCAL_NETWORK"

    private const val VERSION_ANDROID_17 = 37

    // true si hay que pedirlo: solo en Android 17 o superior y si aún no está concedido.
    fun falta(context: Context): Boolean {
        if (Build.VERSION.SDK_INT < VERSION_ANDROID_17) {
            return false
        }
        val estado = ContextCompat.checkSelfPermission(context, PERMISO)
        return estado != PackageManager.PERMISSION_GRANTED
    }
}
