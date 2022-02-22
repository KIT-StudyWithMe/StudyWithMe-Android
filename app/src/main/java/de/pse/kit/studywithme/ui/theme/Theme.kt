package de.pse.kit.myapplication.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme as MaterialTheme3
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable

/**
 * Themes used in the view of the application
 */
private val LightColorPalette3 = ColorScheme(
    primary = Grey200,
    onPrimary = Color.Black,
    primaryContainer = Green200,
    onPrimaryContainer = Color.Black,
    inversePrimary = Color.Black,
    secondary = Green500,
    onSecondary = Color.White,
    secondaryContainer = Green100,
    onSecondaryContainer = Green700,
    tertiary = Green700,
    onTertiary = Color.White,
    tertiaryContainer = Red500,
    onTertiaryContainer = Color.White,
    background = White200,
    onBackground = Color.Black,
    surface = White200,
    onSurface = Color.Black,
    surfaceVariant = White200,
    onSurfaceVariant = Color.Black,
    inverseSurface = White200,
    inverseOnSurface = Color.Black,
    error = Red500,
    onError = Color.White,
    errorContainer = Red500,
    onErrorContainer = Color.White,
    outline = Green700
)

private val LightColorPalette = Colors(
    primary = Grey200,
    primaryVariant = Green200,
    onPrimary = Color.Black,
    secondary = Green100,
    secondaryVariant = Green700,
    onSecondary = Color.Black,
    background = White200,
    onBackground = Color.Black,
    surface = White200,
    onSurface = Color.Black,
    error = Color.Red,
    onError = Color.Black,
    isLight = true
)

private val DarkColorPalette3 = ColorScheme(
    primary = Green700,
    onPrimary = Color.White,
    primaryContainer = Green200,
    onPrimaryContainer = Color.Black,
    inversePrimary = Color.Black,
    secondary = Green500,
    onSecondary = Color.Black,
    secondaryContainer = Green100,
    onSecondaryContainer = Green700,
    tertiary = Green500,
    onTertiary = Color.White,
    tertiaryContainer = Red500,
    onTertiaryContainer = Color.White,
    background = Black100,
    onBackground = Color.White,
    surface = Black100,
    onSurface = Color.White,
    surfaceVariant = Black100,
    onSurfaceVariant = Color.White,
    inverseSurface = Black100,
    inverseOnSurface = Color.White,
    error = Red500,
    onError = Color.White,
    errorContainer = Red500,
    onErrorContainer = Color.White,
    outline = Green700
)

private val DarkColorPalette = Colors(
    primary = Green700,
    primaryVariant = Green200,
    onPrimary = Color.White,
    secondary = Green100,
    secondaryVariant = Green700,
    onSecondary = Color.White,
    background = Black100,
    onBackground = Color.White,
    surface = Black100,
    onSurface = Color.White,
    error = Color.Red,
    onError = Color.White,
    isLight = false
)

@Composable
fun MyApplicationTheme3(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = if (darkTheme) DarkColorPalette3 else LightColorPalette3

    MaterialTheme3(
        colorScheme = colors,
        typography = Typography3,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = if (darkTheme) DarkColorPalette else LightColorPalette

    MaterialTheme(
        colors = colors,
        typography = Typography,
        content = content
    )
}