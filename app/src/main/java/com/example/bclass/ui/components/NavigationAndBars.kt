package com.example.bclass.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bclass.data.model.UserAccount
import com.example.bclass.data.model.UserRole
import com.example.bclass.data.repository.SyncState
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BClassTopBar(
    title: String,
    subtitle: String? = null,
    user: UserAccount?,
    syncState: SyncState,
    onAvatarClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        modifier = modifier.testTag("bclass_top_bar"),
        title = {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TextSecondary
                        )
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = SurfaceCard,
            titleContentColor = TextPrimary
        ),
        actions = {
            // Sync status badge
            val (syncIcon, syncColor, syncLabel) = when (syncState) {
                SyncState.SYNCED -> Triple(Icons.Default.CheckCircle, SuccessGreen, "Tersinkron")
                SyncState.SYNCING -> Triple(Icons.Default.Sync, WarningAmber, "Sinkron...")
                SyncState.OFFLINE -> Triple(Icons.Default.CloudOff, DangerRed, "Luring")
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(end = 12.dp)
                    .clip(CircleShape)
                    .background(SurfaceSunken)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Icon(
                    imageVector = syncIcon,
                    contentDescription = syncLabel,
                    tint = syncColor,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = syncLabel,
                    style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontSize = 10.sp)
                )
            }

            // User initials Avatar
            if (user != null) {
                val initials = user.name.split(" ")
                    .mapNotNull { it.firstOrNull()?.toString() }
                    .take(2)
                    .joinToString("")
                    .uppercase()

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(PrimaryIndigo)
                        .clickable { onAvatarClick() }
                        .testTag("user_avatar_button")
                ) {
                    Text(
                        text = initials.ifEmpty { "U" },
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    )
}

@Composable
fun OfflineBanner(isOffline: Boolean) {
    AnimatedVisibility(visible = isOffline) {
        Surface(
            color = DangerContainer,
            modifier = Modifier.fillMaxWidth().testTag("offline_banner")
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CloudOff,
                    contentDescription = "Mode luring",
                    tint = DangerText,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Kamu sedang luring. Jawaban & aktivitasmu tersimpan di perangkat.",
                    style = MaterialTheme.typography.labelMedium.copy(color = DangerText)
                )
            }
        }
    }
}

@Composable
fun BClassBottomNavigation(
    role: UserRole,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    NavigationBar(
        containerColor = SurfaceCard,
        contentColor = TextSecondary,
        modifier = Modifier.testTag("bclass_bottom_nav")
    ) {
        when (role) {
            UserRole.SISWA -> {
                // 5 tabs: Beranda, Jalur, Kuis, Diskusi, Misi
                val items = listOf(
                    Triple("Beranda", Icons.Default.Home, 0),
                    Triple("Jalur", Icons.Default.Timeline, 1),
                    Triple("Kuis", Icons.Default.Assignment, 2),
                    Triple("Diskusi", Icons.Default.Forum, 3),
                    Triple("Misi", Icons.Default.EmojiEvents, 4)
                )
                items.forEach { (label, icon, index) ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { onTabSelected(index) },
                        icon = { Icon(icon, contentDescription = label) },
                        label = { Text(label) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = TextPrimary,
                            indicatorColor = PrimaryIndigo,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary
                        )
                    )
                }
            }
            UserRole.GURU -> {
                // 5 tabs: Beranda (Smart Dashboard), Kelas, Soal, Rekom. (AI), Diskusi
                val items = listOf(
                    Triple("Beranda", Icons.Default.Dashboard, 0),
                    Triple("Kelas", Icons.Default.People, 1),
                    Triple("Soal", Icons.Default.Quiz, 2),
                    Triple("Rekom. (AI)", Icons.Default.Psychology, 3),
                    Triple("Diskusi", Icons.Default.Forum, 4)
                )
                items.forEach { (label, icon, index) ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { onTabSelected(index) },
                        icon = { Icon(icon, contentDescription = label) },
                        label = { Text(label) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = TextPrimary,
                            indicatorColor = PrimaryIndigo,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary
                        )
                    )
                }
            }
            UserRole.ORANG_TUA -> {
                // 4 tabs: Ringkasan, Kemajuan, Pesan, Saran
                val items = listOf(
                    Triple("Ringkasan", Icons.Default.Assessment, 0),
                    Triple("Kemajuan", Icons.Default.TrendingUp, 1),
                    Triple("Pesan", Icons.AutoMirrored.Filled.Chat, 2),
                    Triple("Saran", Icons.Default.Lightbulb, 3)
                )
                items.forEach { (label, icon, index) ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { onTabSelected(index) },
                        icon = { Icon(icon, contentDescription = label) },
                        label = { Text(label) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = TextPrimary,
                            indicatorColor = PrimaryIndigo,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary
                        )
                    )
                }
            }
            UserRole.ADMIN -> {
                val items = listOf(
                    Triple("Pengguna", Icons.Default.AdminPanelSettings, 0),
                    Triple("Audit", Icons.Default.Security, 1)
                )
                items.forEach { (label, icon, index) ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { onTabSelected(index) },
                        icon = { Icon(icon, contentDescription = label) },
                        label = { Text(label) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = TextPrimary,
                            indicatorColor = PrimaryIndigo,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary
                        )
                    )
                }
            }
        }
    }
}
