package com.enrutatemio.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.enrutatemio.core.designsystem.theme.toColor
import com.enrutatemio.core.model.RouteType

/**
 * Insignia de código de ruta con color según su tipo (E/P/T/A), migrada de la lógica
 * hardcodeada en `ListaRutas.getView()`/`RutasAdapter.getView()` legacy.
 */
@Composable
fun RouteBadge(code: String, type: RouteType, modifier: Modifier = Modifier) {
    Text(
        text = code.uppercase(),
        color = contentColorFor(type.toColor()),
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        modifier = modifier
            .background(color = type.toColor(), shape = RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
    )
}

private fun contentColorFor(background: Color): Color =
    if (background.luminance() > 0.5f) Color.Black else Color.White

private fun Color.luminance(): Float = (0.299f * red + 0.587f * green + 0.114f * blue)
