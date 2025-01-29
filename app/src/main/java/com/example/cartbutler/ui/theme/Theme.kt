package com.example.cartbutler.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun CartbutlerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // Determines whether the theme will be dark
    dynamicColor: Boolean = true, // Enables dynamic colors (Android S and above)
    content: @Composable () -> Unit
) {
    // Selects the color scheme based on conditions
    val colorScheme = when {
        // Uses dynamic colors if supported and enabled
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        // Color scheme for dark theme
        darkTheme -> darkColorScheme(
            primary = PrimaryDark,
            secondary = SecondaryDark,
            background = BackgroundDark,
            surface = SurfaceDark,
            error = ErrorDark,
            onPrimary = OnPrimaryDark,
            onSecondary = OnSecondaryDark,
            onBackground = OnBackgroundDark,
            onSurface = OnSurfaceDark,
            onError = OnErrorDark
        )
        // Color scheme for light theme
        else -> lightColorScheme(
            primary = PrimaryLight,
            secondary = SecondaryLight,
            background = BackgroundLight,
            surface = SurfaceLight,
            error = ErrorLight,
            onPrimary = OnPrimaryLight,
            onSecondary = OnSecondaryLight,
            onBackground = OnBackgroundLight,
            onSurface = OnSurfaceLight,
            onError = OnErrorLight
        )
    }

    // Applies MaterialTheme with the selected color scheme and typography
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Ensure typography is configured
        content = content
    )
}