package com.example.bclass.ui.guru

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.bclass.data.model.RecommendationStatus
import com.example.bclass.data.model.TeacherRecommendation
import com.example.bclass.data.repository.BClassRepository
import com.example.ui.theme.*

@Composable
fun TeacherRecommendationsScreen(
    repository: BClassRepository
) {
    val recommendations by repository.recommendations.collectAsState()
    var editingRecommendation by remember { mutableStateOf<TeacherRecommendation?>(null) }
    var transparencyRecommendation by remember { mutableStateOf<TeacherRecommendation?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BaseBackground)
            .testTag("teacher_recommendations_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text(
                text = "Rekomendasi AI (Human-in-the-Loop)",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            )
            Text(
                text = "Tidak ada tindakan dieksekusi otomatis tanpa persetujuan guru",
                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                modifier = Modifier.padding(top = 2.dp)
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(recommendations) { rec ->
                RecommendationCardItem(
                    rec = rec,
                    onApprove = { repository.approveRecommendation(rec.id) },
                    onEdit = { editingRecommendation = rec },
                    onReject = { repository.rejectRecommendation(rec.id) },
                    onWhyAdvised = { transparencyRecommendation = rec }
                )
                Spacer(modifier = Modifier.height(14.dp))
            }
        }
    }

    // Modal Editor Sheet for "Ubah" action (Section 5.2)
    editingRecommendation?.let { rec ->
        EditRecommendationSheet(
            rec = rec,
            onDismiss = { editingRecommendation = null },
            onSave = { newTitle, newFormat, newDeadline ->
                repository.updateRecommendation(rec.id, newTitle, newFormat, newDeadline)
                editingRecommendation = null
            }
        )
    }

    // Transparency Dialog: "Mengapa disarankan?" (Section 5.2)
    transparencyRecommendation?.let { rec ->
        AlertDialog(
            onDismissRequest = { transparencyRecommendation = null },
            containerColor = SurfaceCard,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Analytics, contentDescription = null, tint = IndigoTint)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Transparansi Analitik AI", color = TextPrimary, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column {
                    Text(
                        text = "Rekomendasi untuk ${rec.studentNumber} (${rec.studentName})",
                        style = MaterialTheme.typography.titleSmall.copy(color = IndigoTint, fontWeight = FontWeight.SemiBold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = rec.whyAdvisedDetails,
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary, lineHeight = 20.sp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Pola Kesalahan Terdeteksi: Faktorisasi C3 · Waktu Respons Rendah",
                        style = MaterialTheme.typography.labelSmall.copy(color = WarningAmber)
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { transparencyRecommendation = null }) {
                    Text("Tutup", color = IndigoTint)
                }
            }
        )
    }
}

@Composable
private fun RecommendationCardItem(
    rec: TeacherRecommendation,
    onApprove: () -> Unit,
    onEdit: () -> Unit,
    onReject: () -> Unit,
    onWhyAdvised: () -> Unit
) {
    Surface(
        color = AIContainer,
        shape = RoundedCornerShape(16.dp),
        border = ButtonDefaults.outlinedButtonBorder.copy(
            brush = androidx.compose.ui.graphics.SolidColor(
                when (rec.status) {
                    RecommendationStatus.DISETUJUI -> SuccessGreen
                    RecommendationStatus.DITOLAK -> DangerRed
                    RecommendationStatus.DIUBAH -> PrimaryIndigo
                    RecommendationStatus.MENUNGGU -> OutlineTrack
                }
            )
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Student + Type Badge + Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val isRemedial = rec.type == "Remedial"
                    Surface(
                        color = if (isRemedial) DangerContainer else PrimaryDeeper,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = rec.type,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (isRemedial) DangerRed else IndigoTint,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "${rec.studentNumber}: ${rec.studentName}",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                }

                // Current decision status pill
                Surface(
                    color = when (rec.status) {
                        RecommendationStatus.DISETUJUI -> SuccessContainer
                        RecommendationStatus.DITOLAK -> DangerContainer
                        RecommendationStatus.DIUBAH -> PrimaryDeeper
                        RecommendationStatus.MENUNGGU -> SurfaceSunken
                    },
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = rec.status.name,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = when (rec.status) {
                                RecommendationStatus.DISETUJUI -> SuccessGreen
                                RecommendationStatus.DITOLAK -> DangerRed
                                RecommendationStatus.DIUBAH -> IndigoTint
                                RecommendationStatus.MENUNGGU -> TextSecondary
                            },
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = rec.actionTitle,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
            )

            Text(
                text = rec.aiReason,
                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Text(
                text = "Format: ${rec.formatMateri} · Tenggat: ${rec.deadline}",
                style = MaterialTheme.typography.labelSmall.copy(color = IndigoTint)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // "Mengapa disarankan?" link
            TextButton(
                onClick = onWhyAdvised,
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.height(28.dp)
            ) {
                Text(
                    text = "Mengapa disarankan? ℹ",
                    style = MaterialTheme.typography.labelSmall.copy(color = IndigoTint, fontWeight = FontWeight.SemiBold)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 3 Fixed Action Buttons (Mockup L3c & Section 5.2): Setujui (hijau) · Ubah (outline) · Tolak (merah outline)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Setujui
                Button(
                    onClick = onApprove,
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Setujui", color = BaseBackground, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

                // Ubah
                OutlinedButton(
                    onClick = onEdit,
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    shape = RoundedCornerShape(10.dp),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = androidx.compose.ui.graphics.SolidColor(PrimaryIndigo)
                    )
                ) {
                    Text("Ubah", color = IndigoTint, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                }

                // Tolak
                OutlinedButton(
                    onClick = onReject,
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    shape = RoundedCornerShape(10.dp),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = androidx.compose.ui.graphics.SolidColor(DangerRed)
                    )
                ) {
                    Text("Tolak", color = DangerRed, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditRecommendationSheet(
    rec: TeacherRecommendation,
    onDismiss: () -> Unit,
    onSave: (String, String, String) -> Unit
) {
    var title by remember { mutableStateOf(rec.actionTitle) }
    var format by remember { mutableStateOf(rec.formatMateri) }
    var deadline by remember { mutableStateOf(rec.deadline) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = SurfaceCard
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Ubah Rekomendasi Tindak Lanjut",
                style = MaterialTheme.typography.titleLarge.copy(color = TextPrimary, fontWeight = FontWeight.Bold)
            )
            Text(
                text = "Untuk ${rec.studentNumber} (${rec.studentName})",
                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Judul Tindakan") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = format,
                onValueChange = { format = it },
                label = { Text("Format Materi") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = deadline,
                onValueChange = { deadline = it },
                label = { Text("Tenggat Waktu") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { onSave(title, format, deadline) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryIndigo),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Simpan dan Setujui Tindak Lanjut", color = Color.White, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
