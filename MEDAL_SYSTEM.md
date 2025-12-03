# Medal System Documentation

## Overview

The Amoxcalli app now includes a complete medal/achievement system that rewards users for reaching milestones in their LSM learning journey. The system automatically detects achievements, claims medals via the backend API, and celebrates with beautiful animations.

## Features

✅ **Automatic Achievement Detection** - Monitors user stats and automatically claims medals when criteria are met
✅ **Beautiful Celebration Animation** - Full-screen confetti animation when medals are earned
✅ **Progress Tracking** - Shows progress towards locked medals
✅ **Backend Integration** - Medals are saved to the database via API
✅ **Global Monitoring** - Works throughout the entire app automatically

---

## Available Medals

Based on `medals.csv`, the following medals can be earned:

| Medal | ID | Requirement | Icon |
|-------|----|-----------|----|
| **Tlaolli** | `ed9dbe08-a37b-4ecb-a550-0616bf665df3` | 3 días de racha | M_Tlaolli.png |
| **Jade** | `26d2e63e-8066-43fd-ae0e-292b58ec2c64` | 7 días de racha | M_Jade.png |
| **Obsidiana** | `f79bd6c8-ae5b-41bb-8d65-3ded14e5c846` | Practica 5 señas | M_Obsidiana.png |
| **Turquesa** | `ad914cf2-cc0f-42b0-8c45-8e3d2d514b28` | Obtén 100 de XP | M_Turquesa.png |
| **Quetzal** | `c17ce2c2-c820-44b4-818f-d602bd95efee` | Completa 3 ejercicios | M_Quetzal.png |
| **Códice dorado** | `9725bcbb-ffdf-45b5-adf4-78c88af2a75a` | Completa 5 ejercicios | M_Oro.png |

All medal icons are hosted on Cloudflare R2: `https://pub-05700fc259bc4e839552241871f5e896.r2.dev/`

---

## Architecture

### Files Created

#### 1. **MedalViewModel.kt**
Location: `app/src/main/java/.../viewmodel/MedalViewModel.kt`

**Purpose:** Manages medal state, achievement checking, and API communication

**Key Methods:**
- `loadAvailableMedals()` - Fetches all available medals from API
- `loadUserMedals(authToken)` - Fetches user's earned medals
- `checkAndClaimMedals(userStats, authToken)` - Checks criteria and claims eligible medals
- `getMedalProgress(medalId, userStats)` - Returns current/required progress for a medal
- `dismissMedalCelebration()` - Clears the celebration dialog

**StateFlows:**
- `availableMedals` - All medals that can be earned
- `userMedals` - Medals the user has earned
- `newlyEarnedMedal` - Triggers celebration dialog
- `isLoading` / `error` - UI state

#### 2. **MedalCelebration.kt**
Location: `app/src/main/java/.../ui/components/medals/MedalCelebration.kt`

**Purpose:** Beautiful full-screen celebration animation

**Components:**
- `MedalCelebrationDialog` - Main dialog with dark overlay
- `ConfettiAnimation` - Animated confetti particles (20 particles)
- `MedalCard` - Card showing medal with scale/rotation animations

**Animations:**
- Confetti particles radiating outward with fade
- Medal icon scales in with bounce effect
- Subtle rotation for medal glow effect

#### 3. **MedalChecker.kt**
Location: `app/src/main/java/.../ui/components/medals/MedalChecker.kt`

**Purpose:** Global composable that monitors achievements

**Integration:** Added to `NavGraph.kt` - runs throughout the entire app

**Behavior:**
- Observes `UserStatsResponse` changes
- Automatically calls `checkAndClaimMedals()` when stats update
- Shows celebration dialog when medals are earned

#### 4. **MedalsList.kt**
Location: `app/src/main/java/.../ui/components/medals/MedalsList.kt`

**Purpose:** Display grids and lists of medals

**Components:**
- `MedalsGrid` - Full grid view of all medals (3 columns)
- `MedalCard` - Individual medal with earned/locked state
- `CompactMedalsDisplay` - Horizontal card for home screen

**Features:**
- Earned medals shown in full color with glow
- Locked medals are grayed out with lock icon 🔒
- Progress indicator (e.g., "3/5") for locked medals
- Checkmark ✓ for earned medals

---

## API Integration

### Endpoints Added to AuthService

#### 1. Get All Medals (Public)
```kotlin
@GET("auth/medals")
suspend fun getAllMedals(): ApiResponse<List<MedalInfo>>
```
Returns all available medals that can be earned.

#### 2. Get User Medals
```kotlin
@GET("auth/me/medals")
suspend fun getUserMedals(
    @Header("Authorization") authToken: String
): ApiResponse<List<Medal>>
```
Returns medals earned by the authenticated user.

#### 3. Claim Medal
```kotlin
@POST("auth/me/medals/{medalId}/claim")
suspend fun claimMedal(
    @Header("Authorization") authToken: String,
    @Path("medalId") medalId: String
): ApiResponse<MedalClaimResponse>
```
Attempts to claim a medal. Backend validates conditions.

**Success Response:**
```json
{
  "success": true,
  "data": {
    "user_medal_id": "uuid",
    "medal_id": "uuid",
    "name": "Tlaolli",
    "description": "3 días de racha",
    "icon_url": "https://...",
    "achieved_at": "2025-12-01T12:00:00.000Z"
  }
}
```

**Failure Response (conditions not met):**
```json
{
  "success": false,
  "message": "Medal conditions not met",
  "required": 7,
  "current": 3
}
```

### DTOs Added to ApiModels.kt

**MedalInfo** - Available medal data
```kotlin
data class MedalInfo(
    val id: String,
    val name: String,
    val description: String,
    val iconUrl: String?
)
```

**MedalClaimResponse** - Response from claiming
```kotlin
data class MedalClaimResponse(
    val userMedalId: String,
    val medalId: String,
    val name: String,
    val description: String,
    val iconUrl: String?,
    val achievedAt: String
)
```

---

## How It Works

### 1. **User Performs Action**
- User completes an exercise
- User views signs
- User maintains streak
- Backend updates `UserStatsResponse`

### 2. **MedalChecker Monitors**
```kotlin
LaunchedEffect(userStats, authToken) {
    if (userStats != null && authToken != null) {
        medalViewModel.checkAndClaimMedals(userStats, authToken)
    }
}
```

### 3. **Achievement Logic**
```kotlin
// Example: Check for Turquesa medal (100 XP)
val xp = userStats.stats?.find { it.name == "experience_points" }?.currentValue ?: 0
if (xp >= 100 && !hasMedal(MEDAL_TURQUESA)) {
    claimMedal(MEDAL_TURQUESA, authToken)
}
```

### 4. **Backend Validation**
- API receives claim request
- Backend validates user actually meets requirements
- If valid, creates `user_medal` record in database
- Returns medal data

### 5. **Celebration**
```kotlin
if (response.success && response.data != null) {
    _newlyEarnedMedal.value = response.data  // Triggers dialog
}
```

### 6. **User Dismisses**
- User clicks "¡Genial!" button
- `dismissMedalCelebration()` clears state
- Medal appears in user's medal collection

---

## Usage Examples

### Display Medals Grid in Profile
```kotlin
@Composable
fun ProfileScreen(
    userStats: UserStatsResponse?,
    medalViewModel: MedalViewModel = viewModel()
) {
    Column {
        // ... other profile content

        MedalsGrid(
            userStats = userStats,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
```

### Show Medal Count on Home
```kotlin
@Composable
fun HomeScreen(
    medalCount: Int,
    onMedalsClick: () -> Unit
) {
    CompactMedalsDisplay(
        medalCount = medalCount,
        onClick = onMedalsClick
    )
}
```

### Manual Medal Check
```kotlin
// If you need to manually trigger medal checking
val medalViewModel: MedalViewModel = viewModel()

Button(onClick = {
    medalViewModel.checkAndClaimMedals(userStats, authToken)
}) {
    Text("Check for New Medals")
}
```

---

## Achievement Criteria Details

### Streak-Based Medals
- **Tlaolli** (3 days) - `localStreak >= 3`
- **Jade** (7 days) - `localStreak >= 7`

**⚠️ IMPORTANT: Streaks are tracked LOCALLY now!**
Streaks (racha) are managed using `StreakManager` with SharedPreferences, independent of the backend. This avoids NullPointerException issues and provides offline streak tracking.

**How Local Streaks Work:**
- Streak checked/updated when `MedalViewModel` initializes
- Tracks: current streak, best streak, last check date
- Automatic increment for consecutive days
- Reset if more than 1 day gap
- Persists across app restarts

### Activity-Based Medals
- **Obsidiana** (5 signs) - `userStats.signViews.size >= 5`
- **Quetzal** (3 exercises) - `userStats.exerciseHistory.size >= 3`
- **Códice dorado** (5 exercises) - `userStats.exerciseHistory.size >= 5`

### XP-Based Medals
- **Turquesa** (100 XP) - `userStats.stats.find { it.name == "experience_points" }?.currentValue >= 100`

---

## Local Streak Tracking System 🔥

### Overview

Streaks (racha) are now tracked **locally using SharedPreferences** instead of relying on backend data. This prevents NullPointerException errors and works offline.

### StreakManager (`data/local/StreakManager.kt`)

**Purpose:** Manages daily login streaks locally

**Storage:** SharedPreferences (`streak_prefs`)

**Data Stored:**
- `current_streak` - Current consecutive days
- `best_streak` - Longest streak ever achieved
- `last_check_date` - Last date user opened app (yyyy-MM-dd format)

### How It Works

#### 1. **App Opens**
```kotlin
MedalViewModel.init() → updateLocalStreak() → StreakManager.checkAndUpdateStreak()
```

#### 2. **Streak Logic**
- **First time:** Streak = 1
- **Consecutive day (yesterday):** Streak++
- **Streak broken (>1 day gap):** Streak = 1
- **Already checked today:** Return current streak

#### 3. **Best Streak Tracking**
- Automatically updates if current > best
- Persists even after streak breaks

### API

**Check and Update:**
```kotlin
val newStreak = streakManager.checkAndUpdateStreak()
```

**Get Current Data:**
```kotlin
val current = streakManager.getCurrentStreak()
val best = streakManager.getBestStreak()
val lastCheck = streakManager.getLastCheckDate()
```

**Reset (Testing/User Request):**
```kotlin
streakManager.resetStreak()
```

**Set for Testing (SUPERADMIN):**
```kotlin
streakManager.setStreakForTesting(7) // Set to 7 days
```

### Integration with Medal System

**MedalViewModel:**
- Maintains `currentStreak` and `bestStreak` StateFlows
- Updates on initialization
- Uses local streak for Tlaolli and Jade medals
- Exposes testing functions for SUPERADMIN

**Medal Checking:**
```kotlin
val streak = _currentStreak.value // LOCAL, not backend!
if (streak >= 3 && !hasMedal(MEDAL_TLAOLLI)) {
    claimMedal(MEDAL_TLAOLLI, authToken)
}
```

### SUPERADMIN Streak Controls

In Admin Panel, SUPERADMINS can:

**View Current Streak:**
- Display shows: "Racha Actual: X días (Mejor: Y)"

**Quick Set Buttons:**
- "Set 3 días" - Test Tlaolli medal
- "Set 7 días" - Test Jade medal

**Reset Button:**
- "Reset Racha" - Clear all streak data

### Example Flow

```
Day 1: User opens app → Streak = 1
Day 2: User opens app → Streak = 2 (consecutive)
Day 3: User opens app → Streak = 3 → 🏅 Tlaolli medal!
Day 5: User opens app (skipped day 4) → Streak = 1 (reset)
Day 6: User opens app → Streak = 2
Day 7: User opens app → Streak = 3
...
Day 12: User opens app → Streak = 7 → 🏅 Jade medal!
```

### Debugging

**Logcat Filter:** `adb logcat | grep StreakManager`

**Debug Logs:**
```
D/StreakManager: Today: 2025-12-01, Last check: 2025-11-30, Current streak: 2
D/StreakManager: Consecutive day! Incrementing streak
D/StreakManager: Streak updated to: 3
D/MedalViewModel: Local streak updated: 3 (best: 3)
```

### Benefits

✅ **No NullPointerException** - Always has a value
✅ **Offline Support** - Works without backend
✅ **Fast** - Instant access from SharedPreferences
✅ **Reliable** - Persists across app restarts
✅ **Testable** - SUPERADMIN controls for easy testing

---

## Testing

### SUPERADMIN Test Button 🛠️

**For SUPERADMIN users only**, a special test button is available in the Admin Panel:

1. **Access Admin Panel:**
   - Navigate to Profile → Admin (only visible to admin/superadmin roles)

2. **SUPERADMIN TOOLS Section:**
   - Button: "🏅 Test All Medal Animations"
   - Only visible if `userRole == UserRole.SUPERADMIN`

3. **How It Works:**
   - Click the button to start the test
   - Shows all 6 medal celebration animations sequentially
   - Dismiss one medal to automatically see the next
   - Full confetti and animation for each medal

**Test Flow:**
```
Tlaolli → Jade → Obsidiana → Turquesa → Quetzal → Códice dorado
```

**Implementation Details:**
- Located in: `AdminScreen.kt` (lines 148-197)
- Function: `MedalViewModel.testAllMedalAnimations()`
- Uses queue system to show medals one by one
- Each medal displays with full celebration animation
- User must dismiss each to proceed to next

---

### Test Medal Claiming Manually

1. **Via API (Postman/cURL):**
```bash
curl -X POST "https://ketzallidbapi-production.up.railway.app/api/auth/me/medals/ed9dbe08-a37b-4ecb-a550-0616bf665df3/claim" \
  -H "Authorization: Bearer YOUR_FIREBASE_TOKEN"
```

2. **Via App (Natural Progression):**
- Complete activities to meet medal requirements
- Medal celebration should appear automatically
- Check `adb logcat | grep Medal` for debug logs

### Debug Logs
```kotlin
Log.d("MedalViewModel", "Starting medal animation test - 6 medals")
Log.d("MedalViewModel", "Showing first test medal: Tlaolli")
Log.d("MedalViewModel", "Showing next test medal: Jade")
Log.d("MedalViewModel", "Attempting to claim medal: $medalId")
Log.d("MedalViewModel", "Successfully claimed medal: ${response.data.name}")
Log.d("MedalViewModel", "User has ${response.data.size} medals")
```

---

## Customization

### Add New Medal

1. **Update `medals.csv`:**
```csv
new-uuid,New Medal,Description,https://.../icon.png
```

2. **Add constant in MedalViewModel:**
```kotlin
companion object {
    const val MEDAL_NEW = "new-uuid"
}
```

3. **Add achievement logic:**
```kotlin
if (newCondition && !currentMedalIds.contains(MEDAL_NEW)) {
    medalsToCheck.add(MEDAL_NEW)
}
```

4. **Add progress tracking:**
```kotlin
fun getMedalProgress(medalId: String, userStats: UserStatsResponse): Pair<Int, Int> {
    // ...
    return when (medalId) {
        MEDAL_NEW -> Pair(currentValue, requiredValue)
        // ...
    }
}
```

### Customize Animation Colors
Edit `MedalCelebration.kt`:
```kotlin
color = when (index % 4) {
    0 -> Special3Color  // Change colors here
    1 -> MainColor
    2 -> ThirdColor
    else -> Color(0xFFFFA726)
}
```

---

## Troubleshooting

### Medal Not Appearing
- Check `adb logcat | grep Medal` for errors
- Verify user stats are being loaded: `userStats != null`
- Verify auth token is present: `authToken != null`
- Check backend response: Medal conditions met?

### Celebration Not Showing
- Ensure `MedalChecker` is added to `NavGraph.kt`
- Verify `newlyEarnedMedal` state is being set
- Check for navigation conflicts (dialogs on dialogs)

### Progress Not Updating
- Verify `UserStatsResponse` is being refreshed
- Check `getMedalProgress()` logic matches achievement criteria
- Ensure stats names match backend: `"experience_points"`, etc.

---

## Performance Notes

- **Automatic Checking:** Medals are checked only when `UserStatsResponse` changes
- **Caching:** Available medals loaded once on ViewModel init
- **Network:** Claims only sent for eligible medals, not all medals
- **UI:** Celebration animation uses `infiniteRepeatable` - dismissed when user clicks button

---

## Next Steps

### Recommended Enhancements

1. **Medal Details Screen** - Show full medal description, requirements, and rarity
2. **Medal Categories** - Group by type (streak, activity, XP)
3. **Medal Showcase** - Allow users to "feature" favorite medals on profile
4. **Push Notifications** - Notify users when they're close to earning a medal
5. **Medal Tiers** - Bronze/Silver/Gold variants of medals
6. **Leaderboard Integration** - Compare medal counts with friends

### Backend Considerations

- Ensure medal conditions are validated on backend
- Prevent duplicate claims (check `user_medals` table)
- Add medal unlock notifications
- Track medal claim timestamps for analytics

---

## Summary

The medal system is now fully integrated and working! 🎉

**What's Implemented:**
- ✅ API endpoints for medals
- ✅ Automatic achievement detection
- ✅ Backend database integration
- ✅ Beautiful celebration animations
- ✅ Medal display components
- ✅ Progress tracking
- ✅ Global monitoring

**User Experience:**
- User completes activities naturally
- App automatically detects achievements
- Beautiful animation celebrates milestones
- Medals saved to database
- Progress visible in profile

The system requires zero user action - medals are awarded automatically as users learn LSM! 🏅
