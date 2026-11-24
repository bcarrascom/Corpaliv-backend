package com.example.corpalivapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.corpalivapp.data.local.CorpalivDatabase
import com.example.corpalivapp.data.remote.RetrofitClient
import com.example.corpalivapp.data.repository.ClimaRepository
import com.example.corpalivapp.data.repository.ColaboracionRepository
import com.example.corpalivapp.data.repository.ProductoRepository
import com.example.corpalivapp.data.repository.ProgramaRepository

class CorpalivViewModelFactory(
    private val database: CorpalivDatabase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return when {
            modelClass.isAssignableFrom(ProgramaViewModel::class.java) -> {
                val repository = ProgramaRepository(database.programaDao())
                ProgramaViewModel(repository) as T
            }

            modelClass.isAssignableFrom(ProductoViewModel::class.java) -> {
                val repository = ProductoRepository(RetrofitClient.productoApi)
                ProductoViewModel(repository) as T
            }

            modelClass.isAssignableFrom(ColaboracionViewModel::class.java) -> {
                val repository = ColaboracionRepository(RetrofitClient.colaboracionApi)
                ColaboracionViewModel(repository) as T
            }

            modelClass.isAssignableFrom(ClimaViewModel::class.java) -> {
                val repository = ClimaRepository(RetrofitClient.climaApi)
                ClimaViewModel(repository) as T
            }

            modelClass.isAssignableFrom(SolicitudesViewModel::class.java) -> {
                val repository = ColaboracionRepository(RetrofitClient.colaboracionApi)
                SolicitudesViewModel(repository) as T
            }

            else -> throw IllegalArgumentException(
                "ViewModel desconocido: ${modelClass.name}"
            )
        }
    }
}