package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo

data class InterMinisterialServiceEntity(
    var id: Int? = null,
    var administrationId: Int,
    var controlUnitId: Int,
    var missionGeneralInfo: MissionGeneralInfoEntity? = null
) {
}
