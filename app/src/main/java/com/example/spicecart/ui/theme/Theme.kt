package com.example.spicecart.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = DarkSpiceRed,
    secondary = DarkSaffron,
    tertiary = DarkGolden,
    background = DarkBackground,
    surface = DarkBackground,
    onPrimary = TextSecondary,
    onSecondary = TextSecondary,
    onTertiary = TextSecondary,
    onBackground = TextSecondary,
    onSurface = TextSecondary
)

private val LightColorScheme = lightColorScheme(
    primary = SpiceRed,
    secondary = SaffronOrange,
    tertiary = GoldenYellow,
    background = CardBackground,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = TextPrimary,
    onSurface = TextPrimary
)

@Composable
fun SpiceCartTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
