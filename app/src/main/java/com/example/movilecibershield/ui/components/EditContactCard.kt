package com.example.movilecibershield.ui.components

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Modifier
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
    var name by remember { mutableStateOf(contact.name) }
    var lastName by remember { mutableStateOf(contact.lastName) }
    var phone by remember { mutableStateOf(contact.phone) }
    var street by remember { mutableStateOf(contact.addressInfo.split(",")[0].trim()) }
    var number by remember { mutableStateOf(contact.addressInfo.split(",")[1].trim()) }

    var regionExpanded by remember { mutableStateOf(false) }
    var communeExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadRegions()
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 520.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = "Editar contacto",
                style = MaterialTheme.typography.titleMedium
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Apellido") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Teléfono") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = street,
                onValueChange = { street = it },
                label = { Text("Calle") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = number,
                onValueChange = { number = it },
                label = { Text("Número") },
                modifier = Modifier.fillMaxWidth()
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
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = regionExpanded)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
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
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = communeExpanded)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
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
                    val communeId = viewModel.selectedCommune?.id ?: return@Button

                    viewModel.updateContact(
                        ContactUpdateWithAddress(
                            id = contact.id,
                            name = name,
                            lastName = lastName,
                            phone = phone,
                            street = street,
                            number = number,
                            communeId = communeId
                        )
                    )

                    onSuccess()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar cambios")
            }
        }
    }
}
