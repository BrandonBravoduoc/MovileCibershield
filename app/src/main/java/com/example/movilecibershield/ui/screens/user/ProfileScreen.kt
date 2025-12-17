package com.example.movilecibershield.ui.screens.user

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.movilecibershield.data.utils.UiEvent
import com.example.movilecibershield.data.utils.uriToMultipart
import com.example.movilecibershield.navigation.Routes
import com.example.movilecibershield.ui.components.*
import com.example.movilecibershield.viewmodel.ContactEditViewModel
import com.example.movilecibershield.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
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

    val snackbarHostState = remember { SnackbarHostState() }

    var editMode by remember { mutableStateOf(false) }
    var createMode by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current
    val scrollState = rememberScrollState()


    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> selectedImageUri = uri }

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    val backgroundBrush = Brush.verticalGradient(
        listOf(Color(0xFF111827), Color(0xFF020617))
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {},
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.Logout, "Cerrar sesión", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
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
                loading -> CircularProgressIndicator(color = Color.White)

                profile != null -> {
                    val user = profile!!

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .verticalScroll(scrollState),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Image(
                            painter = rememberAsyncImagePainter(
                                selectedImageUri ?: user.imageUser
                            ),
                            contentDescription = "Foto perfil",
                            modifier = Modifier
                                .size(110.dp)
                                .clip(CircleShape)
                                .background(Color.Gray)
                                .clickable { imagePicker.launch("image/*") },
                            contentScale = ContentScale.Crop
                        )

                        Spacer(Modifier.height(12.dp))

                        Text(user.userName, style = MaterialTheme.typography.headlineSmall, color = Color.White)
                        Text(user.email, color = Color.Gray)

                        Spacer(Modifier.height(20.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {

                                Text("Información de Envío", style = MaterialTheme.typography.titleMedium)
                                Divider()

                                user.contact?.let { contact ->
                                    if (!editMode) {
                                        Text("Nombre: ${contact.name} ${contact.lastName}")
                                        Text("Teléfono: ${contact.phone}")
                                        Text("Dirección: ${contact.addressInfo}")

                                        Button(
                                            onClick = { editMode = true },
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text("Editar contacto")
                                        }
                                    } else {
                                        EditContactCard(
                                            viewModel = contactEditViewModel,
                                            contact = contact,
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
                                } ?: run {
                                    if (!createMode) {
                                        Text("No tienes información de contacto.")
                                        Button(
                                            onClick = { createMode = true },
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text("Agregar contacto")
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
                            }
                        }

                        Spacer(Modifier.height(24.dp))

                        if (selectedImageUri != null) {
                            Button(
                                onClick = {
                                    val imagePart = uriToMultipart(context, selectedImageUri!!)
                                    viewModel.updateUser(
                                        newUserName = null,
                                        newEmail = null,
                                        imageUser = imagePart
                                    )
                                    selectedImageUri = null
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4ADE80))
                            ) {
                                Text("Guardar nueva foto", color = Color.Black)
                            }
                        }

                        Spacer(Modifier.height(40.dp))
                    }
                }
            }
        }
    }
}
