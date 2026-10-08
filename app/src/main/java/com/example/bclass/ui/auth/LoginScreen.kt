package com.example.bclass.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FamilyRestroom
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bclass.data.model.UserRole
import com.example.ui.theme.*

@Composable
fun LoginScreen(
    onLoginSuccess: (UserRole) -> Unit,
    onNavigateToRegister: () -> Unit,
    onShowPrivacyPolicy: () -> Unit
) {
    var selectedRoleHint by remember { mutableStateOf(UserRole.SISWA) }
    var identifier by remember { mutableStateOf("0052341678") }
    var password by remember { mutableStateOf("bclass2026") }
    var passwordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isSubmitting by remember { mutableStateOf(false) }

    // When role hint changes, prefill appropriate sample identifier
    LaunchedEffect(selectedRoleHint) {
        when (selectedRoleHint) {
            UserRole.SISWA -> {
                identifier = "0052341678"
                password = "password123"
            }
            UserRole.GURU -> {
                identifier = "198504122010012015"
                password = "password123"
            }
            UserRole.ORANG_TUA -> {
                identifier = "081234567890"
                password = "password123"
            }
            UserRole.ADMIN -> {
                identifier = "admin@bclass.edu"
                password = "password123"
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BaseBackground)
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(28.dp))

        // App Logo
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(64.dp)
                .background(PrimaryIndigo, RoundedCornerShape(18.dp))
        ) {
            Text(
                text = "B",
                style = MaterialTheme.typography.displayLarge.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 36.sp
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Masuk ke B-CLASS",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        )

        Text(
            text = "Pilih peran sebagai petunjuk masuk",
            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
            modifier = Modifier.padding(top = 4.dp, bottom = 18.dp)
        )

        // Role Selector Chips (Mockup L1b)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            RoleHintButton(
                title = "Siswa",
                icon = Icons.Default.School,
                isSelected = selectedRoleHint == UserRole.SISWA,
                modifier = Modifier.weight(1f)
            ) { selectedRoleHint = UserRole.SISWA }

            RoleHintButton(
                title = "Guru",
                icon = Icons.Default.Person,
                isSelected = selectedRoleHint == UserRole.GURU,
                modifier = Modifier.weight(1f)
            ) { selectedRoleHint = UserRole.GURU }

            RoleHintButton(
                title = "Orang Tua",
                icon = Icons.Default.FamilyRestroom,
                isSelected = selectedRoleHint == UserRole.ORANG_TUA,
                modifier = Modifier.weight(1f)
            ) { selectedRoleHint = UserRole.ORANG_TUA }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Identifier input field
        val idLabel = when (selectedRoleHint) {
            UserRole.SISWA -> "NISN / Email Siswa"
            UserRole.GURU -> "NIP / NIK / Email Guru"
            UserRole.ORANG_TUA -> "Nomor Ponsel Terdaftar / Email"
            UserRole.ADMIN -> "Email Administrator"
        }

        OutlinedTextField(
            value = identifier,
            onValueChange = {
                identifier = it
                errorMessage = null
            },
            label = { Text(idLabel, color = TextSecondary) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("login_identifier_input"),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = SurfaceSunken,
                unfocusedContainerColor = SurfaceSunken,
                focusedBorderColor = PrimaryIndigo,
                unfocusedBorderColor = OutlineTrack,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            ),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Password input field
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                errorMessage = null
            },
            label = { Text("Kata Sandi", color = TextSecondary) },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                onLoginSuccess(selectedRoleHint)
            }),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Tampilkan kata sandi",
                        tint = TextSecondary
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("login_password_input"),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = SurfaceSunken,
                unfocusedContainerColor = SurfaceSunken,
                focusedBorderColor = PrimaryIndigo,
                unfocusedBorderColor = OutlineTrack,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            ),
            shape = RoundedCornerShape(12.dp)
        )

        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = DangerRed,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Remember me and Forgot password
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = rememberMe,
                    onCheckedChange = { rememberMe = it },
                    colors = CheckboxDefaults.colors(checkedColor = PrimaryIndigo)
                )
                Text("Ingat saya", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
            }

            TextButton(onClick = { /* Help link */ }) {
                Text("Lupa kata sandi?", style = MaterialTheme.typography.bodySmall.copy(color = IndigoTint))
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Login Button (#5F61EE)
        Button(
            onClick = {
                isSubmitting = true
                onLoginSuccess(selectedRoleHint)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("login_submit_button"),
            colors = ButtonDefaults.buttonColors(containerColor = IndigoButton),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Masuk",
                style = MaterialTheme.typography.labelLarge.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Masuk dengan akun sekolah
        OutlinedButton(
            onClick = { onLoginSuccess(selectedRoleHint) },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            border = ButtonDefaults.outlinedButtonBorder.copy(
                brush = androidx.compose.ui.graphics.SolidColor(OutlineTrack)
            )
        ) {
            Text(
                text = "Masuk dengan akun sekolah",
                style = MaterialTheme.typography.labelLarge.copy(color = TextSecondary)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Section 2.4 WAJIB: Klausul Pelindungan Data Pribadi Anak
        Surface(
            color = AIContainer,
            shape = RoundedCornerShape(14.dp),
            border = ButtonDefaults.outlinedButtonBorder.copy(
                brush = androidx.compose.ui.graphics.SolidColor(OutlineTrack)
            ),
            modifier = Modifier.fillMaxWidth().testTag("child_privacy_clause_box")
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = "Perisai UU PDP",
                    tint = IndigoTint,
                    modifier = Modifier.size(24.dp).padding(top = 2.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = "Data anak dilindungi",
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = IndigoTint,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "Pemrosesan data pribadi di B-CLASS mengacu pada UU No. 27 Tahun 2022 (PDP). Data hanya dipakai untuk pembelajaran dan dikunci sesuai peran.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        ),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    Text(
                        text = "Pelajari selengkapnya ↗",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = IndigoTint,
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier.clickable { onShowPrivacyPolicy() }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Belum punya akun? ", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
            Text(
                text = "Daftar",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = IndigoTint,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.clickable { onNavigateToRegister() }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun RoleHintButton(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        color = if (isSelected) PrimaryIndigo else SurfaceCard,
        shape = RoundedCornerShape(12.dp),
        border = if (isSelected) null else ButtonDefaults.outlinedButtonBorder.copy(
            brush = androidx.compose.ui.graphics.SolidColor(OutlineTrack)
        ),
        modifier = modifier
            .height(44.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isSelected) Color.White else TextSecondary,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium.copy(
                    color = if (isSelected) Color.White else TextSecondary,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            )
        }
    }
}
