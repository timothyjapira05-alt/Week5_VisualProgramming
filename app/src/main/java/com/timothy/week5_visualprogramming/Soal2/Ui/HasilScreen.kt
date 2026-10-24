package com.timothy.week5_visualprogramming.Soal2.Ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.timothy.week5_visualprogramming.Soal2.Model.Question
import com.timothy.week5_visualprogramming.Soal2.ViewModel.QuizUiState
import com.timothy.week5_visualprogramming.ui.theme.Week5_VisualProgrammingTheme

@Composable
fun ResultScreen(state: QuizUiState, onRestart: () -> Unit) {
    val wrong = state.wrongAnswers

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Quiz Finished!",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Your Score: ${state.score} / ${state.questions.size}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(16.dp))

        if (wrong.isEmpty()) {
            Text(
                text = "Perfect! No wrong answers.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f, fill = false),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                items(wrong, key = { it.number }) { item ->
                    Column {
                        Text("Q${item.number}: ${item.question}", style = MaterialTheme.typography.bodySmall)
                        Text(
                            "Your Answer: ${item.yourAnswer}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.error,
                        )
                        Text(
                            "Correct Answer: ${item.correctAnswer}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        Spacer(Modifier.height(4.dp))
                        HorizontalDivider()
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        Button(onClick = onRestart, modifier = Modifier.fillMaxWidth()) {
            Text("Restart Quiz")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ResultScreenPreview() {
    Week5_VisualProgrammingTheme {
        ResultScreen(
            state = QuizUiState(
                questions = listOf(Question(1, "Berapa 2 + 2?", listOf("3", "4"), "4")),
                answers = listOf("3")
            ),
            onRestart = {}
        )
    }
}
