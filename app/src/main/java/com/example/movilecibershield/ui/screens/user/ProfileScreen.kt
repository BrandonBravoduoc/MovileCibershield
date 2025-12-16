package com.example.movilecibershield.ui.screens.user

import androidx.compose.foundation.shape.RoundedCornerShape


import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.movilecibershield.data.utils.uriToMultipart
import com.example.movilecibershield.ui.components.EditContactCard
import com.example.movilecibershield.viewmodel.ContactEditViewModel
import com.example.movilecibershield.viewmodel.UserViewModel

@Composable
fun ProfileScreen(
    viewModel: UserViewModel,
    contactEditViewModel: ContactEditViewModel,
    navController: NavController,
    onLogout: () -> Unit
) {
    val profile by viewModel.profile.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    var editMode by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current

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

    Scaffold(
        bottomBar = {
            OutlinedButton(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text("Cerrar sesión")
            }
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.TopCenter
        ) {

            when {
                loading -> CircularProgressIndicator()

                error != null -> Text(
                    text = error ?: "",
                    color = MaterialTheme.colorScheme.error
                )

                profile != null -> {
                    val user = profile!!

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {

                        /* ---------- FOTO PERFIL ---------- */
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
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        /* ---------- CARD ---------- */
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

                                if (!editMode) {

                                    user.contact?.let { contact ->
                                        Text("Nombre: ${contact.name} ${contact.lastName}")
                                        Text("Teléfono: ${contact.phone}")
                                        Text("Dirección: ${contact.addressInfo}")
                                    }

                                    Button(
                                        onClick = { editMode = true },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("Editar contacto")
                                    }

                                } else {

                                    user.contact?.let { contact ->
                                        EditContactCard(
                                            viewModel = contactEditViewModel,
                                            contact = contact,
                                            onSuccess = {
                                                editMode = false
                                                viewModel.loadProfile()
                                            }
                                        )
                                    }

                                    OutlinedButton(
                                        onClick = { editMode = false },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("Cancelar")
                                    }
                                }

                                /* ---------- GUARDAR FOTO ---------- */
                                if (selectedImageUri != null) {
                                    Button(
                                        onClick = {
                                            val imagePart =
                                                uriToMultipart(context, selectedImageUri!!)

                                            viewModel.updateUser(
                                                userName = null,
                                                email = null,
                                                imageUser = imagePart,
                                            )

                                            selectedImageUri = null
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("Guardar foto")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


