package com.example.bclass.ui.orangtua

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
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
import com.example.bclass.data.model.ChatMessage
import com.example.bclass.data.repository.BClassRepository
import com.example.ui.theme.*

@Composable
fun ParentOverviewScreen(
    repository: BClassRepository
) {
    val competencies by repository.studentCompetencies.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BaseBackground)
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
            .testTag("parent_overview_screen")
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Header (Mockup L4a: "Ringkasan Ananda" - "Rafi · Kelas XI-A")
        Text(
            text = "Ringkasan Ananda",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        )
        Text(
            text = "Rafi · Kelas XI-A · Matematika",
            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
            modifier = Modifier.padding(bottom = 14.dp)
        )

        // Human-friendly sentence summary banner (Mockup L4a & Section 6.1)
        Surface(
            color = AIContainer,
            shape = RoundedCornerShape(14.dp),
            border = ButtonDefaults.outlinedButtonBorder.copy(
                brush = androidx.compose.ui.graphics.SolidColor(OutlineTrack)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Support, contentDescription = null, tint = IndigoTint, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "“Ananda stabil minggu ini; materi Fungsi Kuadrat perlu sedikit penguatan dan dukungan di rumah.”",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextPrimary,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 20.sp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 3 Stat Cards (Mockup L4a: Kehadiran 96%, Rerata 74, Misi selesai 5/7)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ParentStatCard(
                value = "96%",
                label = "Kehadiran",
                valueColor = SuccessGreen,
                modifier = Modifier.weight(1f)
            )

            ParentStatCard(
                value = "74",
                label = "Rerata nilai",
                valueColor = TextPrimary,
                modifier = Modifier.weight(1f)
            )

            ParentStatCard(
                value = "5/7",
                label = "Misi selesai",
                valueColor = IndigoTint,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Aktivitas Belajar 7 Hari (Mockup L4a)
        Text(
            text = "Aktivitas Belajar 7 Hari",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
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
                val weekMinutes = listOf(45, 50, 40, 55, 60, 30, 45)
                val weekDays = listOf("Sen", "Sel", "Rab", "Kam", "Jum", "Sab", "Min")

                weekMinutes.forEachIndexed { i, mins ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$mins m",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TextSecondary,
                                fontSize = 10.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .width(22.dp)
                                .height((mins * 1.2).dp)
                                .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                .background(PrimaryIndigo)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = weekDays[i],
                            style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontSize = 10.sp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Kompetensi per Topik (Mockup L4a)
        Text(
            text = "Kompetensi per Topik",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        )
        Text(
            text = "Hijau ≥ 70 · Merah < 70 (Perlu penguatan)",
            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
            modifier = Modifier.padding(bottom = 10.dp)
        )

        Surface(
            color = SurfaceCard,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                competencies.forEach { comp ->
                    val isPass = comp.score >= 70
                    val barColor = if (isPass) SuccessGreen else DangerRed

                    Column(modifier = Modifier.padding(vertical = 4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(comp.name, style = MaterialTheme.typography.bodySmall.copy(color = TextPrimary))
                            Text(
                                text = "${comp.score}%",
                                style = MaterialTheme.typography.labelSmall.copy(color = barColor, fontWeight = FontWeight.Bold)
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
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun ParentAdviceScreen(
    repository: BClassRepository
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BaseBackground)
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
            .testTag("parent_advice_screen")
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Notifikasi & Saran",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        )
        Text(
            text = "Ringkasan pekan ini dan rekomendasi pendampingan di rumah",
            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Periodic Notification Cards (Mockup L4b)
        // Attention Card (Amber)
        Surface(
            color = SurfaceCard,
            shape = RoundedCornerShape(14.dp),
            border = ButtonDefaults.outlinedButtonBorder.copy(
                brush = androidx.compose.ui.graphics.SolidColor(WarningAmber)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.Top) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(AIContainer)
                ) {
                    Icon(Icons.Default.PriorityHigh, contentDescription = null, tint = WarningAmber, modifier = Modifier.size(18.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Surface(color = AIContainer, shape = RoundedCornerShape(4.dp)) {
                        Text(
                            text = "Perlu perhatian",
                            style = MaterialTheme.typography.labelSmall.copy(color = WarningAmber, fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Fungsi Kuadrat di bawah ambang",
                        style = MaterialTheme.typography.titleSmall.copy(color = TextPrimary, fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Skor 64. Remedial terbimbing telah disetujui guru pada Kamis.",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Achievement Card (Green)
        Surface(
            color = SurfaceCard,
            shape = RoundedCornerShape(14.dp),
            border = ButtonDefaults.outlinedButtonBorder.copy(
                brush = androidx.compose.ui.graphics.SolidColor(SuccessGreen)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.Top) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(SuccessContainer)
                ) {
                    Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Surface(color = SuccessContainer, shape = RoundedCornerShape(4.dp)) {
                        Text(
                            text = "Capaian Konsistensi",
                            style = MaterialTheme.typography.labelSmall.copy(color = SuccessText, fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Streak 6 hari berturut-turut",
                        style = MaterialTheme.typography.titleSmall.copy(color = TextPrimary, fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Ananda sangat konsisten menyelesaikan misi belajar mandiri harian.",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Panel Saran Dukungan di Rumah (Mockup L4b)
        Text(
            text = "Saran Dukungan di Rumah",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        )
        Text(
            text = "Prinsip: Mendukung dan mendampingi, bukan pengawasan intrusif",
            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        val tips = listOf(
            "Temani ananda 15 menit membahas topik Fungsi Kuadrat tanpa tekanan atau penghakiman.",
            "Apresiasi konsistensi belajar 6 hari berturut-turut untuk menjaga semangat.",
            "Sepakati jadwal belajar tetap di rumah yang santai dan nyaman.",
            "Ajak ananda menjelaskan materi dengan kata-katanya sendiri."
        )

        Surface(
            color = SurfaceCard,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                tips.forEachIndexed { idx, tip ->
                    Row(
                        modifier = Modifier.padding(vertical = 6.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(top = 6.dp)
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(IndigoTint)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = tip,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = TextPrimary,
                                lineHeight = 20.sp
                            )
                        )
                    }
                    if (idx < tips.size - 1) {
                        HorizontalDivider(color = OutlineTrack, modifier = Modifier.padding(vertical = 4.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun ParentChatScreen(
    repository: BClassRepository
) {
    val messages by repository.chatMessages.collectAsState()
    var inputMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BaseBackground)
            .testTag("parent_chat_screen")
    ) {
        // Chat Header (Mockup L4c)
        Surface(
            color = SurfaceCard,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(SuccessContainer)
                ) {
                    Text("NR", style = MaterialTheme.typography.labelLarge.copy(color = SuccessText, fontWeight = FontWeight.Bold))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("Bu Nurul, M.Pd.", style = MaterialTheme.typography.titleSmall.copy(color = TextPrimary, fontWeight = FontWeight.Bold))
                    Text("Guru Matematika Kelas XI-A", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary))
                }
            }
        }

        // Quick message templates (Mockup L4c & Section 6.3)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilledTonalButton(
                onClick = {
                    repository.sendParentChatMessage("Mohon saran dukungan untuk remedial Fungsi Kuadrat.", isFromParent = true)
                },
                modifier = Modifier.height(32.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Mohon saran dukungan", fontSize = 11.sp)
            }

            FilledTonalButton(
                onClick = {
                    repository.sendParentChatMessage("Kapan waktu konsultasi tatap muka?", isFromParent = true)
                },
                modifier = Modifier.height(32.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Ajukan konsultasi", fontSize = 11.sp)
            }
        }

        // Message List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(messages) { msg ->
                ChatBubble(msg = msg)
                Spacer(modifier = Modifier.height(10.dp))
            }
        }

        // Disclaimer (Section 6.3)
        Text(
            text = "Pesan ini diteruskan ke guru dan dapat dibalas pada jam kerja sekolah.",
            style = MaterialTheme.typography.labelSmall.copy(
                color = TextTertiary,
                fontSize = 10.sp,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp)
        )

        // Bottom Input Row
        Surface(
            color = SurfaceCard,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputMessage,
                    onValueChange = { inputMessage = it },
                    placeholder = { Text("Ketik pesan untuk guru...", color = TextSecondary, fontSize = 13.sp) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(20.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SurfaceSunken,
                        unfocusedContainerColor = SurfaceSunken,
                        focusedBorderColor = PrimaryIndigo,
                        unfocusedBorderColor = OutlineTrack,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        if (inputMessage.isNotBlank()) {
                            repository.sendParentChatMessage(inputMessage, isFromParent = true)
                            inputMessage = ""
                        }
                    },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(PrimaryIndigo)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Kirim",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ChatBubble(msg: ChatMessage) {
    val isMe = msg.isFromParent

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
    ) {
        Surface(
            color = if (isMe) PrimaryIndigo else SurfaceCard,
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isMe) 16.dp else 4.dp,
                bottomEnd = if (isMe) 4.dp else 16.dp
            ),
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = msg.text,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = if (isMe) Color.White else TextPrimary,
                        lineHeight = 18.sp
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = msg.timestamp,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = if (isMe) IndigoTint else TextTertiary,
                        fontSize = 10.sp,
                        textAlign = TextAlign.End
                    ),
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}

@Composable
private fun ParentStatCard(
    value: String,
    label: String,
    valueColor: Color,
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = valueColor
                )
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextSecondary,
                    fontSize = 10.sp
                )
            )
        }
    }
}
