package com.example.financeapp.ui.cards

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.financeapp.data.Card

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCardScreen(
    existingCard: Card? = null,
    onDismiss: () -> Unit,
    onSave: (Card) -> Unit
) {
    var cardHolderName by remember { mutableStateOf(existingCard?.cardHolderName ?: "") }
    var cardNumber by remember { mutableStateOf(existingCard?.cardNumber ?: "") }
    var bankName by remember { mutableStateOf(existingCard?.bankName ?: "") }
    var expiryMonth by remember { mutableStateOf(existingCard?.expiryMonth?.toString() ?: "") }
    var expiryYear by remember { mutableStateOf(existingCard?.expiryYear?.toString() ?: "") }
    var creditLimit by remember { mutableStateOf(existingCard?.creditLimit?.toString() ?: "") }
    var outstandingBalance by remember { mutableStateOf(existingCard?.outstandingBalance?.toString() ?: "") }

    var cardTypeExpanded by remember { mutableStateOf(false) }
    var selectedCardType by remember { mutableStateOf(existingCard?.cardType ?: "Credit") }
    val cardTypes = listOf("Credit", "Debit")

    var networkExpanded by remember { mutableStateOf(false) }
    var selectedNetwork by remember { mutableStateOf(existingCard?.cardNetwork ?: "Visa") }
    val networks = listOf("Visa", "Mastercard", "RuPay", "American Express", "Diners Club")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (existingCard != null) "Edit Card" else "Add Card", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Card Type Dropdown
            ExposedDropdownMenuBox(
                expanded = cardTypeExpanded,
                onExpandedChange = { cardTypeExpanded = !cardTypeExpanded }
            ) {
                OutlinedTextField(
                    value = selectedCardType,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Card Type *") },
                    leadingIcon = { Icon(Icons.Filled.CreditCard, contentDescription = null) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = cardTypeExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(
                    expanded = cardTypeExpanded,
                    onDismissRequest = { cardTypeExpanded = false }
                ) {
                    cardTypes.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type) },
                            onClick = {
                                selectedCardType = type
                                cardTypeExpanded = false
                            }
                        )
                    }
                }
            }

            // Card Holder Name
            OutlinedTextField(
                value = cardHolderName,
                onValueChange = { cardHolderName = it },
                label = { Text("Cardholder Name *") },
                placeholder = { Text("Name on card") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            // Full Card Number
            OutlinedTextField(
                value = cardNumber,
                onValueChange = { if (it.length <= 19) cardNumber = it },
                label = { Text("Card Number *") },
                placeholder = { Text("e.g. 4321 8765 1111 0000") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(12.dp)
            )

            // Card Network
            ExposedDropdownMenuBox(
                expanded = networkExpanded,
                onExpandedChange = { networkExpanded = !networkExpanded }
            ) {
                OutlinedTextField(
                    value = selectedNetwork,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Card Network") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = networkExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(
                    expanded = networkExpanded,
                    onDismissRequest = { networkExpanded = false }
                ) {
                    networks.forEach { network ->
                        DropdownMenuItem(
                            text = { Text(network) },
                            onClick = {
                                selectedNetwork = network
                                networkExpanded = false
                            }
                        )
                    }
                }
            }

            // Issuing Bank
            OutlinedTextField(
                value = bankName,
                onValueChange = { bankName = it },
                label = { Text("Issuing Bank") },
                placeholder = { Text("e.g. HDFC Bank") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            // Expiry Date Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = expiryMonth,
                    onValueChange = { if (it.length <= 2) expiryMonth = it },
                    label = { Text("Month *") },
                    placeholder = { Text("MM") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp)
                )
                OutlinedTextField(
                    value = expiryYear,
                    onValueChange = { if (it.length <= 4) expiryYear = it },
                    label = { Text("Year *") },
                    placeholder = { Text("YYYY") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            // Credit card specific fields
            if (selectedCardType == "Credit") {
                OutlinedTextField(
                    value = creditLimit,
                    onValueChange = { creditLimit = it },
                    label = { Text("Credit Limit (₹)") },
                    placeholder = { Text("e.g. 200000") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = outstandingBalance,
                    onValueChange = { outstandingBalance = it },
                    label = { Text("Outstanding Balance (₹)") },
                    placeholder = { Text("e.g. 15000") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Save Button
            Button(
                onClick = {
                    val month = expiryMonth.toIntOrNull() ?: 1
                    val year = expiryYear.toIntOrNull() ?: 2025
                    if (cardHolderName.isNotBlank() && cardNumber.length >= 13) {
                        onSave(
                            Card(
                                id = existingCard?.id ?: 0L,
                                cardHolderName = cardHolderName.trim(),
                                cardNumber = cardNumber.trim(),
                                cardType = selectedCardType,
                                cardNetwork = selectedNetwork,
                                bankName = bankName.trim(),
                                expiryMonth = month,
                                expiryYear = year,
                                creditLimit = creditLimit.toDoubleOrNull() ?: 0.0,
                                outstandingBalance = outstandingBalance.toDoubleOrNull() ?: 0.0
                            )
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = cardHolderName.isNotBlank() && cardNumber.length >= 13
                        && expiryMonth.isNotBlank() && expiryYear.isNotBlank(),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(if (existingCard != null) "Update Card" else "Save Card", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
