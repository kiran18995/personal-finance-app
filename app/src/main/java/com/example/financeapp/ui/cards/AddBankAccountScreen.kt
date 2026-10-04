package com.example.financeapp.ui.cards

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.financeapp.data.BankAccount

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBankAccountScreen(
    existingAccount: BankAccount? = null,
    onDismiss: () -> Unit,
    onSave: (BankAccount) -> Unit
) {
    var bankName by remember { mutableStateOf(existingAccount?.bankName ?: "") }
    var holderName by remember { mutableStateOf(existingAccount?.accountHolderName ?: "") }
    var accountNumber by remember { mutableStateOf(existingAccount?.accountNumber ?: "") }
    var ifscCode by remember { mutableStateOf(existingAccount?.ifscCode ?: "") }
    var branchName by remember { mutableStateOf(existingAccount?.branchName ?: "") }
    var balance by remember { mutableStateOf(existingAccount?.currentBalance?.toString() ?: "") }
    var expanded by remember { mutableStateOf(false) }
    var selectedAccountType by remember { mutableStateOf(existingAccount?.accountType ?: "Savings") }

    val accountTypes = listOf("Savings", "Current", "Salary", "NRI", "Fixed Deposit")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (existingAccount != null) "Edit Bank Account" else "Add Bank Account", fontWeight = FontWeight.SemiBold) },
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
            // Bank Name
            OutlinedTextField(
                value = bankName,
                onValueChange = { bankName = it },
                label = { Text("Bank Name *") },
                placeholder = { Text("e.g. State Bank of India") },
                leadingIcon = { Icon(Icons.Filled.AccountBalance, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            // Account Holder Name
            OutlinedTextField(
                value = holderName,
                onValueChange = { holderName = it },
                label = { Text("Account Holder Name") },
                placeholder = { Text("e.g. John Doe") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            // Account Number
            OutlinedTextField(
                value = accountNumber,
                onValueChange = { accountNumber = it },
                label = { Text("Account Number") },
                placeholder = { Text("e.g. 1234567890") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(12.dp)
            )

            // Account Type Dropdown
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedAccountType,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Account Type") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    accountTypes.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type) },
                            onClick = {
                                selectedAccountType = type
                                expanded = false
                            }
                        )
                    }
                }
            }

            // IFSC Code
            OutlinedTextField(
                value = ifscCode,
                onValueChange = { ifscCode = it.uppercase() },
                label = { Text("IFSC Code") },
                placeholder = { Text("e.g. SBIN0001234") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            // Branch Name
            OutlinedTextField(
                value = branchName,
                onValueChange = { branchName = it },
                label = { Text("Branch Name") },
                placeholder = { Text("e.g. MG Road, Bangalore") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            // Current Balance
            OutlinedTextField(
                value = balance,
                onValueChange = { balance = it },
                label = { Text("Current Balance (₹) *") },
                placeholder = { Text("e.g. 50000") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Save Button
            Button(
                onClick = {
                    val parsedBalance = balance.toDoubleOrNull() ?: 0.0
                    if (bankName.isNotBlank()) {
                        onSave(
                            BankAccount(
                                id = existingAccount?.id ?: 0L,
                                bankName = bankName.trim(),
                                accountHolderName = holderName.trim(),
                                accountNumber = accountNumber.trim(),
                                ifscCode = ifscCode.trim(),
                                accountType = selectedAccountType,
                                currentBalance = parsedBalance,
                                branchName = branchName.trim()
                            )
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = bankName.isNotBlank() && balance.isNotBlank(),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(if (existingAccount != null) "Update Account" else "Save Account", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
