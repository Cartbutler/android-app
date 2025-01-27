package com.example.cartbutler.ui.theme

import android.app.Activity
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
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
