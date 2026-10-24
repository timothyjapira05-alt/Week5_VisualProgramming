package com.timothy.week5_visualprogramming.Soal2.Ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.timothy.week5_visualprogramming.Soal2.Model.Question
import com.timothy.week5_visualprogramming.Soal2.ViewModel.QuizUiState
import com.timothy.week5_visualprogramming.Soal2.ViewModel.QuizViewModel
import com.timothy.week5_visualprogramming.ui.theme.Week5_VisualProgrammingTheme

/** Menampilkan satu soal; tombol Next baru aktif setelah user memilih jawaban. */
@Composable
fun QuizScreen(state: QuizUiState, viewModel: QuizViewModel) {
    val question = state.currentQuestion ?: return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Question ${state.currentIndex + 1}",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = question.text,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(16.dp))

        question.options.forEach { option ->
            val isSelected = option == state.selected
            Button(
                onClick = { viewModel.selectAnswer(option) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelected) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.primaryContainer,
                    contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onPrimaryContainer,
                ),
            ) {
                Text(option)
            }
        }

        Spacer(Modifier.height(16.dp))
        Button(
            onClick = viewModel::next,
            enabled = state.selected != null,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Next")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun QuizScreenPreview() {
    Week5_VisualProgrammingTheme {
        QuizScreen(
            state = QuizUiState(
                questions = listOf(Question(1, "Berapa 2 + 2?", listOf("3", "4", "5"), "4")),
                answers = emptyList(),
            ),
            viewModel = viewModel()
        )
    }
}
