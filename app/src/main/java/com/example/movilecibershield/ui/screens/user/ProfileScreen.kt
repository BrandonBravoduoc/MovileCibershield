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
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
        topBar = {
            TopAppBar(
                title = { },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = "Cerrar sesión",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
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
                loading -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.White
                )

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
                            .padding(horizontal = 16.dp)
                            .verticalScroll(scrollState)
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

                        if (selectedImageUri != null) {
                            Text(
                                text = "Guardar cambios abajo",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.LightGray,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        } else {
                            Text(
                                text = user.userName,
                                style = MaterialTheme.typography.headlineSmall,
                                color = Color.White,
                                modifier = Modifier.padding(top = 12.dp)
                            )
                            Text(
                                text = user.email,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray,
                                modifier = Modifier.padding(bottom = 20.dp)
                            )
                        }

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

                                if (user.contact != null && !(user.contact.name.isNullOrBlank() && user.contact.lastName.isNullOrBlank())) {
                                    if (!editMode) {
                                        Text("Nombre: ${user.contact.name} ${user.contact.lastName}")
                                        Text("Teléfono: ${user.contact.phone}")
                                        Text("Dirección: ${user.contact.addressInfo}")

                                        Button(
                                            onClick = { editMode = true },
                                            modifier = Modifier.fillMaxWidth(),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = MaterialTheme.colorScheme.primary
                                            )
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
                                            Text("Cancelar edición")
                                        }
                                    }
                                } else {
                                    if (!createMode) {
                                        Text("Aún no tienes información de contacto.")
                                        Button(
                                            onClick = { createMode = true },
                                            modifier = Modifier.fillMaxWidth(),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = MaterialTheme.colorScheme.primary
                                            )
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
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))


                        if (selectedImageUri != null) {
                            Button(
                                onClick = {
                                    val imagePart = uriToMultipart(context, selectedImageUri!!)
                                    viewModel.updateUser(newUserName = null, newEmail = null, imageUser = imagePart)
                                    selectedImageUri = null
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF4ADE80) // Verde
                                )
                            ) {
                                Text("Guardar nueva foto", color = Color.Black)
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }


                        Spacer(modifier = Modifier.height(40.dp))
                    }
                }
            }
        }
    }
}
