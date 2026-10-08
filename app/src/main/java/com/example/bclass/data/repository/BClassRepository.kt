package com.example.bclass.data.repository

import com.example.bclass.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class SyncState {
    SYNCED,
    SYNCING,
    OFFLINE
}

class BClassRepository {

    // Current logged-in user (null = at Landing/Login)
    private val _currentUser = MutableStateFlow<UserAccount?>(null)
    val currentUser: StateFlow<UserAccount?> = _currentUser.asStateFlow()

    // Offline / Data Saver & Preferences
    private val _isOffline = MutableStateFlow(false)
    val isOffline: StateFlow<Boolean> = _isOffline.asStateFlow()

    private val _syncState = MutableStateFlow(SyncState.SYNCED)
    val syncState: StateFlow<SyncState> = _syncState.asStateFlow()

    private val _isDataSaverEnabled = MutableStateFlow(false)
    val isDataSaverEnabled: StateFlow<Boolean> = _isDataSaverEnabled.asStateFlow()

    private val _isDarkMode = MutableStateFlow(true)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _isAllLowercaseStudent = MutableStateFlow(false)
    val isAllLowercaseStudent: StateFlow<Boolean> = _isAllLowercaseStudent.asStateFlow()

    // Mastery threshold (Guru setting, default 70)
    private val _masteryThreshold = MutableStateFlow(70)
    val masteryThreshold: StateFlow<Int> = _masteryThreshold.asStateFlow()

    // Diagnostic Test State
    private val _hasCompletedDiagnostic = MutableStateFlow(true)
    val hasCompletedDiagnostic: StateFlow<Boolean> = _hasCompletedDiagnostic.asStateFlow()

    // Student Competencies
    private val _studentCompetencies = MutableStateFlow(
        listOf(
            TopicCompetency("TOP-01", "Aljabar", 82),
            TopicCompetency("TOP-02", "Fungsi Kuadrat", 64),
            TopicCompetency("TOP-03", "Statistika", 75),
            TopicCompetency("TOP-04", "Geometri", 58),
            TopicCompetency("TOP-05", "Peluang", 91)
        )
    )
    val studentCompetencies: StateFlow<List<TopicCompetency>> = _studentCompetencies.asStateFlow()

    // Student Learning Units (Jalur Belajar)
    private val _learningUnits = MutableStateFlow(
        listOf(
            LearningUnit(
                id = "UNIT-00",
                unitNumber = 0,
                title = "Tes Diagnostik",
                description = "Pemetaan awal kemampuan prasyarat matematika",
                status = UnitStatus.SELESAI,
                score = 78,
                conceptSummary = "Tes diagnostik telah berhasil memetakan penguasaan 5 materi utama.",
                formulaSnippet = "f(x) = ax² + bx + c"
            ),
            LearningUnit(
                id = "UNIT-01",
                unitNumber = 1,
                title = "Unit 1: Aljabar",
                description = "Faktorisasi suku banyak dan sistem persamaan linear",
                status = UnitStatus.SELESAI,
                score = 82,
                conceptSummary = "Pemahaman faktorisasi bentuk kuadrat dan pemfaktoran sempurna.",
                formulaSnippet = "a² - b² = (a - b)(a + b)"
            ),
            LearningUnit(
                id = "UNIT-02",
                unitNumber = 2,
                title = "Unit 2: Fungsi Kuadrat",
                description = "Karakteristik kurva parabola, titik puncak, dan diskriminan",
                status = UnitStatus.REMEDIAL,
                score = 64,
                conceptSummary = "Perlu penguatan konsep titik puncak parabola xp = -b/(2a) dan sumbu simetri.",
                formulaSnippet = "xp = -b / (2a),  yp = -D / (4a)"
            ),
            LearningUnit(
                id = "UNIT-03",
                unitNumber = 3,
                title = "Unit 3: Statistika",
                description = "Ukuran pemusatan dan penyebaran data kelompok",
                status = UnitStatus.PENGAYAAN,
                score = 86,
                conceptSummary = "Studi kasus analisis data riil, kuartil, dan simpangan baku.",
                formulaSnippet = "Mean = Σ(fi · xi) / Σfi"
            ),
            LearningUnit(
                id = "UNIT-04",
                unitNumber = 4,
                title = "Unit 4: Peluang",
                description = "Kombinatorika, permutasi, peluang kejadian majemuk",
                status = UnitStatus.BERJALAN,
                score = null,
                conceptSummary = "Menghitung peluang kejadian saling lepas dan saling bebas.",
                formulaSnippet = "P(A ∪ B) = P(A) + P(B) - P(A ∩ B)"
            ),
            LearningUnit(
                id = "UNIT-05",
                unitNumber = 5,
                title = "Unit 5: Geometri",
                description = "Dimensi tiga: jarak titik ke bidang dan sudut",
                status = UnitStatus.TERKUNCI,
                score = null,
                conceptSummary = "Akan terbuka setelah unit prasyarat tuntas.",
                formulaSnippet = "d = √(Δx² + Δy² + Δz²)"
            )
        )
    )
    val learningUnits: StateFlow<List<LearningUnit>> = _learningUnits.asStateFlow()

    // Student Gamification
    private val _studentLevel = MutableStateFlow(4)
    val studentLevel: StateFlow<Int> = _studentLevel.asStateFlow()

    private val _studentXp = MutableStateFlow(850)
    val studentXp: StateFlow<Int> = _studentXp.asStateFlow()

    private val _studentStreak = MutableStateFlow(6)
    val studentStreak: StateFlow<Int> = _studentStreak.asStateFlow()

    private val _isLeaderboardEnabled = MutableStateFlow(false)
    val isLeaderboardEnabled: StateFlow<Boolean> = _isLeaderboardEnabled.asStateFlow()

    private val _missions = MutableStateFlow(
        listOf(
            MissionItem("MIS-01", "Selesaikan remedial Fungsi Kuadrat", 0.40f, 150, isTeam = false),
            MissionItem("MIS-02", "Baca 3 unit materi Statistika", 1.0f, 200, isTeam = false, isClaimed = false),
            MissionItem("MIS-03", "Proyek analisis data kelompok", 0.30f, 300, isTeam = true)
        )
    )
    val missions: StateFlow<List<MissionItem>> = _missions.asStateFlow()

    private val _badges = MutableStateFlow(
        listOf(
            BadgeItem("BDG-01", "Pejuang Aljabar", "⚡", true, "Selesaikan Unit Aljabar"),
            BadgeItem("BDG-02", "Bintang Diskusi", "★", true, "Buat 5 tanggapan bermakna di Hub"),
            BadgeItem("BDG-03", "Konsisten 7 Hari", "🔥", true, "Belajar berturut-turut tanpa jeda"),
            BadgeItem("BDG-04", "Penakluk Geometri", "?", false, "Raih skor ≥ 80 pada Geometri Dimensi Tiga")
        )
    )
    val badges: StateFlow<List<BadgeItem>> = _badges.asStateFlow()

    // Discussion Hub
    private val _discussionPosts = MutableStateFlow(
        listOf(
            DiscussionPost(
                id = "POST-01",
                authorName = "Bu Nurul, M.Pd.",
                authorRole = "Guru Matematika",
                isTeacherTrigger = true,
                timestamp = "Hari ini, 08:30",
                content = "Pertanyaan Pemantik (C4):\nMengapa grafik fungsi kuadrat bisa memiliki dua, satu, atau tanpa titik potong sumbu-x? Berikan penjelasan berdasarkan nilai diskriminan dan visual kurvanya!",
                agreesCount = 18,
                repliesCount = 6,
                isAgreedByMe = true
            ),
            DiscussionPost(
                id = "POST-02",
                authorName = "Siswa A",
                authorRole = "Siswa XI-A",
                isTeacherTrigger = false,
                timestamp = "Hari ini, 09:15",
                content = "Karena nilai diskriminan menentukan banyaknya akar. Jika D > 0 grafik memotong di dua titik, jika D = 0 grafik menyinggung di satu titik, dan D < 0 melayang.",
                agreesCount = 9,
                repliesCount = 2,
                isAgreedByMe = true
            ),
            DiscussionPost(
                id = "POST-03",
                authorName = "Siswa B",
                authorRole = "Siswa XI-A",
                isTeacherTrigger = false,
                timestamp = "Hari ini, 09:40",
                content = "Saya setuju! Dari rumus abc x = (-b ± √D) / 2a, jika D negatif akar real tidak ada, jadi kurva tidak pernah menembus sumbu x.",
                agreesCount = 6,
                repliesCount = 1,
                isAgreedByMe = false
            )
        )
    )
    val discussionPosts: StateFlow<List<DiscussionPost>> = _discussionPosts.asStateFlow()

    // Teacher Heatmap & Risk Data (8 Siswa)
    private val _teacherStudents = MutableStateFlow(
        listOf(
            StudentRiskProfile("STU-01", "Siswa 01", "Ahmad Fauzi", 82, listOf(40, 50, 45, 30, 40, 20, 35), mapOf("Aljabar" to 82, "Fungsi Kuadrat" to 84, "Statistika" to 75, "Geometri" to 58, "Peluang" to 91), "Performa stabil dan aktif.", false),
            StudentRiskProfile("STU-02", "Siswa 02", "Bella Safitri", 71, listOf(30, 25, 40, 20, 15, 10, 20), mapOf("Aljabar" to 78, "Fungsi Kuadrat" to 54, "Statistika" to 72, "Geometri" to 88, "Peluang" to 65), "Konsep prasyarat fungsi perlu penguatan.", true),
            StudentRiskProfile("STU-03", "Siswa 03", "Cahyo Utomo", 65, listOf(45, 40, 30, 10, 5, 0, 10), mapOf("Aljabar" to 58, "Fungsi Kuadrat" to 52, "Statistika" to 89, "Geometri" to 44, "Peluang" to 80), "Aktivitas turun 3 hari berturut-turut pada akhir pekan.", true),
            StudentRiskProfile("STU-04", "Siswa 04", "Dewi Lestari", 84, listOf(50, 45, 60, 40, 50, 35, 40), mapOf("Aljabar" to 85, "Fungsi Kuadrat" to 81, "Statistika" to 79, "Geometri" to 85, "Peluang" to 92), "Pencapaian sangat baik.", false),
            StudentRiskProfile("STU-05", "Siswa 05", "Eko Prasetyo", 61, listOf(35, 20, 25, 30, 15, 10, 20), mapOf("Aljabar" to 71, "Fungsi Kuadrat" to 66, "Statistika" to 24, "Geometri" to 70, "Peluang" to 77), "Topik Statistika belum dikerjakan tuntas.", true),
            StudentRiskProfile("STU-06", "Siswa 06", "Fajar Hidayat", 49, listOf(45, 30, 20, 10, 5, 0, 15), mapOf("Aljabar" to 49, "Fungsi Kuadrat" to 45, "Statistika" to 65, "Geometri" to 36, "Peluang" to 68), "Aktivitas menurun sejak Kamis dan kesalahan berulang pada konsep prasyarat aljabar. Keputusan tindak lanjut ada pada guru.", true),
            StudentRiskProfile("STU-07", "Siswa 07", "Gita Pertiwi", 91, listOf(60, 55, 65, 50, 45, 40, 50), mapOf("Aljabar" to 93, "Fungsi Kuadrat" to 83, "Statistika" to 90, "Geometri" to 87, "Peluang" to 95), "Potensi pengayaan HOTS C5.", false),
            StudentRiskProfile("STU-08", "Siswa 08", "Hendra Wijaya", 75, listOf(40, 35, 45, 30, 40, 25, 30), mapOf("Aljabar" to 80, "Fungsi Kuadrat" to 77, "Statistika" to 71, "Geometri" to 64, "Peluang" to 82), "Penguasaan cukup rata.", false)
        )
    )
    val teacherStudents: StateFlow<List<StudentRiskProfile>> = _teacherStudents.asStateFlow()

    // Teacher AI Recommendations (Human-in-the-Loop)
    private val _recommendations = MutableStateFlow(
        listOf(
            TeacherRecommendation(
                id = "REC-01",
                studentNumber = "Siswa 06",
                studentName = "Fajar Hidayat",
                type = "Remedial",
                actionTitle = "Jadwalkan Remedial Aljabar & Fungsi Kuadrat",
                aiReason = "Skor 45, kesalahan berulang 3x pada faktorisasi bentuk kuadrat dan titik puncak.",
                status = RecommendationStatus.MENUNGGU,
                formatMateri = "Video Ringkas 5 Menit + Modul Latihan Terarah",
                deadline = "Kamis, 15:00",
                whyAdvisedDetails = "Data analitik kuis mendeteksi 3 jawaban salah beruntun pada soal bertipe faktorisasi C3. Waktu pengerjaan per soal menurun drastis, mengindikasikan kebingungan konseptual."
            ),
            TeacherRecommendation(
                id = "REC-02",
                studentNumber = "Siswa 02",
                studentName = "Bella Safitri",
                type = "Remedial",
                actionTitle = "Penguatan Konsep Prasyarat Fungsi",
                aiReason = "Skor 54 pada kuis fungsi kuadrat, kesulitan mengidentifikasi arah kurva parabola.",
                status = RecommendationStatus.MENUNGGU,
                formatMateri = "LKS Pendamping Mandiri",
                deadline = "Jumat, 16:00",
                whyAdvisedDetails = "Siswa salah menjawab pertanyaan tanda koefisien a terhadap arah kurva terbuka ke atas/bawah."
            ),
            TeacherRecommendation(
                id = "REC-03",
                studentNumber = "Siswa 07",
                studentName = "Gita Pertiwi",
                type = "Pengayaan",
                actionTitle = "Tantangan Studi Kasus Peluang Majemuk",
                aiReason = "Skor 95 pada materi reguler, waktu pengerjaan cepat dan pemahaman konsep tuntas.",
                status = RecommendationStatus.MENUNGGU,
                formatMateri = "Soal Tantangan Olimpiade C5",
                deadline = "Sabtu, 18:00",
                whyAdvisedDetails = "Penguasaan konsep kombinatorika mencapai 95%. Direkomendasikan latihan tingkat lanjut agar motivasi belajar tetap terstimulasi secara optimal."
            )
        )
    )
    val recommendations: StateFlow<List<TeacherRecommendation>> = _recommendations.asStateFlow()

    // Parent Chat
    private val _chatMessages = MutableStateFlow(
        listOf(
            ChatMessage("MSG-01", "Bu Nurul, M.Pd.", "Guru Matematika", "Selamat siang Bapak/Ibu. Ananda Rafi perlu sedikit penguatan pada Fungsi Kuadrat, remedial sudah kami jadwalkan Kamis.", "10:15", isFromParent = false),
            ChatMessage("MSG-02", "Pak Hendra (Wali)", "Orang Tua", "Terima kasih infonya, Bu. Apa yang bisa kami bantu dampingi di rumah?", "10:20", isFromParent = true),
            ChatMessage("MSG-03", "Bu Nurul, M.Pd.", "Guru Matematika", "Mohon temani ananda berlatih 15 menit per hari dan ajak ia menjelaskan kembali langkah-langkahnya tanpa tekanan.", "10:25", isFromParent = false),
            ChatMessage("MSG-04", "Pak Hendra (Wali)", "Orang Tua", "Baik Bu, siap kami dampingi sore ini. Terima kasih banyak atas perhatiannya.", "10:28", isFromParent = true)
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    // Actions
    fun loginAs(role: UserRole) {
        val user = when (role) {
            UserRole.SISWA -> UserAccount(
                id = "USR-SISWA-01",
                name = "Rafi",
                email = "rafi@bclass.edu",
                identifier = "0052341678",
                role = UserRole.SISWA,
                className = "Kelas XI-A",
                subject = "Matematika"
            )
            UserRole.GURU -> UserAccount(
                id = "USR-GURU-01",
                name = "Bu Nurul, M.Pd.",
                email = "nurul@bclass.edu",
                identifier = "198504122010012015",
                role = UserRole.GURU,
                className = "Kelas XI-A",
                subject = "Matematika"
            )
            UserRole.ORANG_TUA -> UserAccount(
                id = "USR-PARENT-01",
                name = "Pak Hendra",
                email = "hendra@wali.edu",
                identifier = "081234567890",
                role = UserRole.ORANG_TUA,
                className = "Kelas XI-A",
                subject = "Matematika",
                linkedChildName = "Rafi"
            )
            UserRole.ADMIN -> UserAccount(
                id = "USR-ADMIN-01",
                name = "Admin Sekolah",
                email = "admin@bclass.edu",
                identifier = "ADM-2026-01",
                role = UserRole.ADMIN,
                className = "Semua Kelas",
                subject = "Sistem & Kurikulum"
            )
        }
        _currentUser.value = user
    }

    fun logout() {
        _currentUser.value = null
    }

    fun setMasteryThreshold(newThreshold: Int) {
        _masteryThreshold.value = newThreshold.coerceIn(50, 90)
    }

    fun toggleOfflineSimulation() {
        val next = !_isOffline.value
        _isOffline.value = next
        _syncState.value = if (next) SyncState.OFFLINE else SyncState.SYNCED
    }

    fun toggleDataSaver() {
        _isDataSaverEnabled.value = !_isDataSaverEnabled.value
    }

    fun toggleDarkMode() {
        _isDarkMode.value = !_isDarkMode.value
    }

    fun toggleAllLowercaseStudent() {
        _isAllLowercaseStudent.value = !_isAllLowercaseStudent.value
    }

    fun toggleLeaderboard() {
        _isLeaderboardEnabled.value = !_isLeaderboardEnabled.value
    }

    fun approveRecommendation(id: String) {
        _recommendations.update { list ->
            list.map { if (it.id == id) it.copy(status = RecommendationStatus.DISETUJUI) else it }
        }
    }

    fun rejectRecommendation(id: String) {
        _recommendations.update { list ->
            list.map { if (it.id == id) it.copy(status = RecommendationStatus.DITOLAK) else it }
        }
    }

    fun updateRecommendation(id: String, newTitle: String, newFormat: String, newDeadline: String) {
        _recommendations.update { list ->
            list.map {
                if (it.id == id) it.copy(
                    actionTitle = newTitle,
                    formatMateri = newFormat,
                    deadline = newDeadline,
                    status = RecommendationStatus.DIUBAH
                ) else it
            }
        }
    }

    fun claimMission(id: String) {
        _missions.update { list ->
            list.map {
                if (it.id == id && !it.isClaimed) {
                    _studentXp.update { xp -> xp + it.xpReward }
                    it.copy(isClaimed = true)
                } else it
            }
        }
    }

    fun addDiscussionPost(content: String, authorName: String, authorRole: String) {
        val newPost = DiscussionPost(
            id = "POST-${System.currentTimeMillis()}",
            authorName = authorName,
            authorRole = authorRole,
            isTeacherTrigger = false,
            timestamp = "Baru saja",
            content = content,
            agreesCount = 0,
            repliesCount = 0,
            isAgreedByMe = false
        )
        _discussionPosts.update { listOf(it.first()) + newPost + it.drop(1) }
    }

    fun togglePostAgree(postId: String) {
        _discussionPosts.update { list ->
            list.map { post ->
                if (post.id == postId) {
                    val nextAgreed = !post.isAgreedByMe
                    val nextCount = if (nextAgreed) post.agreesCount + 1 else (post.agreesCount - 1).coerceAtLeast(0)
                    post.copy(isAgreedByMe = nextAgreed, agreesCount = nextCount)
                } else post
            }
        }
    }

    fun sendParentChatMessage(text: String, isFromParent: Boolean) {
        val newMsg = ChatMessage(
            id = "MSG-${System.currentTimeMillis()}",
            senderName = if (isFromParent) "Pak Hendra (Wali)" else "Bu Nurul, M.Pd.",
            senderRole = if (isFromParent) "Orang Tua" else "Guru Matematika",
            text = text,
            timestamp = "Sekarang",
            isFromParent = isFromParent,
            isRead = true
        )
        _chatMessages.update { it + newMsg }
    }

    fun updateQuizScoreForUnit2(score: Int) {
        val isPassed = score >= _masteryThreshold.value
        _learningUnits.update { list ->
            list.map { unit ->
                if (unit.id == "UNIT-02") {
                    unit.copy(
                        score = score,
                        status = if (isPassed) UnitStatus.SELESAI else UnitStatus.REMEDIAL
                    )
                } else unit
            }
        }
        _studentCompetencies.update { list ->
            list.map { comp ->
                if (comp.name == "Fungsi Kuadrat") comp.copy(score = score) else comp
            }
        }
        if (isPassed) {
            _studentXp.update { it + 100 }
        }
    }

    fun completeDiagnosticTest(algebra: Int, quad: Int, stat: Int, geom: Int, prob: Int) {
        _hasCompletedDiagnostic.value = true
        _studentCompetencies.value = listOf(
            TopicCompetency("TOP-01", "Aljabar", algebra),
            TopicCompetency("TOP-02", "Fungsi Kuadrat", quad),
            TopicCompetency("TOP-03", "Statistika", stat),
            TopicCompetency("TOP-04", "Geometri", geom),
            TopicCompetency("TOP-05", "Peluang", prob)
        )
    }

    // Static question bank for Unit 2 (Fungsi Kuadrat) Formative Quiz
    fun getFormativeQuizQuestions(): List<QuizQuestion> {
        return listOf(
            QuizQuestion(
                id = "Q-01",
                topicId = "TOP-02",
                topicName = "Fungsi Kuadrat",
                cognitiveTag = "C2 · Memahami",
                questionText = "Titik puncak grafik f(x) = x² - 6x + 5 berada di titik ...",
                formula = "f(x) = x² - 6x + 5",
                options = listOf(
                    "(3, 4)",
                    "(3, -4)",
                    "(-3, -4)",
                    "(6, 5)"
                ),
                correctIndex = 1,
                explanation = "Titik puncak berada pada xp = -b / (2a) = -(-6) / (2·1) = 3.\nNilai yp = f(3) = (3)² - 6(3) + 5 = 9 - 18 + 5 = -4.\nJadi titik puncak adalah (3, -4)."
            ),
            QuizQuestion(
                id = "Q-02",
                topicId = "TOP-02",
                topicName = "Fungsi Kuadrat",
                cognitiveTag = "C4 · Menganalisis (HOTS)",
                questionText = "Jika kurva f(x) = -2x² + 4x + k memotong sumbu-x di dua titik berbeda, maka syarat nilai k adalah ...",
                formula = "D = b² - 4ac > 0",
                options = listOf(
                    "k > -2",
                    "k < -2",
                    "k > 2",
                    "k < 2"
                ),
                correctIndex = 0,
                explanation = "Memotong di dua titik berbeda berarti D > 0.\nD = (4)² - 4(-2)(k) = 16 + 8k > 0 ⟹ 8k > -16 ⟹ k > -2."
            ),
            QuizQuestion(
                id = "Q-03",
                topicId = "TOP-02",
                topicName = "Fungsi Kuadrat",
                cognitiveTag = "C3 · Menerapkan",
                questionText = "Sumbu simetri dari persamaan kuadrat y = 2x² - 8x + 3 adalah ...",
                formula = "x = -b / (2a)",
                options = listOf(
                    "x = -2",
                    "x = 2",
                    "x = 4",
                    "x = -4"
                ),
                correctIndex = 1,
                explanation = "Sumbu simetri diperoleh dari rumus x = -b / (2a) = -(-8) / (2 · 2) = 8 / 4 = 2."
            )
        )
    }

    // Diagnostic Quiz Questions
    fun getDiagnosticQuestions(): List<QuizQuestion> {
        return listOf(
            QuizQuestion(
                id = "DIAG-01",
                topicId = "TOP-01",
                topicName = "Aljabar",
                cognitiveTag = "C2 · Konsep Aljabar",
                questionText = "Hasil pemfaktoran dari x² - 9 adalah ...",
                formula = "x² - 9",
                options = listOf(
                    "(x - 3)(x - 3)",
                    "(x + 3)(x - 3)",
                    "(x + 9)(x - 1)",
                    "(x - 9)(x + 1)"
                ),
                correctIndex = 1,
                explanation = "Bentuk a² - b² difaktorkan menjadi (a - b)(a + b). Sehingga x² - 9 = (x - 3)(x + 3)."
            ),
            QuizQuestion(
                id = "DIAG-02",
                topicId = "TOP-02",
                topicName = "Fungsi Kuadrat",
                cognitiveTag = "C2 · Grafik Fungsi",
                questionText = "Jika kurva kuadrat terbuka ke bawah, maka nilai koefisien a harus ...",
                formula = "f(x) = ax² + bx + c",
                options = listOf(
                    "a > 0",
                    "a < 0",
                    "a = 0",
                    "a = 1"
                ),
                correctIndex = 1,
                explanation = "Koefisien a menentukan arah bukaan kurva. a < 0 kurva terbuka ke bawah (memiliki titik maksimum)."
            ),
            QuizQuestion(
                id = "DIAG-03",
                topicId = "TOP-03",
                topicName = "Statistika",
                cognitiveTag = "C2 · Nilai Rata-rata",
                questionText = "Rata-rata dari data 6, 7, 8, 9, 10 adalah ...",
                formula = "x̄ = Σx / n",
                options = listOf(
                    "7",
                    "7.5",
                    "8",
                    "8.5"
                ),
                correctIndex = 2,
                explanation = "Jumlah = 6 + 7 + 8 + 9 + 10 = 40. n = 5. Rata-rata = 40 / 5 = 8."
            ),
            QuizQuestion(
                id = "DIAG-04",
                topicId = "TOP-04",
                topicName = "Geometri",
                cognitiveTag = "C3 · Teorema Pythagoras",
                questionText = "Panjang diagonal ruang kubus dengan rusuk s cm adalah ...",
                formula = "d = s√3",
                options = listOf(
                    "s√2 cm",
                    "s√3 cm",
                    "2s cm",
                    "s√5 cm"
                ),
                correctIndex = 1,
                explanation = "Diagonal sisi adalah s√2, dan diagonal ruang kubus adalah s√3."
            ),
            QuizQuestion(
                id = "DIAG-05",
                topicId = "TOP-05",
                topicName = "Peluang",
                cognitiveTag = "C2 · Peluang Dasar",
                questionText = "Peluang munculnya mata dadu prima pada pelemparan sebuah dadu adalah ...",
                formula = "P(A) = n(A) / n(S)",
                options = listOf(
                    "1/6",
                    "1/3",
                    "1/2",
                    "2/3"
                ),
                correctIndex = 2,
                explanation = "Mata dadu prima adalah {2, 3, 5} ada 3 kemungkinan dari total 6. Peluang = 3/6 = 1/2."
            )
        )
    }
}
