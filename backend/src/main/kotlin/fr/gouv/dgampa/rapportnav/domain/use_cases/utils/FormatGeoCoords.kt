package fr.gouv.dgampa.rapportnav.domain.use_cases.utils

import fr.gouv.dgampa.rapportnav.config.UseCase
import java.util.*

@UseCase
class FormatGeoCoords {
    fun formatLatLon(lat: Double?, lon: Double?): Pair<String?, String?> {
        val latStr = lat?.let { String.format(Locale.US, "%.2f", it) }
        val lonStr = lon?.let { String.format(Locale.US, "%.2f", it) }
        return latStr to lonStr
    }

}
