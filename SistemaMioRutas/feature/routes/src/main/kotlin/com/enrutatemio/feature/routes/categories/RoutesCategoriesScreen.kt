package com.enrutatemio.feature.routes.categories

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.enrutatemio.core.designsystem.R as DesignR
import com.enrutatemio.core.model.RouteType

@Composable
fun RoutesCategoriesRoute(
    onSelectType: (RouteType) -> Unit,
    onSelectFeeders: () -> Unit,
) {
    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = padding.calculateTopPadding()),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                Text(
                    text = "Seleccione el tipo de ruta",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp),
                )
            }
            item {
                RouteCategoryCard(
                    title = "Troncales",
                    description = "Rutas principales del sistema MIO",
                    iconRes = DesignR.drawable.boton_troncal,
                    onClick = { onSelectType(RouteType.TRONCAL) },
                )
            }
            item {
                RouteCategoryCard(
                    title = "Pretroncales",
                    description = "Rutas que conectan diferentes zonas de la ciudad",
                    iconRes = DesignR.drawable.boton_pretroncal,
                    onClick = { onSelectType(RouteType.PRETRONCAL) },
                )
            }
            item {
                RouteCategoryCard(
                    title = "Expresos",
                    description = "Rutas rápidas con paradas específicas",
                    iconRes = DesignR.drawable.boton_expreso,
                    onClick = { onSelectType(RouteType.EXPRESO) },
                )
            }
            item {
                RouteCategoryCard(
                    title = "Alimentadores",
                    description = "Rutas zonales de alimentación",
                    iconRes = DesignR.drawable.boton_alimentador,
                    onClick = onSelectFeeders,
                )
            }
        }
    }
}

@Composable
private fun RouteCategoryCard(
    title: String,
    description: String,
    iconRes: Int,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = title,
                modifier = Modifier.size(64.dp),
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
