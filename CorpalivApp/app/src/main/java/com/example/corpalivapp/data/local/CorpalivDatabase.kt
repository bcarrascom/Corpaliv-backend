package com.example.corpalivapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.corpalivapp.data.model.Programa
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Versión 3: productos y colaboraciones salieron de Room (ahora vienen de los microservicios).
@Database(
    entities = [
        Programa::class
    ],
    version = 3,
    exportSchema = false
)
abstract class CorpalivDatabase : RoomDatabase() {

    abstract fun programaDao(): ProgramaDao

    companion object {
        @Volatile
        private var INSTANCE: CorpalivDatabase? = null

        fun obtenerInstancia(context: Context): CorpalivDatabase {
            return INSTANCE ?: synchronized(this) {
                val instancia = Room.databaseBuilder(
                    context.applicationContext,
                    CorpalivDatabase::class.java,
                    "corpaliv_database"
                )
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)

                            CoroutineScope(Dispatchers.IO).launch {
                                INSTANCE?.let { database ->
                                    cargarProgramasIniciales(database)
                                }
                            }
                        }
                    })
                    // Si cambia la versión, borra la BD local y la crea de nuevo.
                    // Sirve mientras Room se reemplaza por los microservicios.
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .build()

                INSTANCE = instancia
                instancia
            }
        }

        private suspend fun cargarProgramasIniciales(
            database: CorpalivDatabase
        ) {
            val programas = listOf(
                Programa(
                    nombre = "Programa de inclusión",
                    descripcion = "Iniciativa orientada a promover " +
                            "la participación y la inclusión social."
                ),
                Programa(
                    nombre = "Programa de apoyo familiar",
                    descripcion = "Iniciativa de acompañamiento " +
                            "y orientación para las familias."
                )
            )

            programas.forEach { programa ->
                database.programaDao().insertar(programa)
            }
        }
    }
}