package com.example.movilecibershield.ui.screens.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.movilecibershield.viewmodel.UserViewModel
import com.example.movilecibershield.navigation.Routes
import com.example.movilecibershield.ui.components.AppBottomBar

@Composable
fun ProfileScreen(
    viewModel: UserViewModel,
    navController: NavController,
    token: String?
) {
    val profile by viewModel.profile.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(token) {
        viewModel.loadProfile(token)
    }

    Scaffold(
        bottomBar = {
            AppBottomBar(
                navController = navController,
                currentRoute = Routes.PROFILE,
                token = token
            )
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {

            if (loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                return@Box
            }

            error?.let {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Error: $it", color = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(onClick = { viewModel.loadProfile(token) }) {
                        Text("Reintentar")
                    }
                }
                return@Box
            }

            if (token == null) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Debes iniciar sesión para ver tu perfil")
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(onClick = { navController.navigate(Routes.AUTH) }) {
                        Text("Iniciar sesión")
                    }
                }
                return@Box
            }

            profile?.let { user ->

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Image(
                        painter = rememberAsyncImagePainter(user.imageUser),
                        contentDescription = "Foto de perfil",
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(text = user.userName, style = MaterialTheme.typography.titleLarge)
                    Text(text = user.email, style = MaterialTheme.typography.bodyMedium)

                    Spacer(modifier = Modifier.height(20.dp))

                    if (user.contact != null) {
                        Text("Nombre: ${user.contact.name} ${user.contact.lastName}")
                        Text("Teléfono: ${user.contact.phone}")
                        Text("Dirección: ${user.contact.addressInfo}")
                    } else {
                        Text("No has agregado información de contacto")
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {

                            }
                        ) {
                            Text("Agregar contacto")
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))


                    Button(
                        onClick = {

                        }
                    ) {
                        Text("Editar perfil")
                    }
                }
            }
        }
    }
}
