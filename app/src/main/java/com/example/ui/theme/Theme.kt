package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = ShatterpointAmber,
    onPrimary = Color.Black,
    secondary = AllianceCyan,
    onSecondary = Color.Black,
    primaryContainer = DeepCharcoal,
    onPrimaryContainer = Color.White,
    secondaryContainer = LightCharcoal,
    onSecondaryContainer = AllianceCyan,
    background = SpaceBlack,
    onBackground = Color.White,
    surface = DeepCharcoal,
    onSurface = Color.White,
    surfaceVariant = LightCharcoal,
    onSurfaceVariant = Color.White,
    error = ImperialRed,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = ShatterpointAmber,
    onPrimary = Color.Black,
    secondary = RepublicBlue,
    onSecondary = Color.White,
    primaryContainer = Color(0xFFF0F4F8),
    onPrimaryContainer = Color.Black,
    secondaryContainer = Color(0xFFE3F2FD),
    onSecondaryContainer = RepublicBlue,
    background = Color(0xFFFAFBFC),
    onBackground = Color(0xFF121212),
    surface = Color.White,
    onSurface = Color(0xFF121212),
    surfaceVariant = Color(0xFFECEFF1),
    onSurfaceVariant = Color.Black,
    error = ImperialRed,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Keep standard static design palette as it is highly customized for Star Wars
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
