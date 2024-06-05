package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export

data class NavActionInfoEntity(
    val count: Int? = null,
    val durationInHours: Double? = null,
    val amountOfInterrogatedShips: Int? = null
)

fun NavActionInfoEntity.toMapForExport(): Map<String, String> {
    val map = mutableMapOf<String, String>()
    map["count"] = count?.toString() ?: ""
    map["durationInHours"] = durationInHours?.toString() ?: ""
    amountOfInterrogatedShips?.let { map["amountOfInterrogatedShips"] = it.toString() } ?: ""
    return map
}
