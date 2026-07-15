package com.enrutatemio.data.map

import com.enrutatemio.core.model.Coordinates

/**
 * Decodificador de polylines codificadas de Google (algoritmo estándar), migrado de
 * `GMapV2Direction.decodePoly()` legacy.
 */
internal object PolylineDecoder {

    fun decode(encoded: String): List<Coordinates> {
        val poly = mutableListOf<Coordinates>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var shift = 0
            var result = 0
            var b: Int
            do {
                b = encoded[index++].code - 63
                result = result or ((b and 0x1f) shl shift)
                shift += 5
            } while (b >= 0x20)
            val dLat = if (result and 1 != 0) (result shr 1).inv() else (result shr 1)
            lat += dLat

            shift = 0
            result = 0
            do {
                b = encoded[index++].code - 63
                result = result or ((b and 0x1f) shl shift)
                shift += 5
            } while (b >= 0x20)
            val dLng = if (result and 1 != 0) (result shr 1).inv() else (result shr 1)
            lng += dLng

            poly.add(Coordinates(latitude = lat / 1E5, longitude = lng / 1E5))
        }
        return poly
    }
}
