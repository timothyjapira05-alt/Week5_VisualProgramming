package com.timothy.week5_visualprogramming.Soal2.Ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.timothy.week5_visualprogramming.Soal2.ViewModel.QuizUiState
import com.timothy.week5_visualprogramming.Soal2.ViewModel.QuizViewModel
import com.timothy.week5_visualprogramming.ui.theme.Week5_VisualProgrammingTheme

@Composable
fun StartScreen(state: QuizUiState, viewModel: QuizViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Mini Quiz",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "How many questions do you want? (1 - ${viewModel.maxQuestions})",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = state.countInput,
            onValueChange = viewModel::onCountChange,
            label = { Text("Number of questions") },
            singleLine = true,
            isError = state.countError != null,
            supportingText = { state.countError?.let { Text(it) } },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { if (state.canStart) viewModel.startQuiz() }),
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = viewModel::startQuiz,
            enabled = state.canStart,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Start Quiz")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StartScreenPreview() {
    Week5_VisualProgrammingTheme {
        StartScreen(state = QuizUiState(), viewModel = viewModel())
    }
}
