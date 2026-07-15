package com.enrutatemio.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = EnrutateColors.Primary,
    secondary = EnrutateColors.Secondary,
    background = EnrutateColors.Background,
    surface = EnrutateColors.Surface,
)

private val DarkColors = darkColorScheme(
    primary = EnrutateColors.RoutePretroncal,
    secondary = EnrutateColors.RouteTroncal,
)

@Composable
fun EnrutateMioTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colorScheme,
        typography = EnrutateTypography,
        content = content,
    )
}
