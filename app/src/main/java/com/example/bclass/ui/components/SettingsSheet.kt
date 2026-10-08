package com.example.bclass.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
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
import com.example.bclass.data.model.UserAccount
import com.example.bclass.data.model.UserRole
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsSheet(
    user: UserAccount?,
    isDarkMode: Boolean,
    isDataSaver: Boolean,
    isAllLowercase: Boolean,
    isOffline: Boolean,
    onToggleDarkMode: () -> Unit,
    onToggleDataSaver: () -> Unit,
    onToggleAllLowercase: () -> Unit,
    onToggleOffline: () -> Unit,
    onLogout: () -> Unit,
    onDismiss: () -> Unit
) {
    var showPrivacyDialog by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = SurfaceCard,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header: User Profile
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(PrimaryIndigo)
                ) {
                    Text(
                        text = user?.name?.firstOrNull()?.toString()?.uppercase() ?: "B",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = user?.name ?: "Pengguna",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                    Text(
                        text = "${user?.role?.label} · ${user?.identifier}",
                        style = MaterialTheme.typography.bodyMedium.copy(color = IndigoTint)
                    )
                    Text(
                        text = "${user?.className} · ${user?.subject}",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Role-Locking Notice
            Surface(
                color = SurfaceSunken,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Role Locked",
                        tint = IndigoTint,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Sesi terkunci pada peran ${user?.role?.label}. Berpindah peran hanya dapat dilakukan lewat Keluar akun (Strict Role-Locking).",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(color = OutlineTrack)
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Pengaturan Tampilan & Sistem",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Dark Mode Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Mode Gelap", style = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary))
                    Text("Skala Navy-Slate bawaan", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                }
                Switch(
                    checked = isDarkMode,
                    onCheckedChange = { onToggleDarkMode() }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Mode Hemat Data
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Mode Hemat Data", style = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary))
                    Text("Kurangi animasi, unduh hanya teks & ringkasan", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                }
                Switch(
                    checked = isDataSaver,
                    onCheckedChange = { onToggleDataSaver() }
                )
            }

            // All-Lowercase for Student
            if (user?.role == UserRole.SISWA) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Gaya Huruf Kecil (Santai)", style = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary))
                        Text("Gaya santai untuk judul antarmuka siswa", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                    }
                    Switch(
                        checked = isAllLowercase,
                        onCheckedChange = { onToggleAllLowercase() }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Offline Simulation Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Simulasi Mode Luring (Offline)", style = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary))
                    Text("Uji cache lokal dan penyimpanan jawaban", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                }
                Switch(
                    checked = isOffline,
                    onCheckedChange = { onToggleOffline() }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = OutlineTrack)
            Spacer(modifier = Modifier.height(12.dp))

            // Privacy Center & Legal
            OutlinedButton(
                onClick = { showPrivacyDialog = true },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Shield, contentDescription = null, tint = IndigoTint)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Pusat Privasi & UU No. 27/2022 PDP", color = IndigoTint)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Logout Button
            Button(
                onClick = {
                    onDismiss()
                    onLogout()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("logout_button"),
                colors = ButtonDefaults.buttonColors(containerColor = DangerButton),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Keluar", tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Keluar dari Akun", color = Color.White, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (showPrivacyDialog) {
        PrivacyPolicyDialog(onDismiss = { showPrivacyDialog = false })
    }
}

@Composable
fun PrivacyPolicyDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceCard,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Security, contentDescription = null, tint = IndigoTint)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Pelindungan Data Pribadi", style = MaterialTheme.typography.titleMedium.copy(color = TextPrimary, fontWeight = FontWeight.Bold))
            }
        },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Text(
                    text = "B-CLASS mematuhi Undang-Undang No. 27 Tahun 2022 tentang Pelindungan Data Pribadi (UU PDP) dengan prinsip Privacy by Design:",
                    style = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. Minimisasi Data: Hanya nomor identitas sekolah (NISN/NIP) yang disimpan untuk keperluan belajar mengajar.", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                Spacer(modifier = Modifier.height(4.dp))
                Text("2. Role-Based Access Control (RBAC): Siswa, guru, dan orang tua hanya mengakses data dalam ruang lingkup haknya.", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                Spacer(modifier = Modifier.height(4.dp))
                Text("3. Perlindungan Anak: Tidak ada pelacakan iklan, tidak ada penjualan data, dan rekomendasi AI selalu diawasi guru (human-in-the-loop).", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                Spacer(modifier = Modifier.height(4.dp))
                Text("4. Hak Koreksi & Penghapusan: Permohonan ekspor atau hapus data dapat diajukan ke Administrator Sekolah kapan saja.", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = IndigoButton)
            ) {
                Text("Saya Mengerti", color = Color.White)
            }
        }
    )
}
