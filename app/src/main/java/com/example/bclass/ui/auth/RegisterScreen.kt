package com.example.bclass.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.bclass.data.model.UserRole
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegisterSuccess: (UserRole) -> Unit,
    onBackToLogin: () -> Unit,
    onShowPrivacyPolicy: () -> Unit
) {
    var step by remember { mutableStateOf(1) }
    var selectedRole by remember { mutableStateOf(UserRole.SISWA) }
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var identifier by remember { mutableStateOf("") }
    var agreePDP by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Daftar Akun B-CLASS (Langkah $step/5)", color = TextPrimary, style = MaterialTheme.typography.titleMedium) },
                navigationIcon = {
                    IconButton(onClick = {
                        if (step > 1) step-- else onBackToLogin()
                    }) {
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
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Stepper progress indicator
            LinearProgressIndicator(
                progress = { step / 5f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp),
                color = PrimaryIndigo,
                trackColor = OutlineTrack
            )

            Spacer(modifier = Modifier.height(24.dp))

            when (step) {
                1 -> {
                    // Step 1: Pilih Peran
                    Text(
                        text = "1. Pilih Peran Akun",
                        style = MaterialTheme.typography.titleLarge.copy(color = TextPrimary, fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Peran disimpan permanen pada akun dan tidak dapat ditukar setelah pendaftaran.",
                        style = MaterialTheme.typography.bodySmall.copy(color = DangerRed),
                        modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                    )

                    RoleCard(
                        role = UserRole.SISWA,
                        title = "Siswa (SMA/MA)",
                        description = "Akses modul belajar adaptif, kuis HOTS, Discussion Hub, dan gamifikasi misi.",
                        isSelected = selectedRole == UserRole.SISWA,
                        onClick = { selectedRole = UserRole.SISWA }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    RoleCard(
                        role = UserRole.GURU,
                        title = "Guru Mata Pelajaran",
                        description = "Smart Dashboard, peta penguasaan kelas real-time, kontrol AI Human-in-the-Loop.",
                        isSelected = selectedRole == UserRole.GURU,
                        onClick = { selectedRole = UserRole.GURU }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    RoleCard(
                        role = UserRole.ORANG_TUA,
                        title = "Orang Tua / Wali",
                        description = "Ringkasan kemajuan anak yang tertaut, saran pendampingan rumah, komunikasi dengan guru.",
                        isSelected = selectedRole == UserRole.ORANG_TUA,
                        onClick = { selectedRole = UserRole.ORANG_TUA }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { step = 2 },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = IndigoButton),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Lanjutkan ke Data Akun", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
                2 -> {
                    // Step 2: Data Akun
                    Text("2. Lengkapi Data Akun", style = MaterialTheme.typography.titleLarge.copy(color = TextPrimary, fontWeight = FontWeight.Bold))
                    Text("Gunakan nama lengkap sesuai data resmi sekolah.", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary), modifier = Modifier.padding(top = 4.dp, bottom = 16.dp))

                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text("Nama Lengkap", color = TextSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = SurfaceSunken, unfocusedContainerColor = SurfaceSunken,
                            focusedBorderColor = PrimaryIndigo, unfocusedBorderColor = OutlineTrack,
                            focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Alamat Email Aktif", color = TextSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = SurfaceSunken, unfocusedContainerColor = SurfaceSunken,
                            focusedBorderColor = PrimaryIndigo, unfocusedBorderColor = OutlineTrack,
                            focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Kata Sandi (Min. 8 Karakter)", color = TextSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = SurfaceSunken, unfocusedContainerColor = SurfaceSunken,
                            focusedBorderColor = PrimaryIndigo, unfocusedBorderColor = OutlineTrack,
                            focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { step = 3 },
                        enabled = fullName.isNotBlank() && email.isNotBlank(),
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = IndigoButton),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Lanjutkan ke Verifikasi ID", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
                3 -> {
                    // Step 3: Nomor Identitas
                    val idTitle = when (selectedRole) {
                        UserRole.SISWA -> "Nomor Induk Siswa Nasional (NISN)"
                        UserRole.GURU -> "NIP / NIK Guru"
                        UserRole.ORANG_TUA -> "Nomor Ponsel Terdaftar di Sekolah"
                        UserRole.ADMIN -> "ID Pegawai"
                    }

                    Text("3. Verifikasi Nomor Identitas", style = MaterialTheme.typography.titleLarge.copy(color = TextPrimary, fontWeight = FontWeight.Bold))
                    Text("Data diverifikasi terhadap basis data induk sekolah.", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary), modifier = Modifier.padding(top = 4.dp, bottom = 16.dp))

                    OutlinedTextField(
                        value = identifier,
                        onValueChange = { identifier = it },
                        label = { Text(idTitle, color = TextSecondary) },
                        placeholder = { Text(if (selectedRole == UserRole.SISWA) "10 digit angka" else "Masukkan identitas") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = SurfaceSunken, unfocusedContainerColor = SurfaceSunken,
                            focusedBorderColor = PrimaryIndigo, unfocusedBorderColor = OutlineTrack,
                            focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { step = 4 },
                        enabled = identifier.isNotBlank(),
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = IndigoButton),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Lanjutkan ke Persetujuan PDP", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
                4 -> {
                    // Step 4: Persetujuan PDP
                    Text("4. Persetujuan Pelindungan Data", style = MaterialTheme.typography.titleLarge.copy(color = TextPrimary, fontWeight = FontWeight.Bold))
                    Text("Sesuai amanat Undang-Undang No. 27 Tahun 2022 tentang Pelindungan Data Pribadi.", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary), modifier = Modifier.padding(top = 4.dp, bottom = 16.dp))

                    Surface(
                        color = AIContainer,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "Ketentuan Pelindungan Data Anak & Pengguna",
                                style = MaterialTheme.typography.titleSmall.copy(color = IndigoTint, fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "• Data nilai kuis dan analitik hanya dipakai untuk keperluan personalisasi belajar adaptif di B-CLASS.\n" +
                                        "• Orang tua dan guru hanya dapat melihat capaian belajar siswa dalam kelas yang bersangkutan.\n" +
                                        "• Rekomendasi AI bersifat asistif dan memerlukan persetujuan guru (human-in-the-loop).",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, lineHeight = 18.sp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Baca Dokumen Lengkap Hak Pemilik Data ↗",
                                style = MaterialTheme.typography.labelSmall.copy(color = IndigoTint, fontWeight = FontWeight.Bold),
                                modifier = Modifier.clickable { onShowPrivacyPolicy() }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth().clickable { agreePDP = !agreePDP }
                    ) {
                        Checkbox(
                            checked = agreePDP,
                            onCheckedChange = { agreePDP = it },
                            colors = CheckboxDefaults.colors(checkedColor = PrimaryIndigo)
                        )
                        Text(
                            text = "Saya menyetujui pemrosesan data pribadi untuk keperluan pembelajaran sesuai UU PDP.",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextPrimary)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { step = 5 },
                        enabled = agreePDP,
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = IndigoButton),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Konfirmasi dan Buat Akun", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
                5 -> {
                    // Step 5: Sukses
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(72.dp)
                            .background(SuccessContainer, RoundedCornerShape(36.dp))
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(36.dp))
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Akun Berhasil Dibuat!", style = MaterialTheme.typography.titleLarge.copy(color = TextPrimary, fontWeight = FontWeight.Bold))
                    Text(
                        text = if (selectedRole == UserRole.SISWA)
                            "Selamat datang! Sebagai siswa baru, mari ikuti Tes Diagnostik Awal untuk membentuk profil kompetensimu."
                        else "Akun terdaftar dengan peran ${selectedRole.label}.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary),
                        modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
                    )

                    Button(
                        onClick = { onRegisterSuccess(selectedRole) },
                        modifier = Modifier.fillMaxWidth().height(48.dp).testTag("register_finish_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = if (selectedRole == UserRole.SISWA) PrimaryIndigo else IndigoButton),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = if (selectedRole == UserRole.SISWA) "Mulai Tes Diagnostik Awal" else "Masuk ke Beranda",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun RoleCard(
    role: UserRole,
    title: String,
    description: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        color = if (isSelected) PrimaryDeeper else SurfaceCard,
        shape = RoundedCornerShape(16.dp),
        border = ButtonDefaults.outlinedButtonBorder.copy(
            brush = androidx.compose.ui.graphics.SolidColor(if (isSelected) PrimaryIndigo else OutlineTrack)
        ),
        modifier = Modifier.fillMaxWidth().clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = isSelected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(selectedColor = PrimaryIndigo)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(title, style = MaterialTheme.typography.titleMedium.copy(color = TextPrimary, fontWeight = FontWeight.Bold))
                Text(description, style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
            }
        }
    }
}
