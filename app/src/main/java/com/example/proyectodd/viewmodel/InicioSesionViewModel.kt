package com.example.proyectodd.viewmodel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectodd.data.UsuarioDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class InicioSesionViewModel(private val usuarioDao: UsuarioDao): ViewModel() {

    var correo by mutableStateOf("")
    var contrasena by mutableStateOf("")

    var sesionIniciada = mutableStateOf(false)

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje



    fun iniciarSesion() {
        viewModelScope.launch {
            val usuario = usuarioDao.obtenerPorCorreo(correo)
            if (usuario != null && usuario.contrasena == contrasena) {
                sesionIniciada.value = true
                _mensaje.value = ""
                
            } else {
                _mensaje.value = "Correo o contraseña incorrectos"
            }
        }
    }

}
