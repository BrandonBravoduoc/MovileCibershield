package com.example.movilecibershield.ui.components

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.movilecibershield.data.model.user.ContactResponse
import com.example.movilecibershield.data.model.user.ContactUpdateWithAddress
import com.example.movilecibershield.viewmodel.ContactEditViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditContactCard(
    viewModel: ContactEditViewModel,
    contact: ContactResponse,
    onSuccess: () -> Unit
) {
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var street by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }

    LaunchedEffect(contact) {
        name = contact.name
        lastName = contact.lastName
        phone = contact.phone

        val parts = contact.addressInfo.split(",").map { it.trim() }

        if (parts.size >= 2) {
            street = parts[0]
            number = parts[1]
        } else {
            street = contact.addressInfo
            number = ""
        }
    }
    var regionExpanded by remember { mutableStateOf(false) }
    var communeExpanded by remember { mutableStateOf(false) }

    val status = viewModel.updateStatus

    LaunchedEffect(Unit) {
        viewModel.loadRegions()
    }

    LaunchedEffect(status) {
        if (status == "SUCCESS") {
            Toast.makeText(context, "Contacto actualizado", Toast.LENGTH_SHORT).show()
            viewModel.clearStatus()
            onSuccess()
        } else if (status == "ERROR") {
            Toast.makeText(context, "Error al actualizar", Toast.LENGTH_SHORT).show()
            viewModel.clearStatus()
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 520.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(text = "Editar contacto", style = MaterialTheme.typography.titleMedium)

                OutlinedTextField(
                    value = name, onValueChange = { name = it },
                    label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = lastName, onValueChange = { lastName = it },
                    label = { Text("Apellido") }, modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = phone, onValueChange = { phone = it },
                    label = { Text("Teléfono") }, modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = street, onValueChange = { street = it },
                    label = { Text("Calle") }, modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = number, onValueChange = { number = it },
                    label = { Text("Número") }, modifier = Modifier.fillMaxWidth()
                )

                ExposedDropdownMenuBox(
                    expanded = regionExpanded,
                    onExpandedChange = { regionExpanded = !regionExpanded }
                ) {
                    OutlinedTextField(
                        readOnly = true,
                        value = viewModel.selectedRegion?.regionName ?: "",
                        onValueChange = {},
                        label = { Text("Región") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = regionExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = regionExpanded,
                        onDismissRequest = { regionExpanded = false }
                    ) {
                        viewModel.regions.forEach { region ->
                            DropdownMenuItem(
                                text = { Text(region.regionName) },
                                onClick = {
                                    viewModel.selectedRegion = region
                                    viewModel.loadCommunes(region.id)
                                    viewModel.selectedCommune = null
                                    regionExpanded = false
                                }
                            )
                        }
                    }
                }

                ExposedDropdownMenuBox(
                    expanded = communeExpanded,
                    onExpandedChange = { communeExpanded = !communeExpanded }
                ) {
                    OutlinedTextField(
                        readOnly = true,
                        value = viewModel.selectedCommune?.nameCommunity ?: "",
                        onValueChange = {},
                        label = { Text("Comuna") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = communeExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = communeExpanded,
                        onDismissRequest = { communeExpanded = false }
                    ) {
                        viewModel.communes.forEach { commune ->
                            DropdownMenuItem(
                                text = { Text(commune.nameCommunity) },
                                onClick = {
                                    viewModel.selectedCommune = commune
                                    communeExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        val communeId = viewModel.selectedCommune?.id
                        if (communeId == null) {
                            Toast.makeText(context, "Selecciona una comuna", Toast.LENGTH_LONG).show()
                            return@Button
                        }
                        viewModel.updateContact(
                            ContactUpdateWithAddress(
                                id = contact.id, name = name, lastName = lastName, phone = phone,
                                street = street, number = number, communeId = communeId
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = status != "LOADING"
                ) {
                    Text(if (status == "LOADING") "Guardando..." else "Guardar cambios")
                }
            }

            if (status == "LOADING") {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}
