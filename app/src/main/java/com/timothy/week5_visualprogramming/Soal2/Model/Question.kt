package com.timothy.week5_visualprogramming.Soal2.Model

data class Question(
    val id: Int = 0,
    val text: String,
    val options: List<String>,
    val correctAnswer: String,
)

object QuestionBank {
    val questions: List<Question> = listOf(
        Question(
            id = 1,
            text = "Berapa hasil dari 2 x 10?",
            options = listOf("20", "30", "40", "50"),
            correctAnswer = "20"
        ),
        Question(
            id = 2,
            text = "Apa ibu kota Indonesia?",
            options = listOf("Bandung", "Jakarta", "Surabaya", "Ibu Kota Nusantara"),
            correctAnswer = "Jakarta"
        ),
        Question(
            id = 3,
            text = "Siapa Presiden indonesia yang sekarang?",
            options = listOf("Prabowo Subianto", "Jokowi", "Bahlil", "Ganjar"),
            correctAnswer = "Prabowo Subianto"
        ),
        Question(
            id = 4,
            text = "Siapa dosen yang mengajar Visual Programming?",
            options = listOf("Pak Rudi", "Pak Adi", "Bu Citra", "Pak Myche"),
            correctAnswer = "Pak Myche"
        ),
        Question(
            id = 5,
            text = "Siapa yang Beli Macbook M5 Air?",
            options = listOf("Jesson", "Albert", "Darien", "Rey"),
            correctAnswer = "Albert"
        )
    )
}
