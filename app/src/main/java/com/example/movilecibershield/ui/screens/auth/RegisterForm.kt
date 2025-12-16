package com.example.movilecibershield.ui.screens.auth

import androidx.compose.foundation.clickable
import com.example.movilecibershield.data.model.user.UserRegister


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.movilecibershield.ui.components.Button
import com.example.movilecibershield.ui.components.TextField

@Composable
fun RegisterForm(
    isLoading: Boolean,
    errorMessage: String?,
    onRegister: (UserRegister) -> Unit,
    onGoToLogin: () -> Unit
) {
    var userName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var userNameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }

    Card(
        modifier = Modifier
            .widthIn(max = 360.dp)
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = "Crear cuenta",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            TextField(
                value = userName,
                onValueChange = {
                    userName = it
                    userNameError = null
                },
                label = "Usuario",
                isError = userNameError != null,
                errorText = userNameError,
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = null
                },
                label = "Email",
                isError = emailError != null,
                errorText = emailError,
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = null
                },
                label = "Password",
                isPassword = true,
                isError = passwordError != null,
                errorText = passwordError,
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    confirmPasswordError = null
                },
                label = "Confirmar password",
                isPassword = true,
                isError = confirmPasswordError != null,
                errorText = confirmPasswordError,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                text = "Crear cuenta",
                isLoading = isLoading,
                onClick = {
                    var valid = true

                    if (userName.isBlank()) {
                        userNameError = "El usuario es obligatorio"
                        valid = false
                    }

                    if (email.isBlank()) {
                        emailError = "El email es obligatorio"
                        valid = false
                    }

                    if (password.isBlank()) {
                        passwordError = "La contraseña es obligatoria"
                        valid = false
                    }

                    if (confirmPassword.isBlank()) {
                        confirmPasswordError = "Confirma la contraseña"
                        valid = false
                    } else if (password != confirmPassword) {
                        confirmPasswordError = "Las contraseñas no coinciden"
                        valid = false
                    }

                    if (valid) {
                        onRegister(
                            UserRegister(
                                userName = userName,
                                email = email,
                                password = password,
                                confirmPassword = confirmPassword,
                                imageUser = null
                            )
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                containerColor = Color(0xFF1F2937)
            )

            Text(
                text = "¿Ya tienes cuenta? Inicia sesión",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable { onGoToLogin() }
                    .padding(top = 6.dp)
            )
        }
    }
}



