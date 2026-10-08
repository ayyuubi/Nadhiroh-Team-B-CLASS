package com.example.bclass.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.bclass.data.model.UserRole
import com.example.bclass.data.repository.BClassRepository
import com.example.bclass.ui.admin.AdminOverviewScreen
import com.example.bclass.ui.auth.LandingScreen
import com.example.bclass.ui.auth.LoginScreen
import com.example.bclass.ui.auth.RegisterScreen
import com.example.bclass.ui.components.BClassBottomNavigation
import com.example.bclass.ui.components.BClassTopBar
import com.example.bclass.ui.components.OfflineBanner
import com.example.bclass.ui.components.PrivacyPolicyDialog
import com.example.bclass.ui.components.SettingsSheet
import com.example.bclass.ui.guru.ClassManagementScreen
import com.example.bclass.ui.guru.StudentRiskDetailScreen
import com.example.bclass.ui.guru.TeacherDashboardScreen
import com.example.bclass.ui.guru.TeacherRecommendationsScreen
import com.example.bclass.ui.orangtua.ParentAdviceScreen
import com.example.bclass.ui.orangtua.ParentChatScreen
import com.example.bclass.ui.orangtua.ParentOverviewScreen
import com.example.bclass.ui.siswa.*
import com.example.ui.theme.BaseBackground

sealed class ScreenState {
    object Landing : ScreenState()
    object Login : ScreenState()
    object Register : ScreenState()
    object Main : ScreenState()
    // Sub-screens
    object DiagnosticIntro : ScreenState()
    object DiagnosticQuiz : ScreenState()
    object DiagnosticResult : ScreenState()
    data class UnitDetail(val unitId: String) : ScreenState()
    data class Quiz(val unitId: String) : ScreenState()
    data class QuizResult(val score: Int) : ScreenState()
    data class StudentRiskDetail(val studentId: String) : ScreenState()
}

@Composable
fun BClassApp(
    repository: BClassRepository,
    modifier: Modifier = Modifier
) {
    val currentUser by repository.currentUser.collectAsState()
    val isOffline by repository.isOffline.collectAsState()
    val syncState by repository.syncState.collectAsState()
    val isDarkMode by repository.isDarkMode.collectAsState()
    val isDataSaver by repository.isDataSaverEnabled.collectAsState()
    val isAllLowercase by repository.isAllLowercaseStudent.collectAsState()

    var currentScreen by remember { mutableStateOf<ScreenState>(ScreenState.Landing) }
    var selectedTab by remember { mutableIntStateOf(0) }
    var showSettingsSheet by remember { mutableStateOf(false) }
    var showGlobalPrivacyDialog by remember { mutableStateOf(false) }

    // When logging in, route to appropriate landing or diagnostic
    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            if (currentUser?.role == UserRole.SISWA && !repository.hasCompletedDiagnostic.value) {
                currentScreen = ScreenState.DiagnosticIntro
            } else {
                currentScreen = ScreenState.Main
            }
        } else {
            currentScreen = ScreenState.Landing
            selectedTab = 0
        }
    }

    // System Back behavior
    BackHandler(enabled = currentScreen !is ScreenState.Landing) {
        when (currentScreen) {
            is ScreenState.UnitDetail,
            is ScreenState.Quiz,
            is ScreenState.QuizResult,
            is ScreenState.StudentRiskDetail,
            is ScreenState.DiagnosticIntro,
            is ScreenState.DiagnosticQuiz,
            is ScreenState.DiagnosticResult -> {
                currentScreen = ScreenState.Main
            }
            is ScreenState.Login, is ScreenState.Register -> {
                currentScreen = ScreenState.Landing
            }
            is ScreenState.Main -> {
                if (selectedTab != 0) {
                    selectedTab = 0
                }
            }
            ScreenState.Landing -> {
                // Let system exit
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            if (currentUser != null && currentScreen is ScreenState.Main) {
                val title = when (currentUser?.role) {
                    UserRole.SISWA -> "B-CLASS"
                    UserRole.GURU -> "B-CLASS Guru"
                    UserRole.ORANG_TUA -> "B-CLASS Keluarga"
                    UserRole.ADMIN -> "B-CLASS Admin"
                    null -> "B-CLASS"
                }
                val subtitle = when (currentUser?.role) {
                    UserRole.SISWA -> "Siswa · ${currentUser?.className}"
                    UserRole.GURU -> "Guru · ${currentUser?.subject}"
                    UserRole.ORANG_TUA -> "Wali dari ${currentUser?.linkedChildName ?: "Rafi"}"
                    UserRole.ADMIN -> "Administrator Sekolah"
                    null -> null
                }
                Column {
                    BClassTopBar(
                        title = title,
                        subtitle = subtitle,
                        user = currentUser,
                        syncState = syncState,
                        onAvatarClick = { showSettingsSheet = true }
                    )
                    OfflineBanner(isOffline = isOffline)
                }
            }
        },
        bottomBar = {
            if (currentUser != null && currentScreen is ScreenState.Main) {
                BClassBottomNavigation(
                    role = currentUser!!.role,
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it }
                )
            }
        },
        containerColor = BaseBackground
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(BaseBackground)
        ) {
            Crossfade(targetState = currentScreen, label = "screen_transition") { screen ->
                when (screen) {
                    ScreenState.Landing -> {
                        LandingScreen(
                            onNavigateToLogin = { currentScreen = ScreenState.Login },
                            onNavigateToRegister = { currentScreen = ScreenState.Register },
                            onShowPrivacyPolicy = { showGlobalPrivacyDialog = true }
                        )
                    }
                    ScreenState.Login -> {
                        LoginScreen(
                            onLoginSuccess = { role ->
                                repository.loginAs(role)
                            },
                            onNavigateToRegister = { currentScreen = ScreenState.Register },
                            onShowPrivacyPolicy = { showGlobalPrivacyDialog = true }
                        )
                    }
                    ScreenState.Register -> {
                        RegisterScreen(
                            onRegisterSuccess = { role ->
                                repository.loginAs(role)
                            },
                            onBackToLogin = { currentScreen = ScreenState.Login },
                            onShowPrivacyPolicy = { showGlobalPrivacyDialog = true }
                        )
                    }
                    ScreenState.DiagnosticIntro -> {
                        DiagnosticIntroScreen(
                            onStartTest = { currentScreen = ScreenState.DiagnosticQuiz },
                            onBack = { currentScreen = ScreenState.Main }
                        )
                    }
                    ScreenState.DiagnosticQuiz -> {
                        DiagnosticQuizScreen(
                            repository = repository,
                            onComplete = { currentScreen = ScreenState.DiagnosticResult }
                        )
                    }
                    ScreenState.DiagnosticResult -> {
                        DiagnosticResultScreen(
                            repository = repository,
                            onViewLearningPath = {
                                selectedTab = 1
                                currentScreen = ScreenState.Main
                            }
                        )
                    }
                    is ScreenState.UnitDetail -> {
                        UnitDetailScreen(
                            unitId = screen.unitId,
                            repository = repository,
                            onBack = { currentScreen = ScreenState.Main },
                            onStartQuiz = { unitId -> currentScreen = ScreenState.Quiz(unitId) }
                        )
                    }
                    is ScreenState.Quiz -> {
                        QuizScreen(
                            unitId = screen.unitId,
                            repository = repository,
                            onBack = { currentScreen = ScreenState.Main },
                            onQuizFinished = { finalScore ->
                                currentScreen = ScreenState.QuizResult(finalScore)
                            }
                        )
                    }
                    is ScreenState.QuizResult -> {
                        QuizResultScreen(
                            score = screen.score,
                            repository = repository,
                            onContinueUnit = {
                                selectedTab = 1
                                currentScreen = ScreenState.Main
                            },
                            onStartRemedial = {
                                currentScreen = ScreenState.Quiz("UNIT-02")
                            }
                        )
                    }
                    is ScreenState.StudentRiskDetail -> {
                        StudentRiskDetailScreen(
                            studentId = screen.studentId,
                            repository = repository,
                            onBack = { currentScreen = ScreenState.Main }
                        )
                    }
                    ScreenState.Main -> {
                        when (currentUser?.role) {
                            UserRole.SISWA -> {
                                when (selectedTab) {
                                    0 -> StudentHomeScreen(
                                        repository = repository,
                                        currentUser = currentUser,
                                        onNavigateToUnit = { unitId -> currentScreen = ScreenState.UnitDetail(unitId) },
                                        onNavigateToQuiz = { unitId -> currentScreen = ScreenState.Quiz(unitId) },
                                        onNavigateToLearningPath = { selectedTab = 1 }
                                    )
                                    1 -> LearningPathScreen(
                                        repository = repository,
                                        onSelectUnit = { unitId -> currentScreen = ScreenState.UnitDetail(unitId) }
                                    )
                                    2 -> QuizScreen(
                                        unitId = "UNIT-02",
                                        repository = repository,
                                        onBack = { selectedTab = 0 },
                                        onQuizFinished = { score -> currentScreen = ScreenState.QuizResult(score) }
                                    )
                                    3 -> DiscussionHubScreen(
                                        repository = repository,
                                        currentUserName = currentUser?.name ?: "Rafi"
                                    )
                                    4 -> GamificationScreen(
                                        repository = repository
                                    )
                                }
                            }
                            UserRole.GURU -> {
                                when (selectedTab) {
                                    0 -> TeacherDashboardScreen(
                                        repository = repository,
                                        onSelectStudent = { studentId -> currentScreen = ScreenState.StudentRiskDetail(studentId) }
                                    )
                                    1 -> ClassManagementScreen(repository = repository)
                                    2 -> ClassManagementScreen(repository = repository)
                                    3 -> TeacherRecommendationsScreen(repository = repository)
                                    4 -> DiscussionHubScreen(
                                        repository = repository,
                                        currentUserName = currentUser?.name ?: "Bu Nurul, M.Pd."
                                    )
                                }
                            }
                            UserRole.ORANG_TUA -> {
                                when (selectedTab) {
                                    0 -> ParentOverviewScreen(repository = repository)
                                    1 -> ParentOverviewScreen(repository = repository)
                                    2 -> ParentChatScreen(repository = repository)
                                    3 -> ParentAdviceScreen(repository = repository)
                                }
                            }
                            UserRole.ADMIN -> {
                                AdminOverviewScreen(repository = repository)
                            }
                            null -> {
                                Box(modifier = Modifier.fillMaxSize())
                            }
                        }
                    }
                }
            }
        }
    }

    if (showSettingsSheet) {
        SettingsSheet(
            user = currentUser,
            isDarkMode = isDarkMode,
            isDataSaver = isDataSaver,
            isAllLowercase = isAllLowercase,
            isOffline = isOffline,
            onToggleDarkMode = { repository.toggleDarkMode() },
            onToggleDataSaver = { repository.toggleDataSaver() },
            onToggleAllLowercase = { repository.toggleAllLowercaseStudent() },
            onToggleOffline = { repository.toggleOfflineSimulation() },
            onLogout = {
                repository.logout()
                showSettingsSheet = false
            },
            onDismiss = { showSettingsSheet = false }
        )
    }

    if (showGlobalPrivacyDialog) {
        PrivacyPolicyDialog(onDismiss = { showGlobalPrivacyDialog = false })
    }
}
