package fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources

enum class ControlUnitResourceType(val label: String) {
    AIRPLANE("Avion"),
    BARGE("Barge"),
    CAR("Voiture"),
    DRONE("Drône"),
    EQUESTRIAN("Équestre"),
    FAST_BOAT("Vedette"),
    FRIGATE("Frégate"),
    HELICOPTER("Hélicoptère"),
    HYDROGRAPHIC_SHIP("Bâtiment hydrographique"),
    KAYAK("Kayak"),
    LIGHT_FAST_BOAT("Vedette légère"),
    MINE_DIVER("Plongeur démineur"),
    MOTORCYCLE("Moto"),
    NET_LIFTER("Remonte-filets"),
    NO_RESOURCE("Aucun moyen"),
    OTHER("Autre"),
    PATROL_BOAT("Patrouilleur"),
    PEDESTRIAN("Piéton"),
    PIROGUE("Pirogue"),
    RIGID_HULL("Coque rigide"),
    SEA_SCOOTER("Scooter de mer"),
    SEMI_RIGID("Semi-rigide"),
    SUPPORT_SHIP("Bâtiment de soutien"),
    TRAINING_SHIP("Bâtiment-école"),
    TUGBOAT("Remorqueur"),
}

/**
 * Which per-resource usage value applies to a given resource type in the ULAM general information:
 * - [KM]           → "Km parcourus"        (CAR, MOTORCYCLE)
 * - [ENGINE_HOURS] → "Nb d'heures moteur"  (boats)
 * - [NONE]         → no extra field
 */
enum class ResourceUsageKind { KM, ENGINE_HOURS, NONE }

fun ControlUnitResourceType.usageKind(): ResourceUsageKind = when (this) {
    ControlUnitResourceType.CAR,
    ControlUnitResourceType.MOTORCYCLE -> ResourceUsageKind.KM

    ControlUnitResourceType.BARGE,
    ControlUnitResourceType.SUPPORT_SHIP,
    ControlUnitResourceType.HYDROGRAPHIC_SHIP,
    ControlUnitResourceType.TRAINING_SHIP,
    ControlUnitResourceType.RIGID_HULL,
    ControlUnitResourceType.FRIGATE,
    ControlUnitResourceType.PATROL_BOAT,
    ControlUnitResourceType.TUGBOAT,
    ControlUnitResourceType.SEA_SCOOTER,
    ControlUnitResourceType.SEMI_RIGID,
    ControlUnitResourceType.LIGHT_FAST_BOAT,
    ControlUnitResourceType.FAST_BOAT -> ResourceUsageKind.ENGINE_HOURS

    else -> ResourceUsageKind.NONE
}
