package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export

data class NavActionInfoEntity(
    val count: Int,
    val durationInHours: Double,
    val amountOfInterrogatedShips: Int? = null
)

fun NavActionInfoEntity.toMap(): Map<String, String> {
    val map = mutableMapOf<String, String>()
    map["count"] = count.toString()
    map["durationInHours"] = durationInHours.toString()
    amountOfInterrogatedShips?.let { map["amountOfInterrogatedShips"] = it.toString() }
    return map
}
