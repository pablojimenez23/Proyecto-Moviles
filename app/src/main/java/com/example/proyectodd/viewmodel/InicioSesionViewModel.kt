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

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje



    fun iniciarSesion(correo: String, contrasena: String) {
        viewModelScope.launch {
            val usuario = usuarioDao.obtenerUsuario(correo, contrasena)
            if (usuario != null) {
                _mensaje.value = "Inicio de sesión exitoso"
            } else {
                _mensaje.value = "Correo o contraseña incorrectos"
            }
        }
    }

}
