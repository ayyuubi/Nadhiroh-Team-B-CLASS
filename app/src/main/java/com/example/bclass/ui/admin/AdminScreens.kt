package com.example.bclass.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
fun AdminOverviewScreen(
    repository: BClassRepository
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BaseBackground)
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
            .testTag("admin_overview_screen")
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Panel Administrator Sekolah",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        )
        Text(
            text = "Pengelolaan Akun, Penautan Orang Tua, dan Audit UU PDP",
            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Account Stats
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            AdminMetricCard("36", "Siswa Aktif", PrimaryIndigo, Modifier.weight(1f))
            AdminMetricCard("4", "Guru Mapel", SuccessGreen, Modifier.weight(1f))
            AdminMetricCard("34", "Orang Tua Tertaut", IndigoTint, Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Penautan Siswa - Orang Tua
        Surface(
            color = SurfaceCard,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Status Penautan Orang Tua - Siswa",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = TextPrimary)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("• Rafi (NISN 0052341678) ↔ Pak Hendra (081234567890) [TERTAUT ✓]", style = MaterialTheme.typography.bodySmall.copy(color = SuccessText))
                Spacer(modifier = Modifier.height(4.dp))
                Text("• Bella Safitri ↔ Ibu Ratna [TERTAUT ✓]", style = MaterialTheme.typography.bodySmall.copy(color = SuccessText))
                Spacer(modifier = Modifier.height(4.dp))
                Text("• Fajar Hidayat (Siswa 06) ↔ Bapak Agus [MENUNGGU OTP ⚠]", style = MaterialTheme.typography.bodySmall.copy(color = WarningAmber))
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Log Audit UU No. 27/2022
        Surface(
            color = AIContainer,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Security, contentDescription = null, tint = IndigoTint)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Log Audit Keamanan & Privasi (UU PDP)",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = IndigoTint)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text("• 08:30 - Login Siswa Rafi (Sesi aktif 30 menit). Role token valid.", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                Text("• 09:15 - Validasi Rekomendasi AI oleh Guru Bu Nurul (Human-in-the-Loop disetujui).", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                Text("• 10:15 - Notifikasi Ringkasan Orang Tua dienkripsi dan dikirim ke kanal terverifikasi.", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
private fun AdminMetricCard(
    value: String,
    label: String,
    color: Color,
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
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, style = MaterialTheme.typography.titleLarge.copy(color = color, fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(2.dp))
            Text(label, style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 10.sp))
        }
    }
}
