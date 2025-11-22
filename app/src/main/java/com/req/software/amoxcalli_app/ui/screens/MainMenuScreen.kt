package com.req.software.amoxcalli_app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.req.software.amoxcalli_app.domain.model.LearningUnit
import com.req.software.amoxcalli_app.ui.components.BottomNavBar
import com.req.software.amoxcalli_app.ui.components.LessonNode
import com.req.software.amoxcalli_app.ui.components.TopStatusBar
import com.req.software.amoxcalli_app.viewmodel.MainMenuUiState
import com.req.software.amoxcalli_app.viewmodel.MainMenuViewModel
import com.req.software.amoxcalli_app.viewmodel.NavigationEvent

/**
 * Main menu screen - Duolingo-like learning path interface
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMenuScreen(
    userId: String,
    currentRoute: String,
    onNavigateToLesson: (String) -> Unit,
    onNavigateToRoute: (String) -> Unit,
    viewModel: MainMenuViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val navigationEvent by viewModel.navigationEvent.collectAsState()
    var isRefreshing by remember { mutableStateOf(false) }

    // Load data on first composition
    LaunchedEffect(userId) {
        viewModel.loadMainMenu(userId)
    }

    // Handle navigation events
    LaunchedEffect(navigationEvent) {
        when (val event = navigationEvent) {
            is NavigationEvent.NavigateToLesson -> {
                onNavigateToLesson(event.lessonId)
                viewModel.onNavigationHandled()
            }
            is NavigationEvent.NavigateToShop -> {
                onNavigateToRoute("shop")
                viewModel.onNavigationHandled()
            }
            is NavigationEvent.NavigateToProfile -> {
                onNavigateToRoute("profile")
                viewModel.onNavigationHandled()
            }
            null -> {}
        }
    }

    // Handle refresh state
    LaunchedEffect(uiState) {
        if (uiState !is MainMenuUiState.Loading) {
            isRefreshing = false
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentRoute = currentRoute,
                onNavigate = onNavigateToRoute
            )
        },
        floatingActionButton = {
            if (uiState is MainMenuUiState.Success &&
                (uiState as MainMenuUiState.Success).nextRecommendedLessonId != null) {
                FloatingActionButton(
                    onClick = { viewModel.onNextLessonClicked() },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Siguiente lección"
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = uiState) {
                is MainMenuUiState.Loading -> {
                    LoadingContent()
                }

                is MainMenuUiState.Success -> {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Top Status Bar
                        TopStatusBar(
                            userProfile = state.userProfile,
                            onShopClicked = { viewModel.onShopClicked() }
                        )

                        // Learning Path Content
                        LearningPathContent(
                            learningPath = state.learningPath,
                            onLessonClicked = viewModel::onLessonClicked,
                            onRefresh = {
                                isRefreshing = true
                                viewModel.onRefreshRequested()
                            },
                            isRefreshing = isRefreshing,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                is MainMenuUiState.Error -> {
                    if (state.cachedData != null) {
                        // Show cached data with error snackbar
                        Column(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            TopStatusBar(
                                userProfile = state.cachedData.userProfile,
                                onShopClicked = { viewModel.onShopClicked() }
                            )

                            LearningPathContent(
                                learningPath = state.cachedData.learningPath,
                                onLessonClicked = viewModel::onLessonClicked,
                                onRefresh = {
                                    isRefreshing = true
                                    viewModel.onRefreshRequested()
                                },
                                isRefreshing = isRefreshing,
                                modifier = Modifier.weight(1f)
                            )

                            Snackbar(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(state.message)
                            }
                        }
                    } else {
                        ErrorContent(
                            message = state.message,
                            onRetry = { viewModel.onRefreshRequested() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator()
            Text(
                text = "Cargando tu camino de aprendizaje...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Text(
                text = "Error al cargar los datos",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Button(onClick = onRetry) {
                Text("Reintentar")
            }
        }
    }
}

@Composable
private fun LearningPathContent(
    learningPath: com.req.software.amoxcalli_app.domain.model.LearningPath,
    onLessonClicked: (String, String) -> Unit,
    onRefresh: () -> Unit,
    isRefreshing: Boolean,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        // Course Title
        item {
            Text(
                text = learningPath.courseName,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        // Progress Indicator
        item {
            LinearProgressIndicator(
                progress = { learningPath.overallProgress / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            )
            Text(
                text = "Progreso general: ${learningPath.overallProgress}%",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Units with Lessons
        items(learningPath.units) { unit ->
            UnitSection(
                unit = unit,
                onLessonClicked = onLessonClicked
            )
        }
    }
}

@Composable
private fun UnitSection(
    unit: LearningUnit,
    onLessonClicked: (String, String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // Unit Header
        Surface(
            color = if (unit.isLocked)
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            else
                MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = unit.displayName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (unit.isLocked)
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    else
                        MaterialTheme.colorScheme.onPrimaryContainer
                )
                unit.description?.let { description ->
                    Text(
                        text = description,
                        fontSize = 14.sp,
                        color = if (unit.isLocked)
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                        else
                            MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        // Lessons Grid
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Group lessons in rows of 3
            unit.lessons.chunked(3).forEach { rowLessons ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    rowLessons.forEach { lesson ->
                        LessonNode(
                            lesson = lesson,
                            onLessonClicked = onLessonClicked
                        )
                    }
                    // Add empty spaces if row is not full
                    repeat(3 - rowLessons.size) {
                        Spacer(modifier = Modifier.width(88.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
