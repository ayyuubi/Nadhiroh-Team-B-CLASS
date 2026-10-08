package com.example.bclass.ui.siswa

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bclass.data.model.QuizQuestion
import com.example.bclass.data.repository.BClassRepository
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    unitId: String,
    repository: BClassRepository,
    onBack: () -> Unit,
    onQuizFinished: (Int) -> Unit
) {
    val questions = remember { repository.getFormativeQuizQuestions() }
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var selectedOptionIndex by remember { mutableStateOf<Int?>(null) }
    var showExplanation by remember { mutableStateOf(false) }
    var correctAnswersCount by remember { mutableStateOf(0) }

    val currentQ = questions[currentQuestionIndex]

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Kuis Formatif", color = TextPrimary, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                        Text("Fungsi Kuadrat · Kelas XI-A", color = TextSecondary, style = MaterialTheme.typography.labelSmall)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = TextPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SurfaceCard)
            )
        },
        containerColor = BaseBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
                .testTag("quiz_screen")
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Cognitive Tag & Progress
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = AIContainer,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = currentQ.cognitiveTag,
                        style = MaterialTheme.typography.labelSmall.copy(color = IndigoTint, fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Text(
                    text = "Soal ${currentQuestionIndex + 1}/${questions.size}",
                    style = MaterialTheme.typography.labelMedium.copy(color = TextSecondary)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { (currentQuestionIndex + 1) / questions.size.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = PrimaryIndigo,
                trackColor = OutlineTrack
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Question Card (Mockup L2b)
            Surface(
                color = SurfaceCard,
                shape = RoundedCornerShape(16.dp),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = androidx.compose.ui.graphics.SolidColor(OutlineTrack)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = currentQ.questionText,
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = TextPrimary,
                            fontWeight = FontWeight.SemiBold,
                            lineHeight = 24.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Big Option Cards A-D (Mockup L2b)
            currentQ.options.forEachIndexed { optIndex, optionText ->
                val isSelected = selectedOptionIndex == optIndex
                val isCorrect = optIndex == currentQ.correctIndex

                val cardBg = when {
                    showExplanation && isCorrect -> SuccessContainer
                    showExplanation && isSelected && !isCorrect -> DangerContainer
                    isSelected -> PrimaryDeeper
                    else -> SurfaceCard
                }

                val cardBorder = when {
                    showExplanation && isCorrect -> SuccessGreen
                    showExplanation && isSelected && !isCorrect -> DangerRed
                    isSelected -> PrimaryIndigo
                    else -> OutlineTrack
                }

                val letterBg = when {
                    showExplanation && isCorrect -> SuccessGreen
                    showExplanation && isSelected && !isCorrect -> DangerRed
                    isSelected -> PrimaryIndigo
                    else -> SurfaceSunken
                }

                Surface(
                    color = cardBg,
                    shape = RoundedCornerShape(14.dp),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = androidx.compose.ui.graphics.SolidColor(cardBorder)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                        .clickable(enabled = !showExplanation) {
                            selectedOptionIndex = optIndex
                            showExplanation = true
                            if (isCorrect) correctAnswersCount++
                        }
                        .testTag("quiz_option_$optIndex")
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val letter = ('A' + optIndex).toString()
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(letterBg)
                        ) {
                            Text(
                                text = letter,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Text(
                            text = optionText,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = when {
                                    showExplanation && isCorrect -> SuccessText
                                    showExplanation && isSelected && !isCorrect -> DangerText
                                    else -> TextPrimary
                                },
                                fontWeight = if (isSelected || (showExplanation && isCorrect)) FontWeight.Bold else FontWeight.Normal
                            ),
                            modifier = Modifier.weight(1f)
                        )

                        // Status Tag (Mockup L2b: Benar / Jawabanmu)
                        if (showExplanation) {
                            if (isCorrect) {
                                Surface(color = SuccessGreen, shape = RoundedCornerShape(6.dp)) {
                                    Text(
                                        text = "Benar ✓",
                                        style = MaterialTheme.typography.labelSmall.copy(color = BaseBackground, fontWeight = FontWeight.Bold),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            } else if (isSelected) {
                                Surface(color = DangerRed, shape = RoundedCornerShape(6.dp)) {
                                    Text(
                                        text = "Jawabanmu ✗",
                                        style = MaterialTheme.typography.labelSmall.copy(color = Color.White, fontWeight = FontWeight.Bold),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Instant Feedback Card (Mockup L2b)
            AnimatedVisibility(visible = showExplanation) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    Surface(
                        color = AIContainer,
                        shape = RoundedCornerShape(14.dp),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            brush = androidx.compose.ui.graphics.SolidColor(OutlineTrack)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = IndigoTint, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Pembahasan (Umpan Balik Segera)",
                                    style = MaterialTheme.typography.titleSmall.copy(color = IndigoTint, fontWeight = FontWeight.Bold)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = currentQ.explanation,
                                style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary, lineHeight = 20.sp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Button(
                        onClick = {
                            if (currentQuestionIndex + 1 < questions.size) {
                                currentQuestionIndex++
                                selectedOptionIndex = null
                                showExplanation = false
                            } else {
                                // Calculate score (e.g. out of 100)
                                val finalScore = ((correctAnswersCount.toFloat() / questions.size) * 100).toInt()
                                repository.updateQuizScoreForUnit2(finalScore)
                                onQuizFinished(finalScore)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("quiz_next_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = IndigoButton),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = if (currentQuestionIndex + 1 < questions.size) "Soal Berikutnya" else "Lihat Hasil Kuis",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
fun QuizResultScreen(
    score: Int,
    repository: BClassRepository,
    onContinueUnit: () -> Unit,
    onStartRemedial: () -> Unit
) {
    val threshold by repository.masteryThreshold.collectAsState()
    val isPassed = score >= threshold

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BaseBackground)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Hasil Kuis Formatif",
            style = MaterialTheme.typography.titleLarge.copy(color = TextPrimary, fontWeight = FontWeight.Bold)
        )
        Text(
            text = "Fungsi Kuadrat · Kelas XI-A",
            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
            modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
        )

        // Mockup L2c: Big Score Display
        val containerColor = if (isPassed) SuccessContainer else DangerContainer
        val textColor = if (isPassed) SuccessGreen else DangerRed
        val labelTitle = if (isPassed) "Tuntas (Ambang $threshold)" else "Belum Tuntas (Ambang $threshold)"
        val explanationText = if (isPassed)
            "Selamat! Penguasaan materi telah melampaui ambang. Kamu dapat lanjut ke unit berikutnya atau pengayaan yang lebih menantang."
        else
            "Remedial: penjelasan alternatif, latihan penguatan, lalu kuis ulang untuk memperkuat konsep prasyarat."

        Surface(
            color = SurfaceCard,
            shape = RoundedCornerShape(20.dp),
            border = ButtonDefaults.outlinedButtonBorder.copy(
                brush = androidx.compose.ui.graphics.SolidColor(if (isPassed) SuccessGreen else DangerRed)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Score Digit
                Text(
                    text = "$score",
                    style = MaterialTheme.typography.displayLarge.copy(
                        color = textColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 56.sp
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    color = containerColor,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = labelTitle,
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = if (isPassed) SuccessText else DangerText,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = explanationText,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextSecondary,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Action CTA (Mockup L2c)
        if (isPassed) {
            Button(
                onClick = onContinueUnit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("quiz_result_pass_cta"),
                colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Lanjut ke Unit Berikutnya / Pengayaan",
                    fontWeight = FontWeight.Bold,
                    color = BaseBackground
                )
            }
        } else {
            Button(
                onClick = onStartRemedial,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("quiz_result_remedial_cta"),
                colors = ButtonDefaults.buttonColors(containerColor = DangerButton),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Mulai Remedial",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Quick simulation switcher for testing both pass & fail states easily
        OutlinedButton(
            onClick = {
                val toggled = if (score >= 70) 64 else 86
                repository.updateQuizScoreForUnit2(toggled)
            },
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Simulasi Skor Lain (${if (score >= 70) "Uji 64 Belum Tuntas" else "Uji 86 Tuntas"})",
                style = MaterialTheme.typography.labelSmall.copy(color = IndigoTint)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
