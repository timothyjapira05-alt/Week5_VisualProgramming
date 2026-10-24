package com.timothy.week5_visualprogramming.Soal1.ViewModel

import androidx.lifecycle.ViewModel
import com.timothy.week5_visualprogramming.Soal1.Model.Expense
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.collections.filterNot
import kotlin.collections.map
data class ExpenseUiState(
    val expenses: List<Expense> = emptyList(),
    val nameInput: String = "",
    val amountInput: String = "",
    val amountError: String? = null,
    val editingId: Long? = null,
    val editAmountInput: String = "",
    val editAmountError: String? = null,
) {
    val total: Long get() = expenses.sumOf { it.amount }

    val canAdd: Boolean
        get() = nameInput.isNotBlank() && (amountInput.toLongOrNull() ?: 0L) > 0L
}
class ExpenseViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ExpenseUiState())
    val uiState: StateFlow<ExpenseUiState> = _uiState.asStateFlow()
    private var nextId = 1L
    fun onNameChange(value: String) {
        _uiState.update { it.copy(nameInput = value) }
    }
    fun onAmountChange(value: String) {
        val digits = value.filter { it.isDigit() }.take(MAX_DIGITS)
        _uiState.update { it.copy(amountInput = digits, amountError = validateAmount(digits)) }
    }
    fun addExpense() {
        val state = _uiState.value
        val amount = state.amountInput.toLongOrNull()
        if (amount == null || amount <= 0L) {
            _uiState.update { it.copy(amountError = AMOUNT_ERROR) }
            return
        }
        val name = state.nameInput.trim()
        if (name.isEmpty()) return

        val expense = Expense(id = nextId++, name = name, amount = amount)
        _uiState.update {
            it.copy(
                expenses = it.expenses + expense,
                nameInput = "",
                amountInput = "",
                amountError = null,
            )
        }
    }

    fun deleteExpense(id: Long) {
        _uiState.update { state ->
            val removingEditedItem = state.editingId == id
            state.copy(
                expenses = state.expenses.filterNot { it.id == id },
                editingId = if (removingEditedItem) null else state.editingId,
                editAmountInput = if (removingEditedItem) "" else state.editAmountInput,
                editAmountError = if (removingEditedItem) null else state.editAmountError,
            )
        }
    }
    fun startEdit(id: Long) {
        val expense = _uiState.value.expenses.firstOrNull { it.id == id } ?: return
        _uiState.update {
            it.copy(
                editingId = id,
                editAmountInput = expense.amount.toString(),
                editAmountError = null,
            )
        }
    }
    fun onEditAmountChange(value: String) {
        val digits = value.filter { it.isDigit() }.take(MAX_DIGITS)
        _uiState.update { it.copy(editAmountInput = digits, editAmountError = validateAmount(digits)) }
    }

    fun saveEdit() {
        val state = _uiState.value
        val id = state.editingId ?: return
        val amount = state.editAmountInput.toLongOrNull()
        if (amount == null || amount <= 0L) {
            _uiState.update { it.copy(editAmountError = AMOUNT_ERROR) }
            return
        }
        _uiState.update { s ->
            s.copy(
                expenses = s.expenses.map { if (it.id == id) it.copy(amount = amount) else it },
                editingId = null,
                editAmountInput = "",
                editAmountError = null,
            )
        }
    }

    fun cancelEdit() {
        _uiState.update { it.copy(editingId = null, editAmountInput = "", editAmountError = null) }
    }
    private fun validateAmount(input: String): String? {
        if (input.isEmpty()) return null
        return if ((input.toLongOrNull() ?: 0L) <= 0L) AMOUNT_ERROR else null
    }
    companion object {
        const val AMOUNT_ERROR = "Amount must be greater than 0"
        private const val MAX_DIGITS = 12
    }
}
