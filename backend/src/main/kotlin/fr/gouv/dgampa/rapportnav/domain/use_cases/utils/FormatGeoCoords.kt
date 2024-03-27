package fr.gouv.dgampa.rapportnav.domain.use_cases.utils

import fr.gouv.dgampa.rapportnav.config.UseCase

@UseCase
class FormatGeoCoords {
    fun formatLatLon(lat: Double?, lon: Double?): Pair<String?, String?> {
        val latStr = lat?.let { String.format("%.2f", it) }
        val lonStr = lon?.let { String.format("%.2f", it) }
        return latStr to lonStr
    }

}
