package com.example.movilecibershield.ui.screens.auth

import androidx.compose.foundation.clickable
import com.example.movilecibershield.data.model.user.UserRegister


import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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

    Column {

        TextField(
            value = userName,
            onValueChange = { userName = it },
            label = "Usuario",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = "Email",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = "Password",
            isPassword = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = "Confirmar password",
            isPassword = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            text = "Crear cuenta",
            isLoading = isLoading,
            onClick = {
                onRegister(
                    UserRegister(
                        userName,
                        email,
                        password,
                        confirmPassword,
                        imageUser = null
                    )
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "¿Ya tienes cuenta? Inicia sesión",
            modifier = Modifier
                .padding(top = 8.dp)
                .clickable { onGoToLogin() }
        )

        errorMessage?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = it)
        }
    }
}
