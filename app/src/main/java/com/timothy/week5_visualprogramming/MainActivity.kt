package com.timothy.week5_visualprogramming

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.timothy.week5_visualprogramming.Soal1.Ui.ExpenseScreen
import com.timothy.week5_visualprogramming.Soal2.Ui.QuizApp
import com.timothy.week5_visualprogramming.ui.theme.Week5_VisualProgrammingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Week5_VisualProgrammingTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    AppRoot()
                }
            }
        }
    }
}

private enum class Page { HOME, SOAL1, SOAL2 }

@Composable
private fun AppRoot() {
    var page by rememberSaveable { mutableStateOf(Page.HOME) }

    BackHandler(enabled = page != Page.HOME) { page = Page.HOME }

    when (page) {
        Page.HOME -> HomeScreen(
            onOpenSoal1 = { page = Page.SOAL1 },
            onOpenSoal2 = { page = Page.SOAL2 },
        )
        Page.SOAL1 -> ExpenseScreen()
        Page.SOAL2 -> QuizApp(withStartPage = true)
    }
}

@Composable
private fun HomeScreen(onOpenSoal1: () -> Unit, onOpenSoal2: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Assignment Week 5",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(24.dp))
        Button(onClick = onOpenSoal1, modifier = Modifier.fillMaxWidth()) { Text("Soal 1 - Expenses") }
        Spacer(Modifier.height(12.dp))
        Button(onClick = onOpenSoal2, modifier = Modifier.fillMaxWidth()) { Text("Soal 2 - Mini Quiz") }
    }
}
