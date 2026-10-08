package com.example.bclass.ui.guru

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bclass.data.repository.BClassRepository
import com.example.ui.theme.*

@Composable
fun ClassManagementScreen(
    repository: BClassRepository
) {
    val currentThreshold by repository.masteryThreshold.collectAsState()
    var sliderValue by remember { mutableFloatStateOf(currentThreshold.toFloat()) }
    val students by repository.teacherStudents.collectAsState()

    // Calculate how many students are at risk based on slider value
    val atRiskCount = remember(sliderValue, students) {
        val thresh = sliderValue.toInt()
        students.count { s ->
            s.scoresPerTopic.values.any { it < thresh }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BaseBackground)
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
            .testTag("class_management_screen")
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Manajemen Kelas & Penilaian",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        )
        Text(
            text = "Pengaturan ambang ketuntasan, bank soal, dan validasi skor",
            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Section 5.3: Pengaturan Ambang Ketuntasan
        Surface(
            color = SurfaceCard,
            shape = RoundedCornerShape(16.dp),
            border = ButtonDefaults.outlinedButtonBorder.copy(
                brush = androidx.compose.ui.graphics.SolidColor(OutlineTrack)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Ambang Ketuntasan (KKM)",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                    Text(
                        text = "${sliderValue.toInt()} / 100",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryIndigo
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Slider(
                    value = sliderValue,
                    onValueChange = {
                        sliderValue = it
                        repository.setMasteryThreshold(it.toInt())
                    },
                    valueRange = 50f..90f,
                    steps = 7,
                    colors = SliderDefaults.colors(
                        thumbColor = PrimaryIndigo,
                        activeTrackColor = PrimaryIndigo,
                        inactiveTrackColor = OutlineTrack
                    )
                )

                // Live Impact Preview (Section 5.3 & 9)
                Surface(
                    color = AIContainer,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = IndigoTint, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Pratinjau Dampak: Pada ambang ${sliderValue.toInt()}, terdeteksi $atRiskCount dari ${students.size} siswa memerlukan alur remedial.",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Section 5.3: Bank Soal & HOTS
        Text(
            text = "Bank Soal Adaptif (C1 - C5)",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        )
        Text(
            text = "Kunci jawaban diamankan dan tidak dimuat pada sisi siswa",
            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
            modifier = Modifier.padding(bottom = 10.dp)
        )

        val questions = remember { repository.getFormativeQuizQuestions() }
        questions.forEach { q ->
            Surface(
                color = SurfaceCard,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Surface(color = PrimaryDeeper, shape = RoundedCornerShape(6.dp)) {
                            Text(
                                text = q.cognitiveTag,
                                style = MaterialTheme.typography.labelSmall.copy(color = IndigoTint, fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                        Text(q.topicName, style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary))
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(q.questionText, style = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary))
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Kunci Jawaban Guru: ${q.options[q.correctIndex]}",
                        style = MaterialTheme.typography.labelSmall.copy(color = SuccessGreen, fontWeight = FontWeight.SemiBold)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Section 5.3: Validasi Skor Uraian (Human-in-the-Loop)
        Surface(
            color = SurfaceCard,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Validasi Skor Uraian Siswa",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Jawaban Siswa 06: 'Jika D < 0 maka parabola melayang di atas sumbu x.'\nSkor Awal AI: 85/100 (Rubrik: Konsep tepat).",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row {
                    Button(
                        onClick = { /* Approve Score */ },
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Text("Terima Skor", color = BaseBackground, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedButton(
                        onClick = { /* Modify Score */ },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Text("Ubah Skor", color = TextPrimary, fontSize = 11.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}
