package com.example.movilecibershield.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.movilecibershield.data.model.user.ContactCreateWithAddress
import com.example.movilecibershield.viewmodel.ContactEditViewModel
import com.example.movilecibershield.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateContactCard(
    userViewModel: UserViewModel,
    contactViewModel: ContactEditViewModel,
    onSuccess: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var street by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }

    var regionExpanded by remember { mutableStateOf(false) }
    var communeExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        contactViewModel.loadRegions()
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Crear nuevo contacto", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)

            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = lastName, onValueChange = { lastName = it }, label = { Text("Apellido") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Teléfono") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = street, onValueChange = { street = it }, label = { Text("Calle") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = number, onValueChange = { number = it }, label = { Text("Número") }, modifier = Modifier.fillMaxWidth())

            // Selector de Región
            ExposedDropdownMenuBox(expanded = regionExpanded, onExpandedChange = { regionExpanded = !regionExpanded }) {
                OutlinedTextField(
                    readOnly = true,
                    value = contactViewModel.selectedRegion?.regionName ?: "",
                    onValueChange = {},
                    label = { Text("Región") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = regionExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = regionExpanded, onDismissRequest = { regionExpanded = false }) {
                    contactViewModel.regions.forEach { region ->
                        DropdownMenuItem(
                            text = { Text(region.regionName) },
                            onClick = {
                                contactViewModel.selectedRegion = region
                                contactViewModel.loadCommunes(region.id)
                                contactViewModel.selectedCommune = null
                                regionExpanded = false
                            }
                        )
                    }
                }
            }

            // Selector de Comuna
            ExposedDropdownMenuBox(expanded = communeExpanded, onExpandedChange = { communeExpanded = !communeExpanded }) {
                OutlinedTextField(
                    readOnly = true,
                    value = contactViewModel.selectedCommune?.nameCommunity ?: "",
                    onValueChange = {},
                    label = { Text("Comuna") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = communeExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = communeExpanded, onDismissRequest = { communeExpanded = false }) {
                    contactViewModel.communes.forEach { commune ->
                        DropdownMenuItem(
                            text = { Text(commune.nameCommunity) },
                            onClick = {
                                contactViewModel.selectedCommune = commune
                                communeExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    val communeId = contactViewModel.selectedCommune?.id ?: return@Button
                    val newContact = ContactCreateWithAddress(
                        name = name,
                        lastName = lastName,
                        phone = phone,
                        street = street,
                        number = number,
                        communeId = communeId
                    )
                    userViewModel.createContact(newContact)
                    onSuccess()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Contacto")
            }
        }
    }
}
