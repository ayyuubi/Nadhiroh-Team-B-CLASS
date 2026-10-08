package com.example.bclass.ui.guru

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import com.example.bclass.data.model.StudentRiskProfile
import com.example.bclass.data.repository.BClassRepository
import com.example.ui.theme.*

@Composable
fun TeacherDashboardScreen(
    repository: BClassRepository,
    onSelectStudent: (String) -> Unit
) {
    val students by repository.teacherStudents.collectAsState()
    val threshold by repository.masteryThreshold.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BaseBackground)
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
            .testTag("teacher_dashboard_screen")
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Header: Smart Dashboard (Mockup L3a)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Smart Dashboard",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
                Text(
                    text = "Kelas XI-A · Matematika",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                )
            }

            Surface(
                color = SurfaceCard,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Diperbarui 2 mnt lalu",
                    style = MaterialTheme.typography.labelSmall.copy(color = IndigoTint, fontSize = 10.sp),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 3 Summary Cards (Mockup L3a: Rerata 75, Topik tuntas 3/8, Siswa berisiko 2)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            DashboardSummaryCard(
                value = "75",
                label = "Rata-rata kelas",
                valueColor = TextPrimary,
                modifier = Modifier.weight(1f)
            )

            DashboardSummaryCard(
                value = "3/8",
                label = "Topik tuntas",
                valueColor = SuccessGreen,
                modifier = Modifier.weight(1f)
            )

            DashboardSummaryCard(
                value = "2",
                label = "Siswa berisiko",
                valueColor = DangerRed,
                isAlert = true,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Peta Penguasaan per Topik (Heatmap) Header
        Text(
            text = "Peta Penguasaan per Topik",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        )
        Text(
            text = "Hijau ≥ $threshold · Merah < $threshold (Dapat digeser horizontal)",
            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Interactive Heatmap Table Container (Mockup L3a)
        Surface(
            color = SurfaceCard,
            shape = RoundedCornerShape(16.dp),
            border = ButtonDefaults.outlinedButtonBorder.copy(
                brush = androidx.compose.ui.graphics.SolidColor(OutlineTrack)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .horizontalScroll(rememberScrollState())
            ) {
                // Table Header
                Row(
                    modifier = Modifier.padding(bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.width(84.dp)) {
                        Text("Siswa", style = MaterialTheme.typography.labelMedium.copy(color = TextSecondary, fontWeight = FontWeight.Bold))
                    }
                    val topics = listOf("Aljabar", "F.Kuadrat", "Statistik", "Geometri", "Peluang")
                    topics.forEach { t ->
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.width(54.dp)
                        ) {
                            Text(t, style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontWeight = FontWeight.SemiBold, fontSize = 10.sp))
                        }
                    }
                }

                HorizontalDivider(color = OutlineTrack)
                Spacer(modifier = Modifier.height(6.dp))

                // Student Rows (Siswa 01 to Siswa 08)
                students.forEach { student ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelectStudent(student.id) }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.width(84.dp)
                        ) {
                            if (student.isAtRisk) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(DangerRed)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                            }
                            Text(
                                text = student.studentNumber,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = if (student.isAtRisk) DangerRed else TextPrimary,
                                    fontWeight = if (student.isAtRisk) FontWeight.Bold else FontWeight.Normal
                                )
                            )
                        }

                        // 5 Topic Score Cells
                        val topicKeys = listOf("Aljabar", "Fungsi Kuadrat", "Statistika", "Geometri", "Peluang")
                        topicKeys.forEach { key ->
                            val score = student.scoresPerTopic[key] ?: 70
                            val isPass = score >= threshold
                            val cellBg = if (isPass) SuccessContainer else DangerContainer
                            val cellText = if (isPass) SuccessText else DangerText

                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .padding(horizontal = 2.dp)
                                    .width(50.dp)
                                    .height(34.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(cellBg)
                            ) {
                                Text(
                                    text = "$score",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = cellText,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Early Warning System Section (Mockup L3a)
        Text(
            text = "Early Warning System (Peringatan Dini)",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        )
        Spacer(modifier = Modifier.height(10.dp))

        // Siswa 06 Risk Alert Card
        RiskAlertCard(
            studentNumber = "Siswa 06",
            studentName = "Fajar Hidayat",
            warningReason = "3 topik di bawah ambang batas (Aljabar 49, F. Kuadrat 45, Geometri 36). Aktivitas menurun tajam.",
            onDetailClick = { onSelectStudent("STU-06") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Siswa 03 Risk Alert Card
        RiskAlertCard(
            studentNumber = "Siswa 03",
            studentName = "Cahyo Utomo",
            warningReason = "Aktivitas belajar turun 3 hari berturut-turut pada akhir pekan.",
            onDetailClick = { onSelectStudent("STU-03") }
        )

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
private fun DashboardSummaryCard(
    value: String,
    label: String,
    valueColor: Color,
    isAlert: Boolean = false,
    modifier: Modifier = Modifier
) {
    Surface(
        color = SurfaceCard,
        shape = RoundedCornerShape(14.dp),
        border = ButtonDefaults.outlinedButtonBorder.copy(
            brush = androidx.compose.ui.graphics.SolidColor(if (isAlert) DangerRed else OutlineTrack)
        ),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = valueColor,
                    fontSize = 32.sp
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextSecondary,
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}

@Composable
private fun RiskAlertCard(
    studentNumber: String,
    studentName: String,
    warningReason: String,
    onDetailClick: () -> Unit
) {
    Surface(
        color = SurfaceCard,
        shape = RoundedCornerShape(16.dp),
        border = ButtonDefaults.outlinedButtonBorder.copy(
            brush = androidx.compose.ui.graphics.SolidColor(DangerRed)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onDetailClick() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(DangerContainer)
            ) {
                Icon(Icons.Default.Warning, contentDescription = null, tint = DangerRed, modifier = Modifier.size(20.dp))
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "$studentNumber ($studentName)",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                    Surface(
                        color = DangerContainer,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "Berisiko",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = DangerText,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = warningReason,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary,
                        lineHeight = 16.sp
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentRiskDetailScreen(
    studentId: String,
    repository: BClassRepository,
    onBack: () -> Unit
) {
    val students by repository.teacherStudents.collectAsState()
    val student = students.find { it.id == studentId } ?: students[5] // default Siswa 06
    var showActionSnackbar by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(student.studentNumber, color = TextPrimary, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                        Text("${student.studentName} · Kelas XI-A", color = TextSecondary, style = MaterialTheme.typography.labelSmall)
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
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // 7-Day Activity Chart (Mockup L3b)
            Text(
                text = "Aktivitas 7 Hari (Menit)",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = TextPrimary)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                color = SurfaceCard,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    val days = listOf("Sen", "Sel", "Rab", "Kam", "Jum", "Sab", "Min")
                    student.activity7Days.forEachIndexed { idx, minutes ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "$minutes",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (minutes < 15) DangerRed else TextSecondary,
                                    fontSize = 10.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            val barHeight = (minutes * 1.5).coerceIn(4.0, 70.0).dp
                            Box(
                                modifier = Modifier
                                    .width(22.dp)
                                    .height(barHeight)
                                    .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                    .background(if (minutes < 15) DangerRed else PrimaryIndigo)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = days[idx],
                                style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontSize = 10.sp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Topik di bawah ambang (Mockup L3b)
            Text(
                text = "Topik di Bawah Ambang",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = TextPrimary)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                color = SurfaceCard,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    student.scoresPerTopic.forEach { (topic, score) ->
                        if (score < 70) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(topic, style = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary))
                                Text("$score/100", style = MaterialTheme.typography.labelMedium.copy(color = DangerRed, fontWeight = FontWeight.Bold))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Kartu Penyebab Terdeteksi AI (Mockup L3b)
            Surface(
                color = AIContainer,
                shape = RoundedCornerShape(16.dp),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = androidx.compose.ui.graphics.SolidColor(PrimaryIndigo)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Psychology, contentDescription = null, tint = IndigoTint, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Penyebab Terdeteksi (AI)",
                            style = MaterialTheme.typography.titleSmall.copy(color = IndigoTint, fontWeight = FontWeight.Bold)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = student.detectedCauseAi,
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary, lineHeight = 20.sp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons (Mockup L3b)
            Button(
                onClick = { showActionSnackbar = "Remedial telah dijadwalkan untuk ${student.studentNumber}." },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = IndigoButton),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Jadwalkan Remedial", fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = { showActionSnackbar = "Ringkasan telah dikirim ke Orang Tua ${student.studentNumber}." },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = androidx.compose.ui.graphics.SolidColor(OutlineTrack)
                )
            ) {
                Text("Kirim Ringkasan ke Orang Tua", color = TextPrimary)
            }

            if (showActionSnackbar != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    color = SuccessContainer,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = showActionSnackbar!!,
                        color = SuccessText,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
