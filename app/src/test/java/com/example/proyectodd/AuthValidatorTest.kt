package com.example.proyectodd.domain.validator

import com.example.proyectodd.viewmodel.domain.validator.AuthValidator
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * UBICACIÓN: app/src/test/java/com/example/proyectodd/domain/validator/AuthValidatorTest.kt
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class AuthValidatorTest {

    // ==================== VALIDAR NOMBRE ====================

    @Test
    fun `validarNombre con nombre valido retorna null`() {
        val resultado = AuthValidator.validarNombre("Juan Pérez")
        assertThat(resultado).isNull()
    }

    @Test
    fun `validarNombre con nombre vacio retorna error`() {
        val resultado = AuthValidator.validarNombre("")
        assertThat(resultado).isEqualTo("El nombre no puede estar vacío")
    }

    @Test
    fun `validarNombre con nombre de solo espacios retorna error`() {
        val resultado = AuthValidator.validarNombre("   ")
        assertThat(resultado).isEqualTo("El nombre no puede estar vacío")
    }

    @Test
    fun `validarNombre con menos de 3 caracteres retorna error`() {
        val resultado = AuthValidator.validarNombre("Jo")
        assertThat(resultado).isEqualTo("El nombre debe tener al menos 3 caracteres")
    }

    @Test
    fun `validarNombre con exactamente 3 caracteres es valido`() {
        val resultado = AuthValidator.validarNombre("Ana")
        assertThat(resultado).isNull()
    }

    @Test
    fun `validarNombre con nombre largo es valido`() {
        val resultado = AuthValidator.validarNombre("Juan Carlos Pérez González")
        assertThat(resultado).isNull()
    }

    // ==================== VALIDAR CORREO ====================

    @Test
    fun `validarCorreo con correo valido retorna null`() {
        val resultado = AuthValidator.validarCorreo("test@example.com")
        assertThat(resultado).isNull()
    }

    @Test
    fun `validarCorreo con correo vacio retorna error`() {
        val resultado = AuthValidator.validarCorreo("")
        assertThat(resultado).isEqualTo("El correo no puede estar vacío")
    }

    @Test
    fun `validarCorreo con formato invalido retorna error`() {
        val resultado = AuthValidator.validarCorreo("correo_invalido")
        assertThat(resultado).isEqualTo("Correo electrónico inválido")
    }

    @Test
    fun `validarCorreo sin arroba retorna error`() {
        val resultado = AuthValidator.validarCorreo("testexample.com")
        assertThat(resultado).isEqualTo("Correo electrónico inválido")
    }

    @Test
    fun `validarCorreo sin dominio retorna error`() {
        val resultado = AuthValidator.validarCorreo("test@")
        assertThat(resultado).isEqualTo("Correo electrónico inválido")
    }

    @Test
    fun `validarCorreo con espacios retorna error`() {
        val resultado = AuthValidator.validarCorreo("test @example.com")
        assertThat(resultado).isEqualTo("Correo electrónico inválido")
    }

    @Test
    fun `validarCorreo con solo espacios retorna error vacio`() {
        val resultado = AuthValidator.validarCorreo("   ")
        assertThat(resultado).isEqualTo("El correo no puede estar vacío")
    }

    @Test
    fun `validarCorreo con formato complejo valido retorna null`() {
        val resultado = AuthValidator.validarCorreo("user.name+tag@example.co.uk")
        assertThat(resultado).isNull()
    }

    // ==================== VALIDAR CONTRASEÑA ====================

    @Test
    fun `validarContrasena con contrasena valida retorna null`() {
        val resultado = AuthValidator.validarContrasena("password123")
        assertThat(resultado).isNull()
    }

    @Test
    fun `validarContrasena con contrasena vacia retorna error`() {
        val resultado = AuthValidator.validarContrasena("")
        assertThat(resultado).isEqualTo("La contraseña no puede estar vacía")
    }

    @Test
    fun `validarContrasena con menos de 6 caracteres retorna error`() {
        val resultado = AuthValidator.validarContrasena("12345")
        assertThat(resultado).isEqualTo("La contraseña debe tener al menos 6 caracteres")
    }

    @Test
    fun `validarContrasena con exactamente 6 caracteres es valida`() {
        val resultado = AuthValidator.validarContrasena("123456")
        assertThat(resultado).isNull()
    }

    @Test
    fun `validarContrasena con solo espacios retorna error vacia`() {
        val resultado = AuthValidator.validarContrasena("      ")
        assertThat(resultado).isEqualTo("La contraseña no puede estar vacía")
    }

    @Test
    fun `validarContrasena con caracteres especiales es valida`() {
        val resultado = AuthValidator.validarContrasena("P@ssw0rd!")
        assertThat(resultado).isNull()
    }

    // ==================== VALIDAR CONFIRMACIÓN ====================

    @Test
    fun `validarConfirmacionContrasena con contrasenas iguales retorna null`() {
        val resultado = AuthValidator.validarConfirmacionContrasena("password123", "password123")
        assertThat(resultado).isNull()
    }

    @Test
    fun `validarConfirmacionContrasena con confirmacion vacia retorna error`() {
        val resultado = AuthValidator.validarConfirmacionContrasena("password123", "")
        assertThat(resultado).isEqualTo("Debes confirmar la contraseña")
    }

    @Test
    fun `validarConfirmacionContrasena con contrasenas diferentes retorna error`() {
        val resultado = AuthValidator.validarConfirmacionContrasena("password123", "password456")
        assertThat(resultado).isEqualTo("Las contraseñas no coinciden")
    }

    @Test
    fun `validarConfirmacionContrasena case sensitive`() {
        val resultado = AuthValidator.validarConfirmacionContrasena("Password123", "password123")
        assertThat(resultado).isEqualTo("Las contraseñas no coinciden")
    }

    @Test
    fun `validarConfirmacionContrasena con espacios diferentes retorna error`() {
        val resultado = AuthValidator.validarConfirmacionContrasena("password123", "password123 ")
        assertThat(resultado).isEqualTo("Las contraseñas no coinciden")
    }

    @Test
    fun `validarConfirmacionContrasena con confirmacion de solo espacios retorna error`() {
        val resultado = AuthValidator.validarConfirmacionContrasena("password123", "   ")
        assertThat(resultado).isEqualTo("Debes confirmar la contraseña")
    }
}