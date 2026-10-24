package com.timothy.week5_visualprogramming.Soal1.Ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.timothy.week5_visualprogramming.Soal1.Model.Expense
import com.timothy.week5_visualprogramming.Soal1.ViewModel.ExpenseViewModel
import com.timothy.week5_visualprogramming.ui.theme.Week5_VisualProgrammingTheme
import java.util.Locale

@Composable
fun ExpenseScreen(viewModel: ExpenseViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Text(
            text = "Expenses",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = "Total Expense : ${formatRupiah(state.total)}",
            style = MaterialTheme.typography.bodySmall,
        )
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = state.nameInput,
            onValueChange = viewModel::onNameChange,
            label = { Text("Expense Name") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.Top) {
            OutlinedTextField(
                value = state.amountInput,
                onValueChange = viewModel::onAmountChange,
                label = { Text("Amount of Expense") },
                singleLine = true,
                isError = state.amountError != null,
                supportingText = { state.amountError?.let { Text(it) } },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done,
                ),
                modifier = Modifier.weight(1f),
            )
            Spacer(Modifier.width(8.dp))
            FilledIconButton(
                onClick = viewModel::addExpense,
                enabled = state.canAdd,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .size(40.dp),
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add expense")
            }
        }
        Spacer(Modifier.height(8.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(state.expenses, key = { it.id }) { expense ->
                ExpenseItem(
                    expense = expense,
                    isEditing = state.editingId == expense.id,
                    editInput = state.editAmountInput,
                    editError = state.editAmountError,
                    onStartEdit = { viewModel.startEdit(expense.id) },
                    onEditChange = viewModel::onEditAmountChange,
                    onSaveEdit = viewModel::saveEdit,
                    onCancelEdit = viewModel::cancelEdit,
                    onDelete = { viewModel.deleteExpense(expense.id) },
                )
            }
        }
    }
}

@Composable
private fun ExpenseItem(
    expense: Expense,
    isEditing: Boolean,
    editInput: String,
    editError: String?,
    onStartEdit: () -> Unit,
    onEditChange: (String) -> Unit,
    onSaveEdit: () -> Unit,
    onCancelEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
    ) {
        Column(modifier = Modifier.padding(start = 14.dp, end = 6.dp, top = 10.dp, bottom = 10.dp)) {
            Text(
                text = "Name : ${expense.name}",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
            )
            if (isEditing) {
                Spacer(Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.Top) {
                    OutlinedTextField(
                        value = editInput,
                        onValueChange = onEditChange,
                        label = { Text("New amount") },
                        singleLine = true,
                        isError = editError != null,
                        supportingText = { editError?.let { Text(it) } },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done,
                        ),
                        modifier = Modifier.weight(1f),
                    )
                    IconButton(onClick = onSaveEdit, modifier = Modifier.padding(top = 4.dp)) {
                        Icon(Icons.Filled.Check, contentDescription = "Save", tint = Color(0xFF2E7D32))
                    }
                    IconButton(onClick = onCancelEdit, modifier = Modifier.padding(top = 4.dp)) {
                        Icon(Icons.Filled.Close, contentDescription = "Cancel")
                    }
                }
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = formatRupiah(expense.amount),
                        fontSize = 12.sp,
                        modifier = Modifier.weight(1f),
                    )
                    IconButton(onClick = onStartEdit) {
                        Icon(Icons.Filled.Edit, contentDescription = "Edit", tint = Color(0xFFC99700))
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}
fun formatRupiah(amount: Long): String =
    "Rp " + String.format(Locale.US, "%,d", amount).replace(',', '.')

@Preview(showBackground = true)
@Composable
private fun ExpenseScreenPreview() {
    Week5_VisualProgrammingTheme {
        ExpenseScreen()
    }
}
