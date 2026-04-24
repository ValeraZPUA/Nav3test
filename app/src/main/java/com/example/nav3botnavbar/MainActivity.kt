package com.example.nav3botnavbar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MaterialTheme {
                Nav3BottomBarApp()
            }
        }
    }
}

private data object HomeScreen
private data object SearchScreen
private data object ProfileScreen

private data class BottomTab(
    val title: String,
    val route: Any,
    val icon: @Composable () -> Unit
)

@Composable
private fun Nav3BottomBarApp() {
    val tabs = remember {
        listOf(
            BottomTab("Home", HomeScreen) { Icon(Icons.Default.Home, contentDescription = null) },
            BottomTab("Search", SearchScreen) { Icon(Icons.Default.Settings, contentDescription = null) },
            BottomTab("Profile", ProfileScreen) { Icon(Icons.Default.Person, contentDescription = null) }
        )
    }

    val backStack = remember { mutableStateListOf<Any>(HomeScreen) }
    val currentRoute = backStack.lastOrNull()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                tabs.forEach { tab ->
                    NavigationBarItem(
                        selected = currentRoute == tab.route,
                        onClick = {
                            backStack.clear()
                            backStack.add(tab.route)
                        },
                        icon = tab.icon,
                        label = { Text(tab.title) }
                    )
                }
            }
        }
    ) { _ ->
        NavDisplay(
            backStack = backStack,
            onBack = {
                if (backStack.lastOrNull() != HomeScreen) {
                    backStack.clear()
                    backStack.add(HomeScreen)
                }
            },
            entryProvider = { key ->
                when (key) {
                    HomeScreen -> NavEntry(key) { ScreenContent("Home", "Перший екран") }
                    SearchScreen -> NavEntry(key) { ScreenContent("Search", "Другий екран") }
                    ProfileScreen -> NavEntry(key) { ScreenContent("Profile", "Третій екран") }
                    else -> NavEntry(Unit) { ScreenContent("Unknown", "Невідомий екран") }
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun ScreenContent(title: String, subtitle: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text(text = title, style = MaterialTheme.typography.headlineMedium)
            Text(text = subtitle, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
