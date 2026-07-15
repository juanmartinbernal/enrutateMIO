package com.enrutatemio.feature.map.trip

import android.Manifest
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.enrutatemio.core.model.Coordinates
import com.enrutatemio.core.model.Station
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MapTripRoute(viewModel: MapTripViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    LaunchedEffect(Unit) { locationPermissionState.launchPermissionRequest() }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            if (effect is MapTripEffect.ShowError) {
                scope.launch { snackbarHostState.showSnackbar(effect.message) }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            Box {
                FloatingActionButton(onClick = { viewModel.onIntent(MapTripIntent.ToggleMapType) }) {
                    Icon(Icons.Default.Layers, contentDescription = "Cambiar tipo de mapa")
                }
            }
        },
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            MapTripContent(
                state = state,
                isLocationPermissionGranted = locationPermissionState.status.isGranted,
                onMapTapped = { viewModel.onIntent(MapTripIntent.MapTapped(it)) },
            )

            if (state.hasRoute) {
                FloatingActionButton(
                    onClick = { viewModel.onIntent(MapTripIntent.ClearRoute) },
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp),
                ) {
                    Icon(Icons.Default.Clear, contentDescription = "Quitar recorrido")
                }
            }

            state.directions?.let { directions ->
                Text(
                    text = "${directions.durationText} · ${directions.distanceText}",
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(16.dp),
                )
            }
        }
    }
}

@Composable
private fun MapTripContent(
    state: MapTripState,
    isLocationPermissionGranted: Boolean,
    onMapTapped: (Coordinates) -> Unit,
) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(CALI_DEFAULT_CENTER.latitude, CALI_DEFAULT_CENTER.longitude),
            DEFAULT_ZOOM,
        )
    }

    GoogleMap(
        modifier = Modifier.padding(0.dp),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            isMyLocationEnabled = isLocationPermissionGranted,
            mapType = if (state.isHybridMap) MapType.HYBRID else MapType.NORMAL,
        ),
        onMapClick = { latLng -> onMapTapped(Coordinates(latLng.latitude, latLng.longitude)) },
    ) {
        state.stations.forEach { station -> StationMarker(station) }

        state.origin?.let {
            Marker(state = MarkerState(LatLng(it.latitude, it.longitude)), title = "Inicio")
        }
        state.destination?.let {
            Marker(state = MarkerState(LatLng(it.latitude, it.longitude)), title = "Destino")
        }

        state.directions?.legs?.forEach { leg ->
            val points = leg.polylinePoints.map { LatLng(it.latitude, it.longitude) }
            if (points.isNotEmpty()) {
                Polyline(points = points, width = 8f)
            }
        }
    }
}

@Composable
private fun StationMarker(station: Station) {
    Marker(
        state = MarkerState(LatLng(station.coordinates.latitude, station.coordinates.longitude)),
        title = station.name,
        snippet = station.address,
    )
}
