package com.example.bclass.ui.siswa

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bclass.data.model.BadgeItem
import com.example.bclass.data.model.MissionItem
import com.example.bclass.data.repository.BClassRepository
import com.example.ui.theme.*

@Composable
fun GamificationScreen(
    repository: BClassRepository
) {
    val level by repository.studentLevel.collectAsState()
    val xp by repository.studentXp.collectAsState()
    val missions by repository.missions.collectAsState()
    val badges by repository.badges.collectAsState()
    val isLeaderboardEnabled by repository.isLeaderboardEnabled.collectAsState()
    var selectedBadgeForDetail by remember { mutableStateOf<BadgeItem?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BaseBackground)
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
            .testTag("gamification_screen")
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Misi & Lencana",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        )
        Text(
            text = "Gamifikasi berbasis kemajuan belajar personal",
            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Level & XP Card (Mockup L5c: Level 4 · 850 / 1200 XP)
        Surface(
            color = SurfaceCard,
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
                    Column {
                        Text(
                            text = "Level $level",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                        Text(
                            text = "Penjelajah Kurikulum",
                            style = MaterialTheme.typography.bodySmall.copy(color = IndigoTint)
                        )
                    }

                    Text(
                        text = "$xp / 1200 XP",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryIndigo
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                val xpRatio = (xp / 1200f).coerceIn(0f, 1f)
                LinearProgressIndicator(
                    progress = { xpRatio },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = PrimaryIndigo,
                    trackColor = OutlineTrack
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Misi Section (Mockup L5c)
        Text(
            text = "Misi Belajar",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        )
        Spacer(modifier = Modifier.height(10.dp))

        missions.forEach { mission ->
            MissionCard(
                mission = mission,
                onClaim = { repository.claimMission(mission.id) }
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Badges Section (Mockup L5c: 4 columns)
        Text(
            text = "Lencana (Badges)",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        )
        Text(
            text = "Ketuk lencana untuk melihat petunjuk capaian",
            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            badges.forEach { badge ->
                BadgeCircleItem(
                    badge = badge,
                    modifier = Modifier.weight(1f),
                    onClick = { selectedBadgeForDetail = badge }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Papan Peringkat (Opsional - Section 4.5 & Mockup L5c)
        Surface(
            color = SurfaceCard,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Papan Peringkat (Opsional)",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                        Text(
                            text = "Default nonaktif · Menghargai kemajuan",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                        )
                    }

                    Switch(
                        checked = isLeaderboardEnabled,
                        onCheckedChange = { repository.toggleLeaderboard() }
                    )
                }

                AnimatedVisibility(visible = isLeaderboardEnabled) {
                    Column(modifier = Modifier.padding(top = 14.dp)) {
                        Surface(
                            color = PrimaryDeeper,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.TrendingUp, contentDescription = null, tint = IndigoTint)
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Kemajuan Pribadi: Naik +150 XP dibanding pekan lalu! Konsistensi belajar terbaik di kelas.",
                                    style = MaterialTheme.typography.bodySmall.copy(color = IndigoTint, fontWeight = FontWeight.Medium)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }

    // Badge Detail Dialog
    selectedBadgeForDetail?.let { b ->
        AlertDialog(
            onDismissRequest = { selectedBadgeForDetail = null },
            containerColor = SurfaceCard,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(b.iconEmoji, fontSize = 28.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(b.title, color = TextPrimary, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column {
                    Text(
                        text = if (b.isUnlocked) "Status: Terbuka ✓" else "Status: Terkunci (Tantangan)",
                        color = if (b.isUnlocked) SuccessGreen else WarningAmber,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(b.unlockHint, color = TextSecondary)
                }
            },
            confirmButton = {
                TextButton(onClick = { selectedBadgeForDetail = null }) {
                    Text("Tutup", color = IndigoTint)
                }
            }
        )
    }
}

@Composable
private fun MissionCard(
    mission: MissionItem,
    onClaim: () -> Unit
) {
    Surface(
        color = SurfaceCard,
        shape = RoundedCornerShape(14.dp),
        border = ButtonDefaults.outlinedButtonBorder.copy(
            brush = androidx.compose.ui.graphics.SolidColor(OutlineTrack)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = if (mission.isTeam) PrimaryDeeper else SurfaceSunken,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = if (mission.isTeam) "Tim" else "Individu",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = if (mission.isTeam) IndigoTint else TextSecondary,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Text(
                    text = "+${mission.xpReward} XP",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = WarningAmber,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = mission.title,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LinearProgressIndicator(
                    progress = { mission.progress },
                    modifier = Modifier
                        .weight(1f)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = if (mission.progress >= 1f) SuccessGreen else PrimaryIndigo,
                    trackColor = OutlineTrack
                )

                Spacer(modifier = Modifier.width(12.dp))

                if (mission.progress >= 1f && !mission.isClaimed) {
                    Button(
                        onClick = onClaim,
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text("Klaim XP", color = BaseBackground, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }
                } else {
                    Text(
                        text = "${(mission.progress * 100).toInt()}%",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TextSecondary,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun BadgeCircleItem(
    badge: BadgeItem,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        color = if (badge.isUnlocked) PrimaryDeeper else SurfaceSunken,
        shape = RoundedCornerShape(14.dp),
        border = ButtonDefaults.outlinedButtonBorder.copy(
            brush = androidx.compose.ui.graphics.SolidColor(if (badge.isUnlocked) PrimaryIndigo else OutlineTrack)
        ),
        modifier = modifier
            .height(72.dp)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = badge.iconEmoji,
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (badge.isUnlocked) badge.title.take(8) + ".." else "?",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = if (badge.isUnlocked) TextPrimary else TextSecondary,
                    fontSize = 9.sp
                )
            )
        }
    }
}
