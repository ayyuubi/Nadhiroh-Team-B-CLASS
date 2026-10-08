package com.example.bclass.data.model

enum class UserRole(val label: String, val badgeColorHex: Long) {
    SISWA("Siswa", 0xFF6366F1),
    GURU("Guru", 0xFF22C55E),
    ORANG_TUA("Orang Tua", 0xFFF59E0B),
    ADMIN("Admin Sekolah", 0xFFA5B4FC)
}

data class UserAccount(
    val id: String,
    val name: String,
    val email: String,
    val identifier: String, // NISN, NIP, or Phone
    val role: UserRole,
    val className: String = "Kelas XI-A",
    val subject: String = "Matematika",
    val linkedChildName: String? = null
)

data class TopicCompetency(
    val id: String,
    val name: String,
    val score: Int,
    val threshold: Int = 70
) {
    val isMastered: Boolean get() = score >= threshold
}

enum class UnitStatus {
    SELESAI,
    REMEDIAL,
    PENGAYAAN,
    BERJALAN,
    TERKUNCI
}

data class LearningUnit(
    val id: String,
    val unitNumber: Int,
    val title: String,
    val description: String,
    val status: UnitStatus,
    val score: Int? = null,
    val isOfflineAvailable: Boolean = true,
    val conceptSummary: String = "",
    val formulaSnippet: String? = null
)

data class QuizQuestion(
    val id: String,
    val topicId: String,
    val topicName: String,
    val cognitiveTag: String, // e.g. "C4 · Menganalisis (HOTS)"
    val questionText: String,
    val formula: String? = null,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

data class DiscussionPost(
    val id: String,
    val authorName: String,
    val authorRole: String,
    val isTeacherTrigger: Boolean = false,
    val timestamp: String,
    val content: String,
    val agreesCount: Int = 0,
    val repliesCount: Int = 0,
    val isAgreedByMe: Boolean = false
)

data class MissionItem(
    val id: String,
    val title: String,
    val progress: Float, // 0.0f to 1.0f
    val xpReward: Int,
    val isTeam: Boolean,
    val isClaimed: Boolean = false
)

data class BadgeItem(
    val id: String,
    val title: String,
    val iconEmoji: String,
    val isUnlocked: Boolean,
    val unlockHint: String
)

data class StudentRiskProfile(
    val id: String,
    val studentNumber: String, // e.g. "Siswa 06"
    val studentName: String,
    val averageScore: Int,
    val activity7Days: List<Int>, // minutes per day
    val scoresPerTopic: Map<String, Int>,
    val detectedCauseAi: String,
    val isAtRisk: Boolean
)

enum class RecommendationStatus {
    MENUNGGU,
    DISETUJUI,
    DIUBAH,
    DITOLAK
}

data class TeacherRecommendation(
    val id: String,
    val studentNumber: String,
    val studentName: String,
    val type: String, // "Remedial" or "Pengayaan"
    val actionTitle: String,
    val aiReason: String,
    val status: RecommendationStatus = RecommendationStatus.MENUNGGU,
    val formatMateri: String = "Modul Interaktif + 5 Latihan Terpandu",
    val deadline: String = "Kamis, 16:00",
    val whyAdvisedDetails: String = "Siswa menunjukkan penurunan akurasi pada faktorisasi bentuk ax² + bx + c = 0 sebanyak 3 kali percobaan."
)

data class HomeAdvice(
    val id: String,
    val text: String,
    val category: String = "Dukungan Rumah"
)

data class ChatMessage(
    val id: String,
    val senderName: String,
    val senderRole: String,
    val text: String,
    val timestamp: String,
    val isFromParent: Boolean,
    val isRead: Boolean = true
)
