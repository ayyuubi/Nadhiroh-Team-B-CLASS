package com.example.bclass.ui.siswa

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bclass.data.model.DiscussionPost
import com.example.bclass.data.repository.BClassRepository
import com.example.ui.theme.*

@Composable
fun DiscussionHubScreen(
    repository: BClassRepository,
    currentUserName: String
) {
    var selectedSubTab by remember { mutableStateOf(0) } // 0: Forum, 1: Kelompok, 2: Ulasan Sejawat
    val posts by repository.discussionPosts.collectAsState()
    var newPostText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BaseBackground)
            .testTag("discussion_hub_screen")
    ) {
        // Sub-tabs (Mockup L5b: Forum · Kelompok · Ulasan Sejawat)
        TabRow(
            selectedTabIndex = selectedSubTab,
            containerColor = SurfaceCard,
            contentColor = TextPrimary,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedSubTab]),
                    color = PrimaryIndigo
                )
            }
        ) {
            Tab(
                selected = selectedSubTab == 0,
                onClick = { selectedSubTab = 0 },
                text = { Text("Forum", fontWeight = if (selectedSubTab == 0) FontWeight.Bold else FontWeight.Normal) }
            )
            Tab(
                selected = selectedSubTab == 1,
                onClick = { selectedSubTab = 1 },
                text = { Text("Kelompok", fontWeight = if (selectedSubTab == 1) FontWeight.Bold else FontWeight.Normal) }
            )
            Tab(
                selected = selectedSubTab == 2,
                onClick = { selectedSubTab = 2 },
                text = { Text("Ulasan Sejawat", fontWeight = if (selectedSubTab == 2) FontWeight.Bold else FontWeight.Normal) }
            )
        }

        when (selectedSubTab) {
            0 -> {
                // Forum Tab (Mockup L5b)
                Column(modifier = Modifier.weight(1f)) {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp),
                        contentPadding = PaddingValues(vertical = 12.dp)
                    ) {
                        items(posts) { post ->
                            if (post.isTeacherTrigger) {
                                // Pinned Teacher Trigger Question Card (Mockup L5b)
                                PinnedTeacherQuestionCard(post = post)
                            } else {
                                // Student Reply Card
                                StudentReplyCard(
                                    post = post,
                                    onAgreeClick = { repository.togglePostAgree(post.id) }
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                        }

                        // AI Automatic Summary Card (Mockup L5b)
                        item {
                            AiSummaryCard()
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }

                    // Bottom write box
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
                                value = newPostText,
                                onValueChange = { newPostText = it },
                                placeholder = { Text("Tulis tanggapan atau pertanyaan...", color = TextSecondary, fontSize = 13.sp) },
                                modifier = Modifier
                                    .weight(1f)
                                    .heightIn(min = 44.dp),
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
                                    if (newPostText.isNotBlank()) {
                                        repository.addDiscussionPost(newPostText, currentUserName, "Siswa XI-A")
                                        newPostText = ""
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
            1 -> {
                // Group Project Tab
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text("Kerja Kelompok: Analisis Data Statistika", style = MaterialTheme.typography.titleMedium.copy(color = TextPrimary, fontWeight = FontWeight.Bold))
                    Text("Kelompok 2 · Anggota: Rafi, Bella, Ahmad, Dewi", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                    Spacer(modifier = Modifier.height(16.dp))

                    Surface(
                        color = SurfaceCard,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Progres Kelompok: 70%", style = MaterialTheme.typography.titleSmall.copy(color = PrimaryIndigo, fontWeight = FontWeight.Bold))
                            Spacer(modifier = Modifier.height(6.dp))
                            LinearProgressIndicator(
                                progress = { 0.7f },
                                modifier = Modifier.fillMaxWidth().height(6.dp),
                                color = PrimaryIndigo,
                                trackColor = OutlineTrack
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Tugas Aktif: Olah data survei mean, median, modus dalam Google Sheets & slide presentasi.", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                        }
                    }
                }
            }
            2 -> {
                // Peer Review Tab
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text("Ulasan Sejawat (Peer Review)", style = MaterialTheme.typography.titleMedium.copy(color = TextPrimary, fontWeight = FontWeight.Bold))
                    Text("Evaluasi tugas kelompok lain secara konstruktif dengan rubrik", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                    Spacer(modifier = Modifier.height(16.dp))

                    Surface(
                        color = SurfaceCard,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Tugas Kelompok 1: Representasi Diagram Batang", style = MaterialTheme.typography.titleSmall.copy(color = TextPrimary, fontWeight = FontWeight.Bold))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Kriteria: Kejelasan sumbu koordinat, ketepatan interval data, kesimpulan.", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = { /* Submit Review */ },
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryIndigo),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Beri Skor & Komentar Konstruktif", color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PinnedTeacherQuestionCard(post: DiscussionPost) {
    Surface(
        color = AIContainer,
        shape = RoundedCornerShape(16.dp),
        border = ButtonDefaults.outlinedButtonBorder.copy(
            brush = androidx.compose.ui.graphics.SolidColor(PrimaryIndigo)
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
                    color = PrimaryDeeper,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.PushPin, contentDescription = null, tint = IndigoTint, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Pertanyaan Pemantik dari Guru",
                            style = MaterialTheme.typography.labelSmall.copy(color = IndigoTint, fontWeight = FontWeight.Bold)
                        )
                    }
                }

                Text(post.timestamp, style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary))
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = post.content,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 22.sp
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.School, contentDescription = null, tint = IndigoTint, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(post.authorName, style = MaterialTheme.typography.labelSmall.copy(color = IndigoTint))
            }
        }
    }
}

@Composable
private fun StudentReplyCard(
    post: DiscussionPost,
    onAgreeClick: () -> Unit
) {
    Surface(
        color = SurfaceCard,
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(SurfaceSunken)
                    ) {
                        Text(
                            text = post.authorName.firstOrNull()?.toString() ?: "S",
                            style = MaterialTheme.typography.labelSmall.copy(color = TextPrimary, fontWeight = FontWeight.Bold)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(post.authorName, style = MaterialTheme.typography.labelMedium.copy(color = TextPrimary, fontWeight = FontWeight.Bold))
                }
                Text(post.timestamp, style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontSize = 10.sp))
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = post.content,
                style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary, lineHeight = 20.sp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilledTonalButton(
                    onClick = onAgreeClick,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = if (post.isAgreedByMe) PrimaryDeeper else SurfaceSunken,
                        contentColor = if (post.isAgreedByMe) IndigoTint else TextSecondary
                    ),
                    modifier = Modifier.height(34.dp)
                ) {
                    Icon(Icons.Default.ThumbUp, contentDescription = "Setuju", modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Setuju (${post.agreesCount})", fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.width(8.dp))

                OutlinedButton(
                    onClick = { /* Balas */ },
                    shape = RoundedCornerShape(8.dp),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = androidx.compose.ui.graphics.SolidColor(OutlineTrack)
                    ),
                    modifier = Modifier.height(34.dp)
                ) {
                    Text("Balas", fontSize = 12.sp, color = TextSecondary)
                }
            }
        }
    }
}

@Composable
private fun AiSummaryCard() {
    Surface(
        color = AIContainer,
        shape = RoundedCornerShape(14.dp),
        border = ButtonDefaults.outlinedButtonBorder.copy(
            brush = androidx.compose.ui.graphics.SolidColor(OutlineTrack)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = IndigoTint, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Ringkasan Otomatis (AI)",
                    style = MaterialTheme.typography.labelLarge.copy(color = IndigoTint, fontWeight = FontWeight.Bold)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Diskusi menyimpulkan bahwa diskriminan D = b² - 4ac menentukan secara visual banyak titik potong kurva parabola dengan sumbu-x (D > 0 dua titik, D = 0 satu titik, D < 0 tidak memotong).",
                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, lineHeight = 18.sp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Dihasilkan otomatis oleh AI · Dapat dikoreksi guru",
                style = MaterialTheme.typography.labelSmall.copy(color = TextTertiary, fontSize = 9.sp)
            )
        }
    }
}
