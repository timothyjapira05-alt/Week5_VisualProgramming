package com.timothy.week5_visualprogramming.Soal2.Ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.timothy.week5_visualprogramming.Soal2.ViewModel.QuizPhase
import com.timothy.week5_visualprogramming.Soal2.ViewModel.QuizViewModel
import com.timothy.week5_visualprogramming.ui.theme.Week5_VisualProgrammingTheme
//mengatur halaman quiz
@Composable
fun QuizApp(withStartPage: Boolean = true, viewModel: QuizViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(withStartPage) {
        if (!withStartPage) viewModel.startDefault()
    }

    when (state.phase) {
        QuizPhase.START -> if (withStartPage) StartScreen(state, viewModel)
        QuizPhase.QUIZ -> QuizScreen(state, viewModel)
        QuizPhase.RESULT -> ResultScreen(
            state = state,
            onRestart = if (withStartPage) viewModel::backToStart else viewModel::restart,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun QuizAppPreview() {
    Week5_VisualProgrammingTheme {
        QuizApp()
    }
}
