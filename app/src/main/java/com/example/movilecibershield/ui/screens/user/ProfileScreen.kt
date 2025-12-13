package com.example.movilecibershield.ui.screens.user



import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.movilecibershield.viewmodel.UserViewModel

@Composable
fun ProfileScreen(
    viewModel: UserViewModel,
    navController: NavController
) {
    // Cargar perfil al entrar
    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    val profile by viewModel.profile.collectAsState()
    val isLoading by viewModel.loading.collectAsState()
    val errorMessage by viewModel.error.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        when {
            isLoading -> {
                CircularProgressIndicator()
            }

            errorMessage != null -> {
                Text(text = errorMessage ?: "Error desconocido")
            }

            profile != null -> {
                val user = profile!!

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // Imagen del usuario
                    Image(
                        painter = rememberAsyncImagePainter(user.imageUser),
                        contentDescription = "Profile",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Datos del usuario
                    Text(text = "Usuario: ${user.userName}")
                    Text(text = "Email: ${user.email}")

                    Spacer(modifier = Modifier.height(20.dp))

                    // Datos de contacto si existen
                    user.contact?.let { contact ->
                        Text(text = "Nombre: ${contact.name} ${contact.lastName}")
                        Text(text = "Teléfono: ${contact.phone}")
                        Text(text = "Dirección: ${contact.addressInfo}")
                    } ?: Text("No has agregado un contacto aún.")

                    Spacer(modifier = Modifier.height(24.dp))

                    // Botón para ir a editar perfil
                    Button(
                        onClick = {
                            navController.navigate("edit_profile")
                        }
                    ) {
                        Text("Editar Perfil")
                    }
                }
            }
        }
    }
}



