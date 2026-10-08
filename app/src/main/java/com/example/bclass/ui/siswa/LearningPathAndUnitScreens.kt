package com.example.bclass.ui.siswa

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bclass.data.model.LearningUnit
import com.example.bclass.data.model.UnitStatus
import com.example.bclass.data.repository.BClassRepository
import com.example.ui.theme.*

@Composable
fun LearningPathScreen(
    repository: BClassRepository,
    onSelectUnit: (String) -> Unit
) {
    val units by repository.learningUnits.collectAsState()
    val isLowercase by repository.isAllLowercaseStudent.collectAsState()

    fun String.applyStyle(): String = if (isLowercase) this.lowercase() else this

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BaseBackground)
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
            .testTag("learning_path_screen")
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Jalur Belajar Adaptif".applyStyle(),
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        )
        Text(
            text = "Disesuaikan otomatis dari hasil kuis & tes diagnostik",
            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Vertical Timeline List (Mockup L5a)
        units.forEachIndexed { index, unit ->
            TimelineUnitCard(
                unit = unit,
                isLast = index == units.size - 1,
                onClick = {
                    if (unit.status != UnitStatus.TERKUNCI) {
                        onSelectUnit(unit.id)
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
private fun TimelineUnitCard(
    unit: LearningUnit,
    isLast: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = unit.status != UnitStatus.TERKUNCI) { onClick() }
    ) {
        // Left Column: Status indicator dot + connecting vertical line
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(36.dp)
        ) {
            val (iconVector, dotColor, badgeBackground) = when (unit.status) {
                UnitStatus.SELESAI -> Triple(Icons.Default.Check, SuccessGreen, SuccessContainer)
                UnitStatus.REMEDIAL -> Triple(Icons.Default.PriorityHigh, DangerRed, DangerContainer)
                UnitStatus.PENGAYAAN -> Triple(Icons.Default.Star, PrimaryIndigo, AIContainer)
                UnitStatus.BERJALAN -> Triple(Icons.Default.HourglassBottom, WarningAmber, SurfaceSunken)
                UnitStatus.TERKUNCI -> Triple(Icons.Default.Lock, TextTertiary, SurfaceSunken)
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(badgeBackground)
            ) {
                Icon(
                    imageVector = iconVector,
                    contentDescription = null,
                    tint = dotColor,
                    modifier = Modifier.size(18.dp)
                )
            }

            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(72.dp)
                        .background(OutlineTrack)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Right Column: Unit Card
        val cardBorderColor = when (unit.status) {
            UnitStatus.REMEDIAL -> DangerRed
            UnitStatus.PENGAYAAN -> PrimaryIndigo
            else -> OutlineTrack
        }

        Surface(
            color = SurfaceCard,
            shape = RoundedCornerShape(16.dp),
            border = ButtonDefaults.outlinedButtonBorder.copy(
                brush = androidx.compose.ui.graphics.SolidColor(cardBorderColor)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = unit.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (unit.status == UnitStatus.TERKUNCI) TextSecondary else TextPrimary
                        )
                    )

                    // Status Pill
                    val (statusLabel, statusColor) = when (unit.status) {
                        UnitStatus.SELESAI -> "Selesai" to SuccessGreen
                        UnitStatus.REMEDIAL -> "Perlu penguatan" to DangerRed
                        UnitStatus.PENGAYAAN -> "Pengayaan" to IndigoTint
                        UnitStatus.BERJALAN -> "Sedang berjalan" to WarningAmber
                        UnitStatus.TERKUNCI -> "Terkunci" to TextTertiary
                    }

                    Text(
                        text = statusLabel,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = statusColor,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = unit.description,
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                )

                if (unit.score != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Skor Terakhir: ${unit.score} / 100",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = if (unit.score >= 70) SuccessGreen else DangerRed,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnitDetailScreen(
    unitId: String,
    repository: BClassRepository,
    onBack: () -> Unit,
    onStartQuiz: (String) -> Unit
) {
    val units by repository.learningUnits.collectAsState()
    val unit = units.find { it.id == unitId } ?: units[2] // default to unit 2

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(unit.title, color = TextPrimary) },
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
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Surface(
                color = AIContainer,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.School, contentDescription = null, tint = IndigoTint)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (unit.status == UnitStatus.REMEDIAL) "Modul Penguatan (Remedial)" else "Materi Terbimbing",
                            style = MaterialTheme.typography.titleSmall.copy(color = IndigoTint, fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = unit.description,
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text("Ringkasan Konsep Inti", style = MaterialTheme.typography.titleMedium.copy(color = TextPrimary, fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = unit.conceptSummary,
                style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary, lineHeight = 22.sp)
            )

            if (unit.formulaSnippet != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Rumus Kunci", style = MaterialTheme.typography.titleMedium.copy(color = TextPrimary, fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = SurfaceSunken,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = unit.formulaSnippet,
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = IndigoTint,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        ),
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Surface(
                color = SurfaceCard,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Langkah Pengerjaan:", style = MaterialTheme.typography.titleSmall.copy(color = TextPrimary, fontWeight = FontWeight.Bold))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("1. Tentukan nilai a, b, dan c dari persamaan bentuk ax² + bx + c = 0.", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                    Text("2. Hitung sumbu simetri xp = -b / (2a).", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                    Text("3. Substitusikan nilai xp ke dalam fungsi untuk menemukan titik balik puncak (yp).", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = { onStartQuiz(unit.id) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("start_quiz_from_unit"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (unit.status == UnitStatus.REMEDIAL) DangerButton else IndigoButton
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Quiz, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (unit.status == UnitStatus.REMEDIAL) "Mulai Kuis Remedial" else "Uji Pemahaman (Kuis Formatif)",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
