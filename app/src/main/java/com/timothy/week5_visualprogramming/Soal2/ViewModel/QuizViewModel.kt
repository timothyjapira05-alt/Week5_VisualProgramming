package com.timothy.week5_visualprogramming.Soal2.ViewModel

import androidx.lifecycle.ViewModel
import com.timothy.week5_visualprogramming.Soal2.Model.Question
import com.timothy.week5_visualprogramming.Soal2.Model.QuestionBank
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.collections.getOrNull
import kotlin.collections.take

enum class QuizPhase { START, QUIZ, RESULT }
data class WrongAnswer(
    val number: Int,
    val question: String,
    val yourAnswer: String,
    val correctAnswer: String,
)

data class QuizUiState(
    val phase: QuizPhase = QuizPhase.START,
    val countInput: String = "",
    val countError: String? = null,
    val questions: List<Question> = emptyList(),
    val answers: List<String> = emptyList(),
    val selected: String? = null,
) {
    val currentIndex: Int get() = answers.size
    val currentQuestion: Question? get() = questions.getOrNull(currentIndex)

    val canStart: Boolean get() = countInput.isNotEmpty() && countError == null

    val score: Int
        get() = questions.indices.count { i -> answers.getOrNull(i) == questions[i].correctAnswer }

    val wrongAnswers: List<WrongAnswer>
        get() = questions.indices.mapNotNull { i ->
            val answer = answers.getOrNull(i) ?: return@mapNotNull null
            val q = questions[i]
            if (answer == q.correctAnswer) null
            else WrongAnswer(number = i + 1, question = q.text, yourAnswer = answer, correctAnswer = q.correctAnswer)
        }
}

class QuizViewModel internal constructor(
    private val bank: List<Question>,
    private val shuffler: (List<Question>) -> List<Question>,
) : ViewModel() {

    constructor() : this(QuestionBank.questions, { it.shuffled() })

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    val maxQuestions: Int get() = bank.size
    fun onCountChange(value: String) {
        val digits = value.filter { it.isDigit() }.take(3)
        _uiState.update { it.copy(countInput = digits, countError = validateCount(digits)) }
    }

    fun startQuiz() {
        val input = _uiState.value.countInput
        val error = validateCount(input)
        val count = input.toIntOrNull()
        if (count == null || error != null) {
            _uiState.update { it.copy(countError = error ?: "Enter a number") }
            return
        }
        beginQuiz(count)
    }

    fun startDefault() {
        if (_uiState.value.phase == QuizPhase.START) {
            beginQuiz(minOf(DEFAULT_COUNT, bank.size))
        }
    }
    fun selectAnswer(option: String) {
        _uiState.update { if (it.phase == QuizPhase.QUIZ) it.copy(selected = option) else it }
    }

    fun next() {
        _uiState.update { state ->
            val chosen = state.selected ?: return@update state
            if (state.phase != QuizPhase.QUIZ) return@update state
            val answers = state.answers + chosen
            state.copy(
                answers = answers,
                selected = null,
                phase = if (answers.size >= state.questions.size) QuizPhase.RESULT else QuizPhase.QUIZ,
            )
        }
    }

    fun restart() {
        val count = _uiState.value.questions.size
        beginQuiz(if (count > 0) count else minOf(DEFAULT_COUNT, bank.size))
    }
    fun backToStart() {
        _uiState.value = QuizUiState()
    }
    private fun beginQuiz(count: Int) {
        val picked = shuffler(bank).take(count.coerceIn(1, bank.size))
        _uiState.value = QuizUiState(phase = QuizPhase.QUIZ, questions = picked)
    }
    private fun validateCount(input: String): String? {
        if (input.isEmpty()) return null
        val n = input.toIntOrNull() ?: return "Enter a valid number"
        return when {
            n < 1 -> "Minimum is 1 question"
            n > maxQuestions -> "Maximum is $maxQuestions questions"
            else -> null
        }
    }

    companion object {
        const val DEFAULT_COUNT = 3
    }
}
