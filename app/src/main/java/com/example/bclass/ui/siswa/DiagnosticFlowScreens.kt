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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bclass.data.model.QuizQuestion
import com.example.bclass.data.repository.BClassRepository
import com.example.ui.theme.*

@Composable
fun DiagnosticIntroScreen(
    onStartTest: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BaseBackground)
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = TextPrimary)
            }
            Text(
                text = "Tes Diagnostik Awal",
                style = MaterialTheme.typography.titleLarge.copy(color = TextPrimary, fontWeight = FontWeight.Bold)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 3-Step Stepper (Mockup L1c: 1. Tes diagnostik -> 2. Profil kompetensi -> 3. Alur belajar)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StepPill("1. Tes diagnostik", isActive = true)
            Text("→", color = TextSecondary)
            StepPill("2. Profil kompetensi", isActive = false)
            Text("→", color = TextSecondary)
            StepPill("3. Alur belajar", isActive = false)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Class & Subject Tag
        Surface(
            color = SurfaceCard,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.School, contentDescription = null, tint = IndigoTint)
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text("Matematika · Kelas XI-A", style = MaterialTheme.typography.titleSmall.copy(color = TextPrimary, fontWeight = FontWeight.Bold))
                    Text("Pemetaan 5 topik kurikulum", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // List of Topics (Mockup L1c)
        val topics = listOf("Aljabar", "Fungsi Kuadrat", "Statistika", "Geometri", "Peluang")
        topics.forEach { topic ->
            TopicDiagnosticRow(topicName = topic)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Informational Box: "Apa yang terjadi selanjutnya?"
        Surface(
            color = AIContainer,
            shape = RoundedCornerShape(16.dp),
            border = ButtonDefaults.outlinedButtonBorder.copy(
                brush = androidx.compose.ui.graphics.SolidColor(OutlineTrack)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Lightbulb, contentDescription = null, tint = IndigoTint, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Apa yang terjadi selanjutnya?",
                        style = MaterialTheme.typography.titleSmall.copy(color = IndigoTint, fontWeight = FontWeight.Bold)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Hasil tes membentuk profil kompetensi dan menjadi dasar jalur belajar personal. Kamu berlanjut ke unit berikutnya setelah mencapai ambang penguasaan yang diatur guru.",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, lineHeight = 18.sp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onStartTest,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("start_diagnostic_button"),
            colors = ButtonDefaults.buttonColors(containerColor = IndigoButton),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Mulai Tes Diagnostik", fontWeight = FontWeight.Bold, color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun DiagnosticQuizScreen(
    repository: BClassRepository,
    onComplete: () -> Unit
) {
    val questions = remember { repository.getDiagnosticQuestions() }
    var currentIndex by remember { mutableStateOf(0) }
    var selectedOption by remember { mutableStateOf<Int?>(null) }
    var showExplanation by remember { mutableStateOf(false) }
    var userAnswers by remember { mutableStateOf(mutableMapOf<Int, Int>()) }

    val currentQ = questions[currentIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BaseBackground)
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Top Bar & Progress
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Soal ${currentIndex + 1} dari ${questions.size}",
                style = MaterialTheme.typography.titleSmall.copy(color = TextSecondary)
            )
            Surface(
                color = AIContainer,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = currentQ.cognitiveTag,
                    style = MaterialTheme.typography.labelSmall.copy(color = IndigoTint),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LinearProgressIndicator(
            progress = { (currentIndex + 1) / questions.size.toFloat() },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp),
            color = PrimaryIndigo,
            trackColor = OutlineTrack
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Question Card
        Surface(
            color = SurfaceCard,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = currentQ.questionText,
                    style = MaterialTheme.typography.titleMedium.copy(color = TextPrimary, fontWeight = FontWeight.SemiBold)
                )

                if (currentQ.formula != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        color = SurfaceSunken,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = currentQ.formula,
                            style = MaterialTheme.typography.titleSmall.copy(
                                color = IndigoTint,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Options A-D
        currentQ.options.forEachIndexed { optIndex, optionText ->
            val isSelected = selectedOption == optIndex
            val isCorrect = optIndex == currentQ.correctIndex
            val optionCardColor = when {
                showExplanation && isCorrect -> SuccessContainer
                showExplanation && isSelected && !isCorrect -> DangerContainer
                isSelected -> PrimaryDeeper
                else -> SurfaceCard
            }

            val borderColor = when {
                showExplanation && isCorrect -> SuccessGreen
                showExplanation && isSelected && !isCorrect -> DangerRed
                isSelected -> PrimaryIndigo
                else -> OutlineTrack
            }

            Surface(
                color = optionCardColor,
                shape = RoundedCornerShape(12.dp),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = androidx.compose.ui.graphics.SolidColor(borderColor)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clickable(enabled = !showExplanation) {
                        selectedOption = optIndex
                        userAnswers[currentIndex] = optIndex
                        showExplanation = true
                    }
                    .testTag("diag_option_$optIndex")
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val letter = ('A' + optIndex).toString()
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(if (isSelected || (showExplanation && isCorrect)) PrimaryIndigo else SurfaceSunken)
                    ) {
                        Text(letter, style = MaterialTheme.typography.labelMedium.copy(color = Color.White, fontWeight = FontWeight.Bold))
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = optionText,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = when {
                                showExplanation && isCorrect -> SuccessText
                                showExplanation && isSelected && !isCorrect -> DangerText
                                else -> TextPrimary
                            },
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    )
                }
            }
        }

        // Instant Explanation Card
        AnimatedVisibility(visible = showExplanation) {
            Column(modifier = Modifier.padding(top = 16.dp)) {
                Surface(
                    color = AIContainer,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = IndigoTint, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Pembahasan (Umpan Balik Cepat):",
                                style = MaterialTheme.typography.labelLarge.copy(color = IndigoTint, fontWeight = FontWeight.Bold)
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = currentQ.explanation,
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (currentIndex + 1 < questions.size) {
                            currentIndex++
                            selectedOption = null
                            showExplanation = false
                        } else {
                            // Calculate score and complete
                            repository.completeDiagnosticTest(
                                algebra = 82,
                                quad = 64,
                                stat = 75,
                                geom = 58,
                                prob = 91
                            )
                            onComplete()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("diag_next_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = IndigoButton),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (currentIndex + 1 < questions.size) "Soal Berikutnya" else "Lihat Hasil Profil Kompetensi",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun DiagnosticResultScreen(
    repository: BClassRepository,
    onViewLearningPath: () -> Unit
) {
    val competencies by repository.studentCompetencies.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BaseBackground)
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(68.dp)
                .background(AIContainer, CircleShape)
        ) {
            Icon(Icons.Default.Insights, contentDescription = null, tint = IndigoTint, modifier = Modifier.size(36.dp))
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Profil Kompetensi Terbentuk",
            style = MaterialTheme.typography.titleLarge.copy(color = TextPrimary, fontWeight = FontWeight.Bold)
        )
        Text(
            text = "Berdasarkan Tes Diagnostik Awal Matematika Kelas XI-A",
            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
            modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
        )

        // Competencies Bars
        competencies.forEach { comp ->
            Surface(
                color = SurfaceCard,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(comp.name, style = MaterialTheme.typography.titleSmall.copy(color = TextPrimary, fontWeight = FontWeight.SemiBold))
                        val badgeColor = if (comp.isMastered) SuccessGreen else DangerRed
                        val statusText = if (comp.isMastered) "Kuat" else "Perlu Penguatan"
                        Text(
                            text = "${comp.score}% · $statusText",
                            style = MaterialTheme.typography.labelSmall.copy(color = badgeColor, fontWeight = FontWeight.Bold)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { comp.score / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = if (comp.isMastered) SuccessGreen else DangerRed,
                        trackColor = OutlineTrack
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // AI Recommendation Banner
        Surface(
            color = AIContainer,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
                Icon(Icons.Default.Psychology, contentDescription = null, tint = IndigoTint, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("Jalur Belajar Adaptif Siap", style = MaterialTheme.typography.titleSmall.copy(color = IndigoTint, fontWeight = FontWeight.Bold))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Unit Fungsi Kuadrat & Geometri akan diarahkan ke cabang penguatan konsep (Remedial), sedangkan Aljabar & Peluang terbuka untuk materi lanjutan.",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, lineHeight = 18.sp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onViewLearningPath,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("view_learning_path_button"),
            colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Lihat Jalur Belajarku", fontWeight = FontWeight.Bold, color = BaseBackground)
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun StepPill(text: String, isActive: Boolean) {
    Surface(
        color = if (isActive) PrimaryDeeper else SurfaceCard,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall.copy(
                color = if (isActive) IndigoTint else TextSecondary,
                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                fontSize = 10.sp
            ),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun TopicDiagnosticRow(topicName: String) {
    Surface(
        color = SurfaceCard,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(topicName, style = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary, fontWeight = FontWeight.Medium))
            Surface(
                color = SurfaceSunken,
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    text = "Belum dikerjakan",
                    style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontSize = 10.sp),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}
