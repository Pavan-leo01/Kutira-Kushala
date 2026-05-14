package com.mindmat.myappl.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val AppColorScheme = lightColorScheme(

    primary = PurplePrimary,
    secondary = PurpleDark,

    background = LightBackground,
    surface = White,

    onPrimary = White,
    onBackground = TextDark,
    onSurface = TextDark
)

@Composable
fun MyAppTheme(
    content: @Composable () -> Unit
) {

    MaterialTheme(
        colorScheme = AppColorScheme,
        typography = Typography,
        content = content
    )
}