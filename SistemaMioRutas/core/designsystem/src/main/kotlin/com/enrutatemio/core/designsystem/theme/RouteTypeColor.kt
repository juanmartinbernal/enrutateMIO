package com.enrutatemio.core.designsystem.theme

import androidx.compose.ui.graphics.Color
import com.enrutatemio.core.model.RouteType

/** Color asociado a cada tipo de ruta, migrado de la lógica legacy en `ListaRutas`/`RutasAdapter`. */
fun RouteType.toColor(): Color = when (this) {
    RouteType.EXPRESO -> EnrutateColors.RouteExpreso
    RouteType.PRETRONCAL -> EnrutateColors.RoutePretroncal
    RouteType.TRONCAL -> EnrutateColors.RouteTroncal
    RouteType.ALIMENTADOR -> EnrutateColors.RouteAlimentador
}
