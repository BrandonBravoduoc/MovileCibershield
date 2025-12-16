package com.example.movilecibershield.ui.screens.user

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.movilecibershield.data.local.TokenDataStore
import com.example.movilecibershield.data.utils.uriToMultipart
import com.example.movilecibershield.navigation.Routes
import com.example.movilecibershield.ui.components.AppBottomBar
import com.example.movilecibershield.ui.components.CreateContactCard
import com.example.movilecibershield.ui.components.EditContactCard
import com.example.movilecibershield.viewmodel.ContactEditViewModel
import com.example.movilecibershield.viewmodel.UserViewModel

@Composable
fun ProfileScreen(
    viewModel: UserViewModel,
    contactEditViewModel: ContactEditViewModel,
    navController: NavController,
    tokenDataStore: TokenDataStore,
    onLogout: () -> Unit
) {
    val profile by viewModel.profile.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    var editMode by remember { mutableStateOf(false) }
    var createMode by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            selectedImageUri = uri
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    val backgroundBrush = remember {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFF111827),
                Color(0xFF020617)
            )
        )
    }

    Scaffold(
        bottomBar = {
            AppBottomBar(
                navController = navController,
                currentRoute = Routes.PROFILE,
                tokenDataStore = tokenDataStore,
                excludedRoute = Routes.PROFILE
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundBrush)
                .padding(padding),
            contentAlignment = Alignment.TopCenter
        ) {
            when {
                loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))

                error != null -> Text(
                    text = error ?: "",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )

                profile != null -> {
                    val user = profile!!

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .verticalScroll(scrollState)
                    ) {

                        Image(
                            painter = rememberAsyncImagePainter(
                                selectedImageUri ?: user.imageUser
                            ),
                            contentDescription = "Foto perfil",
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .clickable {
                                    imagePicker.launch("image/*")
                                },
                            contentScale = ContentScale.Crop
                        )

                        if (selectedImageUri != null) {
                            Text(
                                text = "Toca Guardar para aplicar la foto",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White, // Ajustamos color texto por el fondo oscuro
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {

                                Text(user.userName, style = MaterialTheme.typography.titleLarge)
                                Text(user.email)

                                Divider()

                                if (user.contact != null) {
                                    if (!editMode) {
                                        Text("Nombre: ${user.contact.name} ${user.contact.lastName}")
                                        Text("Teléfono: ${user.contact.phone}")
                                        Text("Dirección: ${user.contact.addressInfo}")

                                        Button(
                                            onClick = { editMode = true },
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text("Editar contacto")
                                        }
                                    } else {
                                        EditContactCard(
                                            viewModel = contactEditViewModel,
                                            contact = user.contact,
                                            onSuccess = {
                                                editMode = false
                                                viewModel.loadProfile()
                                            }
                                        )
                                        OutlinedButton(
                                            onClick = { editMode = false },
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text("Cancelar")
                                        }
                                    }
                                } else {
                                    if (!createMode) {
                                        Text("Aún no tienes información de contacto.")
                                        Button(
                                            onClick = { createMode = true },
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text("Agregar Contacto")
                                        }
                                    } else {
                                        CreateContactCard(
                                            userViewModel = viewModel,
                                            contactViewModel = contactEditViewModel,
                                            onSuccess = {
                                                createMode = false
                                                viewModel.loadProfile()
                                            }
                                        )
                                        OutlinedButton(
                                            onClick = { createMode = false },
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text("Cancelar")
                                        }
                                    }
                                }

                                Button(
                                    onClick = { navController.navigate(Routes.ORDER_HISTORY) },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Historial de compras")
                                }

                                if (selectedImageUri != null) {
                                    Button(
                                        onClick = {
                                            val imagePart = uriToMultipart(context, selectedImageUri!!)
                                            viewModel.updateUser(newUserName = null, newEmail = null, imageUser = imagePart)
                                            selectedImageUri = null
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("Guardar foto")
                                    }
                                }

                                Divider(modifier = Modifier.padding(vertical = 8.dp))

                                OutlinedButton(
                                    onClick = onLogout,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Cerrar sesión")
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }
        }
    }
}
