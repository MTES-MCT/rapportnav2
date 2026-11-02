package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action

enum class UnitManagementTrainingTypeEnum(val label: String) {
    SHOOTING("Tir"),
    DIVING("Plongée"),
    MAN_OVERBOARD_RECOVERY("Récupération d'homme à la mer"),
    INTERVENTION_TACTICS("Techniques d'interventions (menottage...)"),
    FIRE("Incendie"),
    ABANDON_SHIP("Abandon du navire");

    companion object {
        fun fromString(value: String?): UnitManagementTrainingTypeEnum? =
            entries.find { it.name == value }
    }
}
