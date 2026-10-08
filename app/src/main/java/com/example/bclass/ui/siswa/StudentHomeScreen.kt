package com.example.bclass.ui.siswa

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bclass.data.model.UserAccount
import com.example.bclass.data.repository.BClassRepository
import com.example.ui.theme.*

@Composable
fun StudentHomeScreen(
    repository: BClassRepository,
    currentUser: UserAccount?,
    onNavigateToUnit: (String) -> Unit,
    onNavigateToQuiz: (String) -> Unit,
    onNavigateToLearningPath: () -> Unit
) {
    val competencies by repository.studentCompetencies.collectAsState()
    val level by repository.studentLevel.collectAsState()
    val xp by repository.studentXp.collectAsState()
    val streak by repository.studentStreak.collectAsState()
    val isLowercase by repository.isAllLowercaseStudent.collectAsState()

    fun String.applyStyle(): String = if (isLowercase) this.lowercase() else this

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BaseBackground)
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
            .testTag("student_home_screen")
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Greeting Header (Mockup L2a: "Halo, Rafi" - "Kelas XI-A · Matematika")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Halo, ${currentUser?.name ?: "Rafi"}".applyStyle(),
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
                Text(
                    text = "${currentUser?.className ?: "Kelas XI-A"} · ${currentUser?.subject ?: "Matematika"}",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                )
            }

            Surface(
                color = AIContainer,
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Bolt, contentDescription = null, tint = IndigoTint, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$xp XP",
                        style = MaterialTheme.typography.labelSmall.copy(color = IndigoTint, fontWeight = FontWeight.Bold)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Stat Cards Row (Mockup L2a: Level 4, 6 hari streak, 2 Misi)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatSummaryCard(
                title = "Level $level".applyStyle(),
                subtitle = "Kemajuan belajar",
                icon = Icons.Default.MilitaryTech,
                iconTint = PrimaryIndigo,
                modifier = Modifier.weight(1f)
            )

            StatSummaryCard(
                title = "$streak Hari".applyStyle(),
                subtitle = "Streak belajar",
                icon = Icons.Default.LocalFireDepartment,
                iconTint = WarningAmber,
                modifier = Modifier.weight(1f)
            )

            StatSummaryCard(
                title = "2 Misi".applyStyle(),
                subtitle = "Aktif pekan ini",
                icon = Icons.Default.EmojiEvents,
                iconTint = IndigoTint,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Profil Kompetensi (Mockup L2a)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Profil Kompetensi".applyStyle(),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            )

            Text(
                text = "Lihat Jalur ↗".applyStyle(),
                style = MaterialTheme.typography.labelSmall.copy(
                    color = IndigoTint,
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier.clickable { onNavigateToLearningPath() }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Surface(
            color = SurfaceCard,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                competencies.forEachIndexed { index, comp ->
                    val isPass = comp.score >= 70
                    val barColor = if (isPass) SuccessGreen else DangerRed

                    Column(modifier = Modifier.padding(vertical = 5.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = comp.name,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                            Text(
                                text = "${comp.score}%",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = barColor,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        LinearProgressIndicator(
                            progress = { comp.score / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = barColor,
                            trackColor = OutlineTrack
                        )
                    }

                    if (index < competencies.size - 1) {
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // AI Recommendations Section (Mockup L2a: "Rekomendasi AI")
        Text(
            text = "Rekomendasi AI".applyStyle(),
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        )
        Text(
            text = "Berdasarkan hasil kuis dan pola kesalahan terkini",
            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Remedial Card (Fungsi Kuadrat)
        AiStudentCard(
            tag = "Remedial",
            tagColor = DangerRed,
            title = "Fungsi Kuadrat: penguatan konsep",
            reason = "Berdasarkan kuis terakhir, perlu penguatan pada titik puncak grafik parabola.",
            actionLabel = "Mulai Remedial",
            actionColor = DangerButton,
            onClick = { onNavigateToQuiz("UNIT-02") }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Enrichment Card (Statistika)
        AiStudentCard(
            tag = "Pengayaan",
            tagColor = PrimaryIndigo,
            title = "Statistika: studi kasus data riil",
            reason = "Skor 86 pada ukuran pemusatan, siap menuju analisis data majemuk.",
            actionLabel = "Buka Pengayaan",
            actionColor = PrimaryIndigo,
            onClick = { onNavigateToUnit("UNIT-03") }
        )

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
private fun StatSummaryCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        color = SurfaceCard,
        shape = RoundedCornerShape(14.dp),
        border = ButtonDefaults.outlinedButtonBorder.copy(
            brush = androidx.compose.ui.graphics.SolidColor(OutlineTrack)
        ),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextSecondary,
                    fontSize = 10.sp
                )
            )
        }
    }
}

@Composable
private fun AiStudentCard(
    tag: String,
    tagColor: Color,
    title: String,
    reason: String,
    actionLabel: String,
    actionColor: Color,
    onClick: () -> Unit
) {
    Surface(
        color = AIContainer,
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
                Surface(
                    color = if (tag == "Remedial") DangerContainer else PrimaryDeeper,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = tag,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = tagColor,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "AI Powered",
                        tint = IndigoTint,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Rekomendasi AI",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = IndigoTint,
                            fontSize = 11.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            )

            Text(
                text = reason,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextSecondary,
                    lineHeight = 16.sp
                ),
                modifier = Modifier.padding(vertical = 6.dp)
            )

            Button(
                onClick = onClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp),
                colors = ButtonDefaults.buttonColors(containerColor = actionColor),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = actionLabel,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}
